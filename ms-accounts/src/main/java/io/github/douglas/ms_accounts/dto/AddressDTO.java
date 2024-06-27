package io.github.douglas.ms_accounts.dto;

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
}
