package com.medisecond.repository;

import com.medisecond.model.Appointment;
import com.medisecond.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientOrderByCreatedAtDesc(User patient);

    List<Appointment> findByDoctorOrderByCreatedAtDesc(User doctor);

    List<Appointment> findByDoctorAndAppointmentDateOrderByAppointmentTimeAsc(User doctor, LocalDate date);

    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(User doctor, LocalDate date, String time);
}
