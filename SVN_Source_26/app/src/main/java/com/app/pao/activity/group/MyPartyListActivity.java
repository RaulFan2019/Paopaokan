package com.app.pao.activity.group;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupPartyListAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyListResult;
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
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;
import hwh.com.pulltorefreshlibrary.PullableListView;

/**
 * Created by Raul on 2015/12/8.
 * 我的活动列表
 */
@ContentView(R.layout.activity_my_party_list)
public class MyPartyListActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "MyPartyListActivity";

    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_POST_OK = 3;//请求成功

    /* local view */
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mRefeshLayout;//刷新VIew
    @ViewInject(R.id.listview_party)
    private PullableListView mListView;//列表
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    /* local data */
    private boolean mPostAble;//是否可以发送请求

    private GroupPartyListAdapter mAdapter;//适配器
    private List<GetGroupPartyListResult> mDatas = new ArrayList<GetGroupPartyListResult>();
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
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostAble = true;
                    updateViewByPostResult();
                    mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
                    break;
            }
        }
    };

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回键
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.tv_reload:
                if (mPostAble) {
                    mPostAble = false;
                    PostGetPartyListRequst();
                }
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;

    }

    @Override
    protected void initViews() {
        RelativeLayout loadView = (RelativeLayout) mRefeshLayout.findViewById(R.id.loadmore_view);
        loadView.setVisibility(View.GONE);

        initRefreshListener();
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
    //    mRefeshLayout.autoRefresh();

    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    /**
     * 刷新事件监听
     */
    private void initRefreshListener() {
        if (mPostAble) {
            mPostAble = false;
            PostGetPartyListRequst();
        }

        mRefeshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                if (mPostAble) {
                    mPostAble = false;
                    PostGetPartyListRequst();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetGroupPartyListResult result = mDatas.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("partyid", result.getId());
                startActivity(GroupPartyInfoActivity.class, bundle);
            }
        });
    }


    /**
     * 获取活动列表
     */
    private void PostGetPartyListRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_USER_GROUP_PARTY_LIST;
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        RequestParams params = RequestParamsBuild.buildGetUserPartyListRequest(mContext,userId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDatas = JSON.parseArray(Response, GetGroupPartyListResult.class);
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

    /**
     * 销毁Handler
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }

    /**
     * 更新页面
     */
    private void updateViewByPostResult() {
        mAdapter = new GroupPartyListAdapter(mContext, mDatas);
        mListView.setAdapter(mAdapter);
    }
}
