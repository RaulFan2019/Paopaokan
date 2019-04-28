package com.app.pao.activity.group;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.TagManageRvAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.entity.network.GetTagManagerListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.FullyGridLayoutManager;
import com.app.pao.ui.widget.LoadingView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2016/1/7.
 * 跑团团标签管理界面
 */
@ContentView(R.layout.activity_group_tag_manage)
public class GroupTagManagerActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "GroupTagManagerActivity";

    private static final int MSG_UPDATE_VIEW = 0;//更新页面
    private static final int MSG_UPDATE_ERROR = 1;//更新失败


    /* local view */
    @ViewInject(R.id.rv_tag_manage)
    private RecyclerView mTagRv;//列表
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mRefeshLayout;//刷新页面
    @ViewInject(R.id.tv_title_create_tag)
    private TextView mCreateTagTv;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;
    @ViewInject(R.id.ll_tag_none)
    private LinearLayout mNoneLl;//没有跑团时,显示页面

    /* local data */
    private boolean isFirstIn = true;
    private boolean mPostAble = true;//是否可以POST
    private GetGroupInfoResult mGroupInfo;//跑团信息

    private TagManageRvAdapter mTagAdapter;
    private List<GetTagManagerListResult> mDatas;

    /**
     * 更新页面Handler
     */
    Handler updateViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VIEW) {
                updateViewHandler.removeCallbacks(mPostRunnable);
                mTagAdapter = new TagManageRvAdapter(mContext,mDatas);
                mTagRv.setAdapter(mTagAdapter);
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

    /**
     * 加载对话框
     */
    Runnable mPostRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
        }
    };


    @Override
    @OnClick({R.id.tv_title_create_tag,R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                    finish();
                break;
            case R.id.tv_title_create_tag:
                Bundle b = new Bundle();
                b.putInt("groupId",mGroupInfo.getRungroup().getId());
                startActivity(GroupCreateTagActivity.class,b);
                break;
        }
    }

    @Override
    protected void initData() {
        mGroupInfo = (GetGroupInfoResult) getIntent().getExtras().getSerializable("group");
    }

    @Override
    protected void initViews() {
        mTagRv.setLayoutManager(new FullyGridLayoutManager(this,2));

  //      mTagRv.addItemDecoration(new LinearItemDecoration(this,LinearItemDecoration.VERTICAL_LIST));

        if(mGroupInfo.getRungroup().getRole() ==  AppEnum.groupRole.OWNER || mGroupInfo.getRungroup().getRole() ==  AppEnum.groupRole.MANAGER){
            mCreateTagTv.setVisibility(View.VISIBLE);
        }else {
            mCreateTagTv.setVisibility(View.GONE);
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
        getGroupTagList();
    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler(){
        if(updateViewHandler != null){
            updateViewHandler.removeMessages(MSG_UPDATE_ERROR);
            updateViewHandler.removeMessages(MSG_UPDATE_VIEW);
            updateViewHandler.removeCallbacks(mPostRunnable);
        }
    }

    /**
     * 获取跑团的标签数据
     */
    private void getGroupTagList(){
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUN_GROUP_LABEl_LIST;
        RequestParams params = RequestParamsBuild.buildGetRunGroupLabelList(mContext,mGroupInfo.getRungroup().getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if(updateViewHandler != null){
                    Message msg = new Message();
                    msg.what = MSG_UPDATE_ERROR;
                    msg.obj = errorMsg;
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDatas = JSON.parseArray(Response,GetTagManagerListResult.class);

                if(updateViewHandler != null){
                    Message msg = new Message();
                    msg.what = MSG_UPDATE_VIEW;
                    msg.obj = Response;
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if(updateViewHandler != null){
                    Message msg = new Message();
                    msg.what = MSG_UPDATE_ERROR;
                    msg.obj = s;
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }
}
