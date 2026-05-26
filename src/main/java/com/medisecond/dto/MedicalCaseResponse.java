package com.medisecond.dto;

import com.medisecond.model.MedicalCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalCaseResponse {
    private Long id;
    private String title;
    private String description;
    private String symptoms;
    private String mlPredictedCategory;
    private Double mlConfidenceScore;
    private String status;
    private Long patientId;
    private String patientUsername;
    private Long assignedDoctorId;
    private String assignedDoctorUsername;
    private LocalDateTime createdAt;

    public static MedicalCaseResponse from(MedicalCase c) {
        return MedicalCaseResponse.builder()
                .id(c.getId()).title(c.getTitle()).description(c.getDescription())
                .symptoms(c.getSymptoms()).mlPredictedCategory(c.getMlPredictedCategory())
                .mlConfidenceScore(c.getMlConfidenceScore()).status(c.getStatus().name())
                .patientId(c.getPatient().getId()).patientUsername(c.getPatient().getUsername())
                .assignedDoctorId(c.getAssignedDoctor() != null ? c.getAssignedDoctor().getId() : null)
                .assignedDoctorUsername(c.getAssignedDoctor() != null ? c.getAssignedDoctor().getUsername() : null)
                .createdAt(c.getCreatedAt()).build();
    }
}
