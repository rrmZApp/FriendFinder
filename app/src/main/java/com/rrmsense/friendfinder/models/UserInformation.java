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
    private Boolean showLocation;
    private String image;
    private LocationGPS locationGPS;

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

    public LocationGPS getLocationGPS() {
        return locationGPS;
    }

    public void setLocationGPS(LocationGPS locationGPS) {
        this.locationGPS = locationGPS;
    }
}
