package com.retailpos.orderservice.service;

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
   

        return productClient.getProductById(productId);
    }

    public ProductResponse productFallback(
            Long productId,
            Exception ex) {

        if (ex instanceof FeignException.NotFound) {
            throw new ProductNotFoundException(
                    "Product not found with Id : " + productId);
        }

        throw new ProductServiceUnavailableException(
                "Product Service is currently unavailable");
    }
}