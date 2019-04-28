package com.app.pao.activity.friend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.ApplyFriendRecycleAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.network.GetApplyFriendListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2015/12/2.
 * 好友请求列表
 */
@ContentView(R.layout.activity_apply_friend_list)
public class ApplyFriendListActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "ApplyFriendListActivity";

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.listview)
    private RecyclerView mListView;//列表
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mRefeshLayout;//刷新页面
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    /* local data */
    private boolean isFirstIn = true;
    private boolean mPostAble = true;//是否可以POST
    private boolean mIsRefreshing = false;//判断是否可以刷新

    private ApplyFriendRecycleAdapter mAdapter;

    private List<GetApplyFriendListResult> mDatas = new ArrayList<GetApplyFriendListResult>();


    /**
     * 更新页面Handler
     */
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
                    mPostAble = true;
                    mPostHandler.removeCallbacks(mPostRunnable);
                    mRefeshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostAble = true;
                    mIsRefreshing = false;
                    updateViewByPostResult();
                    mPostHandler.removeCallbacks(mPostRunnable);
                    mLoadViewRl.setVisibility(View.GONE);
                    mLoadV.setVisibility(View.GONE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
            }
        }
    };

    Runnable mPostRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
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
    @OnClick({R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                if (mPostAble) {
                    mPostAble = false;
                    getApplyFriendList();
                }
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    protected void initData() {
        mPostAble = true;
        mAdapter = new ApplyFriendRecycleAdapter(mContext, mDatas);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefreshing;
            }
        });
        RelativeLayout loadView = (RelativeLayout) mRefeshLayout.findViewById(R.id.loadmore_view);
        loadView.setVisibility(View.GONE);
        initRefreshListener();
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        if (mPostAble) {
            mPostAble = false;
            getApplyFriendList();
        }

        if (isFirstIn) {
            isFirstIn = false;
        } else {
            mPostHandler.postDelayed(mPostRunnable, 500);
        }
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    /**
     * 获取好友申请列表
     */
    private void getApplyFriendList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_APPLY_FRIEND_LIST;
        RequestParams params = RequestParamsBuild.buildGetApplyFriendList(mContext);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }

            }

            @Override
            protected void onRightResponse(String Response) {
                mIsRefreshing = true;
                mDatas.clear();
                List<GetApplyFriendListResult> tempList = JSON.parseArray(Response, GetApplyFriendListResult.class);
                if (tempList != null) {
                    mDatas.addAll(tempList);
                }
                int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
                MessageData.readAllNewFriendMessage(mContext, userId);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }

            }

            @Override
            protected void onFinish() {

            }
        });
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
                    getApplyFriendList();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeCallbacks(mPostRunnable);
        }
    }

    /**
     * 更新页面
     */
    private void updateViewByPostResult() {
        mAdapter.notifyDataSetChanged();

        mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    /**
     * 发送同意请求
     */
    public void PostAgreeRequest(final int pos) {
        mDialogBuilder.showProgressDialog(mContext, "同意加为好友", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_APPROVE_ADD_FRIEND;
        RequestParams params = RequestParamsBuild.buildApproveAddFriend(mContext, mDatas.get(pos).getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPostHandler.sendMessage(msg);
                }

            }

            @Override
            protected void onRightResponse(String Response) {
                mIsRefreshing = false;
                GetApplyFriendListResult entity = mDatas.get(pos);
                entity.setStatus(AppEnum.friendApplyStatus.AGREE);
                mDatas.set(pos, entity);
                mAdapter.notifyDataSetChanged();
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

}
