package com.app.pao.entity.network;

import com.app.pao.utils.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by LY on 2016/3/16.
 */
public class GetFriendRankResult {


    /**
     * id : 10373
     * name : 18621196736
     * nickname : 舟溪
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-10-28_56308dacd1018.jpg
     * gender : 0
     * locationprovince : 上海
     * locationcity : 浦东新区
     * length : 9177
     * birthdate : 1979-07-08
     * isrunning : 0
     */

    private int id;
    private String name;
    private String nickname;
    private String avatar;
    private int gender;
    private String locationprovince;
    private String locationcity;
    private int length;
    private String birthdate;
    private int isrunning;

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLocationprovince() {
        return locationprovince;
    }

    public void setLocationprovince(String locationprovince) {
        this.locationprovince = locationprovince;
    }

    public String getLocationcity() {
        return locationcity;
    }

    public void setLocationcity(String locationcity) {
        this.locationcity = locationcity;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getIsrunning() {
        return isrunning;
    }

    public void setIsrunning(int isrunning) {
        this.isrunning = isrunning;
    }

    public char getPinYinFirst(){
        return StringUtils.getPinYinFirst(getNickname());
    }

}
