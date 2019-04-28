package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupApplyMemberListAdapter;
import com.app.pao.adapter.SimpleBaseAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.network.GetGroupApplyMemberListResult;
import com.app.pao.entity.network.GetGroupInfoResult;
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
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;
import hwh.com.pulltorefreshlibrary.PullableListView;

/**
 * Created by Raul on 2015/12/8.
 * 跑团申请入团人员列表
 */
@ContentView(R.layout.activity_group_apply_member_list)
public class GroupApplyMemberListActivity extends BaseAppCompActivity implements View.OnClickListener {


    /* contains */
    private static final String TAG = "GroupApplyMemberListActivity";

    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_POST_OK = 3;//请求成功


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mRefeshLayout;//刷新VIew
    @ViewInject(R.id.listview)
    private PullableListView mListView;//列表
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    private GroupApplyMemberListAdapter mAdapter;

    /* local data */
    private boolean mPostAble;//是否可以发送请求
    private int groupId;
    private boolean isFirstIn = true;

    private List<GetGroupApplyMemberListResult.ApplyEntity> mDatas = new ArrayList<GetGroupApplyMemberListResult
            .ApplyEntity>();

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
                    mPostHandler.removeCallbacks(mPostRunnable);
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostAble = true;
                    updateViewByPostResult();
                    mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    mPostHandler.removeCallbacks(mPostRunnable);
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                if (mPostAble) {
                    mPostAble = false;
                    PostGetApplyListRequst();
                }
                mReladV.setVisibility(View.GONE);
                break;
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    @Override
    protected void initData() {
        mPostAble = true;
        groupId = getIntent().getExtras().getInt("groupid");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
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
        // 第一次进入自动刷新
        if (isFirstIn) {
            if (mPostAble) {
                mPostAble = false;
                PostGetApplyListRequst();
            }
            isFirstIn = false;
        } else {
            mPostHandler.postDelayed(mPostRunnable, 500);
            if (mPostAble) {
                mPostAble = false;
                PostGetApplyListRequst();
            }
        }
    }

    @Override
    protected void destroy() {
        cancelHandler();
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
                    PostGetApplyListRequst();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    /**
     * 同意入团
     */
    private void AgreeApply(final GetGroupApplyMemberListResult.ApplyEntity entity, final int pos) {
        mDialogBuilder.showProgressDialog(mContext, "发送同意请求..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_AGREE_GROUP_APPLY;
        int id = entity.getUserid();
        RequestParams params = RequestParamsBuild.buildGetAgreeGroupApplyRequest(mContext,groupId, id);
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
                entity.setStatus(AppEnum.groupApplyMemberStatus.AGREE);
                mDatas.set(pos, entity);
                mAdapter = new GroupApplyMemberListAdapter(mContext, mDatas);
                mAdapter.setOnItemBtnClickListener(new SimpleBaseAdapter.OnItemBtnClickListener() {
                    @Override
                    public void onItemBtnClick(int pos) {
                        AgreeApply(mDatas.get(pos), pos);
                    }
                });
                int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
                MessageData.readAllNewGroupMessage(mContext, userId);
                mListView.setAdapter(mAdapter);
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
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 获取申请入团列表
     */
    private void PostGetApplyListRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_GROUP_APPLY_MEMBER_LIST;
        int fromid = 0;
        RequestParams params = RequestParamsBuild.buildGetGroupApplyMemberListRequest(mContext,groupId, fromid);
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
                GetGroupApplyMemberListResult result = JSON.parseObject(Response, GetGroupApplyMemberListResult.class);
                mDatas = result.getApply();
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
            mPostHandler.removeCallbacks(mPostRunnable);
        }
    }

    /**
     * 更新页面
     */
    private void updateViewByPostResult() {
        mAdapter = new GroupApplyMemberListAdapter(mContext, mDatas);
        mAdapter.setOnItemBtnClickListener(new SimpleBaseAdapter.OnItemBtnClickListener() {
            @Override
            public void onItemBtnClick(int pos) {
                AgreeApply(mDatas.get(pos), pos);
            }
        });
        mListView.setAdapter(mAdapter);
    }
}
