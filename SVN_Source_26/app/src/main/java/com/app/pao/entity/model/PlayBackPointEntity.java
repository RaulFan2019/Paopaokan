package com.app.pao.entity.model;

/**
 * Created by Raul on 2016/1/12.
 * 回放的每一个事件点
 */
public class PlayBackPointEntity {

    private long timeofset;// 相对于整个跑步历史的时间偏移
    private int bmp;// 心率
    private double latitude;
    private double longitude;
    private int pace;// 配速 (秒/公里)
    private float length;//距离(米)
    private int CameraIndex;

    public PlayBackPointEntity() {
    }

    public PlayBackPointEntity(long timeofset, double latitude, double longitude, int pace, float length) {
        this.timeofset = timeofset;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pace = pace;
        this.length = length;
        this.bmp = 0;
        CameraIndex = -1;
    }


    public PlayBackPointEntity(long timeofset, int bmp, double latitude, double longitude, int pace, float length) {
        super();
        this.timeofset = timeofset;
        this.bmp = bmp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pace = pace;
        this.length = length;
    }

    /**
     * 设置位置
     *
     * @param latitude
     * @param longitude
     * @param pace
     * @param length
     */
    public void setLocation(double latitude, double longitude, int pace, float length) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.pace = pace;
        this.length = length;
    }

    public int getCameraIndex() {
        return CameraIndex;
    }

    public void setCameraIndex(int cameraIndex) {
        CameraIndex = cameraIndex;
    }

    public long getTimeofset() {
        return timeofset;
    }

    public void setTimeofset(long timeofset) {
        this.timeofset = timeofset;
    }

    public int getBmp() {
        return bmp;
    }

    public void setBmp(int bmp) {
        this.bmp = bmp;
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

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
