package com.retailpos.paymentservice.dto;

import java.math.BigDecimal;

import com.retailpos.paymentservice.entity.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentWebhookRequest {

    private String gatewayPaymentId;
    private PaymentStatus status;
    private BigDecimal amount;
}