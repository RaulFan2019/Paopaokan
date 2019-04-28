package com.app.pao.activity.workout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.adapter.ThumbUpListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetThumbsResult;
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
 * Created by Raul on 2015/11/29.
 * 点赞列表
 */
@ContentView(R.layout.activity_thumbup_list)
public class ThumbUpActivity extends BaseAppCompActivity implements View.OnClickListener, AdapterView
        .OnItemClickListener {


    /* contains */
    private static final String TAG = "ThumbUpActivity";
    private static final int MSG_UPDATE_VIEW = 0;//更新页面
    private static final int MSG_UPDATE_ERROR = 1;//更新失败

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mFreshLayout;//刷新页面
    @ViewInject(R.id.listview)
    private ListView listView;//列表

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    /* local data */
    private boolean isFirstIn = true;
    private boolean postAble = true;
    private ThumbUpListAdapter mAdapter;//适配器

    private List<GetThumbsResult> mDataList = new ArrayList<GetThumbsResult>();

    private int mUserId;//用户id
    private String mWorkoutName;//跑步历史名称
    private int targetid;
    private int type;

    /**
     * 更新页面Handler
     */
    Handler updateViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VIEW) {
                postAble = true;
                mFreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
            } else if (msg.what == MSG_UPDATE_ERROR) {
                postAble = true;
                mFreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                mLoadV.setLoadingText("加载失败");
                mReladV.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    @OnClick({R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                postGetThumbUpList();
                break;
        }
    }

    @Override
    @OnItemClick({R.id.listview})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDataList != null && mDataList.size() != 0) {
            if (mDataList.get(position).getUserid() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                return;
            }
            if (LocalApplication.getInstance().getLoginUser(mContext).userId != mDataList.get(position).getUserid()) {
                Bundle bundle = new Bundle();
                bundle.putInt("userid", mDataList.get(position).getUserid());
                startActivity(UserInfoActivity.class, bundle);
            }
        }
    }

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
        postAble = true;
        targetid = getIntent().getExtras().getInt("targetid");
        type = getIntent().getExtras().getInt("type");
        mUserId = getIntent().getExtras().getInt("userid");
        mWorkoutName = getIntent().getExtras().getString("workoutname");
        if (getIntent().hasExtra("thumb")) {
            mDataList = (List<GetThumbsResult>) getIntent().getExtras().getSerializable("thumb");
        } else {
            mDataList = null;
        }
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        RelativeLayout loadView = (RelativeLayout) mFreshLayout.findViewById(R.id.loadmore_view);
        loadView.setVisibility(View.GONE);

        mFreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            //下拉刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                postGetThumbUpList();
            }

            //上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                mFreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            }
        });

        if (mDataList != null) {
            mAdapter = new ThumbUpListAdapter(mContext, mDataList);
            listView.setAdapter(mAdapter);
        } else {
            postGetThumbUpList();
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
        cancelHandler();
    }

    /**
     * 获取点赞列表
     */
    private void postGetThumbUpList() {
        if (!postAble) {
            return;
        }
        postAble = false;
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_THUMB_UP_LIST;
        if (type == AppEnum.dynamicType.PARTY) {
            POST_URL = URLConfig.URL_GET_THUMBUPS;
        }
        RequestParams params;
        if (type == AppEnum.dynamicType.WORKOUT) {
            params = RequestParamsBuild.buildGetThumbUpListRequest(mContext, mUserId, mWorkoutName);
        } else {
            params = RequestParamsBuild.buildGetThumbListRequest(mContext, targetid, type);
        }
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                Message msg = new Message();
                msg.what = MSG_UPDATE_ERROR;
                if (updateViewHandler != null) {
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDataList = JSON.parseArray(Response, GetThumbsResult.class);
                mAdapter = new ThumbUpListAdapter(mContext, mDataList);
                listView.setAdapter(mAdapter);
                Message msg = new Message();
                msg.what = MSG_UPDATE_VIEW;
                if (updateViewHandler != null) {
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
                Message msg = new Message();
                msg.what = MSG_UPDATE_ERROR;
                if (updateViewHandler != null) {
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 取消事件
     */
    private void cancelHandler() {
        if (updateViewHandler != null) {
            updateViewHandler.removeMessages(MSG_UPDATE_ERROR);
            updateViewHandler.removeMessages(MSG_UPDATE_VIEW);
        }
    }

}
