package io.github.douglas.ms_order.dto.order;

public record DeliveryAddressDTO(
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
