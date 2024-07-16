package com.example.demo.domains.dataBase.model;

import jakarta.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;
    @ManyToOne
    private Form fromUserForm;
    @ManyToOne
    private Form toUserForm;
    @Column
    private String comment;


    public Report(User fromUser, User toUser, Form fromUserForm, Form toUserForm,String comment) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.fromUserForm = fromUserForm;
        this.toUserForm = toUserForm;
        this.comment = comment;
    }

    public Report() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
