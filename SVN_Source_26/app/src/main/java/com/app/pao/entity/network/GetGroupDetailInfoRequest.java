package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/26.
 * <p/>
 * new 跑团详情
 */
public class GetGroupDetailInfoRequest implements Serializable{
    private GroupDetailInfo rungroup;

    public GroupDetailInfo getRungroup() {
        return rungroup;
    }

    public void setRungroup(GroupDetailInfo rungroup) {
        this.rungroup = rungroup;
    }

    public static class GroupDetailInfo implements Serializable{

        private int id;
        private String name;
        private String description;
        private String avatar;
        private String wallpaper;
        private String locationcity;
        private String locationprovince;
        private int ownerid;
        private String ownernickname;
        private String owneravatar;
        private String qrcode;
        private int membercount;
        private float totallength;
        private float avgweeklength;
        private int avgpace;
        private int memberapplycount;
        private int role;
        private int status;
        private List<Label> label;
        private List<MemberManager> manager;
        private int managercount;
        private List<Ranking> ranking;
        private int myranking;
        private List<Party> party;
        private int partycount;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getWallpaper() {
            return wallpaper;
        }

        public void setWallpaper(String wallpaper) {
            this.wallpaper = wallpaper;
        }

        public String getLocationcity() {
            return locationcity;
        }

        public void setLocationcity(String locationcity) {
            this.locationcity = locationcity;
        }

        public String getLocationprovince() {
            return locationprovince;
        }

        public void setLocationprovince(String locationprovince) {
            this.locationprovince = locationprovince;
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

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public int getMembercount() {
            return membercount;
        }

        public void setMembercount(int membercount) {
            this.membercount = membercount;
        }

        public float getTotallength() {
            return totallength;
        }

        public void setTotallength(float totallength) {
            this.totallength = totallength;
        }

        public float getAvgweeklength() {
            return avgweeklength;
        }

        public void setAvgweeklength(float avgweeklength) {
            this.avgweeklength = avgweeklength;
        }

        public int getAvgpace() {
            return avgpace;
        }

        public void setAvgpace(int avgpace) {
            this.avgpace = avgpace;
        }

        public int getMemberapplycount() {
            return memberapplycount;
        }

        public void setMemberapplycount(int memberapplycount) {
            this.memberapplycount = memberapplycount;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Label> getLabel() {
            return label;
        }

        public void setLabel(List<Label> label) {
            this.label = label;
        }

        public List<MemberManager> getManager() {
            return manager;
        }

        public void setManager(List<MemberManager> manager) {
            this.manager = manager;
        }

        public int getManagercount() {
            return managercount;
        }

        public void setManagercount(int managercount) {
            this.managercount = managercount;
        }

        public List<Ranking> getRanking() {
            return ranking;
        }

        public void setRanking(List<Ranking> ranking) {
            this.ranking = ranking;
        }

        public int getMyranking() {
            return myranking;
        }

        public void setMyranking(int myranking) {
            this.myranking = myranking;
        }

        public List<Party> getParty() {
            return party;
        }

        public void setParty(List<Party> party) {
            this.party = party;
        }

        public int getPartycount() {
            return partycount;
        }

        public void setPartycount(int partycount) {
            this.partycount = partycount;
        }

        /**
         * 标签
         */
        public static class Label implements Serializable{
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

        /**
         * 团管理员
         */
        public static class MemberManager implements Serializable{
            private String nickname;
            private String avatar;
            private String alias;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }
        }

        /**
         * 排名成员
         */
        public static class Ranking implements Serializable{
            private int id;
            private String name;
            private String nickname;
            private String avatar;
            private String alias;
            private float length;

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

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public float getLength() {
                return length;
            }

            public void setLength(float length) {
                this.length = length;
            }
        }

        /**
         * 活动
         */
        public static class Party implements Serializable{
            private int id;
            private String name;
            private String location;
            private String starttime;
            private String endtime;
            private int status;

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

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }
        }
    }
}
