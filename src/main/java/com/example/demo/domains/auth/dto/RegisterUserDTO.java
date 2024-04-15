package com.example.demo.domains.auth.dto;

public class RegisterUserDTO {
    public String name;
    public String password;
    public String email;
    public String localidad;
    public String playerAmount;
    public String numero;



    public RegisterUserDTO(String name, String password, String email, String localidad, String playerAmount, String numero) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.localidad = localidad;
        this.playerAmount = playerAmount;
        this.numero = numero;
    }
}
