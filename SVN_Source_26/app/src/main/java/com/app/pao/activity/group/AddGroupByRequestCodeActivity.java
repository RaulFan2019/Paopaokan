package com.app.pao.activity.group;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Raul on 2015/11/25.
 * 创建跑团(创建跑团)
 */
@ContentView(R.layout.activity_add_group_by_requestcode)
public class AddGroupByRequestCodeActivity extends BaseAppCompActivity {

    /* contains */
    private static final String TAG = "AddGroupByRequestCodeActivity";


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏


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

    }

    @Override
    protected void initViews() {
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
