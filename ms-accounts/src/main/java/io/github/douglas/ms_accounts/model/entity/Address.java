package io.github.douglas.ms_accounts.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.douglas.ms_accounts.dto.AddressDTO;
import jakarta.persistence.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String zipCode;
    private String street;
    private String neighborhood;
    private String complement;
    private String number;
    private String city;
    private String  state;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonManagedReference
    private Account account;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Address() {

    }

    public Address(AddressDTO addressDTO) {
        BeanUtils.copyProperties(addressDTO, this);
    }

    public Address(UUID id, String zipCode, String street, String neighborhood, String complement, String number, String city, String state) {
        this.id = id;
        this.zipCode = zipCode;
        this.street = street;
        this.neighborhood = neighborhood;
        this.complement = complement;
        this.number = number;
        this.city = city;
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
