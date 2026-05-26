package com.medisecond.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSymptomRequest {
    private String symptoms;
    private String city;
}
