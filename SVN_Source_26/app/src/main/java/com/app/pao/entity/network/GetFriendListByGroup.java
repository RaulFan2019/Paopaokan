package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/12/14.
 * 获取好友在团中的关系
 */
public class GetFriendListByGroup {


    /**
     * count : 20
     * friends : [{"id":"888","name":"888","nickname":"仿真机器人1","avatar":"http://wx.qlogo
     * .cn/mmopen/TTQibyKjrickxxUX7ia2PwiasxRbsVXMgLc8A13iaOfsysUF6GzPiankNY9q7Kk0dibiatBg7aE69L4ZuLWiaCZ1PESMzoMT1muWfRovR/0","gender":"1","totallength":2360283,"isrunning":0,"grouprole":0},{"id":"890","name":"890","nickname":"仿真机器人3","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-08_561606f81f5ca.jpg","gender":"1","totallength":1076295,"isrunning":0,"grouprole":0},{"id":"10182","name":"13671648211","nickname":"沈绉","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-09-25_5604706b7581b.","gender":"1","totallength":186803,"isrunning":0,"grouprole":3},{"id":"10362","name":"13817732931","nickname":"沈爱疯四","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-08_5616645cceeac.jpg","gender":"1","totallength":93240,"isrunning":0,"grouprole":3},{"id":"10379","name":"rm晓俊","nickname":"rm晓俊","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-15_561f9e5bdc483.jpg","gender":"1","totallength":1,"isrunning":0,"grouprole":0},{"id":"10378","name":"11921613015","nickname":"晓俊3","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-15_561f85c72b025.jpg","gender":"0","totallength":0,"isrunning":0,"grouprole":0},{"id":"10382","name":"10091912997","nickname":"大家乐","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-15_561f961d56df4.jpg","gender":"1","totallength":525,"isrunning":0,"grouprole":0},{"id":"10341","name":"","nickname":"王海平","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-16_56208a15384d9.jpg","gender":"0","totallength":0,"isrunning":0,"grouprole":0},{"id":"10385","name":"rmJ9nCvsbUFfe3GNuzf1pK__JyoR0","nickname":"晓俊","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-23_5629a29d204d5.jpg","gender":"1","totallength":12124,"isrunning":0,"grouprole":3},{"id":"10055","name":"13816837003","nickname":"Jessie","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-17_5621211186d4e.","gender":"2","totallength":48602,"isrunning":0,"grouprole":0},{"id":"10405","name":"11921613015","nickname":"俊哥","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-19_562470de549e5.jpg","gender":"1","totallength":20090,"isrunning":0,"grouprole":0},{"id":"10359","name":"oJ9nCvsCPgx3po-fPEaIjB9lHDOo","nickname":"小飞","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-25_562c775cf2863.jpg","gender":"1","totallength":141347,"isrunning":0,"grouprole":3},{"id":"10072","name":"13002179931","nickname":"d","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-06_563c1dbd5337d.jpg","gender":"1","totallength":774574,"isrunning":0,"grouprole":3},{"id":"10076","name":"13701675260","nickname":"Weil","avatar":"","gender":"1","totallength":35704,"isrunning":0,"grouprole":3},{"id":"10154","name":"oJ9nCvvi5zmUem4chhQawEbo1R4M","nickname":"raul","avatar":"http://wx.qlogo.cn/mmopen/TTQibyKjrickxxUX7ia2PwiasxRbsVXMgLc8A13iaOfsysUF6GzPiankNY9q7Kk0dibiatBg7aE69L4ZuLWiaCZ1PESMzoMT1muWfRovR/0","gender":"1","totallength":0,"isrunning":0,"grouprole":3},{"id":"10054","name":"15221798774","nickname":"Raul","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-02_56374c15e66b8.","gender":"2","totallength":57338,"isrunning":0,"grouprole":3},{"id":"10392","name":"oJ9nCvhWkfDd0LIlqtFyj67vMIBM","nickname":"晓俊123","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-05_563b443d7fdc7.jpg","gender":"1","totallength":41709,"isrunning":0,"grouprole":3},{"id":"10060","name":"15332485207","nickname":"阿木木得得得阿快乐","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-06_563c10f841f97.jpg","gender":"1","totallength":479180.55,"isrunning":0,"grouprole":3},{"id":"10043","name":"13817219942","nickname":"真的爱你","avatar":"","gender":"1","totallength":0,"isrunning":0,"grouprole":0},{"id":"10443","name":"15921613015","nickname":"俊男","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-10-31_5634be6ed179f.jpg","gender":"1","totallength":0,"isrunning":0,"grouprole":4}]
     */

    private int count;//人数
    /**
     * id : 888
     * name : 888
     * nickname : 仿真机器人1
     * avatar : http://wx.qlogo
     * .cn/mmopen/TTQibyKjrickxxUX7ia2PwiasxRbsVXMgLc8A13iaOfsysUF6GzPiankNY9q7Kk0dibiatBg7aE69L4ZuLWiaCZ1PESMzoMT1muWfRovR/0
     * gender : 1
     * totallength : 2360283
     * isrunning : 0
     * grouprole : 0
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

    public static class FriendsEntity {
        public int id;
        public String name;
        public String nickname;
        public String avatar;
        public int gender;
        public int totallength;
        public int isrunning;
        public int grouprole;

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

        public void setGrouprole(int grouprole) {
            this.grouprole = grouprole;
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

        public int getGrouprole() {
            return grouprole;
        }
    }
}
