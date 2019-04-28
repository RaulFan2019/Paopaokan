package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raul on 2015/12/4.
 * 获取跑步信息
 */
public class GetGroupInfoResult implements Serializable{


    /**
     * id : 30
     * name : eagle 15
     * description : 描述
     * avatar :
     * locationcity :
     * locationprovince :
     * membercount : 2
     * totallength : 41987642
     * memberapplycount : 2
     * role : 3
     */

    private RungroupEntity rungroup;//跑团信息

    public void setRungroup(RungroupEntity rungroup) {
        this.rungroup = rungroup;
    }

    public RungroupEntity getRungroup() {
        return rungroup;
    }

    public static class RungroupEntity implements Serializable{
        private int id;//跑团ID
        private String name;//跑团名称
        private String description;//跑团描述
        private String avatar;//跑团投向
        private String locationcity;//跑团城市
        private String locationprovince;//跑团省
        private int membercount;//成员数量
        private int totallength;//总里程
        private int memberapplycount;//申请数量
        private int avgweeklength;//人均周跑量
        private int role;//角色
        private String qrcode;//跑团二维码
        private int ownerid;
        private String ownernickname;
        private String owneravatar;
        private String owneralias;
        private int avgpace;
        private List<Label> label;

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

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public int getOwnerid() {
            return ownerid;
        }

        public void setOwnerid(int ownerid) {
            this.ownerid = ownerid;
        }

        public String getOwnernickname() {
            return ownernickname;
        }

        public void setOwnernickname(String ownernickname) {
            this.ownernickname = ownernickname;
        }

        public String getOwneravatar() {
            return owneravatar;
        }

        public void setOwneravatar(String owneravatar) {
            this.owneravatar = owneravatar;
        }

        public String getOwneralias() {
            return owneralias;
        }

        public void setOwneralias(String owneralias) {
            this.owneralias = owneralias;
        }

        public int getAvgpace() {
            return avgpace;
        }

        public void setAvgpace(int avgpace) {
            this.avgpace = avgpace;
        }

        public List<Label> getLabel() {
            return label;
        }

        public void setLabel(List<Label> label) {
            this.label = label;
        }

        public static class Label implements Serializable{
            private int id;
            private String name;
            private int membercount;
            private int hasLabel;

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

            public int getMembercount() {
                return membercount;
            }

            public void setMembercount(int membercount) {
                this.membercount = membercount;
            }

            public int getHasLabel() {
                return hasLabel;
            }

            public void setHasLabel(int hasLabel) {
                this.hasLabel = hasLabel;
            }
        }
    }
}
