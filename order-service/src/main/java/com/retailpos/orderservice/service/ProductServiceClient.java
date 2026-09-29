package com.retailpos.orderservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.retailpos.orderservice.client.ProductClient;
import com.retailpos.orderservice.dto.ProductResponse;
import com.retailpos.orderservice.exception.ProductNotFoundException;
import com.retailpos.orderservice.exception.ProductServiceUnavailableException;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import io.github.resilience4j.retry.annotation.Retry;
@Service
public class ProductServiceClient {

	
	private static final Logger log =
	        LoggerFactory.getLogger(ProductServiceClient.class);
    private final ProductClient productClient;

    public ProductServiceClient(ProductClient productClient) {
        this.productClient = productClient;
    }
    @Retry(name = "productService")
    @CircuitBreaker(
            name = "productService",
            fallbackMethod = "productFallback"
    )
    public ProductResponse getProduct(Long productId) {
   
    	log.debug("Fetching product productId={}", productId);
        return productClient.getProductById(productId);
    }

    public ProductResponse productFallback(
            Long productId,
            Exception ex) {

        if (ex instanceof FeignException.NotFound) {
        	
        	log.warn("Product not found productId={}", productId);
            throw new ProductNotFoundException(
                    "Product not found with Id : " + productId);
        }
        
        log.error(
                "Product service unavailable productId={}",
                productId,
                ex
        );

        throw new ProductServiceUnavailableException(
                "Product Service is currently unavailable");
    }
}