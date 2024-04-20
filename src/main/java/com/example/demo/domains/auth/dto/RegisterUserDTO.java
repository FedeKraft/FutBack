package com.example.demo.domains.auth.dto;

public class RegisterUserDTO {
    public String name;
    public String password;
    public String email;
    public String city;
    public String playerAmount;
    public String number;



    public RegisterUserDTO(String name, String password, String email, String localidad, String playerAmount, String numero) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.city = localidad;
        this.playerAmount = playerAmount;
        this.number = numero;
    }
}
