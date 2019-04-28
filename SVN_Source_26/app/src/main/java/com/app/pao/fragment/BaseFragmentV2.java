package com.app.pao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/3.
 */
public abstract class BaseFragmentV2 extends Fragment {


    protected boolean mIsHidden = false;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initParams();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsHidden) {
            onVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mIsHidden) {
            onInVisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mIsHidden = hidden;
        if (!mIsHidden) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        causeGC();
    }


    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getContext(), cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 初始化布局
     **/
    protected abstract int getLayoutId();


    /**
     * 参数设置
     **/
    protected abstract void initParams();


    /**
     * 参数设置
     **/
    protected abstract void causeGC();

    /**
     * Fragment 可见
     */
    protected abstract void onVisible();

    /**
     * Fragment 不可见
     */
    protected abstract void onInVisible();
}
