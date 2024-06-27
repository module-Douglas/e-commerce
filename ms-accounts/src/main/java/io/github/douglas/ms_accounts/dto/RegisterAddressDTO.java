package io.github.douglas.ms_accounts.dto;

import java.util.UUID;

public record RegisterAddressDTO(
        UUID userId,
        String zipCode,
        String street,
        String neighborhood,
        String complement,
        String number,
        String city,
        String state
) {
}
