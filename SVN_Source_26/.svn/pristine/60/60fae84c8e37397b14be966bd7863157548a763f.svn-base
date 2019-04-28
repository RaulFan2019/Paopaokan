package com.app.pao.activity.group;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupMemberSortListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.entity.network.GetGroupMemberSortResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.ListViewForScrollView;
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
 * Created by Raul on 2015/12/7.
 * 跑团成员排行界面
 */
@ContentView(R.layout.activity_group_member_sort_list)
public class GroupMemberSortListActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* conains */
    private static final String TAG = "GroupMemberSortListActivity";

    private static final int TAB_NAME = 4;
    private static final int TAB_WEEK = 1;
    private static final int TAB_MONTH = 2;
    private static final int TAB_TOTAL = 3;

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.refresh_layout)
    private PullToRefreshLayout mRefeshLayout;//刷新VIew
    @ViewInject(R.id.sort_list)
    private ListViewForScrollView mListView;//列表
    @ViewInject(R.id.tv_my_sort)
    private TextView mMySortTv;//自己的排行位置
    @ViewInject(R.id.ll_my_sort)
    private LinearLayout mMySortLl;//自己排行的布局
    //TAB
    @ViewInject(R.id.ll_tab_week)
    private LinearLayout mWeekLl;//本周布局
    @ViewInject(R.id.ll_tab_month)
    private LinearLayout mMonthLl;//本月布局
    @ViewInject(R.id.ll_tab_total)
    private LinearLayout mTotalLl;//总布局
    @ViewInject(R.id.ll_tab_name)
    private LinearLayout mNameLl;//姓名布局
    @ViewInject(R.id.tv_week)
    private TextView mWeekTv;//本周文本
    @ViewInject(R.id.tv_month)
    private TextView mMonthTv;//本月文本
    @ViewInject(R.id.tv_total)
    private TextView mTotalTv;//总计文本
    @ViewInject(R.id.tv_name)
    private TextView mNameTv;//按姓名文本

    @ViewInject(R.id.ll_tab)
    private LinearLayout mTabLl;//TAB 显示栏位
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;


    /* local data */
    private boolean mPostAble;//是否可以发送请求
    private GetGroupDetailInfoRequest mGroupInfo;//跑团详情

    private GroupMemberSortListAdapter mAdapter;

    //排行榜列表
    private List<GetGroupMemberSortResult> mNameList = new ArrayList<GetGroupMemberSortResult>();
    private List<GetGroupMemberSortResult> mWeekList = new ArrayList<GetGroupMemberSortResult>();
    private List<GetGroupMemberSortResult> mMonthList = new ArrayList<GetGroupMemberSortResult>();
    private List<GetGroupMemberSortResult> mTotalList = new ArrayList<GetGroupMemberSortResult>();
