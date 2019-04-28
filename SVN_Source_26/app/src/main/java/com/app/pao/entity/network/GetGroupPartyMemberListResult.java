package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Administrator on 2016/1/19.
 * <p/>
 * 跑团活动成员列表
 */
public class GetGroupPartyMemberListResult {

    private int personrole;
    private int personstatus;// 1,已报名 2，已签到 3,未报名 4，不是团员 5，活动发起者
    private int userid;
    private String avatar;
    private String nickname;
    private String name;
    private int gender;
    private String birthdate;
    private String signuptime;
    private String checkintime;//如果checktime不等于空字符串，表示签到了。每个配速的报名人数和签到人数，需要客户端自己统计。
    private String pace;//报名时候的配速
    private int isleader;//1:领跑者; 0:不是领跑者
    private String alias;
    private String displayname;
    private List<Label> label;

    public int getPersonstatus() {
        return personstatus;
    }

    public void setPersonstatus(int personstatus) {
        this.personstatus = personstatus;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getSignuptime() {
        return signuptime;
    }

    public void setSignuptime(String signuptime) {
        this.signuptime = signuptime;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public int getIsleader() {
        return isleader;
    }

    public void setIsleader(int isleader) {
        this.isleader = isleader;
    }

    public int getPersonrole() {
        return personrole;
    }

    public void setPersonrole(int personrole) {
        this.personrole = personrole;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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

    public List<Label> getLabel() {
        return label;
    }

    public void setLabel(List<Label> label) {
        this.label = label;
    }

    public class Label {
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
}
