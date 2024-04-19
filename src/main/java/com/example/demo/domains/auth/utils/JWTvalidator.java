package com.example.demo.domains.auth.utils;

import io.jsonwebtoken.Claims;

public class JWTvalidator {
    private final JWTGen jwtGen;
    public JWTvalidator(JWTGen jwtGen) {
        this.jwtGen = jwtGen;
    }
    public Long getID(String token) throws Exception {
        String jwt = token.substring(7);
        Claims claims = jwtGen.getClaims(jwt);
        return Long.valueOf(claims.get("id").toString());
    }

}
