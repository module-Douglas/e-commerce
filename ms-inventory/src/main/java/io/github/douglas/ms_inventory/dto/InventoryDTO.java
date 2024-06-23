package io.github.douglas.ms_inventory.dto;

import io.github.douglas.ms_inventory.model.entity.Inventory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

public record InventoryDTO(
        Long id,
        Long productId,
        BigDecimal unitValue,
        Long stockAmount
) {
    public InventoryDTO(Inventory inventory) {
        this(inventory.getId(), inventory.getProductId(), inventory.getUnitValue(), inventory.getStockAmount());
    }
}
