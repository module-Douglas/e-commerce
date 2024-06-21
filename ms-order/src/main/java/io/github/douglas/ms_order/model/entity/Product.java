package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.dto.ProductDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;


public class Product {

    private Long productId;
    private BigDecimal unitValue;
    private Long quantity;

    public Product() {
    }

    public Product(ProductDTO productDTO) {
        BeanUtils.copyProperties(productDTO, this);
    }

    public Product(Long productId, BigDecimal unitValue, Long quantity) {
        this.productId = productId;
        this.unitValue = unitValue;
        this.quantity = quantity;
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
