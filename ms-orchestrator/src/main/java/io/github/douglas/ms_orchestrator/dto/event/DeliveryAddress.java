package io.github.douglas.ms_orchestrator.dto.event;

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
}
