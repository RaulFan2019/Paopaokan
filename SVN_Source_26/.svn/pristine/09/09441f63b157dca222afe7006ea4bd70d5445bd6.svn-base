package com.app.pao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.pao.fragment.history.HistoryBasicInfoFragmentV2;
import com.app.pao.fragment.history.HistoryHeartrateInfoFragmentV2;
import com.app.pao.fragment.history.HistorySpliteListFragmentV2;
import com.app.pao.fragment.run.LiveBasicFragment;
import com.app.pao.fragment.run.LiveSplitFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Raul.Fan on 2016/5/5.
 */
public class LivePagerAdapter extends FragmentStatePagerAdapter {

    public enum Tab {
        BASIC("直播"),
        SPLIT("分段");

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

    public LivePagerAdapter(FragmentManager fm, Tab[] tabs, Fragment[] Fragments) {
        super(fm);
        mTabs = tabs;
//        mFragments = new Fragment[mTabs.length];
        mFragments = Fragments;


        //dirty way to get reference of cached fragment
        try {
            ArrayList<Fragment> mActive = (ArrayList<Fragment>) sActiveField.get(fm);
            if (mActive != null) {
                for (Fragment fragment : mActive) {
                    if (fragment instanceof LiveBasicFragment)
                        setFragment(Tab.BASIC, fragment);
                    else if (fragment instanceof LiveSplitFragment)
                        setFragment(Tab.SPLIT, fragment);
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
                    mFragments[position] = LiveBasicFragment.newInstance();
                    break;
                case SPLIT:
                    mFragments[position] = LiveSplitFragment.newInstance();
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
