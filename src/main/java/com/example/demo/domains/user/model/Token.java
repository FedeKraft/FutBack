package com.example.demo.domains.user.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tokens2")
public class Token {
    @Id
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name="name", nullable=false)
    private User user;

    public Token() {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}