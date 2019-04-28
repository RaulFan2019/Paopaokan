package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/12/5.
 * 获取跑团成员列表
 */
public class GetGroupMemberListResult {


    /**
     * count : 2
     * member : [{"id":"10013","name":"13816559301","nickname":"eagle","avatar":"","gender":"1","role":1,
     * "isrunning":0},{"id":"139","name":"15921613019","nickname":"晓俊 手机号","avatar":"http://7xk0si.com1.z0.glb
     * .clouddn.com/2015-11-05_563b4ec976ce0.jpg","gender":"1","role":3,"isrunning":0}]
     */

    private int count;//成员数量
    /**
     * id : 10013
     * name : 13816559301
     * nickname : eagle
     * avatar :
     * gender : 1
     * role : 1
     * isrunning : 0
     */

    private List<MemberEntity> member;//成员列表

    public void setCount(int count) {
        this.count = count;
    }

    public void setMember(List<MemberEntity> member) {
        this.member = member;
    }

    public int getCount() {
        return count;
    }

    public List<MemberEntity> getMember() {
        return member;
    }

    public static class MemberEntity {
        private int id;//用户id
        private String name;//用户名
        private String nickname;//昵称
        private String avatar;//头像
        private int gender;//性别
        private int role;//角色
        private int isrunning;//是否正在跑步

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

        public void setRole(int role) {
            this.role = role;
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

        public int getRole() {
            return role;
        }

        public int getIsrunning() {
            return isrunning;
        }
    }
}
