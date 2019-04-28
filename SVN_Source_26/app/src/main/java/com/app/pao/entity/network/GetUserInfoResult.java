package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * Created by Raul on 2015/11/26.
 * 获取用户个人信息
 */
public class GetUserInfoResult implements Serializable{


    /**
     * id : 140
     * name : 13501636086
     * weight : 74
     * height : 169
     * age : 35
     * registerdate : 2015-07-03 06:47:28
     * nickname : 韩向阳
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-20_564f41db512ce.jpg
     * gender : 1
     * location : 310100
     * locationprovince : 上海
     * locationcity : 静安区
     * qrcode : 123Go://userid/140
     * birthdate : 1975-01-29
     * passwordisblank : 0
     * mobile : 13501636086
     * weixinnickname :
     * runage : 4.8
     * totallength : 796929
     * totalcount : 205
     * totalduration : 282328
     * totalcalorie : 59195
     * isFriend : 0
     * hasSendApply : 0
     */

    public int id;
    public String name;
    public int weight;
    public int height;
    public int age;
    public String registerdate;
    public String nickname;
    public String avatar;
    public int gender;
    public String birthdate;
    public String updatetime;
    public String location;
    public String locationprovince;
    public String locationcity;
    public int livingroomvisits;
    public int livingroomwatchminutes;
    public int clockcount;
    public String qrcode;
    public int passwordisblank;
    public String mobile;
    public String weixinnickname;
    public double runage;
    public float totallength;
    public float weeklength;
    public int totalcount;
    public long totalduration;
    public int totalcalorie;
    public int hasSendApply;
    public int isFriend;
    public int friendcount;
    public int commonfriendcount;
    public int rungroupcount;
    public boolean isrunning;//0没跑，1是正在跑步
    public float length;//已经跑了多少公里

//    private UserMedalRecordEntity fastest5;
//    private UserMedalRecordEntity fastest10;

    public float getWeeklength() {
        return weeklength;
    }

    public void setWeeklength(float weeklength) {
        this.weeklength = weeklength;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLocationprovince(String locationprovince) {
        this.locationprovince = locationprovince;
    }

    public void setLocationcity(String locationcity) {
        this.locationcity = locationcity;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setPasswordisblank(int passwordisblank) {
        this.passwordisblank = passwordisblank;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setWeixinnickname(String weixinnickname) {
        this.weixinnickname = weixinnickname;
    }

    public void setRunage(double runage) {
        this.runage = runage;
    }

    public void setTotallength(float totallength) {
        this.totallength = totallength;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public void setTotalduration(long totalduration) {
        this.totalduration = totalduration;
    }

    public void setTotalcalorie(int totalcalorie) {
        this.totalcalorie = totalcalorie;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public void setHasSendApply(int hasSendApply) {
        this.hasSendApply = hasSendApply;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public String getRegisterdate() {
        return registerdate;
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

    public String getLocation() {
        return location;
    }

    public String getLocationprovince() {
        return locationprovince;
    }

    public String getLocationcity() {
        return locationcity;
    }

    public String getQrcode() {
        return qrcode;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getPasswordisblank() {
        return passwordisblank;
    }

    public String getMobile() {
        return mobile;
    }

    public String getWeixinnickname() {
        return weixinnickname;
    }

    public double getRunage() {
        return runage;
    }

    public float getTotallength() {
        return totallength;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public long getTotalduration() {
        return totalduration;
    }

    public int getTotalcalorie() {
        return totalcalorie;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public int getHasSendApply() {
        return hasSendApply;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getLivingroomvisits() {
        return livingroomvisits;
    }

    public void setLivingroomvisits(int livingroomvisits) {
        this.livingroomvisits = livingroomvisits;
    }

    public int getLivingroomwatchminutes() {
        return livingroomwatchminutes;
    }

    public void setLivingroomwatchminutes(int livingroomwatchminutes) {
        this.livingroomwatchminutes = livingroomwatchminutes;
    }

    public int getClockcount() {
        return clockcount;
    }

    public void setClockcount(int clockcount) {
        this.clockcount = clockcount;
    }

    public int getFriendcount() {
        return friendcount;
    }

    public void setFriendcount(int friendcount) {
        this.friendcount = friendcount;
    }

    public int getCommonfriendcount() {
        return commonfriendcount;
    }

    public void setCommonfriendcount(int commonfriendcount) {
        this.commonfriendcount = commonfriendcount;
    }

    public int getRungroupcount() {
        return rungroupcount;
    }

    public void setRungroupcount(int rungroupcount) {
        this.rungroupcount = rungroupcount;
    }

    public boolean isrunning() {
        return isrunning;
    }

    public void setIsrunning(boolean isrunning) {
        this.isrunning = isrunning;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    //    public static class UserMedalRecordEntity implements Serializable {
//        private int workoutid;//跑步历史ID
//        private String starttime;//开始时间
//        private long duration;//时间
//        private float length;//距离
//        private int pace;//配速
//        private int thumbupcount;//赞次数
//        private int hasGiveThumbup;//是否给赞
//
//        public int getWorkoutid() {
//            return workoutid;
//        }
//
//        public void setWorkoutid(int workoutid) {
//            this.workoutid = workoutid;
//        }
//
//        public String getStarttime() {
//            return starttime;
//        }
//
//        public void setStarttime(String starttime) {
//            this.starttime = starttime;
//        }
//
//        public long getDuration() {
//            return duration;
//        }
//
//        public void setDuration(long duration) {
//            this.duration = duration;
//        }
//
//        public float getLength() {
//            return length;
//        }
//
//        public void setLength(float length) {
//            this.length = length;
//        }
//
//        public int getPace() {
//            return pace;
//        }
//
//        public void setPace(int pace) {
//            this.pace = pace;
//        }
//
//        public int getThumbupcount() {
//            return thumbupcount;
//        }
//
//        public void setThumbupcount(int thumbupcount) {
//            this.thumbupcount = thumbupcount;
//        }
//
//        public int getHasGiveThumbup() {
//            return hasGiveThumbup;
//        }
//
//        public void setHasGiveThumbup(int hasGiveThumbup) {
//            this.hasGiveThumbup = hasGiveThumbup;
//        }
//    }
}
