package com.medisecond.dto;
import lombok.*;
import java.math.BigDecimal;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentOrderRequest {
    private BigDecimal amount;
    private Long caseId;
}
