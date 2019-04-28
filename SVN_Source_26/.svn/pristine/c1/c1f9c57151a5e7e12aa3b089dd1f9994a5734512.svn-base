package com.app.pao.activity.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.MessageRvAdapter;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.db.DBEntityMessage;
import com.app.pao.ui.widget.helper.SimpleItemTouchHelperCallback;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/12/10.
 * 消息列表界面
 */
@ContentView(R.layout.activity_message_list_replace)
public class MessageListActivityReplace extends BaseAppCompActivity {

    /* contains */
    private static final String TAG = "MessageListActivity";


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    //    @ViewInject(R.id.rv_message)
    private RecyclerView mRecyclerView;//列表
    @ViewInject(R.id.ll_none)
    private LinearLayout mNoneLl;


    /* local data */
    private MessageRvAdapter mAdapter;//消息适配器
    private List<DBEntityMessage> mDatas;//数据
//    private ItemTouchHelper mItemTouchHelper;

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
        mDatas = new ArrayList<DBEntityMessage>();
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_message);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mDatas = MessageData.getAllMessageByUser(mContext,userId);
        mAdapter = new MessageRvAdapter(mContext, mDatas);
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void doMyOnCreate() {
    }

    @Override
    protected void updateData() {
    }

    @Override
    protected void updateViews() {
        mAdapter.notifyDataSetChanged();
        if (mDatas.size() == 0) {
            mNoneLl.setVisibility(View.VISIBLE);
        }else {
            mNoneLl.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void destroy() {

    }

}
