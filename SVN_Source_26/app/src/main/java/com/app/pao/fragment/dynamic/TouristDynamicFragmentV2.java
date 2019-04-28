package com.app.pao.fragment.dynamic;

import com.app.pao.R;
import com.app.pao.fragment.BaseFragmentV2;

import butterknife.OnClick;

/**
 * Created by Raul.Fan on 2016/9/13.
 */
public class TouristDynamicFragmentV2 extends BaseFragmentV2 {

    public static TouristDynamicFragmentV2 newInstance() {
        TouristDynamicFragmentV2 fragment = new TouristDynamicFragmentV2();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tourist_dynamic;
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

    @OnClick(R.id.btn_go_to_login)
    public void onClick() {
        getActivity().finish();
    }
}
