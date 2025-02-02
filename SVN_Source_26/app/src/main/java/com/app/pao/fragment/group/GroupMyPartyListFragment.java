package com.app.pao.fragment.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.adapter.GroupMyPartyListAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Administrator on 2016/1/22.
 */
public class GroupMyPartyListFragment extends BaseFragment implements View.OnClickListener {

    /* contains */
    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;

    /* local view */
    @ViewInject(R.id.refresh_layout_group_party_list)
    private PullToRefreshLayout mRefreshLayout;
    @ViewInject(R.id.rv_group_party_list)
    private RecyclerView mPartyListRv;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;
    @ViewInject(R.id.ll_none)
    private LinearLayout mNoneLl;

    /* local data */
    private GroupMyPartyListAdapter mPartyAdapter;
    private List<GetDynamicListResult.Party> mPartyDataList;
    private GroupFragment mParentFragment;

    private boolean mPostAble;//是否可以发送请求
    private boolean isFirstIn = true;
    private boolean loadMore = true;

    private String mRefreshDate;//更新时间

    public static GroupMyPartyListFragment newInstance() {
        GroupMyPartyListFragment fragment = new GroupMyPartyListFragment();
        return fragment;
    }

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
                    mPostHandler.removeCallbacks(mPostRunnable);
                    mPostAble = true;
                    mRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostHandler.removeCallbacks(mPostRunnable);
                    mPostAble = true;
                    updateViewByPostResult();
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
    protected int getLayoutId() {
        return R.layout.fragment_group_my_party_list;
    }

    @Override
    protected void initParams() {
        initData();
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPostAble = true;
        if (isFirstIn) {
            postGetPartyListRequest(true);
            isFirstIn = false;
        } else {
//            postGetPartyListRequest(true);
//            if (mParentFragment.getmCurrentFragment() instanceof GroupMyPartyListFragment) {
//                mPostHandler.postDelayed(mPostRunnable, 0);
//            }
        }
    }

    @Override
    @OnClick({R.id.tv_reload})
    public void onClick(View v) {
        if (v.getId() == R.id.tv_reload) {
            mLoadV.setLoadingText("加载中..");
            mReladV.setVisibility(View.INVISIBLE);
            postGetPartyListRequest(true);
        }
    }

    private void initData() {
        mPartyDataList = new ArrayList<GetDynamicListResult.Party>();
        mParentFragment = (GroupFragment) getParentFragment();
    }


    private void initViews() {
        mPartyListRv.setLayoutManager(new FullyLinearLayoutManager(mContext));
        mPartyAdapter = new GroupMyPartyListAdapter(mContext, mPartyDataList);
        mPartyListRv.setAdapter(mPartyAdapter);

//        RelativeLayout loadView = (RelativeLayout) mRefreshLayout.findViewById(R.id.loadmore_view);
//        loadView.setVisibility(View.GONE);


        mRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                postGetPartyListRequest(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (loadMore) {
                    postGetPartyListRequest(false);
                } else {
                    mRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
    }

    /**
     * 获取活动列表
     */
    private void postGetPartyListRequest(boolean isFresh) {
        if (isFresh) {
            loadMore = true;
            mRefreshDate = "";
            mPartyDataList.clear();
        }
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_ALL_PARTY;
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        RequestParams params = RequestParamsBuild.buildGetAllPartyListRequest(mContext, userId, mRefreshDate);
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
                mPartyDataList.addAll(JSON.parseArray(Response, GetDynamicListResult.Party.class));
                if (mPartyDataList.size() > 0) {
                    mRefreshDate = mPartyDataList.get(mPartyDataList.size() - 1).getStarttime();
                }
                if (JSON.parseArray(Response, GetDynamicListResult.Party.class).size() < 20) {
                    loadMore = false;
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
     * 更新页面
     */
    private void updateViewByPostResult() {
        if (mPartyDataList.size() > 0) {
            mNoneLl.setVisibility(View.INVISIBLE);
        } else {
            mNoneLl.setVisibility(View.VISIBLE);
        }
        mRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPartyAdapter.notifyDataSetChanged();
    }
}
