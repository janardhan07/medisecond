package com.medisecond.dto;
import lombok.*;
import java.math.BigDecimal;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentOrderResponse {
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String razorpayKey;
}
