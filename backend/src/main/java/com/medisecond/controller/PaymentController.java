package com.medisecond.controller;

import com.medisecond.dto.PaymentOrderRequest;
import com.medisecond.dto.PaymentOrderResponse;
import com.medisecond.dto.PaymentVerifyRequest;
import com.medisecond.model.User;
import com.medisecond.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create_order")
    public ResponseEntity<PaymentOrderResponse> createOrder(
            @RequestBody PaymentOrderRequest req, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createOrder(req, user));
    }

    @PostMapping("/verify_payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerifyRequest req) {
        try {
            String result = paymentService.verifyPayment(req);
            return ResponseEntity.ok(Map.of("status", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
