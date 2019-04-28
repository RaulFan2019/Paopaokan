package com.app.pao.entity.model;

import com.app.pao.entity.network.GetPersonRecordResult;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/15.
 */
public class UserOptMedalEntity implements Serializable{
    public static final int TYPE_LONGEST = 1;
    public static final int TYPE_MAX_DURATION = 2;
    public static final int TYPE_FASTEST_PACE = 3;
    public static final int TYPE_FASTEST_5 = 4;
    public static final int TYPE_FASTEST_10 = 5;
    public static final int TYPE_FASTEST_HALF_MARATHON = 6;
    public static final int TYPE_FASTEST_FULL_MARATHON = 7;

    private GetPersonRecordResult.MedalRecordEntity mUserMedal;
    private int mMedalType;

    public UserOptMedalEntity() {
    }

    public UserOptMedalEntity(GetPersonRecordResult.MedalRecordEntity mUserMedal, int mMedalType) {
        this.mUserMedal = mUserMedal;
        this.mMedalType = mMedalType;
    }

    public GetPersonRecordResult.MedalRecordEntity getUserMedal() {
        return mUserMedal;
    }

    public void setUserMedal(GetPersonRecordResult.MedalRecordEntity mUserMedal) {
        this.mUserMedal = mUserMedal;
    }

    public int getMedalType() {
        return mMedalType;
    }

    public void setMedalType(int mMedalType) {
        this.mMedalType = mMedalType;
    }


}
