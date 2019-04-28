package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by LY on 2016/3/30.
 */
public class DynamicRecyclerViewList {
    private int type;
    private List<GetRaceListResult.RaceEntity> mRaceList;
    private List<GetRunningUserResult.FriendsEntity> FriendList;
    private String startTime;
    private GetRunningUserResult.FriendsEntity friendsEntity;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<GetRaceListResult.RaceEntity> getmRaceList() {
        return mRaceList;
    }

    public void setmRaceList(List<GetRaceListResult.RaceEntity> mRaceList) {
        this.mRaceList = mRaceList;
    }

    public List<GetRunningUserResult.FriendsEntity> getFriendList() {
        return FriendList;
    }

    public void setFriendList(List<GetRunningUserResult.FriendsEntity> friendList) {
        FriendList = friendList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public GetRunningUserResult.FriendsEntity getFriendsEntity() {
        return friendsEntity;
    }

    public void setFriendsEntity(GetRunningUserResult.FriendsEntity friendsEntity) {
        this.friendsEntity = friendsEntity;
    }
}
