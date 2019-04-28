package com.app.pao.activity.group;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

/**
 * Created by Raul on 2015/12/5.
 * 创建跑团时的提示页面
 */
@ContentView(R.layout.activity_create_group_tip)
public class CreateGroupTipActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "CreateGroupTipActivity";

    /* local data */
    private boolean hasCreate;//已创建
    private String groupName;//自己跑团的名称

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.btn_create)
    private Button mCreateBtn;//创造跑团按钮
    @ViewInject(R.id.tv_tip)
    private TextView mTipTv;//提示文字

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
        hasCreate = getIntent().getExtras().getBoolean("hasgroup");
        groupName = getIntent().getExtras().getString("groupname");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        if (hasCreate) {
            mCreateBtn.setVisibility(View.GONE);
            String tip = "\t\t您已经创建了<" + groupName +
                    ">跑团，无法创建新的跑团。" +  getResources().getText(R.string.Tv_CreateGroupTip_Body_HasGroup);

            mTipTv.setText(tip);
        } else {
            mCreateBtn.setVisibility(View.VISIBLE);
            mTipTv.setText(getResources().getText(R.string.Tv_CreateGroupTip_Body_NotHasGroup));
        }
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

    @Override
    @OnClick({R.id.btn_create})
    public void onClick(View v) {
        if (v.getId() == R.id.btn_create) {
            startActivity(CreateGroupCheckActivity.class);
            finish();
        }
    }
}
