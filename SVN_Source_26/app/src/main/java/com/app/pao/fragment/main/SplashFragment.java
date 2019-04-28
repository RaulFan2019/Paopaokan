package com.app.pao.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 引导页 Fragment
 * Created by Raul on 2015/9/28.
 */
public class SplashFragment extends Fragment {

    private  static final String LAYOUT_ID = "layoutId";

    public static SplashFragment newInstance(int layoutId) {
        Bundle args = new Bundle();
        SplashFragment fragment = new SplashFragment();
        args.putInt(LAYOUT_ID,layoutId);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getArguments().getInt(LAYOUT_ID,-1),container,false);
        return view;
    }
}
