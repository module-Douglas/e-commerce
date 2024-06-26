package io.github.douglas.ms_product.dto.event;

public record DeliveryAddress(
        String addressId,
        String zipCode,
        String street,
        String neighborhood,
        String complement,
        String number,
        String city,
        String state
) {
}
