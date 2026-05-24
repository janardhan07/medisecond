package com.medisecond.dto;

import com.medisecond.model.User;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String phoneNumber;
    // Doctor fields
    private String specialty;
    private String city;
    private String clinicAddress;
    private Integer experienceYears;
    private Integer consultationFee;
    private String bio;
    private Double rating;
    private Integer ratingCount;
    private Boolean available;

    public static UserDto from(User u) {
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .role(u.getRole().name())
                .phoneNumber(u.getPhoneNumber())
                .specialty(u.getSpecialty())
                .city(u.getCity())
                .clinicAddress(u.getClinicAddress())
                .experienceYears(u.getExperienceYears())
                .consultationFee(u.getConsultationFee())
                .bio(u.getBio())
                .rating(u.getRating())
                .ratingCount(u.getRatingCount())
                .available(u.getAvailable())
                .build();
    }
}
