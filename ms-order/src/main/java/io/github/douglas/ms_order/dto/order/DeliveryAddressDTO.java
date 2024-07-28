package io.github.douglas.ms_order.dto.order;

import java.util.UUID;

public record DeliveryAddressDTO(
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
