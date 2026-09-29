package com.retailpos.paymentservice.gateway;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentGatewayResponse {

    private boolean success;
    private String gatewayPaymentId;
    private String message;
}
