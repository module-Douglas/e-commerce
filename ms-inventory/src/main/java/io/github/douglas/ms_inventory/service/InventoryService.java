package io.github.douglas.ms_inventory.service;

import io.github.douglas.ms_inventory.dto.InventoryDTO;

public interface InventoryService {
    void registerInventory(String payload);

    InventoryDTO findByProductId(Long productId);
}
