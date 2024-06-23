package io.github.douglas.ms_inventory.service.impl;

import io.github.douglas.ms_inventory.dto.InventoryDTO;
import io.github.douglas.ms_inventory.model.repository.InventoryRepository;
import io.github.douglas.ms_inventory.service.InventoryService;
import io.github.douglas.ms_inventory.utils.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final JsonUtil jsonUtil;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, JsonUtil jsonUtil) {
        this.inventoryRepository = inventoryRepository;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void registerInventory(String payload) {
        inventoryRepository.save(
                jsonUtil.toInventory(payload)
        );
    }

    @Override
    public InventoryDTO findByProductId(Long productId) {
        return new InventoryDTO(
                inventoryRepository.findByProductId(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );
    }
}
