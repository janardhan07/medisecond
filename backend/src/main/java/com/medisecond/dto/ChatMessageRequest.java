package com.medisecond.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChatMessageRequest {
    private String message;
}
