package com.medisecond.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DoctorProfileRequest {
    private String specialty;
    private String city;
    private String clinicAddress;
    private Integer experienceYears;
    private Integer consultationFee;
    private String bio;
    private Boolean available;
}
