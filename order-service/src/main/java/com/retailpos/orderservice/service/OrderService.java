package com.retailpos.orderservice.service;

import com.retailpos.orderservice.dto.OrderItemRequest;

public interface OrderService {

    Long createOrder();

    void addItemToOrder(
            Long orderId,
            OrderItemRequest request);
}