package com.app.pao.utils.comparator;

import com.app.pao.entity.network.GetFriendRankResult;
import com.app.pao.entity.network.GetGroupMemberSortResult;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/3/28.
 */
public class GroupMemberRankingNameComparator implements Comparator<GetGroupMemberSortResult> {
    @Override
    public int compare(GetGroupMemberSortResult lhs, GetGroupMemberSortResult rhs) {
        if(lhs.getFirstPinYinChar()=='#'){
            return 1;
        }else if(rhs.getFirstPinYinChar()=='#'){
            return -1;
        }else
            return lhs.getFirstPinYinChar()-rhs.getFirstPinYinChar();
    }
}
