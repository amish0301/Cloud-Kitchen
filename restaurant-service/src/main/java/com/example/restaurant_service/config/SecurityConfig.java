package com.example.restaurant_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.restaurant_service.auth.GatewayHeaderAuthFilter;
import com.example.restaurant_service.auth.InternalSecretFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // enables @PreAuthorize on controller methods
public class SecurityConfig {

    @Autowired
    private GatewayHeaderAuthFilter gatewayHeaderAuthFilter;

    @Value("${internal.api.secret}")
    private String internalApiSecret;

    /**
     * Dedicated chain for the internal service-to-service API. Because it declares a
     * {@code securityMatcher}, only /internal/** requests hit this chain, and @Order(1)
     * makes Spring evaluate it before the general app chain below.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain internalFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/internal/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(new InternalSecretFilter(internalApiSecret), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * General application chain for everything else — trusts the gateway-set identity
     * headers (X-User-Id / X-User-Role) gateway filter as it is.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(gatewayHeaderAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
