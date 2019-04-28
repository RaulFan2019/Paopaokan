package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raul on 2015/12/3.
 * 获取共同好友
 */
public class GetSameFriendResult {

    /**
     * count : 9
     * friends : [{"id":"10054","name":"10221798774","nickname":"Raul","avatar":"http://7xk0si.com1.z0.glb.clouddn
     * .com/2015-11-02_56374c15e66b8.","gender":"2","totallength":57338,"isrunning":0},{"id":"10182",
     * "name":"13671648211","nickname":"沈绉","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-20_564f0763aa453
     * .jpg","gender":"1","totallength":186803,"isrunning":0},{"id":"10060","name":"15332485207","nickname":"阿木木得得地",
     * "avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-29_565ada4982a3a.jpg","gender":"1",
     * "totallength":480130.55,"isrunning":0},{"id":"10378","name":"11921613015","nickname":"晓俊3",
     * "avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-15_561f85c72b025.jpg","gender":"0","totallength":0,
     * "isrunning":0},{"id":"10341","name":"","nickname":"王海平","avatar":"http://7xk0si.com1.z0.glb.clouddn
     * .com/2015-10-16_56208a15384d9.jpg","gender":"0","totallength":0,"isrunning":0},{"id":"10359",
     * "name":"oJ9nCvsCPgx3po-fPEaIjB9lHDOo","nickname":"小飞","avatar":"http://7xk0si.com1.z0.glb.clouddn
     * .com/2015-10-25_562c775cf2863.jpg","gender":"1","totallength":141347,"isrunning":0},{"id":"10076",
     * "name":"13701675260","nickname":"Weil","avatar":"","gender":"1","totallength":35704,"isrunning":0},
     * {"id":"10423","name":"rm","nickname":"赵元","avatar":"http://7xk0si.com1.z0.glb.clouddn
     * .com/2015-11-06_563c5d7d6dfeb.","gender":"1","totallength":6653.66,"isrunning":0},{"id":"10614",
     * "name":"15221798774","nickname":"raul","avatar":"http://7xk0si.com1.z0.glb.clouddn
     * .com/2015-11-17_564ac2a0edc29.","gender":"1","totallength":379.3,"isrunning":0}]
     */

    private int count;//好友数量
    /**
     * id : 10054
     * name : 10221798774
     * nickname : Raul
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-02_56374c15e66b8.
     * gender : 2
     * totallength : 57338
     * isrunning : 0
     */

    private List<FriendsEntity> friends;//好友列表

    public void setCount(int count) {
        this.count = count;
    }

    public void setFriends(List<FriendsEntity> friends) {
        this.friends = friends;
    }

    public int getCount() {
        return count;
    }

    public List<FriendsEntity> getFriends() {
        return friends;
    }

    public static class FriendsEntity implements Serializable {
        private int id;//用户id
        private String name;//用户名
        private String nickname;//用户昵称
        private String avatar;//用户头像
        private int gender;//用户性别
        private int totallength;//总公里
        private int isrunning;//是否在跑步

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

        public void setTotallength(int totallength) {
            this.totallength = totallength;
        }

        public void setIsrunning(int isrunning) {
            this.isrunning = isrunning;
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

        public int getTotallength() {
            return totallength;
        }

        public int getIsrunning() {
            return isrunning;
        }
    }
}
