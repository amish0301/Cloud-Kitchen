package com.example.restaurant_service.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Guards the internal service-to-service API (/internal/**) with a shared secret.
 *
 * This is the mirror image of {@code GatewayHeaderAuthFilter}: the gateway trusts
 * upstream headers it sets itself, and here we trust callers that present the
 * agreed X-Internal-Secret. Wired into the dedicated /internal/** security chain
 * by {@code SecurityConfig} (constructor-injected secret), so it is deliberately
 * NOT a @Component (avoids double registration).
 */
public class InternalSecretFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Internal-Secret";

    private final String expectedSecret;

    public InternalSecretFilter(String expectedSecret) {
        this.expectedSecret = expectedSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String provided = request.getHeader(HEADER);
        if (provided == null || !provided.equals(expectedSecret)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Invalid or missing internal secret\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
