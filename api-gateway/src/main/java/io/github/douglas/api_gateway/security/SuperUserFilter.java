package io.github.douglas.api_gateway.security;

import io.github.douglas.api_gateway.config.exception.UnauthorizedException;
import io.github.douglas.api_gateway.config.exception.ValidationException;
import io.github.douglas.api_gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SuperUserFilter extends AbstractGatewayFilterFactory<SuperUserFilter.Config> {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String REQUIRED_ROLE = "ROLE_ADMIN";

    private final JwtUtil jwtUtil;

    public SuperUserFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public  GatewayFilter apply(Config config) {
        return this::filter;
    }

    private Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isPrefixMissing(request) || isAuthHeaderMissing(request)) {
            throw new ValidationException("Authorization header is missing in request.");
        }

        final String token = getAuthHeader(request);

        jwtUtil.verifyTokenExpiration(token);
        var roles = jwtUtil.getRoles(token);

        if (!roles.contains(REQUIRED_ROLE)) {
            throw new UnauthorizedException("Access denied to this resource.");
        }

        return chain.filter(exchange);
    }

    private Boolean isPrefixMissing(ServerHttpRequest request) {
        var header = request.getHeaders().getFirst("Authorization");
        return header == null || !header.startsWith(TOKEN_PREFIX);
    }

    private Boolean isAuthHeaderMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private String getAuthHeader(ServerHttpRequest request) {
        var header = request.getHeaders().getOrEmpty("Authorization").get(0);
        return header.replace(TOKEN_PREFIX, "").trim();
    }

    public static class Config {

    }

}
