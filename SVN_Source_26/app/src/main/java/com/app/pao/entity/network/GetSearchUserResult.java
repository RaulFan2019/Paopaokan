package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/11/25.
 */
public class GetSearchUserResult {


    /**
     * users : [{"from":1,"id":"147","name":"13916611418","nickname":"James ","avatar":"","gender":"1","isFriend":0,
     * "hasSendApply":0},{"from":1,"id":"10026","name":"13918344836","nickname":"Dorothy","avatar":"","gender":"1",
     * "isFriend":0,"hasSendApply":1}]
     * count : 2
     */

    private int count;//搜索数量
    /**
     * from : 1
     * id : 147
     * name : 13916611418
     * nickname : James
     * avatar :
     * gender : 1
     * isFriend : 0
     * hasSendApply : 0
     */

    public List<UsersEntity> users;//用户列表

    public void setCount(int count) {
        this.count = count;
    }

    public void setUsers(List<UsersEntity> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }

    public List<UsersEntity> getUsers() {
        return users;
    }

    public static class UsersEntity {
        public int from;//来源
        public int id;//用户ID
        public String name;//用户名称
        public String nickname;//昵称
        public String avatar;//头像
        public int gender;//性别
        public int isFriend;//是否是朋友
        public int hasSendApply;//是否发出申请

        public void setFrom(int from) {
            this.from = from;
        }

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

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }

        public void setHasSendApply(int hasSendApply) {
            this.hasSendApply = hasSendApply;
        }

        public int getFrom() {
            return from;
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

        public int getIsFriend() {
            return isFriend;
        }

        public int getHasSendApply() {
            return hasSendApply;
        }
    }
}
