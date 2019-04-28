package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * Created by Raul on 2016/1/15.
 */
public class GetThumbsResult implements Serializable{


    /**
     * userid : 139
     * thumbuptime : 2016-01-10 16:32:30
     * username : 15921613019
     * nickname : 挑灯看剑
     * gender : 1
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-23_5652d1e66c065.jpg
     */

    private int userid;
    private String thumbuptime;
    private String username;
    private String nickname;
    private int gender;
    private String avatar;

    public GetThumbsResult(){

    }
    public GetThumbsResult(int userid, String thumbuptime, String username, String nickname, int gender, String
            avatar) {
        super();
        this.userid = userid;
        this.thumbuptime = thumbuptime;
        this.username = username;
        this.nickname = nickname;
        this.gender = gender;
        this.avatar = avatar;
    }


    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setThumbuptime(String thumbuptime) {
        this.thumbuptime = thumbuptime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserid() {
        return userid;
    }

    public String getThumbuptime() {
        return thumbuptime;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGender() {
        return gender;
    }

    public String getAvatar() {
        return avatar;
    }
}
