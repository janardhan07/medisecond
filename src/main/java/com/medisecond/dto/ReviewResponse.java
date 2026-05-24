package com.medisecond.dto;

import com.medisecond.model.DoctorReview;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private String patientUsername;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public static ReviewResponse from(DoctorReview r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .doctorId(r.getDoctor().getId())
                .patientId(r.getPatient().getId())
                .patientUsername(r.getPatient().getUsername())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
