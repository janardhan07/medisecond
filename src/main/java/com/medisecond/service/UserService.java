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
    private final UserRepository userRepo;
    private final DoctorReviewRepository reviewRepo;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String u) throws UsernameNotFoundException {
        return userRepo.findByUsername(u).orElseThrow(() -> new UsernameNotFoundException("User not found: " + u));
    }

    public User register(RegisterRequest r) {
        if (userRepo.existsByUsername(r.getUsername())) throw new RuntimeException("Username already taken");
        if (userRepo.existsByEmail(r.getEmail())) throw new RuntimeException("Email already in use");
        User.Role role;
        try {
            role = User.Role.valueOf(r.getRole() != null ? r.getRole().toUpperCase() : "PATIENT");
        } catch (Exception e) {
            role = User.Role.PATIENT;
        }
        return userRepo.save(User.builder()
                .username(r.getUsername()).email(r.getEmail()).password(encoder.encode(r.getPassword()))
                .role(role).phoneNumber(r.getPhoneNumber()).fullName(r.getFullName()).gender(r.getGender())
                .specialty(r.getSpecialty()).city(r.getCity()).area(r.getArea())
                .clinicName(r.getClinicName()).clinicAddress(r.getClinicAddress())
                .experienceYears(r.getExperienceYears()).consultationFee(r.getConsultationFee())
                .bio(r.getBio()).qualifications(r.getQualifications()).languages(r.getLanguages())
                .onlineConsultation(Boolean.TRUE.equals(r.getOnlineConsultation()))
                .age(r.getAge()).bloodGroup(r.getBloodGroup()).build());
    }

    public List<UserDto> searchDoctors(String city, String specialty, String area) {
        return userRepo.searchDoctors(
                (city != null && !city.isBlank()) ? city : null,
                (specialty != null && !specialty.isBlank()) ? specialty : null,
                (area != null && !area.isBlank()) ? area : null
        ).stream().map(UserDto::from).toList();
    }

    public UserDto getDoctorById(Long id) {
        return UserDto.from(userRepo.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found")));
    }

    public UserDto updateDoctorProfile(User doctor, DoctorProfileRequest r) {
        if (r.getFullName() != null) doctor.setFullName(r.getFullName());
        if (r.getGender() != null) doctor.setGender(r.getGender());
        if (r.getPhoneNumber() != null) doctor.setPhoneNumber(r.getPhoneNumber());
        if (r.getSpecialty() != null) doctor.setSpecialty(r.getSpecialty());
        if (r.getCity() != null) doctor.setCity(r.getCity());
        if (r.getArea() != null) doctor.setArea(r.getArea());
        if (r.getClinicName() != null) doctor.setClinicName(r.getClinicName());
        if (r.getClinicAddress() != null) doctor.setClinicAddress(r.getClinicAddress());
        if (r.getExperienceYears() != null) doctor.setExperienceYears(r.getExperienceYears());
        if (r.getConsultationFee() != null) doctor.setConsultationFee(r.getConsultationFee());
        if (r.getBio() != null) doctor.setBio(r.getBio());
        if (r.getQualifications() != null) doctor.setQualifications(r.getQualifications());
        if (r.getLanguages() != null) doctor.setLanguages(r.getLanguages());
        if (r.getAvailable() != null) doctor.setAvailable(r.getAvailable());
        if (r.getOnlineConsultation() != null) doctor.setOnlineConsultation(r.getOnlineConsultation());
        return UserDto.from(userRepo.save(doctor));
    }

    public ReviewResponse submitReview(Long doctorId, ReviewRequest req, User patient) {
        User doctor = userRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        if (req.getRating() < 1 || req.getRating() > 5) throw new RuntimeException("Rating must be 1-5");
        DoctorReview review = reviewRepo.findByDoctorAndPatient(doctor, patient)
                .orElse(DoctorReview.builder().doctor(doctor).patient(patient).build());
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        reviewRepo.save(review);
        var all = reviewRepo.findByDoctorOrderByCreatedAtDesc(doctor);
        double avg = all.stream().mapToInt(DoctorReview::getRating).average().orElse(0);
        doctor.setRating(Math.round(avg * 10.0) / 10.0);
        doctor.setRatingCount(all.size());
        userRepo.save(doctor);
        return ReviewResponse.from(review);
    }

    public List<ReviewResponse> getDoctorReviews(Long doctorId) {
        User doctor = userRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Not found"));
        return reviewRepo.findByDoctorOrderByCreatedAtDesc(doctor).stream().map(ReviewResponse::from).toList();
    }

    public List<String> getAllCities() {
        return userRepo.findAllDoctorCities();
    }

    public List<String> getAllSpecialties() {
        return userRepo.findAllDoctorSpecialties();
    }
}
