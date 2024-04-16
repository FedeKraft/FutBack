package com.example.demo.domains.auth.dto;

public class LoginUserDTO {
    public String email;
    public String password;

    public LoginUserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
