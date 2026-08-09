package com.retailpos.productservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.retailpos.productservice.enums.Category;

public class ProductResponse {

    private Long id;
    private String productCode;
    private String productName;
    private String description;
    private Category category;
    private String brand;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String productCode, String productName, String description,
            Category category, String brand, BigDecimal price, LocalDateTime createdAt) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}