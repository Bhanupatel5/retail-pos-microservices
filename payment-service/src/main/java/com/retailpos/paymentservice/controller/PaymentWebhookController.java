package com.retailpos.paymentservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailpos.paymentservice.dto.PaymentWebhookRequest;
import com.retailpos.paymentservice.exception.InvalidWebhookException;
import com.retailpos.paymentservice.gateway.WebhookSignatureVerifier;
import com.retailpos.paymentservice.service.PaymentService;


@RestController
@RequestMapping("/payments")
public class PaymentWebhookController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final WebhookSignatureVerifier webhookSignatureVerifier;

    public PaymentWebhookController(
            ObjectMapper objectMapper,
            WebhookSignatureVerifier webhookSignatureVerifier,
            PaymentService paymentService) {

        this.objectMapper = objectMapper;
        this.webhookSignatureVerifier = webhookSignatureVerifier;
        this.paymentService = paymentService;
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestBody String rawPayload,
            @RequestHeader("X-Webhook-Signature") String signature) {

        if (!webhookSignatureVerifier.verify(rawPayload, signature)) {
        	throw new InvalidWebhookException("Invalid webhook signature");
        }

        try {
            PaymentWebhookRequest request =
                    objectMapper.readValue(
                            rawPayload,
                            PaymentWebhookRequest.class
                    );

            paymentService.handleWebhook(request);

        } catch (JsonProcessingException ex) {
        	throw new InvalidWebhookException(
        		    "Invalid webhook payload", ex);
        }
    }
}
