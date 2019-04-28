package com.app.pao.utils.comparator;

import com.app.pao.entity.network.GetFriendRankResult;

import java.util.Comparator;

/**
 * Created by LY on 2016/3/17.
 */
public class FriendRankingNameComparator implements Comparator<GetFriendRankResult> {
    @Override
    public int compare(GetFriendRankResult lhs, GetFriendRankResult rhs) {
        if(lhs.getPinYinFirst()=='#'){
            return 1;
        }else if(rhs.getPinYinFirst()=='#'){
            return -1;
        }else
        return lhs.getPinYinFirst()-rhs.getPinYinFirst();
    }
}
