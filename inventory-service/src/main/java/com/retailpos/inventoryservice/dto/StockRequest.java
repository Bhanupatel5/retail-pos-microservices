package com.retailpos.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StockRequest {

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    public StockRequest() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}