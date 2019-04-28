package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/16.
 */
public class GetClockResult {

    /**
     * clocks : [{"userid":"13078","clocktime":"2016-04-27 17:21:16","nickname":"醉红颜","gender":"1","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2016-03-25_56f5476d3d453.jpg","province":"上海","city":"浦东新区","clockindex":1},{"userid":"13075","clocktime":"2016-04-27 17:07:44","nickname":"海峰 ","gender":"1","avatar":"http://wx.qlogo.cn/mmopen/RS5fXGGU1FG9iaPuEsJDCGb99Tic7QCpvC0LRHOwR31VAvIFhzIyWeLGU7FE9UnuTTyBwT9q1Ikq5IklbfbDyZkSXdwGicwg2og/0","province":"","city":"","clockindex":3},{"userid":"13075","clocktime":"2016-04-27 17:07:37","nickname":"海峰 ","gender":"1","avatar":"http://wx.qlogo.cn/mmopen/RS5fXGGU1FG9iaPuEsJDCGb99Tic7QCpvC0LRHOwR31VAvIFhzIyWeLGU7FE9UnuTTyBwT9q1Ikq5IklbfbDyZkSXdwGicwg2og/0","province":"","city":"","clockindex":2},{"userid":"13075","clocktime":"2016-04-27 17:06:53","nickname":"海峰 ","gender":"1","avatar":"http://wx.qlogo.cn/mmopen/RS5fXGGU1FG9iaPuEsJDCGb99Tic7QCpvC0LRHOwR31VAvIFhzIyWeLGU7FE9UnuTTyBwT9q1Ikq5IklbfbDyZkSXdwGicwg2og/0","province":"","city":"","clockindex":1}]
     * clockcount : 4
     */

    public int clockcount;
    public int usercount;
    /**
     * userid : 13078
     * clocktime : 2016-04-27 17:21:16
     * nickname : 醉红颜
     * gender : 1
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2016-03-25_56f5476d3d453.jpg
     * province : 上海
     * city : 浦东新区
     * clockindex : 1
     */

    public List<ClocksEntity> clocks;

    public void setClockcount(int clockcount) {
        this.clockcount = clockcount;
    }

    public void setClocks(List<ClocksEntity> clocks) {
        this.clocks = clocks;
    }

    public int getClockcount() {
        return clockcount;
    }

    public int getUsercount() {
        return usercount;
    }

    public void setUsercount(int usercount) {
        this.usercount = usercount;
    }

    public List<ClocksEntity> getClocks() {
        return clocks;
    }

    public static class ClocksEntity {
        public int userid;
        public String clocktime;
        public String nickname;
        public int gender;
        public String avatar;
        public String province;
        public String city;
        public int clockindex;

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public void setClocktime(String clocktime) {
            this.clocktime = clocktime;
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

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setClockindex(int clockindex) {
            this.clockindex = clockindex;
        }

        public int getUserid() {
            return userid;
        }

        public String getClocktime() {
            return clocktime;
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

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public int getClockindex() {
            return clockindex;
        }

    }
}
