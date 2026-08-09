package com.retailpos.productservice.service;

import java.util.List;

import com.retailpos.productservice.dto.ProductRequest;
import com.retailpos.productservice.dto.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

}