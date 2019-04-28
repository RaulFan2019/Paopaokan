package com.app.pao.fragment.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.group.GroupMemberRankingActivity;
import com.app.pao.adapter.GroupMemberRankingMonthAdapter;
import com.app.pao.adapter.GroupMemberRankingWeekAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupMemberSortResult;
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

/**
 * Created by Administrator on 2016/1/28.
 */
public class GroupMemberRankingMonthFragment extends BaseFragment implements View.OnClickListener{

    private static final String VALUE_KEY = "groupId";
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;
    @ViewInject(R.id.rv_member_ranking_month_list)
    private RecyclerView mMonthRankingRv;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private com.rey.material.widget.TextView mReladV;

    private GroupMemberRankingMonthAdapter mMonthAdapter;
    private List<GetGroupMemberSortResult> mMonthList;

    private int mGroupId;
    private GetGroupDetailInfoRequest mGroupInfo;
    private GroupMemberRankingFragment mParentFragment;

    private Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
                if(mLoadViewRl.getVisibility() == View.VISIBLE) {
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                }
            } else if (msg.what == MSG_POST_OK) {
                refreshView();
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
            }
        }
    };

    @Override
    @OnClick({R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_reload:
                postGetSortWeekRequest();
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                break;
        }
    }

    public static GroupMemberRankingMonthFragment newInstance() {
        GroupMemberRankingMonthFragment nameFragment = new GroupMemberRankingMonthFragment();
        return nameFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_ranking_month;
    }

    @Override
    protected void initParams() {
        mMonthList = new ArrayList<>();
        GroupMemberRankingActivity mActivity = (GroupMemberRankingActivity) getActivity();
        mGroupId = mActivity.getmGroupId();
        mGroupInfo = mActivity.getmGroupInfo();
        mParentFragment = (GroupMemberRankingFragment) getParentFragment();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        postGetSortWeekRequest();
    }


    @Override
    public void onDestroy() {
        cancelHandler();
        super.onDestroy();
    }

    private void cancelHandler(){
        if(mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
    }

    private void initView() {
//        mMonthRankingRv.setLayoutManager(new FullyLinearLayoutManager(mContext));
        mMonthRankingRv.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void refreshView() {
//        if (mMonthAdapter == null) {
            mMonthAdapter = new GroupMemberRankingMonthAdapter(mContext,mGroupInfo, mMonthList);
            mMonthRankingRv.setAdapter(mMonthAdapter);
//        } else {
//            mMonthAdapter.notifyDataSetChanged();
//        }

        //显示排位信息
        int mRanking = 0;
        for(int i = 0;i< mMonthList.size();i++){
            if(mMonthList.get(i).getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()){
                mRanking = i+1;
                break;
            }
        }
        mParentFragment.mRankTotal.setText(mMonthList.size()+"");
        mParentFragment.mRank.setText(mRanking+"");
//        Spanned showText = Html.fromHtml("<font color=\"#888888\">您本月在" + mMonthList.size() + "名成员中排名第</font><font color=\"#F06522\" ><big> " + mRanking + " </big></font>名");
//        mMonthRankingTextTv.setText(showText);
    }

    /**
     * 获取姓名排行榜
     */
    private void postGetSortWeekRequest() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNGROUP_RANK_LIST;
        int type = AppEnum.groupRankType.MONTH;
        RequestParams params = RequestParamsBuild.buildGetGroupRankRequest(mContext,mGroupId, type);
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
                if (mMonthList != null) {
                    mMonthList.clear();
                }
                mMonthList.addAll(JSON.parseArray(Response, GetGroupMemberSortResult.class));
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    mPostHandler.sendMessage(msg);
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

}
