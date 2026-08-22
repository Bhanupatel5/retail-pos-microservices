package com.retailpos.orderservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retailpos.orderservice.dto.OrderItemRequest;
import com.retailpos.orderservice.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder() {

        Long orderId = orderService.createOrder();

        return ResponseEntity.ok(orderId);
    }
    
    @PostMapping("/{orderId}/items")
    public ResponseEntity<Void> addItemToOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequest request) {

        orderService.addItemToOrder(orderId, request);

        return ResponseEntity.ok().build();
    }
}