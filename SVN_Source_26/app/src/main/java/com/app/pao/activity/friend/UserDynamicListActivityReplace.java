package com.app.pao.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.workout.FriendHistoryInfoActivityV2;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.adapter.HistoryListAdapterReplace;
import com.app.pao.adapter.HistoryListAdapterV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.adapter.HistoryListWithTitleItem;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.network.GetWorkoutListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
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
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2015/12/4.
 * 用户动态列表
 */
@ContentView(R.layout.activity_user_dynamic_list)
public class UserDynamicListActivityReplace extends BaseAppCompActivity implements View.OnClickListener {


    /* contains */
    private static final String TAG = "UserDynamicListActivityReplace";

    private static final int MSG_UPDATE_VIEW = 0;//更新页面
    private static final int MSG_UPDATE_ERROR = 1;//更新失败
    private static final int RESULT_BACK_DELECT = 2;//删除历史返回调用

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.lv_dynamic_list)
    private RecyclerView listView;//列表
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout mFreshLayout;//刷新页面
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;
    @ViewInject(R.id.ll_none)
    private LinearLayout mNoneLl;//没有历史记录时的页面
    RelativeLayout loadView;

    /* local data */
    private boolean isFirstIn = true;
    private boolean postAble = true;//是否可以POST
    private int mHistoryCount;//跑步历史数量
    private boolean mHasMoreHistory;

    private HistoryListAdapterV2 mAdapter;//适配器

    private int eyePosition;//眼睛的位置

    private List<GetWorkoutListResult.WorkoutEntity> mWorkoutEntityList = new ArrayList<GetWorkoutListResult
            .WorkoutEntity>();//历史列表
    private List<HistoryListWithTitleItem> mHistoryList = new ArrayList<>();//
    private String lastItemMonth = "";//最后一个月份字符串

    /* local data */
    private int userId;//用户id
    private String userNickName;//用户昵称


    /**
     * 更新页面Handler
     */
    Handler updateViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VIEW) {
                mAdapter.notifyDataSetChanged();
                listView.scrollToPosition(eyePosition);
                if ((boolean) msg.obj) {
                    mFreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    if (mHistoryList == null || mHistoryList.size() == 0) {
                        mNoneLl.setVisibility(View.VISIBLE);
                    }
                } else {
                    mFreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                if (mHasMoreHistory) {
                    loadView.setVisibility(View.VISIBLE);
                } else {
                    loadView.setVisibility(View.GONE);
                    mLoadV.setVisibility(View.GONE);
                }

                updateViewHandler.removeCallbacks(mPostRunnable);
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
                postAble = true;

            } else if (msg.what == MSG_UPDATE_ERROR) {
                if ((boolean) msg.obj) {
                    mFreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                } else {
                    mFreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                updateViewHandler.removeCallbacks(mPostRunnable);
                mLoadV.setLoadingText("加载失败");
                mReladV.setVisibility(View.VISIBLE);
                postAble = true;
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
    @OnClick({R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                mLoadV.setLoadingText("加载中");
                mReladV.setVisibility(View.GONE);
                doRefresh();
                break;
        }
    }

    @Override
    protected void initData() {
        mHistoryCount = 0;
        postAble = true;
        eyePosition = 0;
        mHasMoreHistory = true;
        userId = getIntent().getExtras().getInt("userid");
        userNickName = getIntent().getExtras().getString("nickname");
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(userNickName + "的动态 ");
        setSupportActionBar(mToolbar);
        loadView = (RelativeLayout) mFreshLayout.findViewById(R.id.loadmore_view);

        //这一项必须存在
        mAdapter = new HistoryListAdapterV2(mContext, mHistoryList);
        mAdapter.setOnItemClickListener(new HistoryListAdapterV2.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(int position) {
                GetWorkoutListResult.WorkoutEntity workoutEntity = mHistoryList.get(position).entity;
                Bundle bundle = new Bundle();
                bundle.putString("workoutname", workoutEntity.getName());
                bundle.putInt("workoutid", workoutEntity.getId());
                startActivityForResult(FriendHistoryInfoActivityV2.class, bundle, RESULT_BACK_DELECT);
            }
        });
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setAdapter(mAdapter);

        mFreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            //下拉刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                doRefresh();
            }

            //上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                if (postAble) {
                    if (mHasMoreHistory) {
                        postAble = false;
                        postGetWorkoutList(false);
                        eyePosition = mHistoryList.size();
                    } else {
                        T.showShort(mContext, "没有更多跑步信息了");
                        mFreshLayout.loadmoreFinish(PullToRefreshLayout.DONE);
                    }
                }
            }
        });
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        // 第一次进入自动刷新
        if (isFirstIn) {
            isFirstIn = false;
            doRefresh();
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
     * 若是删除返回的则刷新列表
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_BACK_DELECT) {
                updateViewHandler.postDelayed(mPostRunnable, 500);
                doRefresh();
            }
        }
    }

    /**
     * 下拉刷新
     */
    private void doRefresh() {
        if (postAble) {
            mHasMoreHistory = true;
            mWorkoutEntityList.clear();
            mHistoryList.clear();
            mHistoryCount = 0;
            lastItemMonth = "";
            postGetWorkoutList(true);
        }
    }

    /**
     * 获取跑步历史列表
     */
    private void postGetWorkoutList(final boolean isFresh) {
        postAble = false;
        mWorkoutEntityList.clear();
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_WORKOUT_LIST;
        String limit = mHistoryCount + "," + 10;
        RequestParams params = RequestParamsBuild.buildGetWorkoutListRequest(mContext, userId, limit);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                Message msg = new Message();
                msg.what = MSG_UPDATE_ERROR;
                msg.obj = isFresh;
                if (updateViewHandler != null) {
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetWorkoutListResult result = JSON.parseObject(Response, GetWorkoutListResult.class);
                if (result.getWorkout() == null) {
                    mHistoryCount += 0;
                    mHasMoreHistory = false;
                } else {
                    mHistoryCount += result.getWorkout().size();
                    if (result.getWorkout().size() < 10) {
                        mHasMoreHistory = false;
                    }
                }
                parseWorkoutList(result.getWorkout());
                Message msg = new Message();
                msg.what = MSG_UPDATE_VIEW;
                msg.obj = isFresh;
                if (updateViewHandler != null) {
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
                Message msg = new Message();
                msg.what = MSG_UPDATE_ERROR;
                msg.obj = isFresh;
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
            updateViewHandler.removeCallbacks(mPostRunnable);
        }
    }

    /**
     * 解析跑步记录（按月分类）
     */
    private void parseWorkoutList(List<GetWorkoutListResult.WorkoutEntity> WorkoutEntityList) {
        for (GetWorkoutListResult.WorkoutEntity entity : WorkoutEntityList) {
            //获取跑步历史当前月份
            String titleStr = "";
            if (entity.starttime != null) {
                titleStr = TimeUtils.dateToMothString(TimeUtils.stringToDate(entity.starttime));
            } else {
                titleStr = TimeUtils.dateToMothString(TimeUtils.stringToDate(entity.name));
            }
            //新的月份
            if (!titleStr.equals(lastItemMonth)) {
                lastItemMonth = titleStr;
                mHistoryList.add(new HistoryListWithTitleItem(HistoryListWithTitleItem.ITEM_TITEL, null, titleStr));
            }
            mHistoryList.add(new HistoryListWithTitleItem(HistoryListWithTitleItem.ITEM_HISTORY, entity, ""));
        }
    }
}
