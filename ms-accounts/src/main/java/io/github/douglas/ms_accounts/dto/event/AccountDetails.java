package io.github.douglas.ms_accounts.dto.event;

import io.github.douglas.ms_accounts.model.entity.Account;

import java.util.UUID;

public record AccountDetails(
        UUID userId,
        String email
) {
    public static AccountDetails setAccountDetails(Account user) {
        return new AccountDetails(
                user.getId(),
                user.getEmail()
        );
    }
}
