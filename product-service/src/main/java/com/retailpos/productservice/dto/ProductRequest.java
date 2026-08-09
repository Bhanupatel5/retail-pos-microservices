package com.retailpos.productservice.dto;

import java.math.BigDecimal;

import com.retailpos.productservice.enums.Category;

public class ProductRequest {

    private String productName;
    private String description;
    private Category category;
    private String brand;
    private BigDecimal price;

    public ProductRequest() {
    }

    public ProductRequest(String productName, String description, Category category, String brand,
            BigDecimal price) {
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.price = price;
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
}