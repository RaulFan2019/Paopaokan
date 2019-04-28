package com.app.pao.activity.friend;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupListAdapterReplace;
import com.app.pao.adapter.UserGroupListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
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

/**
 * Created by Raul on 2015/12/4.
 * 用户跑团列表
 */
@ContentView(R.layout.activity_user_group_list)
public class UserGroupListActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "UserGroupListActivity";

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.lv_user_group_list)
    private RecyclerView mListView;//列表

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;


    /* local data */
    private GroupListAdapterReplace mAdapter;
    private List<GetGroupListResult> mDatas = new ArrayList<GetGroupListResult>();
    private static List<Integer> nullList = new ArrayList<>();
    private int userId;
    private String userNickName;//用户昵称

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
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                PostGetGroupRequst();
                break;
        }
    }

    @Override
    protected void initData() {
        userId = getIntent().getExtras().getInt("userid");
        userNickName = getIntent().getExtras().getString("nickname");
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(userNickName + "的跑团列表");
        setSupportActionBar(mToolbar);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void doMyOnCreate() {
        PostGetGroupRequst();
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

    /**
     * 获取跑团列表请求
     */
    private void PostGetGroupRequst() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_GROUP_LIST;
        RequestParams params = RequestParamsBuild.buildGetGroupListRequest(mContext, userId);
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
                List<GetGroupListResult> tempList = JSON.parseArray(Response, GetGroupListResult.class);
                if (LocalApplication.getInstance().getLoginUser(mContext).userId == userId) {
                    mDatas.addAll(tempList);
                } else {
                    for (GetGroupListResult result : tempList) {
                        if (result.status != AppEnum.groupStatus.WAITING
                                && result.role != AppEnum.groupRole.HAS_APPRO) {
                            mDatas.add(result);
                        }
                    }
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

    /**
     * 更新页面
     */
    private void updateViewByPostResult() {
        mAdapter = new GroupListAdapterReplace(mContext, mDatas, nullList);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 销毁Handler
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
        mPostHandler = null;
    }
}
