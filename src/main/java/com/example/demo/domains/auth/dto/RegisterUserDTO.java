package com.example.demo.domains.auth.dto;

public class RegisterUserDTO {
    public String name;
    public String password;
    public String email;


    public RegisterUserDTO(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
