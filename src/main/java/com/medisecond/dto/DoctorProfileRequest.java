package com.medisecond.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileRequest {
    private String fullName, gender, phoneNumber;
    private String specialty, city, area, clinicName, clinicAddress;
    private Integer experienceYears, consultationFee;
    private String bio, qualifications, languages;
    private Boolean available, onlineConsultation;
}
