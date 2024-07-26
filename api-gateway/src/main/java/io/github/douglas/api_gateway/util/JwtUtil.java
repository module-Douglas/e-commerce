package io.github.douglas.api_gateway.util;

import io.github.douglas.api_gateway.config.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String JWT_SECRET;

    public List<String> getRoles(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object roles = claims.get("roles");
        if (roles instanceof ArrayList<?>) {
            return (ArrayList<String>) roles;
        }
        return Collections.emptyList();
    }

    public void verifyTokenExpiration(String token) {
        var expiration = getAllClaimsFromToken(token).getExpiration();
        if (expiration.before(new Date())) {
            throw new UnauthorizedException("Expired Access Token. Please do login again.");
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET)))
                .build().parseClaimsJws(token).getBody();
    }
}
