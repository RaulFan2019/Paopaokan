package com.app.pao.entity.network;

/**
 * Created by LY on 2016/3/19.
 */
public class FriendRankNameWithPyFirst {
    private int type;
    private GetFriendRankResult friendRankResult;
    private char pinYinFirst;
    private int lastLine;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GetFriendRankResult getFriendRankResult() {
        return friendRankResult;
    }

    public void setFriendRankResult(GetFriendRankResult friendRankResult) {
        this.friendRankResult = friendRankResult;
    }

    public char getPinYinFirst() {
        return pinYinFirst;
    }

    public void setPinYinFirst(char pinYinFirst) {
        this.pinYinFirst = pinYinFirst;
    }

    public int getLastLine() {
        return lastLine;
    }

    public void setLastLine(int lastLine) {
        this.lastLine = lastLine;
    }

    public FriendRankNameWithPyFirst(int type, GetFriendRankResult friendRankResult, char pinYinFirst, int lastLine) {
        this.type = type;
        this.friendRankResult = friendRankResult;
        this.pinYinFirst = pinYinFirst;
        this.lastLine = lastLine;
    }
}
