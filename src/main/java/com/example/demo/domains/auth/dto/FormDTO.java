package com.example.demo.domains.auth.dto;

import com.example.demo.domains.dataBase.model.Notification;

public class FormDTO {

    public Long fromNotificationId;
    public int goalsInFavor;
    public int goalsAgainst;
    public String punctuality;
    public String fairPlay;
    public String comment;


    public FormDTO(int goalsInFavor, int goalsAgainst, String punctuality, String fairPlay, String comment) {
        this.goalsInFavor = goalsInFavor;
        this.goalsAgainst = goalsAgainst;
        this.punctuality = punctuality;
        this.fairPlay = fairPlay;
        this.comment = comment;
    }
}
