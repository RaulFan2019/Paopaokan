package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 * <p/>
 * 我的照片管理
 */
public class GetGroupPartyMyPhotoReqult {

    private int userid;
    private String nickname;
    private String avatar;
    private List<UserPicture> picture;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public List<UserPicture> getPicture() {
        return picture;
    }

    public void setPicture(List<UserPicture> picture) {
        this.picture = picture;
    }

    public static class UserPicture {
        private int photoid;
        private String url;
        private int mCheckType;//标识该x项是否选择

        public int getPhotoid() {
            return photoid;
        }

        public void setPhotoid(int photoid) {
            this.photoid = photoid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getmCheckType() {
            return mCheckType;
        }

        public void setmCheckType(int mCheckType) {
            this.mCheckType = mCheckType;
        }
    }
}
