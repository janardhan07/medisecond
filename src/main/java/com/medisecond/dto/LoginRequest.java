package com.medisecond.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
