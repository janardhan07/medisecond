package com.medisecond.controller;

import com.medisecond.dto.*;
import com.medisecond.model.User;
import com.medisecond.service.JwtService;
import com.medisecond.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            User user = userService.register(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.from(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
            User user = (User) auth.getPrincipal();
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(LoginResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .user(UserDto.from(user))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(UserDto.from(user));
    }
}
