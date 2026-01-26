package com.example.api_gateway.filter;

import com.example.api_gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwAuthenticationFilter implements GlobalFilter, Ordered {
    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(JwAuthenticationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.debug("Incoming Request : {}", path);

        // Public path than Skip
        if (path.startsWith("/auth")) return chain.filter(exchange);

        // Verification
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing Authorization header for path: {}", path);
            return unauthorized(exchange, "Invalid or Expired Token");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.isTokenValid(token)) {
            log.warn("Invalid or expired JWT for path: {}", path);
            return unauthorized(exchange, "Invalid or expired token");
        }

        String userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractUserRole(token);

        log.debug("Authenticated request: userId={}, role={}, path={}", userId, role, path);

        // Forwarding user info in Header
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().header("X-User-Id", userId).header("X-User-Role", role).build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

}
