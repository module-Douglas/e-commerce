package io.github.douglas.ms_inventory.service;

import io.github.douglas.ms_inventory.dto.InventoryDTO;
import io.github.douglas.ms_inventory.dto.event.Event;

import java.util.UUID;

public interface InventoryService {
    void registerInventory(String payload);

    InventoryDTO findByProductId(UUID productId);

    void orderUpdate(String payload);

    void rollbackInventory(String payload);
}
