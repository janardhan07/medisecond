package com.medisecond.dto;

import com.medisecond.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long patientId, doctorId;
    private String patientName, doctorName, doctorSpecialty, doctorCity;
    private String appointmentDate, appointmentTime;
    private String symptoms, notes, doctorNotes, prescription;
    private String status, consultType, paymentStatus;
    private Integer amount;
    private String razorpayOrderId;
    private String createdAt;

    public static AppointmentResponse from(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .patientId(a.getPatient().getId()).patientName(a.getPatient().getFullName() != null ? a.getPatient().getFullName() : a.getPatient().getUsername())
                .doctorId(a.getDoctor().getId()).doctorName("Dr. " + (a.getDoctor().getFullName() != null ? a.getDoctor().getFullName() : a.getDoctor().getUsername()))
                .doctorSpecialty(a.getDoctor().getSpecialty()).doctorCity(a.getDoctor().getCity())
                .appointmentDate(a.getAppointmentDate().toString()).appointmentTime(a.getAppointmentTime())
                .symptoms(a.getSymptoms()).notes(a.getNotes()).doctorNotes(a.getDoctorNotes()).prescription(a.getPrescription())
                .status(a.getStatus().name()).consultType(a.getConsultType().name())
                .paymentStatus(a.getPaymentStatus().name()).amount(a.getAmount())
                .razorpayOrderId(a.getRazorpayOrderId())
                .createdAt(a.getCreatedAt() != null ? a.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null)
                .build();
    }
}
