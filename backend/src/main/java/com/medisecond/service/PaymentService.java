package com.medisecond.service;

import com.medisecond.dto.PaymentOrderRequest;
import com.medisecond.dto.PaymentOrderResponse;
import com.medisecond.dto.PaymentVerifyRequest;
import com.medisecond.model.MedicalCase;
import com.medisecond.model.PaymentTransaction;
import com.medisecond.model.User;
import com.medisecond.repository.MedicalCaseRepository;
import com.medisecond.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentTransactionRepository transactionRepository;
    private final MedicalCaseRepository caseRepository;

    // Simulated Razorpay – replace with real SDK in production
    private static final String RAZORPAY_KEY_ID = "rzp_test_YourTestKeyId123";

    public PaymentOrderResponse createOrder(PaymentOrderRequest req, User user) {
        // Simulate Razorpay order creation
        String fakeOrderId = "order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        MedicalCase medicalCase = null;
        if (req.getCaseId() != null) {
            medicalCase = caseRepository.findById(req.getCaseId()).orElse(null);
        }

        PaymentTransaction tx = PaymentTransaction.builder()
                .user(user)
                .medicalCase(medicalCase)
                .razorpayOrderId(fakeOrderId)
                .amount(req.getAmount())
                .status(PaymentTransaction.PaymentStatus.CREATED)
                .build();

        transactionRepository.save(tx);

        return PaymentOrderResponse.builder()
                .orderId(fakeOrderId)
                .amount(req.getAmount())
                .currency("INR")
                .razorpayKey(RAZORPAY_KEY_ID)
                .build();
    }

    public String verifyPayment(PaymentVerifyRequest req) {
        PaymentTransaction tx = transactionRepository
                .findByRazorpayOrderId(req.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // In production: verify HMAC signature using Razorpay SDK
        // For MVP, we accept the simulated payment
        tx.setRazorpayPaymentId(req.getRazorpayPaymentId());
        tx.setRazorpaySignature(req.getRazorpaySignature());
        tx.setStatus(PaymentTransaction.PaymentStatus.PAID);
        transactionRepository.save(tx);

        return "Payment successful";
    }
}
