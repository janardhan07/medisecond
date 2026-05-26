package com.medisecond.controller;

import com.medisecond.dto.AppointmentRequest;
import com.medisecond.dto.AppointmentResponse;
import com.medisecond.model.User;
import com.medisecond.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService apptService;

    @PostMapping
    public ResponseEntity<?> book(@RequestBody AppointmentRequest req, Authentication auth) {
        try {
            return ResponseEntity.status(201).body(apptService.book(req, (User) auth.getPrincipal()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> list(Authentication auth) {
        return ResponseEntity.ok(apptService.getForUser((User) auth.getPrincipal()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> get(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(apptService.getById(id, (User) auth.getPrincipal()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body, Authentication auth) {
        return ResponseEntity.ok(apptService.updateStatus(id, body.get("status"), body.get("doctorNotes"), body.get("prescription"), (User) auth.getPrincipal()));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(apptService.cancel(id, (User) auth.getPrincipal()));
    }

    @GetMapping("/slots")
    public ResponseEntity<List<String>> slots(@RequestParam Long doctorId, @RequestParam String date) {
        return ResponseEntity.ok(apptService.getAvailableSlots(doctorId, date));
    }
}
