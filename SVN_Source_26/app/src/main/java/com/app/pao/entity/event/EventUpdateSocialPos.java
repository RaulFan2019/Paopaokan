package com.app.pao.entity.event;

/**
 * Created by Raul.Fan on 2016/5/10.
 */
public class EventUpdateSocialPos {

    public int pos;
    public double latitude;
    public double longitude;

    public EventUpdateSocialPos() {
    }

    public EventUpdateSocialPos(int pos) {
        super();
        this.pos = pos;
    }

    public EventUpdateSocialPos(int pos, double latitude, double longitude) {
        super();
        this.pos = pos;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
