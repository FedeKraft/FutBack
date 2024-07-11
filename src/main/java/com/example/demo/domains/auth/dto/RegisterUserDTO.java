package com.example.demo.domains.auth.dto;

import com.example.demo.domains.dataBase.model.UserStatus;

public class RegisterUserDTO {
    public String name;
    public String password;
    public String email;
    public String city;
    public String playerAmount;

    public String number;

    public UserStatus status;
    public int elo;
    public String stars;


    public RegisterUserDTO(String name, String password, String email, String city, String playerAmount, String number, UserStatus status, int elo, String stars) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.city = city;
        this.playerAmount = playerAmount;
        this.number = number;
        this.status = status;
        this.elo = elo;
        this.stars = stars;
    }

}
