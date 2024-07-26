package io.github.douglas.ms_accounts.dto;

public record AccessTokenDTO(
        String accessToken,
        String tokenType,
        String refreshToken,
        String accessTokenExpiration
) {
}
