package io.github.douglas.ms_inventory.service;

import io.github.douglas.ms_inventory.dto.InventoryDTO;
import io.github.douglas.ms_inventory.dto.event.Event;

public interface InventoryService {
    void registerInventory(String payload);

    InventoryDTO findByProductId(String productId);

    void orderUpdate(String payload);

    void rollbackInventory(Event event);
}
