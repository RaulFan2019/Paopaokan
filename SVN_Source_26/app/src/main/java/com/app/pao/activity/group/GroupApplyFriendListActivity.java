package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupApplyFriendListAdapter;
import com.app.pao.adapter.SimpleBaseAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetFriendListByGroup;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupInfoResult;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/12/13.
 * 跑团邀请好友页面
 */
@ContentView(R.layout.activity_group_apply_friend)
public class GroupApplyFriendListActivity extends BaseAppCompActivity {

    /* contains */
    private static final String TAG = "GroupApplyFriendListActivity";

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 2;
    private static final int MSG_POST_INVITE_OK = 3;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.listview)
    private ListView mListView;//列表
    @ViewInject(R.id.rl_load)
    private RelativeLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;

    private GroupApplyFriendListAdapter mAdapter;

    /* local data */
    private GetGroupDetailInfoRequest mGroupInfo;//跑团详情
    private boolean mPostAble;//是否可以发送请求

    private List<GetFriendListByGroup.FriendsEntity> mDatas = new ArrayList<GetFriendListByGroup.FriendsEntity>();

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
                    if(mDialogBuilder.progressDialog != null){
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    mLoadV.setLoadingText("加载失败");
                    T.showShort(mContext, (String) msg.obj);
                    mPostAble = true;
                    break;
                //发送请求成功,跳转到验证码输入界面
                case MSG_POST_OK:
                    mPostAble = true;
                    refeshView();
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
                    break;
                case MSG_POST_INVITE_OK:
                    if(mDialogBuilder.progressDialog != null){
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        mGroupInfo = (GetGroupDetailInfoRequest) getIntent().getExtras().getSerializable("group");
        mPostAble = true;
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void doMyOnCreate() {
        GetFriendList();
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {

    }

    private void GetFriendList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_FRIEND_LIST_IN_GROUP;
        RequestParams params = RequestParamsBuild.buildGetFriendListInGroup(mContext,mGroupInfo.getRungroup().getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetFriendListByGroup resultEntity = JSON.parseObject(Response, GetFriendListByGroup.class);
                mDatas = resultEntity.getFriends();
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
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
    private void refeshView() {
        mAdapter = new GroupApplyFriendListAdapter(mContext, mDatas);
        mAdapter.setOnItemBtnClickListener(new SimpleBaseAdapter.OnItemBtnClickListener() {
            @Override
            public void onItemBtnClick(int pos) {
                if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER || mGroupInfo.getRungroup().getRole
                        () == AppEnum.groupRole.MANAGER) {
                    invitFriend(pos);
                } else {
                    RecommendFriend(pos);
                }
            }
        });
        mListView.setAdapter(mAdapter);
    }

    /**
     * 发送邀请
     */
    private void invitFriend(final int pos) {
        mDialogBuilder.showProgressDialog(mContext, "正在发送邀请..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_INVITE_FRIEND;
        RequestParams params = RequestParamsBuild.buildInviteFriend(mContext,mDatas.get(pos).getId(), mGroupInfo.getRungroup()
                .getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetFriendListByGroup.FriendsEntity entity = mDatas.get(pos);
                entity.setGrouprole(AppEnum.friendInGroupRole.INVITE);
                mDatas.set(pos,entity);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_INVITE_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    /**
     * 发送推荐
     */
    private void RecommendFriend(final int pos) {
        mDialogBuilder.showProgressDialog(mContext, "正在发送推荐..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_RECOMMEND_FRIEND;
        RequestParams params = RequestParamsBuild.buildReCommendFriend(mContext,mDatas.get(pos).getId(), mGroupInfo
                .getRungroup().getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetFriendListByGroup.FriendsEntity entity = mDatas.get(pos);
                entity.setGrouprole(AppEnum.friendInGroupRole.INVITE);
                mDatas.set(pos,entity);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_INVITE_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }
}
