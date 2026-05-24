package com.medisecond.service;

import com.medisecond.dto.*;
import com.medisecond.model.DoctorReview;
import com.medisecond.model.User;
import com.medisecond.repository.DoctorReviewRepository;
import com.medisecond.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DoctorReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User.Role role;
        try {
            role = User.Role.valueOf(req.getRole() != null ? req.getRole().toUpperCase() : "PATIENT");
        } catch (IllegalArgumentException e) {
            role = User.Role.PATIENT;
        }

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .phoneNumber(req.getPhoneNumber())
                // Doctor profile fields
                .specialty(req.getSpecialty())
                .city(req.getCity())
                .clinicAddress(req.getClinicAddress())
                .experienceYears(req.getExperienceYears())
                .consultationFee(req.getConsultationFee())
                .bio(req.getBio())
                .build();

        return userRepository.save(user);
    }

    // ── Doctor profile update ────────────────────────────────────────────────

    public UserDto updateDoctorProfile(User doctor, DoctorProfileRequest req) {
        doctor.setSpecialty(req.getSpecialty());
        doctor.setCity(req.getCity());
        doctor.setClinicAddress(req.getClinicAddress());
        doctor.setExperienceYears(req.getExperienceYears());
        doctor.setConsultationFee(req.getConsultationFee());
        doctor.setBio(req.getBio());
        if (req.getAvailable() != null) {
            doctor.setAvailable(req.getAvailable());
        }
        return UserDto.from(userRepository.save(doctor));
    }

    // ── Doctor search ────────────────────────────────────────────────────────

    public List<UserDto> searchDoctors(String city, String specialty) {
        List<User> doctors;
        boolean hasCity      = city != null && !city.isBlank();
        boolean hasSpecialty = specialty != null && !specialty.isBlank();

        if (hasCity && hasSpecialty) {
            doctors = userRepository.findDoctorsByCityAndSpecialty(city, specialty);
        } else if (hasCity) {
            doctors = userRepository.findDoctorsByCity(city);
        } else if (hasSpecialty) {
            doctors = userRepository.findDoctorsBySpecialty(specialty);
        } else {
            doctors = userRepository.findAllAvailableDoctors();
        }

        return doctors.stream().map(UserDto::from).toList();
    }

    public List<String> getAllCities() {
        return userRepository.findAllDoctorCities();
    }

    public UserDto getDoctorById(Long id) {
        User doctor = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        if (doctor.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("User is not a doctor");
        }
        return UserDto.from(doctor);
    }

    // ── Reviews ──────────────────────────────────────────────────────────────

    public ReviewResponse submitReview(Long doctorId, ReviewRequest req, User patient) {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("Target user is not a doctor");
        }

        if (req.getRating() < 1 || req.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // One review per patient per doctor — update if exists
        DoctorReview review = reviewRepository.findByDoctorAndPatient(doctor, patient)
                .orElse(DoctorReview.builder().doctor(doctor).patient(patient).build());

        review.setRating(req.getRating());
        review.setComment(req.getComment());
        reviewRepository.save(review);

        // Recalculate doctor's average rating
        List<DoctorReview> allReviews = reviewRepository.findByDoctorOrderByCreatedAtDesc(doctor);
        double avg = allReviews.stream().mapToInt(DoctorReview::getRating).average().orElse(0.0);
        doctor.setRating(Math.round(avg * 10.0) / 10.0);
        doctor.setRatingCount(allReviews.size());
        userRepository.save(doctor);

        return ReviewResponse.from(review);
    }

    public List<ReviewResponse> getDoctorReviews(Long doctorId) {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return reviewRepository.findByDoctorOrderByCreatedAtDesc(doctor)
                .stream().map(ReviewResponse::from).toList();
    }
}
