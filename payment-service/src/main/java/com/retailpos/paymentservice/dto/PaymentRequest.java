package com.retailpos.paymentservice.dto;

import java.math.BigDecimal;

import com.retailpos.paymentservice.entity.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private Long orderId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private String idempotencyKey;
}