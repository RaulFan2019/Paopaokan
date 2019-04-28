package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 */
public class GetFriendListResult {


    /**
     * count : 1
     * friends : [{"id":"139","name":"15921613019","nickname":"晓俊 手机号","avatar":"http://7xk0si.com1.z0.glb.clouddn
     * .com/2015-11-05_563b4ec976ce0.jpg","gender":"1","isrunning":0}]
     */

    public int count;//好友数量
    /**
     * id : 139
     * name : 15921613019
     * nickname : 晓俊 手机号
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-05_563b4ec976ce0.jpg
     * gender : 1
     * isrunning : 0
     */

    public List<FriendsEntity> friends;//好友列表

    public void setCount(int count) {
        this.count = count;
    }

    public void setFriends(List<FriendsEntity> friends) {
        this.friends = friends;
    }

    public int getCount() {
        return count;
    }

    public List<FriendsEntity> getFriends() {
        return friends;
    }

    public static class FriendsEntity implements Serializable {
        public int id;//用户ID
        public String name;//用户手机号
        public String nickname;//用户昵称
        public String avatar;//用户头像
        public int gender;//用户性别
        public int isrunning;//用户是否在跑步
        public int totallength;//总长度

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setIsrunning(int isrunning) {
            this.isrunning = isrunning;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getGender() {
            return gender;
        }

        public int getIsrunning() {
            return isrunning;
        }

        public int getTotallength() {
            return totallength;
        }

        public void setTotallength(int totallength) {
            this.totallength = totallength;
        }
    }
}
