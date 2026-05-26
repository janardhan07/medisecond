package com.medisecond.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(nullable = false)
    private LocalDate appointmentDate;
    @Column(nullable = false)
    private String appointmentTime;

    private String symptoms;
    @Column(columnDefinition = "TEXT")
    private String notes;
    @Column(columnDefinition = "TEXT")
    private String doctorNotes;
    @Column(columnDefinition = "TEXT")
    private String prescription;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PENDING;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ConsultType consultType = ConsultType.IN_PERSON;

    private Integer amount;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private String razorpayOrderId;
    private String razorpayPaymentId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Status {PENDING, CONFIRMED, COMPLETED, CANCELLED}

    public enum ConsultType {IN_PERSON, ONLINE}

    public enum PaymentStatus {PENDING, PAID, REFUNDED}
}
