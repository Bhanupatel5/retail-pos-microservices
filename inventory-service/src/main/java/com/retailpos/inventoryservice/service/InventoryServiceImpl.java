package com.retailpos.inventoryservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailpos.inventoryservice.client.ProductClient;
import com.retailpos.inventoryservice.dto.InventoryRequest;
import com.retailpos.inventoryservice.dto.InventoryResponse;
import com.retailpos.inventoryservice.dto.ProductResponse;
import com.retailpos.inventoryservice.dto.StockRequest;
import com.retailpos.inventoryservice.entity.Inventory;
import com.retailpos.inventoryservice.exception.DuplicateInventoryException;
import com.retailpos.inventoryservice.exception.InsufficientStockException;
import com.retailpos.inventoryservice.exception.InventoryNotFoundException;
import com.retailpos.inventoryservice.exception.ProductNotFoundException;
import com.retailpos.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public InventoryResponse createInventory(InventoryRequest request) {

    	ProductResponse product;

    	try {
    	    product = productClient.getProductById(request.getProductId());
    	} catch (Exception ex) {
    	    throw new ProductNotFoundException(
    	            "Product not found or inactive with Id : "
    	                    + request.getProductId());
    	}

    	if (product == null) {
    	    throw new ProductNotFoundException(
    	            "Product not found or inactive with Id : "
    	                    + request.getProductId());
    	}

        

        if (inventoryRepository.existsByProductId(
                request.getProductId())) {

            throw new DuplicateInventoryException(
                    "Inventory already exists for Product Id : "
                            + request.getProductId());
        }

        Inventory inventory = new Inventory();

        inventory.setProductId(request.getProductId());
        inventory.setQuantity(request.getQuantity());
        inventory.setReorderLevel(request.getReorderLevel());
        inventory.setActive(true);
        inventory.setCreatedAt(LocalDateTime.now());

        Inventory savedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(savedInventory);
    }
    
    @Override
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .filter(Inventory::getActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponse getInventoryByProductId(
            Long productId) {

        Inventory inventory =
                inventoryRepository
                        .findByProductIdAndActiveTrue(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for Product Id : "
                                                + productId));

        return mapToResponse(inventory);
    }

    @Override
    public InventoryResponse updateInventory(
            Long productId,
            InventoryRequest request) {

        Inventory inventory =
                inventoryRepository
                        .findByProductIdAndActiveTrue(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for Product Id : "
                                                + productId));

        if (!productId.equals(request.getProductId())) {
            throw new IllegalArgumentException(
                    "Product ID cannot be changed");
        }

        inventory.setQuantity(request.getQuantity());
        inventory.setReorderLevel(request.getReorderLevel());
        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    @Override
    public void deleteInventory(Long productId) {

        Inventory inventory =
                inventoryRepository
                        .findByProductIdAndActiveTrue(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for Product Id : "
                                                + productId));

        inventory.setActive(false);
        inventory.setUpdatedAt(LocalDateTime.now());

        inventoryRepository.save(inventory);
    }

    private InventoryResponse mapToResponse(
            Inventory inventory) {

        InventoryResponse response =
                new InventoryResponse();

        response.setId(inventory.getId());
        response.setProductId(inventory.getProductId());
        response.setQuantity(inventory.getQuantity());
        response.setReorderLevel(inventory.getReorderLevel());
        response.setActive(inventory.getActive());
        response.setCreatedAt(inventory.getCreatedAt());
        response.setUpdatedAt(inventory.getUpdatedAt());

        return response;
    }
    
    @Override
    public InventoryResponse addStock(Long productId, StockRequest request) {

        Inventory inventory =
                inventoryRepository
                        .findByProductIdAndActiveTrue(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for Product Id : "
                                                + productId));

        inventory.setQuantity(
                inventory.getQuantity() + request.getQuantity());

        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    @Override
    public InventoryResponse reduceStock(
            Long productId,
            StockRequest request) {

        Inventory inventory =
                inventoryRepository
                        .findByProductIdAndActiveTrue(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for Product Id : "
                                                + productId));

        int currentQuantity = inventory.getQuantity();
        int reduceQuantity = request.getQuantity();

        if (reduceQuantity > currentQuantity) {
            throw new InsufficientStockException(
                    "Insufficient stock for Product Id : "
                            + productId);
        }

        inventory.setQuantity(
                currentQuantity - reduceQuantity);

        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }
}