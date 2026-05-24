package com.medisecond.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private UserDto user;
}
