package io.github.douglas.ms_product.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.douglas.ms_product.dto.SupplierDTO;
import jakarta.persistence.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private String name;
    @Column(unique = true, length = 14)
    private String cnpj;
    @Column(unique = true)
    private String email;
    @Column(unique = true, length = 15)
    private String phoneNumber;
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();
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
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Supplier() {

    }

    public Supplier(UUID id) {
        this.id = id;
    }

    public Supplier(String name, String email, String cnpj, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.cnpj = cnpj;
        this.phoneNumber = phoneNumber;
    }

    public Supplier(UUID id, String name, String cnpj, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
