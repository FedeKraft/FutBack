package com.example.demo.domains.dataBase.model;

import jakarta.persistence.*;


@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;
    @Enumerated(EnumType.STRING)
    private MatchStatus status;
    @OneToOne
    private Form fromUserForm;
    @OneToOne
    private Form toUserForm;


    public Match(User fromUser, User toUser, MatchStatus status) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
        this.fromUserForm = null;
        this.toUserForm = null;
    }

    public Match() {
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Form getFromUserForm() {
        return fromUserForm;
    }

    public void setFromUserForm(Form fromUserForm) {
        this.fromUserForm = fromUserForm;
    }

    public Form getToUserForm() {
        return toUserForm;
    }

    public void setToUserForm(Form toUserForm) {
        this.toUserForm = toUserForm;
    }
}