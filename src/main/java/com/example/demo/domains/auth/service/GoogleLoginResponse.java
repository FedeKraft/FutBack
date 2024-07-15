package com.example.demo.domains.auth.service;

import com.example.demo.domains.auth.dto.TokenDTO;

public class GoogleLoginResponse {
    public boolean userExists;
    public TokenDTO token;

    public GoogleLoginResponse(boolean userExists, TokenDTO token) {
        this.userExists = userExists;
        this.token = token;
    }
}
