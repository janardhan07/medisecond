package com.medisecond.dto;

import com.medisecond.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username, email, role, phoneNumber, fullName, gender;
    private String specialty, city, area, clinicName, clinicAddress;
    private Integer experienceYears, consultationFee;
    private String bio, qualifications, languages;
    private Double rating;
    private Integer ratingCount;
    private Boolean available, onlineConsultation;
    private Integer age;
    private String bloodGroup;
    private String medicalHistory;

    public static UserDto from(User u) {
        return UserDto.builder()
                .id(u.getId()).username(u.getUsername()).email(u.getEmail())
                .role(u.getRole().name()).phoneNumber(u.getPhoneNumber())
                .fullName(u.getFullName()).gender(u.getGender())
                .specialty(u.getSpecialty()).city(u.getCity()).area(u.getArea())
                .clinicName(u.getClinicName()).clinicAddress(u.getClinicAddress())
                .experienceYears(u.getExperienceYears()).consultationFee(u.getConsultationFee())
                .bio(u.getBio()).qualifications(u.getQualifications()).languages(u.getLanguages())
                .rating(u.getRating()).ratingCount(u.getRatingCount())
                .available(u.getAvailable()).onlineConsultation(u.getOnlineConsultation())
                .age(u.getAge()).bloodGroup(u.getBloodGroup()).medicalHistory(u.getMedicalHistory())
                .build();
    }
}
