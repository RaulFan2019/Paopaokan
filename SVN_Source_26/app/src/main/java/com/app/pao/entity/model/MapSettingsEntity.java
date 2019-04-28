package com.app.pao.entity.model;

import java.io.Serializable;

/**
 * Created by Raul on 2016/1/13.
 */
public class MapSettingsEntity implements Serializable {

    /* 地图范围 */
    private int workoutid;
    private double mMinLaLng;
    private double mMaxLaLng;
    private double mMinLoLng;
    private double mMaxLoLng;
    private String mNickName;//昵称
    private String mAvatar;//头像
    private int mAvgPace;//配速
    private int mAvgHeartrate;//平均心率
    private int mAge;//年龄



    public void setMapBounds(double mMinLaLng, double mMaxLaLng, double mMinLoLng, double mMaxLoLng) {
        this.mMinLaLng = mMinLaLng;
        this.mMaxLaLng = mMaxLaLng;
        this.mMinLoLng = mMinLoLng;
        this.mMaxLoLng = mMaxLoLng;
    }

    public int getWorkoutid() {
        return workoutid;
    }

    public void setWorkoutid(int workoutid) {
        this.workoutid = workoutid;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public int getmAvgHeartrate() {
        return mAvgHeartrate;
    }

    public void setmAvgHeartrate(int mAvgHeartrate) {
        this.mAvgHeartrate = mAvgHeartrate;
    }

    public int getmAvgPace() {
        return mAvgPace;
    }

    public void setmAvgPace(int mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public String getmNickName() {
        return mNickName;
    }

    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public double getmMinLaLng() {
        return mMinLaLng;
    }

    public void setmMinLaLng(double mMinLaLng) {
        this.mMinLaLng = mMinLaLng;
    }

    public double getmMaxLaLng() {
        return mMaxLaLng;
    }

    public void setmMaxLaLng(double mMaxLaLng) {
        this.mMaxLaLng = mMaxLaLng;
    }

    public double getmMinLoLng() {
        return mMinLoLng;
    }

    public void setmMinLoLng(double mMinLoLng) {
        this.mMinLoLng = mMinLoLng;
    }

    public double getmMaxLoLng() {
        return mMaxLoLng;
    }

    public void setmMaxLoLng(double mMaxLoLng) {
        this.mMaxLoLng = mMaxLoLng;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }
}
