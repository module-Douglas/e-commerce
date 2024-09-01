package io.github.douglas.ms_inventory.dto;

import io.github.douglas.ms_inventory.enums.Status;
import io.github.douglas.ms_inventory.model.entity.Inventory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InventoryDTO(
        UUID id,
        UUID productId,
        BigDecimal unitValue,
        Long stockAmount,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public InventoryDTO(Inventory inventory) {
        this(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getUnitValue(),
                inventory.getStockAmount(),
                inventory.getStatus(),
                inventory.getCreatedAt(),
                inventory.getUpdatedAt()
        );
    }
}
