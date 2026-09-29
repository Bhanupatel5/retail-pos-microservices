package com.retailpos.paymentservice.gateway;

import java.math.BigDecimal;

import com.retailpos.paymentservice.entity.PaymentMethod;

public interface PaymentGatewayClient {

	 PaymentGatewayResponse processPayment(
	            BigDecimal amount,
	            PaymentMethod paymentMethod
	    );
}