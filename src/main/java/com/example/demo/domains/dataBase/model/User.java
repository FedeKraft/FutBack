package com.example.demo.domains.dataBase.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "player_amount", nullable = false)
    private String playerAmount;
    @Column(name = "number", nullable = false)
    private String number;
    @Enumerated(EnumType.STRING)
    private UserStatus status;


    public User(String name, String email, String password, String city, String playerAmount, String number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.playerAmount = playerAmount;
        this.number = number;
        this.status = UserStatus.ACTIVE;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(String playerAmount) {
        this.playerAmount = playerAmount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserStatus getStatus() { return status; }

    public void setStatus(UserStatus status) { this.status = status; }
}
