package com.rrmsense.friendfinder.models;


import java.util.ArrayList;

/**
 * Created by Talha on 2/23/2017.
 */

public class UserInformation {

    private String id;
    private String email;
    private String name;
    private String phone;
    private String latitude;
    private String longitude;
    private Boolean showLocation;
    private String image;

    private ArrayList<String> friends;

    public UserInformation() {
    }

    public UserInformation(String id, String email, String name, String image) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.image = image;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public Boolean getShowLocation() {
        return showLocation;
    }

    public void setShowLocation(Boolean showLocation) {
        this.showLocation = showLocation;
    }
}