package io.github.douglas.ms_inventory.model.entity;

import io.github.douglas.ms_inventory.dto.InventoryDTO;
import jakarta.persistence.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private BigDecimal unitValue;
    private Long stockAmount;

    public Inventory() {

    }

    public Inventory(InventoryDTO inventoryDTO) {
        BeanUtils.copyProperties(inventoryDTO, this);
    }

    public Inventory(Long id, Long productId, BigDecimal unitValue, Long stockAmount) {
        this.id = id;
        this.productId = productId;
        this.unitValue = unitValue;
        this.stockAmount = stockAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public Long getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Long stockAmount) {
        this.stockAmount = stockAmount;
    }
}
