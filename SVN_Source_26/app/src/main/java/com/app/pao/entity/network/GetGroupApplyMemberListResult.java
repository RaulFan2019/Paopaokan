package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/12/8.
 * 跑团申请人员列表
 */
public class GetGroupApplyMemberListResult {


    /**
     * count : 2
     * apply : [{"id":"377","userid":"140","type":"1","username":"13501636086","nickname":"韩向阳",
     * "avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-01_5635e9756fb44.jpg","gender":"1","status":"1",
     * "applydate":"2015-11-23 13:47:13"},{"id":"56","userid":"147","type":"1","username":"13916611418",
     * "nickname":"James ","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-08_563f09db00b6e.jpg",
     * "gender":"1","status":"3","applydate":"2015-08-27 16:56:32"}]
     */

    private int count;//数量
    /**
     * id : 377
     * userid : 140
     * type : 1
     * username : 13501636086
     * nickname : 韩向阳
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-01_5635e9756fb44.jpg
     * gender : 1
     * status : 1
     * applydate : 2015-11-23 13:47:13
     */

    private List<ApplyEntity> apply;

    public void setCount(int count) {
        this.count = count;
    }

    public void setApply(List<ApplyEntity> apply) {
        this.apply = apply;
    }

    public int getCount() {
        return count;
    }

    public List<ApplyEntity> getApply() {
        return apply;
    }

    public static class ApplyEntity {
        public int id;//消息id
        public int userid;//用户id
        public int type;//类型
        public String username;
        public String nickname;
        public String avatar;
        public int gender;//性别
        public int status;//状态
        public String applydate;
        public float totallength;//总公里

        public float getTotallength() {
            return totallength;
        }

        public void setTotallength(float totallength) {
            this.totallength = totallength;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public void setStatus(int status) {
            this.status = status;
        }

        public void setApplydate(String applydate) {
            this.applydate = applydate;
        }

        public int getId() {
            return id;
        }

        public int getUserid() {
            return userid;
        }

        public int getType() {
            return type;
        }

        public String getUsername() {
            return username;
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

        public int getStatus() {
            return status;
        }

        public String getApplydate() {
            return applydate;
        }
    }
}
