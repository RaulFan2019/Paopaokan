package com.app.pao.activity.regist;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.lidroid.xutils.view.annotation.ViewInject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Raul on 2015/11/12.
 * 用户隐私
 */
public class PrivacyActivity extends BaseActivityV3 {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
    protected int getLayoutId() {
        return R.layout.activity_privacy;
    }

    @Override
    protected void initData() {
        mCheckNewData = false;
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }
}
