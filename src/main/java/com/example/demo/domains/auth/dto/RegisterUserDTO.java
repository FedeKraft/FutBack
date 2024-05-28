package com.example.demo.domains.auth.dto;

public class RegisterUserDTO {
    public String name;
    public String password;
    public String email;
    public String city;
    public String playerAmount;
    public String number;
    public int elo;


    public RegisterUserDTO(String name, String password, String email, String city, String playerAmount, String number, int elo) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.city = city;
        this.playerAmount = playerAmount;
        this.number = number;
        this.elo = elo;
    }

}