//    private List<>
    //排行榜自己的位置
    private int mNameRank;//姓名排序中自己的位置
    private int mWeekRank;//自己在周的位置
    private int mMonthRank;//自己在月的位置
    private int mTotalRank;//自己的总排行的位置
    private int mUserId;//用户ID

    private int mTabIndex;
    private boolean isFirstIn = true;

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
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostAble = true;
                    updateViewByPostResult();
                    mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    mMySortLl.setVisibility(View.VISIBLE);
                    mTabLl.setVisibility(View.VISIBLE);
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

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

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.ll_tab_name,R.id.ll_tab_week, R.id.ll_tab_month, R.id.ll_tab_total,R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab_name:
                resetAllLayout();
                mNameLl.setBackgroundResource(R.drawable.btn_history_tab_left_select);
                mNameTv.setTextColor(Color.parseColor("#ffffff"));
                mTabIndex = TAB_NAME;
                updateViewByPostResult();
                break;
            case R.id.ll_tab_week:
                resetAllLayout();
                mWeekLl.setBackgroundResource(R.drawable.btn_history_tab_middle_select);
                mWeekTv.setTextColor(Color.parseColor("#ffffff"));
                mTabIndex = TAB_WEEK;
                updateViewByPostResult();
                break;
            case R.id.ll_tab_month:
                resetAllLayout();
                mMonthLl.setBackgroundResource(R.drawable.btn_history_tab_middle_select);
                mMonthTv.setTextColor(Color.parseColor("#ffffff"));
                mTabIndex = TAB_MONTH;
                updateViewByPostResult();
                break;
            case R.id.ll_tab_total:
                resetAllLayout();
                mTotalLl.setBackgroundResource(R.drawable.btn_history_tab_right_select);
                mTotalTv.setTextColor(Color.parseColor("#ffffff"));
                mTabIndex = TAB_TOTAL;
                updateViewByPostResult();
                break;
            case R.id.tv_reload:
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                if (mPostAble) {
                    mPostAble = false;
                    PostGetSortNameRequst();
                }
                break;
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;
        //-1 代表在时间内未跑
        mWeekRank = -1;
        mMonthRank = -1;
        mTotalRank = -1;
        mNameRank = -1;
        mUserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mTabIndex = TAB_NAME;
        mGroupInfo = (GetGroupDetailInfoRequest) getIntent().getExtras().getSerializable("group");
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
        // 第一次进入自动刷新
        if (isFirstIn) {
            if (mPostAble) {
                mPostAble = false;
                PostGetSortNameRequst();
            }
            isFirstIn = false;
        }
    }

    @Override
    protected void updateViews() {

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
                    PostGetSortWeekRequst();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
                if (mAdapter.getItem(position).getId() == userId) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("userid", mAdapter.getItem(position).getId());
                bundle.putSerializable("group", mGroupInfo);
                startActivity(GroupMemberActivity.class, bundle);
            }
        });
    }

    /**
     * 获取姓名排行榜
     */
    private void PostGetSortNameRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNGROUP_RANK_LIST;
        int groupid = mGroupInfo.getRungroup().getId();
        int type = AppEnum.groupRankType.NAME;
        RequestParams params = RequestParamsBuild.buildGetGroupRankRequest(mContext,groupid, type);
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
                mNameList = JSON.parseArray(Response, GetGroupMemberSortResult.class);
                for (int i = 0; i < mNameList.size(); i++) {
                    if (mNameList.get(i).getId() == mUserId) {
                        mNameRank = i;
                    }
                }
                PostGetSortWeekRequst();
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
     * 获取本周排行榜
     */
    private void PostGetSortWeekRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNGROUP_RANK_LIST;
        int groupid = mGroupInfo.getRungroup().getId();
        int type = AppEnum.groupRankType.WEEK;
        RequestParams params = RequestParamsBuild.buildGetGroupRankRequest(mContext,groupid, type);
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
                mWeekList = JSON.parseArray(Response, GetGroupMemberSortResult.class);
                for (int i = 0; i < mWeekList.size(); i++) {
                    if (mWeekList.get(i).getId() == mUserId) {
                        mWeekRank = i;
                    }
                }
                PostGetSortMonthRequst();
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
     * 获取本周排行榜
     */
    private void PostGetSortMonthRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNGROUP_RANK_LIST;
        int groupid = mGroupInfo.getRungroup().getId();
        int type = AppEnum.groupRankType.MONTH;
        RequestParams params = RequestParamsBuild.buildGetGroupRankRequest(mContext,groupid, type);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mMonthList = JSON.parseArray(Response, GetGroupMemberSortResult.class);
                for (int i = 0; i < mMonthList.size(); i++) {
                    if (mMonthList.get(i).getId() == mUserId) {
                        mMonthRank = i;
                    }
                }
                PostGetSortTotalRequst();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取本周排行榜
     */
    private void PostGetSortTotalRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNGROUP_RANK_LIST;
        int groupid = mGroupInfo.getRungroup().getId();
        int type = AppEnum.groupRankType.TOTAL;
        RequestParams params = RequestParamsBuild.buildGetGroupRankRequest(mContext,groupid, type);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mTotalList = JSON.parseArray(Response, GetGroupMemberSortResult.class);
                for (int i = 0; i < mTotalList.size(); i++) {
                    if (mTotalList.get(i).getId() == mUserId) {
                        mTotalRank = i;
                    }
                }
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 更新排行页面
     */
    private void updateViewByPostResult() {
        String sortStr = "";
        switch (mTabIndex) {
            case TAB_WEEK:
                mAdapter = new GroupMemberSortListAdapter(mContext, mWeekList);
                if (mWeekRank == -1) {
                    sortStr = "您本周暂无跑步";
                } else {
                    sortStr = "您的本周排行:" + (mWeekRank + 1);
                }
                break;
            case TAB_MONTH:
                mAdapter = new GroupMemberSortListAdapter(mContext, mMonthList);
                if (mMonthRank == -1) {
                    sortStr = "您本月暂无跑步";
                } else {
                    sortStr = "您的本月排行:" + (mMonthRank + 1);
                }
                break;
            case TAB_TOTAL:
                mAdapter = new GroupMemberSortListAdapter(mContext, mTotalList);
                if (mTotalRank == -1) {
                    sortStr = "您暂无跑步";
                } else {
                    sortStr = "您的总排行:" + (mTotalRank + 1);
                }
                break;
            case TAB_NAME:
                mAdapter = new GroupMemberSortListAdapter(mContext, mNameList);
//                if (mNameRank == -1) {
//                    sortStr = "您暂无跑步";
//                } else {
//                    sortStr = "您的总排行:" + (mNameRank + 1);
//                }
                sortStr = "共"+mNameList.size()+"名成员";
                break;
        }
        mMySortTv.setText(sortStr);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 重置Tab页面
     */
    private void resetAllLayout() {
        mNameLl.setBackgroundResource(R.drawable.btn_history_tab_left_normal);
        mWeekLl.setBackgroundResource(R.drawable.btn_history_tab_middle_normal);
        mMonthLl.setBackgroundResource(R.drawable.btn_history_tab_middle_normal);
        mTotalLl.setBackgroundResource(R.drawable.btn_history_tab_right_normal);

        mNameTv.setTextColor(Color.parseColor("#f06522"));
        mWeekTv.setTextColor(Color.parseColor("#f06522"));
        mMonthTv.setTextColor(Color.parseColor("#f06522"));
        mTotalTv.setTextColor(Color.parseColor("#f06522"));
    }

    /**
     * 销毁Handler响应
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
    }
}
