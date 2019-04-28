package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupUserPartyListAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
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

/**
 * Created by Administrator on 2016/1/20.
 */
@ContentView(R.layout.activity_group_user_party_list)
public class GroupUserPartyListActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    /* local view */
    @ViewInject(R.id.tv_title_group_user_party)
    private TextView mTitleTv;
    @ViewInject(R.id.rv_user_party)
    private RecyclerView mUserPartyRv;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    private List<GetGroupPartyListResult> mDataList;
    private GroupUserPartyListAdapter mAdapter;

    private int mUserId;
    private String mUserName;
    private int mGroupId;//跑团ID

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
                mLoadV.setLoadingText("加载失败");
                mReladV.setVisibility(View.VISIBLE);
            } else if (msg.what == MSG_POST_OK) {
                if(mAdapter == null) {
                    mAdapter = new GroupUserPartyListAdapter(mContext, mDataList);
                    mUserPartyRv.setAdapter(mAdapter);
                }else {
                    mAdapter.notifyDataSetChanged();
                }
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
            }
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mGroupId = getIntent().getExtras().getInt("groupid");
        mUserId = getIntent().getIntExtra("userid",0);
        mUserName = getIntent().getStringExtra("userName");
        mDataList  = new ArrayList<GetGroupPartyListResult>();
    }

    @Override
    protected void initViews() {
        mUserPartyRv.setLayoutManager(new FullyLinearLayoutManager(this));
        mTitleTv.setText(mUserName+"的活动列表");
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        PostGetPartyListRequst();
    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    /**
     * 销毁Handler
     */
    private void cancelHandler() {
        if (handler != null) {
            handler.removeMessages(MSG_POST_OK);
            handler.removeMessages(MSG_POST_ERROR);
        }
    }

    /**
     * 获取活动列表
     */
    private void PostGetPartyListRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_USER_GROUP_PARTY_LIST;
        RequestParams params = RequestParamsBuild.buildGetUserGroupPartyListRequest(mContext,mUserId,mGroupId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDataList.clear();
                List<GetGroupPartyListResult> mPartyList = JSON.parseArray(Response, GetGroupPartyListResult.class);
                mDataList.addAll(mPartyList);
                if (handler != null) {
                    handler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

}
