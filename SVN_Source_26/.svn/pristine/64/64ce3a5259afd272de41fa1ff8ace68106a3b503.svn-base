package com.app.pao.activity.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.adapter.MessageListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.db.DBEntityMessage;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/12/10.
 * 消息列表界面
 */
@ContentView(R.layout.activity_message_list)
public class MessageListActivity extends BaseAppCompActivity implements AdapterView.OnItemClickListener {

    /* contains */
    private static final String TAG = "MessageListActivity";


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.lv_message)
    private ListView mListView;//列表


    /* local data */
    private MessageListAdapter mAdapter;//消息适配器
    private List<DBEntityMessage> mDatas;//数据

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
    }

    @Override
    protected void doMyOnCreate() {
    }

    @Override
    protected void updateData() {
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mDatas = MessageData.getAllMessageByUser(mContext, userId);
    }

    @Override
    protected void updateViews() {
        mAdapter = new MessageListAdapter(mContext, mDatas);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void destroy() {

    }

    @Override
    @OnItemClick(R.id.lv_message)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DBEntityMessage entityMessage = mDatas.get(position);
        entityMessage.setStatus(AppEnum.messageRead.OLD);
        MessageData.updateMessage(mContext,entityMessage);
        if (entityMessage.getType() == AppEnum.messageType.APPLY_ADD_FRIEND || entityMessage.getType() == AppEnum
                .messageType.APPROVE_ADD_FRIEND) {
            Bundle bundle = new Bundle();
            bundle.putInt("userid", entityMessage.getFromuserid());
            startActivity(UserInfoActivity.class,bundle);
        } else if (entityMessage.getType() == AppEnum.messageType.INVITE_JOIN_RUN_GROUP || entityMessage.getType() ==
                AppEnum.messageType.APPROVE_JOIN_RUN_GROUP) {
            Bundle bundle = new Bundle();
            bundle.putInt("groupid", Integer.parseInt(entityMessage.getExtra()));
            startActivity(GroupInfoActivity.class, bundle);
        } else if (entityMessage.getType() == AppEnum.messageType.APPLY_JOIN_RUNGROUP) {
            Bundle bundle = new Bundle();
            bundle.putInt("groupid", Integer.parseInt(entityMessage.getExtra()));
            startActivity(GroupInfoActivity.class, bundle);
        }
    }
}
