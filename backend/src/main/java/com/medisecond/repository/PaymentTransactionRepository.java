package com.medisecond.repository;

import com.medisecond.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByRazorpayOrderId(String razorpayOrderId);
}
