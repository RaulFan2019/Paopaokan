package com.app.pao.entity.event;

/**
 * Created by Raul.Fan on 2016/5/8.
 */
public class EventComment {

    public int position;
    public double latitude;
    public double longitude;
    public int replayUserId;
    public String replayUserName;
    public String replayUserNickName;
    public int replayUserGender;
    public String replayUserAvatar;

    public EventComment() {

    }

    public EventComment(double latitude, double longitude, int position, int replayUserId, String replayUserNickName) {
        super();
        this.position = position;
        this.latitude = latitude;
        this.longitude = longitude;
        this.replayUserId = replayUserId;
        this.replayUserNickName = replayUserNickName;
    }

    public EventComment(int position, int replayUserId, String replayUserNickName) {
        super();
        this.position = position;
        this.replayUserId = replayUserId;
        this.replayUserNickName = replayUserNickName;
    }

    public EventComment(int position, double latitude, double longitude, int replayUserId, String replayUserNickName,
                        int replayUserGender, String replayUserAvatar, String replayUserName) {
        this.position = position;
        this.latitude = latitude;
        this.longitude = longitude;
        this.replayUserId = replayUserId;
        this.replayUserNickName = replayUserNickName;
        this.replayUserGender = replayUserGender;
        this.replayUserAvatar = replayUserAvatar;
        this.replayUserName = replayUserName;
    }
}
