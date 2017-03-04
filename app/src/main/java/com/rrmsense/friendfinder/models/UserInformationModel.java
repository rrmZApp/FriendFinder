package com.rrmsense.friendfinder.models;


/**
 * Created by Talha on 2/23/2017.
 */

public class UserInformationModel {

    private String id;
    private String email;
    private String name;
    private String mobile;
    private Boolean showLocation;
    private String image;
    private LocationGPS locationGPS;
    private String token;

    public UserInformationModel() {
    }

    public UserInformationModel(String id, String email, String name, String image, boolean showLocation) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.image = image;
        this.showLocation = showLocation;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
