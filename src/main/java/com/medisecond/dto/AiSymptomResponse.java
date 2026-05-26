package com.medisecond.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiSymptomResponse {
    private String suggestedSpecialty;
    private String reasoning;
    private Double confidence;
    private List<String> alternativeSpecialties;
    private List<UserDto> recommendedDoctors;
}
