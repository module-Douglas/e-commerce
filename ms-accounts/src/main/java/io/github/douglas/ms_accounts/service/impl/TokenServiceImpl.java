package io.github.douglas.ms_accounts.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.douglas.ms_accounts.config.exception.ValidationException;
import io.github.douglas.ms_accounts.dto.AccessTokenDTO;
import io.github.douglas.ms_accounts.model.entity.User;
import io.github.douglas.ms_accounts.model.repository.RefreshTokenRepository;
import io.github.douglas.ms_accounts.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static io.github.douglas.ms_accounts.enums.Sources.MS_ACCOUNTS;
import static java.lang.String.format;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Date ACCESS_TOKEN_DURATION = new Date(System.currentTimeMillis() + 86400000);
    private static final String REFRESH_TOKEN_PATTERN = "%s_%s_%s";
    private static final String TOKEN_TYPE = "Bearer";
    @Value("${spring.jwt.secret}")
    private static String JWT_SECRET;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AccessTokenDTO generateTokens(User user) {

        try {
            var accessToken = JWT.create()
                    .withIssuer(MS_ACCOUNTS.name())
                    .withSubject(user.getEmail())
                    .withClaim("roles", user.getRoles().stream().toList())
                    .withExpiresAt(ACCESS_TOKEN_DURATION)
                    .sign(Algorithm.HMAC512(JWT_SECRET));

            var refreshToken = generateRefreshToken();

            return new AccessTokenDTO(
                    accessToken,
                    refreshToken,
                    TOKEN_TYPE,
                    ACCESS_TOKEN_DURATION.toString()
            );
        } catch (Exception e) {
            throw new ValidationException(format("Something went wrong generation tokens: %s", e.getMessage()));
        }
    }

    private String generateRefreshToken() {
        return format(REFRESH_TOKEN_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID(), Math.random());
    }

}
