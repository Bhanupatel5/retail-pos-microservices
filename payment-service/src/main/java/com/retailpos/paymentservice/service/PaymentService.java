package com.retailpos.paymentservice.service;

import com.retailpos.paymentservice.dto.PaymentRequest;
import com.retailpos.paymentservice.dto.PaymentResponse;
import com.retailpos.paymentservice.dto.PaymentWebhookRequest;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);
    void handleWebhook(PaymentWebhookRequest request);
}