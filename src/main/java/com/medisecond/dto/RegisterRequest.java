package com.medisecond.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username, email, password, role, phoneNumber, fullName, gender;
    private String specialty, city, area, clinicName, clinicAddress;
    private Integer experienceYears, consultationFee;
    private String bio, qualifications, languages;
    private Boolean onlineConsultation;
    private Integer age;
    private String bloodGroup;
}
