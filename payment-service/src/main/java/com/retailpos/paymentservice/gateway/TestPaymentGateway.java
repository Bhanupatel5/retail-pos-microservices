package com.retailpos.paymentservice.gateway;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.retailpos.paymentservice.entity.PaymentMethod;

@Component
public class TestPaymentGateway implements PaymentGatewayClient {

	@Override
	public PaymentGatewayResponse processPayment(
	        BigDecimal amount,
	        PaymentMethod paymentMethod) {

	    PaymentGatewayResponse response = new PaymentGatewayResponse();
	    
//	    throw new RuntimeException("Simulated gateway failure");

	    response.setSuccess(true);
	    response.setGatewayPaymentId("TEST-" + System.currentTimeMillis());
	    response.setMessage("Payment successful");

	    return response;
	}
}