package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = System.getenv("JWT_SECRET");
    private static final long EXPIRATION = 1000 * 60 * 15;

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String getToken(Long userId, Role role){
        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", "access")
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserId(String token){
        return this.parseToken(token).getSubject();
    }

    public String getRole(String token){
        return this.parseToken(token).get("role", String.class);
    }

    public boolean validate(String token){
        try{
            this.parseToken(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
