package com.example.demo.domains.dataBase.model;

import jakarta.persistence.*;


@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "message", nullable = false)
    private String message;
    @Column(name = "responded", nullable = false)
    private boolean responded;
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;
    @ManyToOne
    private Match match;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResponded() {
        return responded;
    }

    public void setResponded(boolean responded) {
        this.responded = responded;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Match getMatch() { return match; }

    public void setMatch(Match match) { this.match = match; }
}