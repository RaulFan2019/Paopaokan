package com.app.pao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.lidroid.xutils.ViewUtils;

public abstract class BaseFragment extends Fragment {

    protected String TAG;
    protected Context mContext;// 上下文
    protected MyDialogBuilderV1 mDialogBuilder;
    protected Bundle mSavedInstanceState;
    protected InputMethodManager imm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getFragmentManager().getClass().getName();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ViewUtils.inject(this, view);
        mContext = getActivity();
        mDialogBuilder = new MyDialogBuilderV1();
        mSavedInstanceState = savedInstanceState;
        imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        initParams();
        return view;
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
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

}
