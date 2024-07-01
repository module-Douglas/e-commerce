package io.github.douglas.ms_accounts.dto.event;

import io.github.douglas.ms_accounts.model.entity.User;

public record AccountDetails(
        String userId,
        String email
) {
    public static AccountDetails setAccountDetails(User user) {
        return new AccountDetails(
                user.getId().toString(),
                user.getEmail()
        );
    }
}
