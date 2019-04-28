package com.app.pao.fragment.friend;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.adapter.FriendRankingLengthAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetFriendRankResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by LY on 2016/3/16.
 */
public class FriendRankingWeekFragment extends BaseFragment implements View.OnClickListener {

    /* contains */
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    /* local view */
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadLl;
    @ViewInject(R.id.lv_load)
    private LoadingView mLoadLv;
    @ViewInject(R.id.tv_reload)
    private TextView mReLoadTv;
    @ViewInject(R.id.rv_friend_ranking_name_list)
    private RecyclerView mFriendRankNameRv;
    //    @ViewInject(R.id.tv_user_rank_time)
//    private TextView mRankTime;
//    @ViewInject(R.id.tv_user_rank_totle_people)
//    private TextView mRankTotal;
//    @ViewInject(R.id.tv_user_rank)
//    private TextView mRank;
    @ViewInject(R.id.refresh_layout_friend_list)
    private PullToRefreshLayout mRefeshLayout;//刷新VIew
    private boolean mPostAble;//是否可以发送请求
    private FriendRankingLengthAdapter mAdapter;

    private boolean isFirstIn;
    private int type;
    private FriendRankingFragment mParentFragment;

    private List<GetFriendRankResult> mFriendNameList = new ArrayList<>();

    private Handler mPsotHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_POST_ERROR:
                    mPostAble = true;
                    T.showShort(mContext, msg.obj.toString());
                    mPsotHandler.removeCallbacks(mPsotRunnable);
                    if (mLoadLl.getVisibility() == View.VISIBLE) {
                        mLoadLv.setLoadingText("加载失败");
                        mReLoadTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_POST_OK:
                    mPostAble = true;
                    updateViewByPostResult();
                    mPsotHandler.removeCallbacks(mPsotRunnable);
                    mLoadLl.setVisibility(View.GONE);
                    break;
            }

            if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                mDialogBuilder.progressDialog.dismiss();
            }
        }
    };

    Runnable mPsotRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
        }
    };

    @Override
    @OnClick({R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                getFriendSortNameRequest();
                mLoadLv.setLoadingText("加载中...");
                mReLoadTv.setVisibility(View.GONE);
                break;
        }
    }

    public static FriendRankingWeekFragment newInstance(int t) {
        FriendRankingWeekFragment nameFragment = new FriendRankingWeekFragment();
        nameFragment.type = t;
        return nameFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_ranking_length;
    }

    @Override
    protected void initParams() {
        initDate();
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPostAble = true;
        if (isFirstIn) {
            isFirstIn = false;
            getFriendSortNameRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelHandle();
    }

    private void initDate() {
        mFriendNameList = new ArrayList<>();
        isFirstIn = true;
        mPostAble = true;
        mParentFragment = (FriendRankingFragment) getParentFragment();
    }

    private void initViews() {
        //注册下方的上拉刷新条
        RelativeLayout loadView = (RelativeLayout) mRefeshLayout.findViewById(R.id.loadmore_view);
        //隐藏这个刷新条
        loadView.setVisibility(View.GONE);
        //初始化列表
        mAdapter = new FriendRankingLengthAdapter(mFriendNameList, mContext);
        mFriendRankNameRv.setLayoutManager(new LinearLayoutManager(mContext));
        mFriendRankNameRv.setAdapter(mAdapter);
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
                    getFriendSortNameRequest();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    private void getFriendSortNameRequest() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RANKING_FRIEND_LIST;
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
//        int type = AppEnum.friendRankType.WEEK;
        RequestParams params = RequestParamsBuild.buildGetFriendRankRequest(mContext, userId, type);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPsotHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPsotHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (mFriendNameList != null) {
                    mFriendNameList.clear();
                }
                mFriendNameList.addAll(JSON.parseArray(Response, GetFriendRankResult.class));
                if (mPsotHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    mPsotHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPsotHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    mPsotHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * POST返回完成之后进行的操作
     */
    private void updateViewByPostResult() {
        mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        if (mFriendNameList.size() > 0) {

            mParentFragment.mRankTotal.setText(mFriendNameList.size() + "");
            int rank = -1;
            for (int i = 0; i < mFriendNameList.size(); i++) {
                if (mFriendNameList.get(i).getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                    rank = i;
                }
            }
            if (rank != -1) {
                mParentFragment.mRankTv.setText(rank + 1 + "");
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    private void cancelHandle() {
        if (mPsotHandler != null) {
            mPsotHandler.removeMessages(MSG_POST_ERROR);
            mPsotHandler.removeMessages(MSG_POST_OK);
        }
    }
}
