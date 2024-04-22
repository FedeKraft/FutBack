package com.example.demo.domains.user.model;

import jakarta.persistence.*;


@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    @ManyToOne
    private User user;

}