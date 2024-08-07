package io.github.douglas.ms_accounts.dto.event;

import io.github.douglas.ms_accounts.model.entity.Address;

import java.util.UUID;

public record DeliveryAddress(
        UUID addressId,
        String zipCode,
        String street,
        String neighborhood,
        String complement,
        String number,
        String city,
        String state
) {
    public static DeliveryAddress setAddress(Address address) {
        return new DeliveryAddress(
                address.getId(),
                address.getZipCode(),
                address.getStreet(),
                address.getNeighborhood(),
                address.getComplement(),
                address.getNumber(),
                address.getCity(),
                address.getState()
        );
    }
}
