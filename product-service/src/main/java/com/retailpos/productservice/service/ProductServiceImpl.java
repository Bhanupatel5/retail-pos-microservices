package com.retailpos.productservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailpos.productservice.dto.ProductRequest;
import com.retailpos.productservice.dto.ProductResponse;
import com.retailpos.productservice.entity.Product;
import com.retailpos.productservice.entity.ProductCodeSequence;
import com.retailpos.productservice.exception.DuplicateProductException;
import com.retailpos.productservice.exception.ProductNotFoundException;
import com.retailpos.productservice.repository.ProductCodeSequenceRepository;
import com.retailpos.productservice.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
	private ProductCodeSequenceRepository productCodeSequenceRepository;
    
    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductCodeSequenceRepository productCodeSequenceRepository) {

        this.productRepository = productRepository;
        this.productCodeSequenceRepository = productCodeSequenceRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        // Check duplicate product
        if (productRepository.existsByProductNameIgnoreCaseAndBrandIgnoreCase(
                request.getProductName(),
                request.getBrand())) {

            throw new DuplicateProductException(
                    "Product already exists with Name : "
                            + request.getProductName()
                            + " and Brand : "
                            + request.getBrand());
        }

        // Generate sequence number
        ProductCodeSequence sequence =
                productCodeSequenceRepository.save(
                        new ProductCodeSequence());

        // Generate product code
        String productCode =
                generateProductCode(sequence.getId());

        // Create Product
        Product product = new Product();

        product.setProductCode(productCode);
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());

        // Save Product only once
        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findByActiveTrue();

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with Id : " + id));

        return mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with Id : " + id));

        if (productRepository.existsByProductNameIgnoreCaseAndBrandIgnoreCaseAndIdNot(
                request.getProductName(),
                request.getBrand(),
                id)) {

            throw new DuplicateProductException(
                    "Product already exists with Name : "
                            + request.getProductName()
                            + " and Brand : "
                            + request.getBrand());
        }

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with Id : " + id));

        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setProductCode(product.getProductCode());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setBrand(product.getBrand());
        response.setPrice(product.getPrice());
        response.setCreatedAt(product.getCreatedAt());

        return response;
    }

    private String generateProductCode(Long productId) {

        return "PRD" + String.format("%04d", productId);
    }
}