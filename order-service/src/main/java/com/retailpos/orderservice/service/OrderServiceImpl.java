package com.retailpos.orderservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retailpos.orderservice.client.InventoryClient;

import com.retailpos.orderservice.dto.OrderItemRequest;
import com.retailpos.orderservice.dto.ProductResponse;
import com.retailpos.orderservice.entity.Order;
import com.retailpos.orderservice.entity.OrderItem;
import com.retailpos.orderservice.entity.OrderStatus;
import com.retailpos.orderservice.exception.InsufficientStockException;
import com.retailpos.orderservice.exception.InvalidOrderStatusException;
import com.retailpos.orderservice.exception.OrderNotFoundException;

import com.retailpos.orderservice.repository.OrderItemRepository;
import com.retailpos.orderservice.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
   
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    
    
  

    @Override
    @Transactional
    public Long createOrder() {

        Order order = new Order();

        order.setOrderNumber(
                "ORD-" + System.currentTimeMillis());

        order.setStatus(OrderStatus.DRAFT);

        order.setTotalAmount(BigDecimal.ZERO);

        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder =
                orderRepository.save(order);

        return savedOrder.getId();
    }
    
    
    
    /*
     * Adds a product to a draft order.
     *
     * 1. Find and validate the order.
     * 2. Get product details and price from Product Service.
     * 3. Check available stock from Inventory Service.
     * 4. If the product already exists in the order, increase its quantity.
     * 5. Otherwise, create a new OrderItem.
     * 6. Recalculate the item total and overall order total.
     */

    @Override
    @Transactional
    public void addItemToOrder(
            Long orderId,
            OrderItemRequest request) {

        Order order = orderRepository.findById(orderId)
        		.orElseThrow(() ->
                new OrderNotFoundException(
                        "Order not found with Id : " + orderId));

        if (order.getStatus() != OrderStatus.DRAFT) {
        	throw new InvalidOrderStatusException(
        	        "Items can only be added to a draft order");
        }

        ProductResponse product =
                productServiceClient.getProduct(
                        request.getProductId());

        Optional<OrderItem> existingItem =
                orderItemRepository.findByOrderIdAndProductId(
                        orderId,
                        request.getProductId());

        Integer availableQuantity =
                inventoryClient.getAvailableQuantity(
                        request.getProductId());

        if (existingItem.isPresent()) {

            OrderItem item = existingItem.get();

            BigDecimal oldItemTotal =
                    item.getTotalPrice();

            int newQuantity =
                    item.getQuantity()
                            + request.getQuantity();

            if (newQuantity > availableQuantity) {
            	throw new InsufficientStockException(
            	        "Insufficient stock for Product Id : "
            	                + request.getProductId());
            }

            item.setQuantity(newQuantity);

            BigDecimal newItemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            newQuantity));

            item.setTotalPrice(newItemTotal);

            orderItemRepository.save(item);

            order.setTotalAmount(
                    order.getTotalAmount()
                            .subtract(oldItemTotal)
                            .add(newItemTotal));

        } else {

            if (request.getQuantity()
                    > availableQuantity) {

            	throw new InsufficientStockException(
            	        "Insufficient stock for Product Id : "
            	                + request.getProductId());
            }

            OrderItem item = new OrderItem();

            item.setOrder(order);

            item.setProductId(product.getId());

            item.setQuantity(request.getQuantity());

            item.setUnitPrice(product.getPrice());

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            request.getQuantity()));

            item.setTotalPrice(itemTotal);

            orderItemRepository.save(item);

            order.setTotalAmount(
                    order.getTotalAmount()
                            .add(itemTotal));
        }

        orderRepository.save(order);
    }
}