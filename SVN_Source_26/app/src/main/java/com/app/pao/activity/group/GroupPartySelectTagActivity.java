package com.app.pao.activity.group;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupPartySelectTagAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetTagManagerListResult;
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
 * Created by Administrator on 2016/1/18.
 */
@ContentView(R.layout.activity_group_party_tag_list)
public class GroupPartySelectTagActivity extends BaseAppCompActivity implements View.OnClickListener{

    private static final int MSG_POST_OK = 0;
    private static final int MSG_POST_ERROR = 1;

    @ViewInject(R.id.rv_group_tag)
    private RecyclerView mTagRv;//标签列表
    private GroupPartySelectTagAdapter mTagAdapter;

    private List<GetTagManagerListResult> mDataList; //源数据
    private List<GetTagManagerListResult> mSelectData;//编辑保存的数据


    private int mGroupId;
    private int mUserId;


    Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_OK) {
                mTagAdapter = new GroupPartySelectTagAdapter(mDataList);
                mTagRv.setAdapter(mTagAdapter);
            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            }
        }
    };

    @Override
    @OnClick({R.id.btn_confirm_tag, R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_tag:
                mSelectData = mTagAdapter.getmCheckedList();
                if(mSelectData != null) {
                    ArrayList<String> labelNameList = new ArrayList<>();
                    StringBuffer mLabelIdList = new StringBuffer();
                    if(mSelectData.size() > 0){
                        mLabelIdList.append(mSelectData.get(0).getId());
                        labelNameList.add(mSelectData.get(0).getName());
                        for (int i = 1;i < mSelectData.size();i++){
                            mLabelIdList.append(","+mSelectData.get(i).getId());
                            labelNameList.add(mSelectData.get(i).getName());
                        }
                    }

                    Intent i = new Intent();
                    i.putExtra("mLabelIdList",mLabelIdList.toString());
                    i.putStringArrayListExtra("labelNameList",labelNameList);
                    setResult(RESULT_OK, i);
                    finish();
                }else{
                    T.showShort(mContext,"请选择标签");
                }
                break;
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mDataList = new ArrayList<GetTagManagerListResult>();
        mSelectData = new ArrayList<GetTagManagerListResult>();

        mGroupId = getIntent().getIntExtra("groupId",0);
    //    mUserId = getIntent().getIntExtra("userId", 0);
    }


    @Override
    protected void initViews() {
        mTagRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void doMyOnCreate() {
        getGroupTagList();
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

    /**
     * 获取跑团的标签数据
     */
    private void getGroupTagList(){
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUN_GROUP_LABEl_LIST;
        RequestParams params = RequestParamsBuild.buildGetRunGroupLabelList(mContext,mGroupId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if(postHandler != null){
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    postHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mDataList = JSON.parseArray(Response, GetTagManagerListResult.class);
                for (int i = 0;i<mDataList.size();i++){
                    mDataList.get(i).setIsChecked(0);
                }

                if(postHandler != null){
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    postHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if(postHandler != null){
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
}
