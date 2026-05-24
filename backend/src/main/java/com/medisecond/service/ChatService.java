package com.medisecond.service;

import com.medisecond.dto.ChatMessageRequest;
import com.medisecond.dto.ChatMessageResponse;
import com.medisecond.model.ChatMessage;
import com.medisecond.model.MedicalCase;
import com.medisecond.model.User;
import com.medisecond.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatRepository;
    private final MedicalCaseService caseService;

    public List<ChatMessageResponse> getMessages(Long caseId, User user) {
        MedicalCase medicalCase = caseService.getCaseEntityById(caseId);
        checkChatAccess(medicalCase, user);
        return chatRepository.findByMedicalCaseOrderByCreatedAtAsc(medicalCase)
                .stream().map(ChatMessageResponse::from).toList();
    }

    public ChatMessageResponse sendMessage(Long caseId, ChatMessageRequest req, User user) {
        MedicalCase medicalCase = caseService.getCaseEntityById(caseId);
        checkChatAccess(medicalCase, user);

        ChatMessage msg = ChatMessage.builder()
                .medicalCase(medicalCase)
                .sender(user)
                .message(req.getMessage())
                .build();

        return ChatMessageResponse.from(chatRepository.save(msg));
    }

    private void checkChatAccess(MedicalCase c, User user) {
        boolean allowed = switch (user.getRole()) {
            case PATIENT -> c.getPatient().getId().equals(user.getId());
            case DOCTOR  -> c.getAssignedDoctor() != null && c.getAssignedDoctor().getId().equals(user.getId());
            case ADMIN   -> true;
        };
        if (!allowed) throw new AccessDeniedException("You cannot access this chat");
    }
}
