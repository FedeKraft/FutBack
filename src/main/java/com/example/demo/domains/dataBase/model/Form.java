package com.example.demo.domains.dataBase.model;

import jakarta.persistence.*;

@Entity
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @Column(name = "goals_in_favor", nullable = false)
    private int goalsInFavor;
    @Column(name = "goals_against", nullable = false)
    private int goalsAgainst;
    @Column(name = "punctuality", nullable = false)
    private String punctuality;
    @Column(name = "fair_play", nullable = false)
    private String fairPlay;
    @Column(name = "comment")
    private String comment;


    public Form(int goalsInFavor, int goalsAgainst, String punctuality, String fairPlay, String comment) {
        this.goalsInFavor = goalsInFavor;
        this.goalsAgainst = goalsAgainst;
        this.punctuality = punctuality;
        this.fairPlay = fairPlay;
        this.comment = comment;
    }

    public Form() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getGoalsInFavor() {
        return goalsInFavor;
    }

    public void setGoalsInFavor(int goalsInFavor) {
        this.goalsInFavor = goalsInFavor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public String getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(String punctuality) {
        this.punctuality = punctuality;
    }

    public String getFairPlay() {
        return fairPlay;
    }

    public void setFairPlay(String fairPlay) {
        this.fairPlay = fairPlay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}