package com.example.user_service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtUtil {

    @Value("$auth.jwt.secret")
    private SecretKey secret;

    public Claims validateToken(String token) {
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
    }

    public String extractUserId(Claims claim) {
        return claim.getSubject();
    }

    public boolean isTokenExpired(String token) {
        return validateToken(token)
                .getExpiration()
                .before(new Date());
    }
}
