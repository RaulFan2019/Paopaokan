package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * Created by Raul on 2015/11/15.
 * 登录返回结果对象
 */
public class GetCommonLoginResult implements Serializable {


    /**
     * {"id":"10614","name":"15221798774","weight":"0","height":"0","age":"0","registerdate":"2015-11-17 14:01:05",
     * "nickname":"raul","avatar":"http:\/\/7xk0si.com1.z0.glb.clouddn.com\/2015-11-17_564ac2a0edc29.","gender":"1",
     * "birthdate":"1980-01-01","updatetime":"2015-11-17 14:01:05","location":"310100",
     * "locationprovince":"\u4e0a\u6d77","locationcity":"\u6d66\u4e1c\u65b0\u533a",
     * "sessionid":"46e12cf2c011fc8bbcd36f7cdaa5a7f54476b7eb","passwordisblank":0,"mobile":"15221798774",
     * "weixinnickname":""}
     */

    public int id;//用户ID
    public String name;//用户名
    public String nickname;//昵称
    // private String email;//邮件
    public int weight;//体重
    public int height;//身高
    public int age;//年龄
    public String registerdate;//注册
    public String avatar;//头像
    public int gender;//性别
    public String birthdate;//生日
    public Object updatetime;//保留字段
    public String location;//位置编码
    public String locationprovince;//省
    public String locationcity;//市
    public String sessionid;
    public String mobile;  //手机号码
    public String weixinnickname;//微信昵称
    public int passwordisblank;//密码是否为空
    public String qrcode;//二维码
    public int thirdpartyaccount;//是否绑定第三方微信


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setEmail(String email) {
//        this.email = email;
//    }

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

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setUpdatetime(Object updatetime) {
        this.updatetime = updatetime;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public String getEmail() {
//        return email;
//    }

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

    public String getBirthdate() {
        return birthdate;
    }

    public Object getUpdatetime() {
        return updatetime;
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

    public GetCommonLoginResult() {

    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public GetCommonLoginResult(int id, String name, int weight, int height, int age,
                                String registerdate, String nickname, String avatar, int gender, String birthdate,
                                Object updatetime, String location, String locationprovince, String locationcity,
                                String sessionid, String mobile, String weixinnickname, int passwordisblank, String qrcode, int thirdpartyaccount) {
        super();
        this.id = id;
        this.name = name;
        this.thirdpartyaccount = thirdpartyaccount;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.registerdate = registerdate;
        this.nickname = nickname;
        this.avatar = avatar;
        this.gender = gender;
        this.birthdate = birthdate;
        this.updatetime = updatetime;
        this.location = location;
        this.locationprovince = locationprovince;
        this.locationcity = locationcity;
        this.sessionid = sessionid;
        this.mobile = mobile;
        this.weixinnickname = weixinnickname;
        this.passwordisblank = passwordisblank;
        this.qrcode = qrcode;
    }


}
