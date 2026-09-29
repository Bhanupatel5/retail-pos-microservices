package com.retailpos.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<String> handlePaymentNotFound(
	        PaymentNotFoundException ex) {

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(ex.getMessage());
	}
	
	@ExceptionHandler(PaymentAmountMismatchException.class)
	public ResponseEntity<String> handlePaymentAmountMismatch(
	        PaymentAmountMismatchException ex) {

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(ex.getMessage());
	}
	
	@ExceptionHandler(InvalidWebhookException.class)
	public ResponseEntity<String> handleInvalidWebhook(
	        InvalidWebhookException ex) {

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(ex.getMessage());
	}
}

