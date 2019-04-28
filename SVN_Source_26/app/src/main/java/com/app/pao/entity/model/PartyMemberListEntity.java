package com.app.pao.entity.model;

import com.app.pao.entity.network.GetGroupPartyMemberListResult;

/**
 * Created by Administrator on 2016/1/19.
 * 活动报名列表
 */
public class PartyMemberListEntity {

    private GetGroupPartyMemberListResult member;
    private boolean isHeader;
    private String headerStr;
    private String headerNumStr;

    public PartyMemberListEntity(GetGroupPartyMemberListResult member, boolean isHeader, String headerStr,String headerNumStr) {
        this.member = member;
        this.isHeader = isHeader;
        this.headerStr = headerStr;
        this.headerNumStr = headerNumStr;
    }

    public GetGroupPartyMemberListResult getMember() {
        return member;
    }

    public void setMember(GetGroupPartyMemberListResult member) {
        this.member = member;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public String getHeaderStr() {
        return headerStr;
    }

    public void setHeaderStr(String headerStr) {
        this.headerStr = headerStr;
    }

    public String getHeaderNumStr() {
        return headerNumStr;
    }

    public void setHeaderNumStr(String headerNumStr) {
        this.headerNumStr = headerNumStr;
    }
}
