package com.app.pao.entity.model;

/**
 * Created by Raul on 2016/1/13.
 * 回放的分段
 */
public class PlayBackSpliteEntity {

    private long id;// 序号
    private double Latitude;// 纬度
    private double Longitude;// 经度
    private int pace;
    private boolean isCurrPace;

    public PlayBackSpliteEntity(){

    }

    public PlayBackSpliteEntity(long id, double latitude, double longitude,int pace) {
        super();
        this.id = id;
        Latitude = latitude;
        Longitude = longitude;
        this.pace = pace;
        isCurrPace = false;
    }

    public boolean isCurrPace() {
        return isCurrPace;
    }

    public void setIsCurrPace(boolean isCurrPace) {
        this.isCurrPace = isCurrPace;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
