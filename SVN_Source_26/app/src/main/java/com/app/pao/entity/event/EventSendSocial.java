package com.app.pao.entity.event;

/**
 * Created by Raul.Fan on 2016/5/8.
 */
public class EventSendSocial {


    public static final int THUMB = 1;
    public static final int COMMENTS = 2;
    public static final int VOICE = 3;

    public int socialType;
    public String comments;
    public int replayId;
    public String replayUserNickName;
    public String replayUserName;
    public int replayUserGender;
    public String replayUserAvatar;
    public int mHasGiveThumbup;

    public EventSendSocial() {
    }

    public EventSendSocial(int socialType, String comments, int replayId, String replayUserNickName,
                           String replayUserName, int replayUserGender, String replayUserAvatar, int HasGiveThumbup) {
        super();
        this.socialType = socialType;
        this.comments = comments;
        this.replayId = replayId;
        this.replayUserNickName = replayUserNickName;
        this.replayUserName = replayUserName;
        this.replayUserGender = replayUserGender;
        this.replayUserAvatar = replayUserAvatar;
        this.mHasGiveThumbup = HasGiveThumbup;
    }


}
