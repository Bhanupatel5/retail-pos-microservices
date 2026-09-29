package com.retailpos.paymentservice.gateway;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebhookSignatureVerifier {

    private final String webhookSecret;

    public WebhookSignatureVerifier(
            @Value("${payment.webhook.secret}") String webhookSecret) {

        this.webhookSecret = webhookSecret;
    }


public boolean verify(String payload, String receivedSignature) {

    try {
        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey =
                new SecretKeySpec(
                        webhookSecret.getBytes(StandardCharsets.UTF_8),
                        "HmacSHA256"
                );

        mac.init(secretKey);

        byte[] hash =
                mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        String expectedSignature =
                HexFormat.of().formatHex(hash);

        return expectedSignature.equals(receivedSignature);

    } catch (GeneralSecurityException ex) {
        throw new IllegalStateException(
                "Unable to verify webhook signature", ex);
    }
}
}