package com.app.pao.entity.model;

import com.app.pao.entity.network.GetSearFriendbyNameList;

public class PhoneNumEntity {

    private String contactName;// 得到联系人名称
    private Long contactid;// 得到联系人ID
    private Long photoid;// 得到联系人头像ID
    private String phoneNumber;// 得到手机号码
    private String sortKey; //联系人拼音汉字组合 用于汉字排序（如 ： HUA 华 WEI 为 KE 客 FU 服）
    private GetSearFriendbyNameList.UsersEntity usersEntity;

    public PhoneNumEntity(String contactName, Long contactid, Long photoid, String phoneNumber, String sortKey) {
        super();
        this.contactName = contactName;
        this.contactid = contactid;
        this.photoid = photoid;
        this.phoneNumber = phoneNumber;
        this.sortKey = sortKey;
        usersEntity = null;
    }

    public GetSearFriendbyNameList.UsersEntity getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(GetSearFriendbyNameList.UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public PhoneNumEntity() {
        super();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getContactid() {
        return contactid;
    }

    public void setContactid(Long contactid) {
        this.contactid = contactid;
    }

    public Long getPhotoid() {
        return photoid;
    }

    public void setPhotoid(Long photoid) {
        this.photoid = photoid;
    }

}
