package com.retailpos.inventoryservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retailpos.inventoryservice.dto.InventoryRequest;
import com.retailpos.inventoryservice.dto.InventoryResponse;
import com.retailpos.inventoryservice.dto.StockRequest;
import com.retailpos.inventoryservice.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request) {

        InventoryResponse response =
                inventoryService.createInventory(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {

        List<InventoryResponse> response =
                inventoryService.getAllInventory();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(
            @PathVariable Long productId) {

        InventoryResponse response =
                inventoryService.getInventoryByProductId(productId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryRequest request) {

        InventoryResponse response =
                inventoryService.updateInventory(
                        productId,
                        request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable Long productId) {

        inventoryService.deleteInventory(productId);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{productId}/availability")
    public ResponseEntity<Integer> getAvailableQuantity(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getAvailableQuantity(productId));
    }
    @PostMapping("/{productId}/add")
    public ResponseEntity<InventoryResponse> addStock(
            @PathVariable Long productId,
            @Valid @RequestBody StockRequest request) {

        InventoryResponse response =
                inventoryService.addStock(productId, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/reduce")
    public ResponseEntity<InventoryResponse> reduceStock(
            @PathVariable Long productId,
            @Valid @RequestBody StockRequest request) {

        InventoryResponse response =
                inventoryService.reduceStock(productId, request);

        return ResponseEntity.ok(response);
    }
}