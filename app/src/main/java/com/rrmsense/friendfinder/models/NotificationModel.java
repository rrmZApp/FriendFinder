package com.rrmsense.friendfinder.models;

/**
 * Created by Talha on 3/4/2017.
 */

public class NotificationModel {
    String from;
    String to;
    String title;
    String body;
    String date;
    String type;

    public NotificationModel() {
    }

    public NotificationModel(String from, String to, String title, String body, String date, String type) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.body = body;
        this.date = date;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
