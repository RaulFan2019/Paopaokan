package com.app.pao.entity.network;

import com.app.pao.utils.StringUtils;

import java.util.List;

/**
 * Created by Raul on 2015/12/7.
 * 跑团成员排行结果
 */
public class GetGroupMemberSortResult {


    /**
     * id : 139
     * name : 15921613019
     * nickname : 晓俊 手机号
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-05_563b4ec976ce0.jpg
     * gender : 1
     * locationprovince : 上海
     * locationcity : 浦东新区
     * length : 8.931514205E7
     */

    private int id;//用户id
    private String name;//用户名称
    private String nickname;//用户昵称
    private String avatar;//头像
    private int gender;//性别
    private String locationprovince;//省
    private String locationcity;//市
    private float length;//距离
    private String alias;
    private String displayname;
    private int role ;
    private List<Label> label;
    private int isrunning;
    private String birthdate;


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

    public void setLocationprovince(String locationprovince) {
        this.locationprovince = locationprovince;
    }

    public void setLocationcity(String locationcity) {
        this.locationcity = locationcity;
    }

    public void setLength(float length) {
        this.length = length;
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

    public String getLocationprovince() {
        return locationprovince;
    }

    public String getLocationcity() {
        return locationcity;
    }

    public float getLength() {
        return length;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<Label> getLabel() {
        return label;
    }

    public void setLabel(List<Label> label) {
        this.label = label;
    }

    public int getIsrunning() {
        return isrunning;
    }

    public void setIsrunning(int isrunning) {
        this.isrunning = isrunning;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public static class Label{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public char getFirstPinYinChar(){
        return StringUtils.getPinYinFirst(displayname);
    }
}
