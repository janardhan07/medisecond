package com.medisecond.dto;
import com.medisecond.model.DoctorReview;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewRequest {
    private Integer rating;   // 1–5
    private String comment;
}
