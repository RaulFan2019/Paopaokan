package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * Created by Raul on 2015/12/1.
 * 获取跑团列表
 */
public class GetGroupListResult implements Serializable{

    /**
     * id : 144
     * name : ggg
     * description : Now is the time for all good developers to come to serve their country.
     * <p/>
     * Now is the time for all good developers to come to serve their country.
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-24_5653c7fec587b.jpg
     * locationcity : 密云县
     * locationprovince : 北京
     * membercount : 1
     * totallength : 775221
     * memberapplycount : 0
     * role : 1
     * status : 3
     */

    public int id;
    public String name;
    public String description;
    public String avatar;
    public String locationcity;
    public String locationprovince;
    public int membercount;
    public int totallength;
    public int memberapplycount;
    public int role;
    public int status;
    public int avgweeklength;//人均周跑量

    public int getAvgweeklength() {
        return avgweeklength;
    }

    public void setAvgweeklength(int avgweeklength) {
        this.avgweeklength = avgweeklength;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setLocationcity(String locationcity) {
        this.locationcity = locationcity;
    }

    public void setLocationprovince(String locationprovince) {
        this.locationprovince = locationprovince;
    }

    public void setMembercount(int membercount) {
        this.membercount = membercount;
    }

    public void setTotallength(int totallength) {
        this.totallength = totallength;
    }

    public void setMemberapplycount(int memberapplycount) {
        this.memberapplycount = memberapplycount;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLocationcity() {
        return locationcity;
    }

    public String getLocationprovince() {
        return locationprovince;
    }

    public int getMembercount() {
        return membercount;
    }

    public int getTotallength() {
        return totallength;
    }

    public int getMemberapplycount() {
        return memberapplycount;
    }

    public int getRole() {
        return role;
    }

    public int getStatus() {
        return status;
    }
}
