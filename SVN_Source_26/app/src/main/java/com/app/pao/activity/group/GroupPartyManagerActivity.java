package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupPartyManagerAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyMemberListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 * <p/>
 * 转交活动
 */
@ContentView(R.layout.activity_group_party_manager)
public class GroupPartyManagerActivity extends BaseAppCompActivity implements View.OnClickListener {

    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    //    @ViewInject(R.id.et_search_member_input)
//    private EditText mSearchEt;
//    @ViewInject(R.id.iv_search_member_fork)
//    private ImageView mSearchForkIv;
    private RecyclerView mMemberRv;

    private int mPartyId;//活动id
    private int mGroupId;
    private GroupPartyManagerAdapter mAdapter;

    private List<GetGroupPartyMemberListResult> mDataList;

    Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_OK) {
                mAdapter = new GroupPartyManagerAdapter(mContext, mDataList, new GroupPartyManagerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                       if(mDataList.get(position).getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER){
                           removePartyManager(mDataList.get(position).getUserid());
                       }else{
                           addPartyManager(mDataList.get(position).getUserid());
                       }
                    }
                });

                mMemberRv.setAdapter(mAdapter);
            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            }
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mPartyId = getIntent().getIntExtra("partyId", 0);
        mGroupId = getIntent().getIntExtra("groupid", 0);
    }

    @Override
    protected void initViews() {
        mMemberRv = (RecyclerView) findViewById(R.id.rv_group_member);
        mMemberRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        getPartyMemberList();
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (postHandler != null) {
            postHandler.removeMessages(MSG_POST_ERROR);
            postHandler.removeMessages(MSG_POST_OK);
        }
    }

//    /**
//     * 修改活动所有者
//     */
//    private void changePartyOwner(int mChangeId) {
//        mDialogBuilder.showProgressDialog(mContext, "正在处理...", false, false);
//
//        HttpUtils http = new HttpUtils();
//        String POST_URL = URLConfig.URL_CHANGE_PARTY_OWNER;
//        RequestParams params = RequestParamsBuild.buildChangePartyOwner(mPartyId, mOwnerId, mChangeId);
//        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
//            @Override
//            protected void onErrorResponse(int errorCode, String errorMsg) {
//                T.showShort(mContext, errorMsg);
//            }
//
//            @Override
//            protected void onRightResponse(String Response) {
//                T.showShort(mContext, "修改成功。");
//                finish();
//            }
//
//            @Override
//            protected void onFailureResponse(HttpException e, String s) {
//                T.showShort(mContext, s);
//            }
//
//            @Override
//            protected void onFinish() {
//                mDialogBuilder.Destroy();
//            }
//        });
//    }


    /**
     * 获取活动成员
     */
    private void getPartyMemberList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_MEMBER_LIST;
        RequestParams params = RequestParamsBuild.buildGetPartyMemberListRequest(mContext,mPartyId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (postHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    postHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDataList = JSON.parseArray(Response, GetGroupPartyMemberListResult.class);
                for (GetGroupPartyMemberListResult member :mDataList){
                    if(member.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER){
                        mDataList.remove(member);
                        break;
                    }
                }

                if (postHandler != null) {
                    postHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (postHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    postHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 添加活动管理员
     * @param userId
     */
    private void addPartyManager(int userId){
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_ADD_PARTY_MANAGER;
        RequestParams params = RequestParamsBuild.buildAddPartyManager(mContext,mPartyId, userId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext,errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext,"设置成功。");
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,s);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 删除活动管理员
     * @param userId
     */
    private void removePartyManager(int userId){
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_REMOVE_PARTY_MANAGER;
        RequestParams params = RequestParamsBuild.buildRemovePartyManager(mContext,mPartyId,userId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext,errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext,"已取消");
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,s);
            }

            @Override
            protected void onFinish() {

            }
        });
    }
}
