package com.retailpos.inventoryservice.service;

import java.util.List;

import com.retailpos.inventoryservice.dto.InventoryRequest;
import com.retailpos.inventoryservice.dto.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest request);

    List<InventoryResponse> getAllInventory();

    InventoryResponse getInventoryByProductId(Long productId);

    InventoryResponse updateInventory(Long productId, InventoryRequest request);

    void deleteInventory(Long productId);
}