package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Raul on 2015/11/5.
 * 用户数据库类
 */
@Table(name = "user")
public class DBUserEntity {

    @Id
    @NoAutoIncrement
    @Column(column = "userId")
    public int userId;//用户ID
    @Column(column = "name")
    public String name;// 用户名称
    @Column(column = "email")
    private String email;// 用户邮件
    @Column(column = "weight")
    public int weight;// 体重(单位:公斤)
    @Column(column = "height")
    public int height;// 身高(单位:CM)
    @Column(column = "age")
    public int age;// 年龄
    @Column(column = "nickname")
    public String nickname;// 昵称
    @Column(column = "gender")
    public int gender;// 性别
    @Column(column = "birthday")
    public String birthday;// 生日
    @Column(column = "avatar")
    public String avatar;//头像地址
    @Column(column = "province")
    public String province;// 省名称
    @Column(column = "city")
    public String city;// 市名称
    @Column(column = "sessionid")
    public String sessionid;//sessionid
    @Column(column = "mobile")
    public String mobile;//电话号码
    @Column(column = "weixinnickname")
    public String weixinnickname;//微信昵称
    @Column(column = "qrcode")
    public String qrcode;//个人二维码
    @Column(column = "runInfo")
    public String runInfo;//跑步信息
    @Column(column = "passwordisblank")
    public int passwordisblank;//密码是否为空
    @Column(column = "thirdpartyaccount")
    public int thirdpartyaccount;//微信是否绑定
    @Column(column = "weeklength")
    public int weeklength;//平均周跑量
    @Column(column = "clockcount")
    public int clockcount;//闹钟数量
    @Column(column = "friendcount")
    public int friendcount;//好友数量
    @Column(column = "groupcount")
    public int groupcount;//跑团数量
    @Column(column = "lastShowInPerperLength")
    public int lastShowInPerperLength;//上一次在准备跑页面显示的距离

    public DBUserEntity() {

    }


    public DBUserEntity(int userId, String name, String email, int weight, int height,
                        int age, String nickname, int gender, String birthday,
                        String avatar, String province, String city, String sessionid, String mobile, String
                                weixinnickname, String qrcode, int passwordisblank, int thirdpartyaccount) {
        super();
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.avatar = avatar;
        this.province = province;
        this.city = city;
        this.sessionid = sessionid;
        this.mobile = mobile;
        this.weixinnickname = weixinnickname;
        this.qrcode = qrcode;
        this.runInfo = "";
        this.passwordisblank = passwordisblank;
        this.thirdpartyaccount = thirdpartyaccount;
        this.clockcount = 0;
        this.friendcount = 0;
        this.groupcount = 0;
        this.lastShowInPerperLength = 0;
    }

    public int getLastShowInPerperLength() {
        return lastShowInPerperLength;
    }

    public void setLastShowInPerperLength(int lastShowInPerperLength) {
        this.lastShowInPerperLength = lastShowInPerperLength;
    }

    public int getFriendcount() {
        return friendcount;
    }

    public void setFriendcount(int friendcount) {
        this.friendcount = friendcount;
    }

    public int getGroupcount() {
        return groupcount;
    }

    public void setGroupcount(int groupcount) {
        this.groupcount = groupcount;
    }

    public int getClockcount() {
        return clockcount;
    }

    public void setClockcount(int clockcount) {
        this.clockcount = clockcount;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getRunInfo() {
        return runInfo;
    }

    public void setRunInfo(String runInfo) {
        this.runInfo = runInfo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeixinnickname() {
        return weixinnickname;
    }

    public void setWeixinnickname(String weixinnickname) {
        this.weixinnickname = weixinnickname;
    }

    public int getPasswordisblank() {
        return passwordisblank;
    }

    public void setPasswordisblank(int passwordisblank) {
        this.passwordisblank = passwordisblank;
    }

    public int getThirdpartyaccount() {
        return thirdpartyaccount;
    }

    public void setThirdpartyaccount(int thirdpartyaccount) {
        this.thirdpartyaccount = thirdpartyaccount;
    }

    public int getWeeklength() {
        return weeklength;
    }

    public void setWeeklength(int weeklength) {
        this.weeklength = weeklength;
    }

}

