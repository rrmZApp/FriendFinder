package com.rrmsense.friendfinder.models;

/**
 * Created by Talha on 2/26/2017.
 */

public class LocationGPS {
    double longitude;
    double latitude;

    public LocationGPS() {
    }

    public LocationGPS(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
