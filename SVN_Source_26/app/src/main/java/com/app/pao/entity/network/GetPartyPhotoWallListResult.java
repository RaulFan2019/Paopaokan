package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 */
public class GetPartyPhotoWallListResult {
    private List<UserPicture> users;

    public List<UserPicture> getUsers() {
        return users;
    }

    public void setUsers(List<UserPicture> users) {
        this.users = users;
    }

    /**
     *
     */
    public static class UserPicture{
        private int userid;
        private String nickname;
        private String avatar;
        private List<WallPicture> picture;

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

        public List<WallPicture> getPicture() {
            return picture;
        }

        public void setPicture(List<WallPicture> picture) {
            this.picture = picture;
        }

        /**
         *
         */
        public static class WallPicture{
            private int photoid;
            private String url;

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
        }
    }
}
