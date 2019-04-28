package com.app.pao.activity.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

/**
 * Created by Administrator on 2015/12/2.
 */
@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseAppCompActivity{

    @ViewInject(R.id.tv_app_ver)
    private TextView mAppVerTv;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏

    private String mAppVerStr;//版本字符串


    /**
     * Toolbar 点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        //获取版本号
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            mAppVerStr = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initViews() {
        mAppVerTv.setText("当前版本："+mAppVerStr);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {

    }



}
