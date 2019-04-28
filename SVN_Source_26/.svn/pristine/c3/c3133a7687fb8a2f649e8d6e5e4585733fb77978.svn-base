package com.app.pao.activity.group;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.party.CreateGroupPartyActivity;
import com.app.pao.adapter.GroupPartyListAdapterReplace;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2015/12/7.
 * 跑团活动列表界面
 */
@ContentView(R.layout.activity_group_party_list)
public class GroupPartyListActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "GroupPartyListActivity";

    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_POST_OK = 3;//请求成功

    /* local view */
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mRefeshLayout;//刷新VIew
    @ViewInject(R.id.btn_create_party)
    private Button mCreatePartyBtn;//发布活动按钮
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;
    @ViewInject(R.id.ll_none)
    private LinearLayout mNoneLl;
    @ViewInject(R.id.rv_group_party_list)
    private RecyclerView mRv;

    /* local data */
    private boolean mPostAble;//是否可以发送请求
    private boolean isFirstIn = true;
    private GetGroupDetailInfoRequest mGroupInfo;//跑团详情

    private GroupPartyListAdapterReplace mAdapter;//适配器
    private List<GetDynamicListResult.Party> mDatas = new ArrayList<GetDynamicListResult.Party>();

    private String mRefreshDate;//更新时间


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

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.btn_create_party, R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回键
            case R.id.title_bar_left_menu:
                finish();
                break;
            //跳转发布活动
            case R.id.btn_create_party:
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", mGroupInfo);
                startActivity(CreateGroupPartyActivity.class, bundle);
                break;
            case R.id.tv_reload:
                if (mPostAble) {
                    mPostAble = false;
                    PostGetPartyListRequst(true);
                    mReladV.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;
        mGroupInfo = (GetGroupDetailInfoRequest) getIntent().getExtras().getSerializable("group");
    }

    @Override
    protected void initViews() {
        if (mGroupInfo == null) {
            return;
        }
        if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.MANAGER || mGroupInfo.getRungroup().getRole() ==
                AppEnum.groupRole.OWNER) {
            mCreatePartyBtn.setVisibility(View.VISIBLE);
        } else {
            mCreatePartyBtn.setVisibility(View.INVISIBLE);
        }
        mRv.setLayoutManager(new FullyLinearLayoutManager(mContext));
        mAdapter = new GroupPartyListAdapterReplace(mContext, mDatas);
        mRv.setAdapter(mAdapter);

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
        if (isFirstIn) {
            if (mPostAble) {
                mPostAble = false;
                PostGetPartyListRequst(true);
            }
            isFirstIn = false;
        } else {
            mPostHandler.postDelayed(mPostRunnable, 500);
            if (mPostAble) {
                mPostAble = false;
                PostGetPartyListRequst(true);
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
                    PostGetPartyListRequst(true);
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }


    /**
     * 获取活动列表
     */
    private void PostGetPartyListRequst(boolean isFresh) {
        if (isFresh) {
            mRefreshDate = "";
//            TimeUtils.NowTime();
            mDatas.clear();
        }

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_ALL_PARTY;
        int groupid = mGroupInfo.getRungroup().getId();
        RequestParams params = RequestParamsBuild.buildGetGroupPartyListRequest(mContext,groupid,
                LocalApplication.getInstance().getLoginUser(mContext).userId,mRefreshDate);
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
                mDatas.addAll(JSON.parseArray(Response, GetDynamicListResult.Party.class));
                if (mDatas.size() > 0) {
                    mRefreshDate = mDatas.get(mDatas.size() - 1).getStarttime();
                }
                if (mPostHandler != null) {
                    mPostHandler.removeCallbacks(mPostRunnable);
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
        if (mDatas.size() > 0) {
            mNoneLl.setVisibility(View.INVISIBLE);
        } else {
            mNoneLl.setVisibility(View.VISIBLE);
        }
        mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mAdapter.notifyDataSetChanged();
    }
}
