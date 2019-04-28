package com.app.pao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.pao.fragment.group.GroupMemberRankingMonthFragment;
import com.app.pao.fragment.group.GroupMemberRankingNameFragment;
import com.app.pao.fragment.group.GroupMemberRankingTotalFragment;
import com.app.pao.fragment.group.GroupMemberRankingWeekFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/1/29.
 *
 * 成员排行的viewPager adapter
 */
public class GroupMemberRankingPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public GroupMemberRankingPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
