package com.app.pao.entity.network;

/**
 * Created by Administrator on 2016/3/27.
 */
public class GroupRankNameWithFirstPy {
    private int type;
    private char firstPinYin;
    private GetGroupMemberSortResult groupMember;
    private int lastLine;

    public GroupRankNameWithFirstPy() {
    }

    public GroupRankNameWithFirstPy(int type, char firstPinYin, GetGroupMemberSortResult groupMember, int lastLine) {
        this.type = type;
        this.firstPinYin = firstPinYin;
        this.groupMember = groupMember;
        this.lastLine = lastLine;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public char getFirstPinYin() {
        return firstPinYin;
    }

    public void setFirstPinYin(char firstPinYin) {
        this.firstPinYin = firstPinYin;
    }

    public GetGroupMemberSortResult getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(GetGroupMemberSortResult groupMember) {
        this.groupMember = groupMember;
    }

    public int getLastLine() {
        return lastLine;
    }

    public void setLastLine(int lastLine) {
        this.lastLine = lastLine;
    }
}
