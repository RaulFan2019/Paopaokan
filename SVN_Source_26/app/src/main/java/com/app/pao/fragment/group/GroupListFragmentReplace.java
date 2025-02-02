package com.app.pao.fragment.group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.group.CreateGroupTipActivity;
import com.app.pao.activity.group.SearchGroupActivity;
import com.app.pao.activity.main.ScanQRCodeActivityReplace;
import com.app.pao.adapter.GroupListAdapterReplace;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.event.EventJpush;
import com.app.pao.entity.network.GetGroupListResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;
import hwh.com.pulltorefreshlibrary.PullableRecylerView;

/**
 * Created by Administrator on 2016/1/22.
 * <p/>
 * 跑团列表
 */
public class GroupListFragmentReplace extends BaseFragment implements View.OnClickListener{

    /* contains */
    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;

    /* local view */
    @ViewInject(R.id.refresh_layout_group_list)
    private PullToRefreshLayout mRefreshLayout;
    @ViewInject(R.id.rv_group_list)
    private PullableRecylerView mGroupRv;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadLl;
    @ViewInject(R.id.lv_load)
    private LoadingView mLoadLv;
    @ViewInject(R.id.tv_reload)
    private android.widget.TextView mReLoadTv;
    @ViewInject(R.id.ll_none)
    public LinearLayout mNoneLl;
    @ViewInject(R.id.ll_base)
    private LinearLayout mBaseFriendListLl;


    /* local data */
    private List<GetGroupListResult> mGroupDataList = new ArrayList<>();
    private GroupListAdapterReplace mGroupAdapter;

    private boolean mPostAble;//是否可以发送请求
    private boolean isFirstIn = true;

    private List<Integer> mHasApplyGroupIds = new ArrayList<>();//有消息的Group列表
    private BitmapUtils mBitmapU;
    private int userid;

    public static GroupListFragmentReplace newInstance() {
        GroupListFragmentReplace fragment = new GroupListFragmentReplace();
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
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }

                    if (mLoadLl.getVisibility() == View.VISIBLE) {
                        mLoadLv.setLoadingText("加载失败");
                        mReLoadTv.setVisibility(View.VISIBLE);
                    }
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostHandler.removeCallbacks(mPostRunnable);
                    mPostAble = true;
                    updateViewByPostResult();
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }

                    mLoadLv.setVisibility(View.GONE);
                    mLoadLl.setVisibility(View.GONE);
//                    mGroupListFragment.updateViewByPostResult();//如果网络返回成功，则对跑团列表进行操作
//                    if(mGroupDataList.size()==0){
//                        mNoneLl.setVisibility(View.VISIBLE);
////                        mBaseFriendListLl.setVisibility(View.GONE);
//                    }else{
//                        mNoneLl.setVisibility(View.GONE);
//
//                    }
                    break;
            }
        }
    };

    public void setGroupDataList(List<GetGroupListResult> mGroupDataList) {
        this.mGroupDataList = mGroupDataList;
    }

    Runnable mPostRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_list_replace;
    }

    @Override
    protected void initParams() {
        initData();
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        updateApplyGroup();
        mPostAble = true;
        if (isFirstIn) {
            isFirstIn = false;
            postGetGroupRequest();

        }
    }

    /**
     * 接收到Jpush改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventJpush event) {
        int type = event.getMsgType();
        if (type == AppEnum.messageType.APPLY_JOIN_RUNGROUP) {
            updateApplyGroup();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userid = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mBitmapU = new BitmapUtils(mContext);
    }

    private void updateApplyGroup() {
        int userId = LocalApplication.getInstance().getLoginUser(mContext).userId;
        mHasApplyGroupIds.clear();
        mHasApplyGroupIds.addAll(MessageData.getHasApplyGroupIdList(mContext, userId));

        mGroupAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        //初始化列表
        mGroupAdapter = new GroupListAdapterReplace(mContext, mGroupDataList, mHasApplyGroupIds);
        mGroupRv.setAdapter(mGroupAdapter);
        mGroupRv.setLayoutManager(new LinearLayoutManager(mContext));

        RelativeLayout loadView = (RelativeLayout) mRefreshLayout.findViewById(R.id.loadmore_view);
        loadView.setVisibility(View.GONE);

        mRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                postGetGroupRequest();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                mRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    /**
     * 更新页面
     */
    public void updateViewByPostResult() {
        if (mGroupDataList.size() > 0) {
            mNoneLl.setVisibility(View.INVISIBLE);
        } else {
            mNoneLl.setVisibility(View.VISIBLE);
        }
        mRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mGroupAdapter.notifyDataSetChanged();

    }


    /**
     * 获取跑团列表请求
     */
    private void postGetGroupRequest() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_GROUP_LIST;
        RequestParams params = RequestParamsBuild.buildGetGroupListRequest(mContext, LocalApplication.getInstance().getLoginUser(mContext).getUserId());
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
                mGroupDataList.clear();
                mGroupDataList.addAll(JSON.parseArray(Response, GetGroupListResult.class));
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
     * 检查是否自己有跑团
     */
    public String checkHasGroup() {
        String groupName = null;
        for (int i = 0; i < mGroupDataList.size(); i++) {
            GetGroupListResult groupListResult = mGroupDataList.get(i);
            if (groupListResult.getRole() == AppEnum.groupRole.OWNER) {
                groupName = groupListResult.getName();
                break;
            }
        }
        return groupName;
    }


    @Override
    @OnClick({ R.id.btn_add_group, R.id.ll_creat_group,R.id.ll_search_group,R.id.ll_search_group_sys,R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()){
            //创建跑团
            case R.id.ll_creat_group:
                Bundle bundle = new Bundle();
                bundle.putBoolean("hasgroup", false);
                bundle.putString("groupname", null);
                startActivity(CreateGroupTipActivity.class, bundle);
                break;
            //搜索跑团
            case R.id.ll_search_group:
                startActivity(SearchGroupActivity.class);
                break;
            //扫一扫加跑团
            case R.id.ll_search_group_sys:
                Bundle bundl = new Bundle();
                bundl.putInt("hasScanSys", 0);
                startActivity(ScanQRCodeActivityReplace.class, bundl);
                break;
            case R.id.tv_reload:
                postGetGroupRequest();
                mLoadLv.setLoadingText("加载中...");
                mReLoadTv.setVisibility(View.GONE);
                break;
        }
    }
}
