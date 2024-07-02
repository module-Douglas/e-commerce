package io.github.douglas.ms_inventory.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_inventory_historic")
public class InventoryHistoric {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;
    private String orderId;
    private String transactionId;
    private Long orderQuantity;
    private Long oldQuantity;
    private Long newQuantity;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public InventoryHistoric() {

    }

    public InventoryHistoric(Inventory inventory, String orderId, String transactionId, Long orderQuantity, Long oldQuantity, Long newQuantity) {
        this.inventory = inventory;
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.orderQuantity = orderQuantity;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
    }

    public InventoryHistoric(UUID id, Inventory inventory, String orderId, String transactionId, Long orderQuantity, Long oldQuantity, Long newQuantity) {
        this.id = id;
        this.inventory = inventory;
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.orderQuantity = orderQuantity;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Long getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(Long oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public Long getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Long newQuantity) {
        this.newQuantity = newQuantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
