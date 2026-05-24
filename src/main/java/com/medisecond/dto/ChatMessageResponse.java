package com.medisecond.dto;
import com.medisecond.model.ChatMessage;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChatMessageResponse {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String message;
    private LocalDateTime createdAt;
    public static ChatMessageResponse from(ChatMessage m) {
        return ChatMessageResponse.builder()
            .id(m.getId()).senderId(m.getSender().getId())
            .senderUsername(m.getSender().getUsername())
            .message(m.getMessage()).createdAt(m.getCreatedAt()).build();
    }
}
