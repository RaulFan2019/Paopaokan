package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/11/25.
 * 通过姓名列表获取用户
 */
public class GetSearFriendbyNameList {


    /**
     * users : [{"id":"10443","name":"15921613015","nickname":"俊男","avatar":"http://7xk0si.com1.z0.glb.clouddn
     * .com/2015-10-31_5634be6ed179f.jpg","gender":"1","hasSendApply":0,"isFriend":1},{"id":0,"name":"123",
     * "nickname":"","avatar":"","gender":"","hasSendApply":0,"isFriend":0}]
     * matchcount : 1
     */

    private int matchcount;//匹配数量
    /**
     * id : 10443
     * name : 15921613015
     * nickname : 俊男
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-10-31_5634be6ed179f.jpg
     * gender : 1
     * hasSendApply : 0
     * isFriend : 1
     */

    private List<UsersEntity> users;

    public void setMatchcount(int matchcount) {
        this.matchcount = matchcount;
    }

    public void setUsers(List<UsersEntity> users) {
        this.users = users;
    }

    public int getMatchcount() {
        return matchcount;
    }

    public List<UsersEntity> getUsers() {
        return users;
    }

    public static class UsersEntity {
        private int id;//用户id
        private String name;//用户名
        private String nickname;//昵称
        private String avatar;//头像
        private String gender;//性别
        private int hasSendApply;//是否申请
        private int isFriend;//是否是好友

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

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setHasSendApply(int hasSendApply) {
            this.hasSendApply = hasSendApply;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
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

        public String getGender() {
            return gender;
        }

        public int getHasSendApply() {
            return hasSendApply;
        }

        public int getIsFriend() {
            return isFriend;
        }
    }
}
