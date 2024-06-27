package io.github.douglas.ms_accounts.dto;

import io.github.douglas.ms_accounts.model.entity.Address;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddressDTO(
        UUID id,
        String zipCode,
        String street,
        String neighborhood,
        String complement,
        String number,
        String city,
        String state,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public AddressDTO(Address address) {
        this(
                address.getId(),
                address.getZipCode(),
                address.getStreet(),
                address.getNeighborhood(),
                address.getComplement(),
                address.getNumber(),
                address.getCity(),
                address.getState(),
                address.getCreatedAt(),
                address.getUpdatedAt()
        );
    }
}
