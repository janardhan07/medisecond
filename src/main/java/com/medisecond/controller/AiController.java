package com.medisecond.controller;

import com.medisecond.dto.AiSymptomRequest;
import com.medisecond.dto.AiSymptomResponse;
import com.medisecond.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<AiSymptomResponse> analyze(@RequestBody AiSymptomRequest req) {
        return ResponseEntity.ok(aiService.analyze(req.getSymptoms(), req.getCity()));
    }
}
