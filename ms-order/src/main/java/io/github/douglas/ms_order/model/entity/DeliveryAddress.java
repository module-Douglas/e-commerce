package io.github.douglas.ms_order.model.entity;

import java.util.UUID;

public class DeliveryAddress {

    private UUID addressId;
    private String zipCode;
    private String street;
    private String neighborhood;
    private String complement;
    private String number;
    private String city;
    private String state;

    public DeliveryAddress() {
    }

    public DeliveryAddress(UUID addressId) {
        this.addressId = addressId;
    }

    public DeliveryAddress(UUID addressId, String zipCode, String street, String neighborhood, String complement, String number, String city, String state) {
        this.addressId = addressId;
        this.zipCode = zipCode;
        this.street = street;
        this.neighborhood = neighborhood;
        this.complement = complement;
        this.number = number;
        this.city = city;
        this.state = state;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
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
}
