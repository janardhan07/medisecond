package com.medisecond.service;

import com.medisecond.dto.AppointmentRequest;
import com.medisecond.dto.AppointmentResponse;
import com.medisecond.model.Appointment;
import com.medisecond.model.User;
import com.medisecond.repository.AppointmentRepository;
import com.medisecond.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository apptRepo;
    private final UserRepository userRepo;

    public AppointmentResponse book(AppointmentRequest req, User patient) {
        User doctor = userRepo.findById(req.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
        LocalDate date = LocalDate.parse(req.getAppointmentDate());
        if (apptRepo.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, date, req.getAppointmentTime()))
            throw new RuntimeException("This slot is already booked");
        Appointment.ConsultType ct;
        try {
            ct = Appointment.ConsultType.valueOf(req.getConsultType() != null ? req.getConsultType() : "IN_PERSON");
        } catch (Exception e) {
            ct = Appointment.ConsultType.IN_PERSON;
        }
        Appointment a = Appointment.builder()
                .patient(patient).doctor(doctor).appointmentDate(date)
                .appointmentTime(req.getAppointmentTime()).symptoms(req.getSymptoms())
                .notes(req.getNotes()).consultType(ct)
                .amount(doctor.getConsultationFee()).build();
        return AppointmentResponse.from(apptRepo.save(a));
    }

    public List<AppointmentResponse> getForUser(User user) {
        return switch (user.getRole()) {
            case PATIENT ->
                    apptRepo.findByPatientOrderByCreatedAtDesc(user).stream().map(AppointmentResponse::from).toList();
            case DOCTOR ->
                    apptRepo.findByDoctorOrderByCreatedAtDesc(user).stream().map(AppointmentResponse::from).toList();
            case ADMIN -> apptRepo.findAll().stream().map(AppointmentResponse::from).toList();
        };
    }

    public AppointmentResponse getById(Long id, User user) {
        Appointment a = apptRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        checkAccess(a, user);
        return AppointmentResponse.from(a);
    }

    public AppointmentResponse updateStatus(Long id, String status, String doctorNotes, String prescription, User doctor) {
        Appointment a = apptRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        try {
            a.setStatus(Appointment.Status.valueOf(status));
        } catch (Exception e) {
        }
        if (doctorNotes != null) a.setDoctorNotes(doctorNotes);
        if (prescription != null) a.setPrescription(prescription);
        return AppointmentResponse.from(apptRepo.save(a));
    }

    public AppointmentResponse cancel(Long id, User user) {
        Appointment a = apptRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        a.setStatus(Appointment.Status.CANCELLED);
        return AppointmentResponse.from(apptRepo.save(a));
    }

    public List<String> getAvailableSlots(Long doctorId, String date) {
        List<String> all = List.of("09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "12:00", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30");
        User doctor = userRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Not found"));
        LocalDate d = LocalDate.parse(date);
        List<Appointment> booked = apptRepo.findByDoctorAndAppointmentDateOrderByAppointmentTimeAsc(doctor, d);
        Set<String> bookedTimes = new HashSet<>();
        booked.forEach(a -> {
            if (a.getStatus() != Appointment.Status.CANCELLED) bookedTimes.add(a.getAppointmentTime());
        });
        return all.stream().filter(s -> !bookedTimes.contains(s)).toList();
    }

    private void checkAccess(Appointment a, User u) {
        boolean ok = switch (u.getRole()) {
            case PATIENT -> a.getPatient().getId().equals(u.getId());
            case DOCTOR -> a.getDoctor().getId().equals(u.getId());
            case ADMIN -> true;
        };
        if (!ok) throw new org.springframework.security.access.AccessDeniedException("Access denied");
    }
}
