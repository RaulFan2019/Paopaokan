package com.app.pao.fragment.group;

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
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.activity.group.MyPartyListActivity;
import com.app.pao.activity.group.SearchGroupActivity;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.adapter.GroupListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.network.GetGroupListResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.ListViewForScrollView;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2015/11/16.
 * 跑团列表页面
 */
public class GroupListFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    /* contains */
    private static final String TAG = "GroupListFragment";

    private static final int MSG_UPDATE_VIEW = 0;//更新页面
    private static final int MSG_UPDATE_ERROR = 1;//更新失败

    /* local view */
    @ViewInject(R.id.listview_group)
    private ListViewForScrollView listView;//列表
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mRefeshLayout;//刷新页面

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;
    @ViewInject(R.id.ll_none)
    private LinearLayout mNoneLl;//没有跑团时,显示页面


    /* local data */
    private boolean isFirstIn = true;
    private boolean mPostAble = true;//是否可以POST
    private int mGroupMsgCount;//跑团相关未读消息数量

    private GroupListAdapter mAdapter;

    private List<GetGroupListResult> mDatas = new ArrayList<GetGroupListResult>();

    /**
     * 更新页面Handler
     */
    Handler updateViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VIEW) {
                updateViewHandler.removeCallbacks(mPostRunnable);
                mAdapter = new GroupListAdapter(mContext, mDatas, mGroupMsgCount);
                listView.setAdapter(mAdapter);
                mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                mPostAble = true;
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
                if (mDatas.size() == 0) {
                    mNoneLl.setVisibility(View.VISIBLE);
                } else {
                    mNoneLl.setVisibility(View.INVISIBLE);
                }
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            } else if (msg.what == MSG_UPDATE_ERROR) {
                mPostAble = true;
                updateViewHandler.removeCallbacks(mPostRunnable);
                mRefeshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                mLoadV.setLoadingText("加载失败");
                mReladV.setVisibility(View.VISIBLE);
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
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
     * 列表item点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    @OnItemClick(R.id.listview_group)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GetGroupListResult entity = mDatas.get(position);
        if (entity.getStatus() == AppEnum.groupStatus.DISMISS) {
            T.showShort(mContext, "该跑团已申请解散..");
        } else if (entity.getStatus() == AppEnum.groupStatus.WAITING && entity.getRole() == AppEnum.groupRole.OWNER) {
            T.showShort(mContext, "该跑团正在审核中..");
        } else {
            LaunchGroupInfo(entity);
        }
    }

    @Override
    @OnClick({R.id.btn_add_group, R.id.frame_party, R.id.tv_reload, R.id.btn_goto_search_group})
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_group) {
            String groupName = "";
            //检查自己是否有跑团
            boolean hasGroup = false;
            for (int i = 0; i < mDatas.size(); i++) {
                GetGroupListResult groupListResult = mDatas.get(i);
                if (groupListResult.getRole() == AppEnum.groupRole.OWNER) {
                    groupName = groupListResult.getName();
                    hasGroup = true;
                    break;
                }
            }
            MainActivityV2 activity = (MainActivityV2) getActivity();
            activity.showGroupTitleMenuList(hasGroup, groupName);
        } else if (v.getId() == R.id.frame_party) {
            startActivity(MyPartyListActivity.class);
        } else if (v.getId() == R.id.tv_reload) {
            mLoadV.setLoadingText("加载中..");
            mReladV.setVisibility(View.INVISIBLE);
            PostGetGroupRequst();
        } else if (v.getId() == R.id.btn_goto_search_group) {
            startActivity(SearchGroupActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_list;
    }

    @Override
    protected void initParams() {
        initData();
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mGroupMsgCount = MessageData.getNewGroupMsgCount(mContext, userId);
        if (isFirstIn) {
            PostGetGroupRequst();
            isFirstIn = false;
        } else {
            updateViewHandler.postDelayed(mPostRunnable, 500);
            PostGetGroupRequst();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelHandler();
    }

    private void initData() {
        mPostAble = true;
    }

    private void initViews() {
        RelativeLayout loadView = (RelativeLayout) mRefeshLayout.findViewById(R.id.loadmore_view);
        loadView.setVisibility(View.GONE);
        initRefreshListener();
        listView.setFocusable(false);
    }


    /**
     * 刷新事件监听
     */
    private void initRefreshListener() {
        mRefeshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                if (mPostAble) {
                    PostGetGroupRequst();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }


    /**
     * 获取跑团列表请求
     */
    private void PostGetGroupRequst() {
        mPostAble = false;
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_GROUP_LIST;
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        RequestParams params = RequestParamsBuild.buildGetGroupListRequest(mContext, userId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (updateViewHandler != null) {
                    updateViewHandler.sendEmptyMessage(MSG_UPDATE_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDatas = JSON.parseArray(Response, GetGroupListResult.class);
                if (updateViewHandler != null) {
                    updateViewHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (updateViewHandler != null) {
                    updateViewHandler.sendEmptyMessage(MSG_UPDATE_ERROR);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    private void cancelHandler() {
        if (updateViewHandler != null) {
            updateViewHandler.removeMessages(MSG_UPDATE_ERROR);
            updateViewHandler.removeMessages(MSG_UPDATE_VIEW);
        }
    }

    /**
     * 启动团信息页面
     */
    private void LaunchGroupInfo(final GetGroupListResult entity) {
        Bundle bundle = new Bundle();
        bundle.putInt("groupid", entity.getId());
        startActivity(GroupInfoActivity.class, bundle);
    }


}
