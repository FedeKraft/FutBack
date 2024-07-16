package com.example.demo.domains.dataBase.model;

import jakarta.persistence.*;


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
    @Column(name = "elo", nullable = false)
    private int elo;
    @Column(name = "stars", nullable = false)
    private String stars;
    private String resetToken;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    public User(String name, String email, String password, String city, String playerAmount, String number, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.playerAmount = playerAmount;
        this.number = number;
        this.status = UserStatus.ACTIVE;
        this.elo = 1000;
        this.stars = "5";
        this.role = role;
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

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int updateElo(int opponentElo, double result) {
        int k = 20;
        int newElo = (int) (elo + k * (result - 1 / (1 + Math.pow(10, (opponentElo - elo) / 400))));
        return newElo;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public void updateStars(String fairPlay, int totalMatches) {
        int fairPlayInt = Integer.parseInt(fairPlay);
        int starsInt = Integer.parseInt(stars);
        int newStarsInt = ((starsInt * totalMatches) + fairPlayInt) / (totalMatches + 1);
        setStars(String.valueOf(newStarsInt));
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
