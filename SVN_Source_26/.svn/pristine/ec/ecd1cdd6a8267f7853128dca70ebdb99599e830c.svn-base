package com.app.pao.activity.workout;

import android.location.LocationListener;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.MyClockListAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetApplyFriendListResult;
import com.app.pao.entity.network.GetClockResult;
import com.app.pao.entity.network.GetFriendListResult;
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
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/16.
 */
@ContentView(R.layout.activity_clock_list)
public class MyClockListActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;


    /* local data */
    private int mUserId;//用户Id
    private MyClockListAdapter mAdapter;
    private List<GetClockResult.ClocksEntity> clockList = new ArrayList<>();
    private int mUserCount;//用户数量
    private int mClockCount;

    /* local view */
    @ViewInject(R.id.rv_clock_list)
    private RecyclerView mClockRv;
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.tv_person_count)
    private TextView mPersonCountTv;//人数量
    @ViewInject(R.id.tv_clock_count)
    private TextView mClockCountTv;//闹钟数量

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;
    @ViewInject(R.id.ll_no_clock)
    private LinearLayout mNoneClockLl;//没有闹钟的页面

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
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                    break;
                //刷新成功
                case MSG_POST_OK:
                    updateViewByPostResult();
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    @OnClick({R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                PostGetClockRequst();
                break;
        }
    }

    @Override
    protected void initData() {
        mUserId = getIntent().getIntExtra("userid", LocalApplication.getInstance().getLoginUser(mContext).userId);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);

        mAdapter = new MyClockListAdapter(mContext, clockList);
        mClockRv.setLayoutManager(new LinearLayoutManager(mContext));
        mClockRv.setAdapter(mAdapter);
    }

    @Override
    protected void doMyOnCreate() {
        PostGetClockRequst();
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
    }

    /**
     * 更新页面
     */
    private void updateViewByPostResult() {
        if (mClockCount > 0) {
            mNoneClockLl.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
        mPersonCountTv.setText(mUserCount + "");
        mClockCountTv.setText(mClockCount + "");
    }

    /**
     * 获取闹钟请求
     */
    private void PostGetClockRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_USER_CLOCK;
        RequestParams params = RequestParamsBuild.buildGetFriendListRequest(mContext, mUserId);
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
                GetClockResult result = JSON.parseObject(Response, GetClockResult.class);
                mClockCount = result.clockcount;
                mUserCount = result.usercount;
                if (result.clocks != null) {
                    clockList.clear();
                    clockList.addAll(result.clocks);
                }

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
}
