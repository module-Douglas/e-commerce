package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.config.exception.ValidationException;
import io.github.douglas.ms_accounts.dto.AccessTokenDTO;
import io.github.douglas.ms_accounts.model.entity.RefreshToken;
import io.github.douglas.ms_accounts.model.entity.Account;
import io.github.douglas.ms_accounts.model.repository.RefreshTokenRepository;
import io.github.douglas.ms_accounts.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.kafka.common.errors.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static io.github.douglas.ms_accounts.enums.Sources.MS_ACCOUNTS;
import static java.lang.String.format;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Date ACCESS_TOKEN_DURATION = new Date(System.currentTimeMillis() + 86400000);
    private static final String REFRESH_TOKEN_PATTERN = "%s_%s_%s";
    private static final String TOKEN_TYPE = "Bearer";
    @Value("${spring.jwt.secret}")
    private String JWT_SECRET;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AccessTokenDTO generateTokens(Account user) {
        try {
            var claims = generateClaims(user);

            var accessToken = Jwts.builder()
                    .claim("roles", claims)
                    .setIssuer(MS_ACCOUNTS.name())
                    .setSubject(user.getEmail())
                    .setExpiration(ACCESS_TOKEN_DURATION)
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET)))
                    .compact();

            var refreshToken = generateRefreshToken(user);
            refreshTokenRepository.save(refreshToken);

            return new AccessTokenDTO(
                    accessToken,
                    TOKEN_TYPE,
                    refreshToken.getRefreshToken(),
                    ACCESS_TOKEN_DURATION.toString()
            );
        } catch (Exception e) {
            throw new ValidationException(format("Something went wrong generation tokens: %s", e.getMessage()));
        }
    }

    @Override
    public AccessTokenDTO generateTokensByRefreshToken(String request) {
        var refreshToken = refreshTokenRepository.findByRefreshToken(request)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token. Please do login again."));
        verifyExpiration(refreshToken);

        return generateTokens(refreshToken.getUser());
    }

    private ArrayList<String> generateClaims(Account user) {
        ArrayList<String> claims = new ArrayList<>();

        user.getRoles().forEach(role -> claims.add(role.getName()));
        return claims;
    }

    private RefreshToken generateRefreshToken(Account user) {
        var refreshToken = refreshTokenRepository.findByUser(user);
        refreshToken.ifPresent(refreshTokenRepository::delete);

        return new RefreshToken(
                format(REFRESH_TOKEN_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID(), Math.random()),
                user
        );
    }

    private void verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new AuthenticationException("Refresh token expired. Please do login again.");
        }
    }

}
