package com.app.pao.entity.network;

/**
 * Created by Administrator on 2016/1/13.
 *
 * 标签成员
 */
public class GetLabelMemberListResult {
    private int userid;
    private String name;
    private String nickname;
    private String avatar;
    private int gender;
    private int hasLabel;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHasLabel() {
        return hasLabel;
    }

    public void setHasLabel(int hasLabel) {
        this.hasLabel = hasLabel;
    }
}
