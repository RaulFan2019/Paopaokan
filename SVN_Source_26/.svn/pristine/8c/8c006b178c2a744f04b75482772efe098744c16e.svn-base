package com.app.pao.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.pao.ActivityStackManager;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Raul.Fan on 2016/4/20.
 */
public abstract class BaseAppCompActivityV2 extends AppCompatActivity {

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getAppManager().addActivity(this);
        ViewUtils.inject(this);
        initData();
        initViews();
        doMyOnCreate();
    }

    /**
     * on Resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 友盟集成
        MobclickAgent.onResume(this);
        updateData();
        updateViews();
    }

    /**
     * on Pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        // 友盟集成
        MobclickAgent.onPause(this);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * on Destroy
     */
    @Override
    protected void onDestroy() {
        destroy();
        ActivityStackManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    //准备数据
    protected abstract void initData();

    //准备数据
    protected abstract void initViews();

    //执行Create结束后的事情
    protected abstract void doMyOnCreate();

    //更新数据
    protected abstract void updateData();

    //更新界面
    protected abstract void updateViews();

    //销毁界面的时候需要做的事
    protected abstract void destroy();
}
