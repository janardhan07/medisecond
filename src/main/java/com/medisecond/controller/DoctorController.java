package com.medisecond.controller;

import com.medisecond.dto.DoctorProfileRequest;
import com.medisecond.dto.ReviewRequest;
import com.medisecond.dto.ReviewResponse;
import com.medisecond.dto.UserDto;
import com.medisecond.model.User;
import com.medisecond.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String area) {
        return ResponseEntity.ok(userService.searchDoctors(city, specialty, area));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> cities() {
        return ResponseEntity.ok(userService.getAllCities());
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<String>> specialties() {
        return ResponseEntity.ok(userService.getAllSpecialties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDoctorById(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDoctorReviews(id));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody DoctorProfileRequest req, Authentication auth) {
        User user = (User) auth.getPrincipal();
        if (user.getRole() != User.Role.DOCTOR)
            return ResponseEntity.status(403).body(Map.of("error", "Only doctors can update doctor profiles"));
        return ResponseEntity.ok(userService.updateDoctorProfile(user, req));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> submitReview(@PathVariable Long id, @RequestBody ReviewRequest req, Authentication auth) {
        User user = (User) auth.getPrincipal();
        if (user.getRole() != User.Role.PATIENT)
            return ResponseEntity.status(403).body(Map.of("error", "Only patients can review doctors"));
        try {
            return ResponseEntity.status(201).body(userService.submitReview(id, req, user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
