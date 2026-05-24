package com.medisecond.controller;

import com.medisecond.dto.*;
import com.medisecond.model.User;
import com.medisecond.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // ── Public search endpoints (no auth required) ───────────────────────────

    /**
     * Search doctors by city and/or specialty.
     * GET /api/doctors/search?city=Mumbai&specialty=Cardiologist
     * GET /api/doctors/search?city=Delhi
     * GET /api/doctors/search?specialty=Neurologist
     * GET /api/doctors/search  ← returns all available doctors
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchDoctors(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(userService.searchDoctors(city, specialty));
    }

    /** Returns list of all cities that have doctors */
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        return ResponseEntity.ok(userService.getAllCities());
    }

    /** Get a specific doctor's profile by ID */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDoctorById(id));
    }

    /** Get reviews for a doctor */
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> getDoctorReviews(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDoctorReviews(id));
    }

    // ── Authenticated endpoints ──────────────────────────────────────────────

    /** Doctor updates their own profile */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody DoctorProfileRequest req, Authentication auth) {
        User user = (User) auth.getPrincipal();
        if (user.getRole() != User.Role.DOCTOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only doctors can update doctor profiles"));
        }
        return ResponseEntity.ok(userService.updateDoctorProfile(user, req));
    }

    /** Patient submits a review for a doctor */
    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> submitReview(
            @PathVariable Long id,
            @RequestBody ReviewRequest req,
            Authentication auth) {
        User user = (User) auth.getPrincipal();
        if (user.getRole() != User.Role.PATIENT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only patients can submit reviews"));
        }
        try {
            ReviewResponse response = userService.submitReview(id, req, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
