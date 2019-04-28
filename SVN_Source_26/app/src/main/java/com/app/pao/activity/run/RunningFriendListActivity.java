package com.app.pao.activity.run;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.FriendListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetFriendListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;
import hwh.com.pulltorefreshlibrary.PullableListView;
import hwh.com.pulltorefreshlibrary.PullableRecylerView;

/**
 * Created by Raul on 2015/12/1.
 * 正在跑步的人员列表
 */
@ContentView(R.layout.activity_running_friend_list)
public class RunningFriendListActivity extends BaseAppCompActivity {

    /* contains */
    private static final String TAG = "RunningFriendListActivity";

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.refresh_layout_friend_list)
    private PullToRefreshLayout mRefeshLayout;//刷新VIew
    @ViewInject(R.id.lv_running_list)
    private PullableRecylerView mListView;//列表

    private FriendListAdapter mAdapter;

    /* local data */
    private boolean mPostAble;//是否可以发送请求
    private List<GetFriendListResult.FriendsEntity> mRunnerList = new ArrayList<GetFriendListResult.FriendsEntity>();
    private boolean isFirstIn;

    /**
     * 请求返回处理
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //发送请求错误
                case MSG_POST_ERROR:
                    T.showShort(mContext, (String) msg.obj);
                    mPostAble = true;
                    mRefeshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostAble = true;
                    mAdapter = new FriendListAdapter(mContext, mRunnerList);
                    mListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

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
        mPostAble = true;
        isFirstIn = true;
        mRunnerList = (List<GetFriendListResult.FriendsEntity>) getIntent().getExtras().get("runner");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mAdapter = new FriendListAdapter(mContext, mRunnerList);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setAdapter(mAdapter);

        RelativeLayout loadView = (RelativeLayout) mRefeshLayout.findViewById(R.id.loadmore_view);
        loadView.setVisibility(View.GONE);
        initRefreshListener();
    }

    /**
     * 刷新事件监听
     */
    private void initRefreshListener() {
        mRefeshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                if (mPostAble) {
                    mPostAble = false;
                    PostGetFriendRequst();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                GetFriendListResult.FriendsEntity entity = mRunnerList.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putInt("userId", entity.getId());
//                bundle.putString("userNickName", entity.getNickname());
//                bundle.putInt("userGender", entity.getGender());
//                bundle.putString("avatar", entity.getAvatar());
//                startActivity(LiveActivityReplace.class, bundle);
//            }
//        });
    }

    /**
     * 获取好友列表请求
     */
    private void PostGetFriendRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_FRIEND;
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        RequestParams params = RequestParamsBuild.buildGetFriendListRequest(mContext,userId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.obj = errorMsg;
                    msg.what = MSG_POST_ERROR;
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetFriendListResult result = JSON.parseObject(Response, GetFriendListResult.class);
                List<GetFriendListResult.FriendsEntity> FriendList = result.getFriends();
                mRunnerList.clear();
                for (GetFriendListResult.FriendsEntity friendsEntity : FriendList) {
                    if (friendsEntity.getIsrunning() == AppEnum.IsRunning.RUNNING) {
                        mRunnerList.add(friendsEntity);
                    }
                }
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.obj = s;
                    msg.what = MSG_POST_ERROR;
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        if (isFirstIn) {
            isFirstIn = false;
        } else {
            mRefeshLayout.autoRefresh();
        }
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler = null;
        }
    }
}
