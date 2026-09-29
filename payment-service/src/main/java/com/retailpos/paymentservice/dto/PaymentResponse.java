package com.retailpos.paymentservice.dto;

import java.math.BigDecimal;

import com.retailpos.paymentservice.entity.PaymentMethod;
import com.retailpos.paymentservice.entity.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {

    private Long paymentId;

    private Long orderId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;
}