package com.retailpos.paymentservice.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.retailpos.paymentservice.dto.PaymentRequest;
import com.retailpos.paymentservice.dto.PaymentResponse;
import com.retailpos.paymentservice.dto.PaymentWebhookRequest;
import com.retailpos.paymentservice.entity.Payment;
import com.retailpos.paymentservice.entity.PaymentStatus;
import com.retailpos.paymentservice.exception.PaymentAmountMismatchException;
import com.retailpos.paymentservice.exception.PaymentNotFoundException;
import com.retailpos.paymentservice.gateway.PaymentGatewayClient;
import com.retailpos.paymentservice.gateway.PaymentGatewayResponse;
import com.retailpos.paymentservice.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger log =
	        LoggerFactory.getLogger(PaymentServiceImpl.class);
	private final PaymentRepository paymentRepository;
	private final PaymentGatewayClient paymentGatewayClient;


	public PaymentServiceImpl(
	        PaymentRepository paymentRepository,
	        PaymentGatewayClient paymentGatewayClient) {

	    this.paymentRepository = paymentRepository;
	    this.paymentGatewayClient = paymentGatewayClient;
	}
	
	@Override
	public PaymentResponse processPayment(PaymentRequest request) {
		Optional<Payment> p=paymentRepository.findByIdempotencyKey(request.getIdempotencyKey());
		if (p.isEmpty()) {
			Payment payment = new Payment();
			
			payment.setOrderId(request.getOrderId());
			payment.setAmount(request.getAmount());
			payment.setPaymentMethod(request.getPaymentMethod());
			payment.setIdempotencyKey(request.getIdempotencyKey());
			payment.setStatus(PaymentStatus.PENDING);
			LocalDateTime now = LocalDateTime.now();

			payment.setCreatedAt(now);
			payment.setUpdatedAt(now);
			
			payment = paymentRepository.save(payment);
			
			
			try {
				PaymentGatewayResponse gatewayResponse =
    			        paymentGatewayClient.processPayment(
    			                payment.getAmount(),
    			                payment.getPaymentMethod()
    			        );
				
				 payment.setGatewayPaymentId(
				            gatewayResponse.getGatewayPaymentId()
				    );

    		  if (gatewayResponse.isSuccess()) {
    			    payment.setStatus(PaymentStatus.SUCCESS);
    			} else {
    			    payment.setStatus(PaymentStatus.FAILED);
    			}
			payment.setUpdatedAt(LocalDateTime.now());
			
			paymentRepository.save(payment);
			
			}
			catch (Exception ex) {
				log.error(
				        "Payment processing failed unexpectedly for paymentId={}",
				        payment.getPaymentId(),
				        ex
				    );
			}
			
			return mapToResponse(payment);
		}
		else {
			
		    Payment payment=p.get();
		    
		    if (payment.getStatus() == PaymentStatus.PENDING) {
		    	  try {
		    		  PaymentGatewayResponse gatewayResponse =
		    			        paymentGatewayClient.processPayment(
		    			                payment.getAmount(),
		    			                payment.getPaymentMethod()
		    			        );
		    		  payment.setGatewayPaymentId(
		    		            gatewayResponse.getGatewayPaymentId()
		    		    );

		    		  if (gatewayResponse.isSuccess()) {
		    			    payment.setStatus(PaymentStatus.SUCCESS);
		    			} else {
		    			    payment.setStatus(PaymentStatus.FAILED);
		    			}

		    	        payment.setUpdatedAt(LocalDateTime.now());
		    	        paymentRepository.save(payment);

		    	    } catch (Exception ex) {

		    	        log.error(
		    	                "Payment retry failed unexpectedly for paymentId={}",
		    	                payment.getPaymentId(),
		    	                ex
		    	        );
		    	    }
		    }
	        
	        return mapToResponse(payment);
		}
			
	}
	
	private PaymentResponse mapToResponse(Payment payment) {

	    PaymentResponse response = new PaymentResponse();

	    response.setPaymentId(payment.getPaymentId());
	    response.setOrderId(payment.getOrderId());
	    response.setAmount(payment.getAmount());
	    response.setPaymentMethod(payment.getPaymentMethod());
	    response.setStatus(payment.getStatus());

	    return response;
	}
	
	@Override
	public void handleWebhook(PaymentWebhookRequest request) {

	    Optional<Payment> paymentOptional =
	            paymentRepository.findByGatewayPaymentId(
	                    request.getGatewayPaymentId()
	            );

	    if (paymentOptional.isEmpty()) {
	        throw new PaymentNotFoundException(
	                "Payment not found for gatewayPaymentId: "
	                        + request.getGatewayPaymentId()
	        );
	    }

	    Payment payment = paymentOptional.get();

	    if (payment.getAmount().compareTo(request.getAmount()) != 0) {
	    	throw new PaymentAmountMismatchException(
	    	        "Payment amount mismatch for gatewayPaymentId: "
	    	                + request.getGatewayPaymentId()
	    	);
	    }

	    if (payment.getStatus() != PaymentStatus.PENDING) {
	        return;
	    }

	    payment.setStatus(request.getStatus());
	    payment.setUpdatedAt(LocalDateTime.now());

	    paymentRepository.save(payment);
	}

}