package com.example.demo.domains.auth.utils;

import com.example.demo.domains.auth.dto.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTGen {
    static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public TokenDTO generateToken(Long id, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", String.valueOf(id));
        claims.put("role", role);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // token válido por 24 horas
                .signWith(KEY)
                .compact();

        return new TokenDTO(jwt);
    }

    public Claims getClaims(String token) throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new Exception("Token inválido");
        }
    }
}

