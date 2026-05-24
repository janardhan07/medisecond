package com.medisecond.controller;

import com.medisecond.dto.MedicalCaseRequest;
import com.medisecond.dto.MedicalCaseResponse;
import com.medisecond.model.User;
import com.medisecond.service.MedicalCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical/cases")
@RequiredArgsConstructor
public class MedicalCaseController {

    private final MedicalCaseService caseService;

    @GetMapping
    public ResponseEntity<List<MedicalCaseResponse>> listCases(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(caseService.getCasesForUser(user));
    }

    @PostMapping
    public ResponseEntity<MedicalCaseResponse> createCase(
            @RequestBody MedicalCaseRequest req, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(caseService.createCase(req, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalCaseResponse> getCase(
            @PathVariable Long id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(caseService.getCaseById(id, user));
    }
}
