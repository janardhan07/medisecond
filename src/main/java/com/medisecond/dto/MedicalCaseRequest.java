package com.medisecond.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedicalCaseRequest {
    private String title;
    private String description;
    private String symptoms;
}
