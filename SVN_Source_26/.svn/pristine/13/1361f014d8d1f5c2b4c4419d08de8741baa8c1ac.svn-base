package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupPartyMemberListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.model.PartyMemberListEntity;
import com.app.pao.entity.network.GetGroupPartyMemberListResult;
import com.app.pao.entity.network.GetPartyInfoDetailResult;
import com.app.pao.entity.network.GetPartyInfoResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
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
 * Created by Administrator on 2016/1/19.
 * 活动成员的列表
 */
@ContentView(R.layout.activity_group_party_member_list)
public class GroupPartyMemberListActivity extends BaseAppCompActivity implements View.OnClickListener {
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;


    @ViewInject(R.id.rv_party_member)
    private RecyclerView mMemberRv;

    private List<GetGroupPartyMemberListResult> mDataList;
    private List<PartyMemberListEntity> memberList;
    private GroupPartyMemberListAdapter memberListAdapter;

    private GetPartyInfoDetailResult mPartyInfor;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            } else if (msg.what == MSG_POST_OK) {
                int isOwner;
                if (mPartyInfor.getOwnerid() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                    isOwner = AppEnum.groupPartyPersonStatus.PARTY_OWNER;
                } else {
                    isOwner = 0;
                }
                memberListAdapter = new GroupPartyMemberListAdapter(mContext, isOwner, memberList);
                mMemberRv.setAdapter(memberListAdapter);
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
        mDataList = new ArrayList<GetGroupPartyMemberListResult>();
        mPartyInfor = (GetPartyInfoDetailResult) getIntent().getSerializableExtra("party");
    }

    @Override
    protected void initViews() {
        mMemberRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        getPartyMemberList();
    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (handler != null) {
            handler.removeMessages(MSG_POST_OK);
            handler.removeMessages(MSG_POST_ERROR);
        }
    }

    /**
     * 获取活动成员
     */
    private void getPartyMemberList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_MEMBER_LIST;
        RequestParams params = RequestParamsBuild.buildGetPartyMemberListRequest(mContext,mPartyInfor.getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDataList = JSON.parseArray(Response, GetGroupPartyMemberListResult.class);
                paserData(mDataList);
                if (handler != null) {
                    handler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 解析数据
     *
     * @param mDataList
     */
    private void paserData(List<GetGroupPartyMemberListResult> mDataList) {
        memberList = new ArrayList<PartyMemberListEntity>();
        if(mDataList == null || mDataList.size() == 0){
            T.showShort(mContext,"目前无人报名!");
//            finish();
            return;
        }
        //插入组织人
        PartyMemberListEntity entity;
// = new PartyMemberListEntity(null, true, mDataList.get(0).getPace(),"");
//        memberList.add(entity);
//        entity = new PartyMemberListEntity(mDataList.get(0), false, "","");
//        memberList.add(entity);

        String pace = "99999999";
//        int signNum = 0;
        int registNum = 0;
        for (int i = 0; i < mDataList.size(); i++) {
            GetGroupPartyMemberListResult result = mDataList.get(i);
            //若是活动负责人
//            if (result.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER) {
//                entity = new PartyMemberListEntity(result, false, "","");
//                memberList.add(entity);
//                continue;
//            }
            //若不是活动负责人
            if (!result.getPace().equals(pace)) {
                pace = result.getPace();
                List<PartyMemberListEntity> tempList = new ArrayList<PartyMemberListEntity>();
                for (int j = i; j < mDataList.size(); j++) {
                    GetGroupPartyMemberListResult resultj = mDataList.get(j);
                    if (!resultj.getPace().equals(pace)) {
                        i--;
                        break;
                    }
                    entity = new PartyMemberListEntity(resultj, false, "","");
                    tempList.add(entity);
                    registNum++;
//                    if (resultj.getPersonstatus() == 2) {
//                        signNum++;
//                    }
                    i++;
                }
                if (pace.isEmpty()) {
                    pace = "无配速";
                }
                entity = new PartyMemberListEntity(null, true, pace,registNum+"");
                memberList.add(entity);
                memberList.addAll(tempList);
                registNum = 0;
//                signNum = 0;
            }
        }
    }

}
