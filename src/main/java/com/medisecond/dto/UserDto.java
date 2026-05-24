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
    public static UserDto from(User u) {
        return UserDto.builder()
            .id(u.getId()).username(u.getUsername()).email(u.getEmail())
            .role(u.getRole().name()).phoneNumber(u.getPhoneNumber()).build();
    }
}
