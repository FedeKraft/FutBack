package com.example.demo.domains.auth.dto;

public class FormDTO {

    public Long fromNotificationId;
    public int goalsInFavor;
    public int goalsAgainst;
    public String fairPlay;
    public String comment;


    public FormDTO(int goalsInFavor, int goalsAgainst, String fairPlay, String comment) {
        this.goalsInFavor = goalsInFavor;
        this.goalsAgainst = goalsAgainst;
        this.fairPlay = fairPlay;
        this.comment = comment;
    }
}
