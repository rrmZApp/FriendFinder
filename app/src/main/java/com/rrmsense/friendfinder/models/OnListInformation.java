package com.rrmsense.friendfinder.models;

/**
 * Created by Talha on 3/1/2017.
 */

public class OnListInformation {
    String email;
    Boolean status;

    public OnListInformation() {
    }

    public OnListInformation(String email, Boolean status) {
        this.email = email;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
