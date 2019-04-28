package com.app.pao.fragment.friend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.app.pao.R;
import com.app.pao.fragment.BaseFragmentV2;
import com.rey.material.widget.TabPageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Raul.Fan on 2016/9/13.
 */
public class TouristFriendFragmentV2 extends BaseFragmentV2 {

    public static TouristFriendFragmentV2 newInstance() {
        TouristFriendFragmentV2 fragment = new TouristFriendFragmentV2();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tourist_friend;
    }

    @Override
    protected void initParams() {

    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInVisible() {

    }

    @OnClick(R.id.btn_friend_to_login)
    public void onClick() {
        getActivity().finish();
    }
}
