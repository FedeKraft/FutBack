package com.example.demo.domains.auth.dto;

public class LoginUserDTO {
    public String name;
    public String password;

    public LoginUserDTO(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
