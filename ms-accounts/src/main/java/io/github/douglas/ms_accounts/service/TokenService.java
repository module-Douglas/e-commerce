package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.AccessTokenDTO;
import io.github.douglas.ms_accounts.dto.RefreshTokenDTO;
import io.github.douglas.ms_accounts.model.entity.User;

public interface TokenService {
    AccessTokenDTO generateTokens(User user);

    AccessTokenDTO generateTokensByRefreshToken(RefreshTokenDTO request);
}
