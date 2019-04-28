package com.app.pao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.pao.fragment.history.HistoryBasicInfoFragmentV2;
import com.app.pao.fragment.history.HistoryHeartrateInfoFragmentV2;
import com.app.pao.fragment.history.HistorySpliteListFragmentV2;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Raul.Fan on 2016/5/5.
 */
public class HistoryInfoPagerAdapter extends FragmentStatePagerAdapter {

    public enum Tab {
        BASIC("概览"),
        SPLIT("分段"),
        HEARTRATE("心率");
        private final String name;

        private Tab(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        public String toString() {
            return name;
        }
    }

    Fragment[] mFragments;
    Tab[] mTabs;

    private static final Field sActiveField;

    static {
        Field f = null;
        try {
            Class<?> c = Class.forName("android.support.v4.app.FragmentManagerImpl");
            f = c.getDeclaredField("mActive");
            f.setAccessible(true);
        } catch (Exception e) {
        }
        sActiveField = f;
    }

    public HistoryInfoPagerAdapter(FragmentManager fm, Tab[] tabs ,Fragment[]  Fragments) {
        super(fm);
        mTabs = tabs;
//        mFragments = new Fragment[mTabs.length];
        mFragments = Fragments;


        //dirty way to get reference of cached fragment
        try {
            ArrayList<Fragment> mActive = (ArrayList<Fragment>) sActiveField.get(fm);
            if (mActive != null) {
                for (Fragment fragment : mActive) {
                    if (fragment instanceof HistoryBasicInfoFragmentV2)
                        setFragment(Tab.BASIC, fragment);
                    else if (fragment instanceof HistorySpliteListFragmentV2)
                        setFragment(Tab.SPLIT, fragment);
                    else if (fragment instanceof HistoryHeartrateInfoFragmentV2)
                        setFragment(Tab.HEARTRATE, fragment);
                }
            }
        } catch (Exception e) {
        }
    }

    private void setFragment(Tab tab, Fragment f) {
        for (int i = 0; i < mTabs.length; i++)
            if (mTabs[i] == tab) {
                mFragments[i] = f;
                break;
            }
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments[position] == null) {
            switch (mTabs[position]) {
                case BASIC:
                    mFragments[position] = HistoryBasicInfoFragmentV2.newInstance();
                    break;
                case SPLIT:
                    mFragments[position] = HistorySpliteListFragmentV2.newInstance();
                    break;
                case HEARTRATE:
                    mFragments[position] = HistoryHeartrateInfoFragmentV2.newInstance();
                    break;
            }
        }
        return mFragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position].toString();
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

}
