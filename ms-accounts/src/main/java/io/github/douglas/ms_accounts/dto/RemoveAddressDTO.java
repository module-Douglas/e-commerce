package io.github.douglas.ms_accounts.dto;

import java.util.UUID;

public record RemoveAddressDTO(
        UUID accountId,
        UUID addressId
) {
}
