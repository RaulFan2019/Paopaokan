package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raul on 2015/11/29
 * 获取点赞列表结果.
 */
public class GetThumbListResult implements Serializable{

    /**
     * count : 1
     * thumbup : [{"userid":"139","username":"15921613019","nickname":"晓俊 手机号","gender":"1","avatar":"http://7xk0si
     * .com1.z0.glb.clouddn.com/2015-11-05_563b4ec976ce0.jpg","thumbuptime":"2015-11-20 10:21:50"}]
     */

    private int count;//点赞数量
    /**
     * userid : 139
     * username : 15921613019
     * nickname : 晓俊 手机号
     * gender : 1
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-05_563b4ec976ce0.jpg
     * thumbuptime : 2015-11-20 10:21:50
     */

    private List<ThumbupEntity> thumbup;//点赞列表

    public void setCount(int count) {
        this.count = count;
    }

    public void setThumbup(List<ThumbupEntity> thumbup) {
        this.thumbup = thumbup;
    }

    public int getCount() {
        return count;
    }

    public List<ThumbupEntity> getThumbup() {
        return thumbup;
    }

    public static class ThumbupEntity implements Serializable{
        private int userid;
        private String username;
        private String nickname;
        private int gender;
        private String avatar;
        private String thumbuptime;

        public void setUserid(int userid) {
            this.userid = userid;
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

        public void setThumbuptime(String thumbuptime) {
            this.thumbuptime = thumbuptime;
        }

        public int getUserid() {
            return userid;
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

        public String getThumbuptime() {
            return thumbuptime;
        }
    }
}
