package com.medisecond.controller;

import com.medisecond.dto.ChatMessageRequest;
import com.medisecond.dto.ChatMessageResponse;
import com.medisecond.model.User;
import com.medisecond.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments/cases/{caseId}/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable Long caseId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(chatService.getMessages(caseId, user));
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @PathVariable Long caseId,
            @RequestBody ChatMessageRequest req,
            Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.sendMessage(caseId, req, user));
    }
}
