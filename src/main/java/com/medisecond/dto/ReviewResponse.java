package com.medisecond.dto;

import com.medisecond.model.DoctorReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long doctorId, patientId;
    private String patientUsername, patientName;
    private Integer rating;
    private String comment, createdAt;

    public static ReviewResponse from(DoctorReview r) {
        return ReviewResponse.builder()
                .id(r.getId()).doctorId(r.getDoctor().getId())
                .patientId(r.getPatient().getId()).patientUsername(r.getPatient().getUsername())
                .patientName(r.getPatient().getFullName() != null ? r.getPatient().getFullName() : r.getPatient().getUsername())
                .rating(r.getRating()).comment(r.getComment())
                .createdAt(r.getCreatedAt() != null ? r.getCreatedAt().toString() : null)
                .build();
    }
}
