package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.AccessTokenDTO;
import io.github.douglas.ms_accounts.model.entity.Account;

public interface TokenService {
    AccessTokenDTO generateTokens(Account user);

    AccessTokenDTO generateTokensByRefreshToken(String request);
}
