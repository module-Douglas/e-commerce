package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String transactionId;
    private AccountDetails accountDetails;
    private DeliveryAddress deliveryAddress;
    private BigDecimal totalAmount;
    private Long totalItems;
    private LocalDateTime createdAt;
    private Sources source;
    private Status status;
    private Set<Product> products = new HashSet<>();
    private Set<History> historic;

    public Order() {

    }

    public Order(String id, String transactionId, AccountDetails accountDetails, DeliveryAddress deliveryAddress, BigDecimal totalAmount, Long totalItems, LocalDateTime createdAt, Sources source, Status status, Set<Product> products, Set<History> historic) {
        this.id = id;
        this.transactionId = transactionId;
        this.accountDetails = accountDetails;
        this.deliveryAddress = deliveryAddress;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
        this.source = source;
        this.status = status;
        this.products = products;
        this.historic = historic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Sources getSource() {
        return source;
    }

    public void setSource(Sources source) {
        this.source = source;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<History> getHistoric() {
        return historic;
    }

    public void setHistoric(Set<History> historic) {
        this.historic = historic;
    }
}
