package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.dto.ProductDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.UUID;


public class Product {

    private UUID productId;
    private BigDecimal unitValue;
    private Long quantity;

    public Product() {
    }

    public Product(ProductDTO productDTO) {
        BeanUtils.copyProperties(productDTO, this);
    }

    public Product(UUID productId, BigDecimal unitValue, Long quantity) {
        this.productId = productId;
        this.unitValue = unitValue;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
