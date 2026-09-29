package com.retailpos.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailpos.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);
}