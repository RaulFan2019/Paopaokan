package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupMemberTagListAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetMamberTagListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 * <p/>
 * 设置团员标签页
 */
@ContentView(R.layout.activity_group_member_tag_list)
public class GroupMemberTagListActivity extends BaseAppCompActivity implements View.OnClickListener {
    private static final String TAG = "GroupMemberTagListActivity";

    private static final int MSG_POST_OK = 0;
    private static final int MSG_POST_ERROR = 1;

    private RecyclerView mTagRv;//标签列表

    private GroupMemberTagListAdapter mTagAdapter;
    private List<GetMamberTagListResult> mData; //源数据
    private List<GetMamberTagListResult> mSelectData;//编辑保存的数据

    private int mGroupId;
    private int mUserId;


    Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_OK) {
                mTagAdapter = new GroupMemberTagListAdapter(mData);
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
                mSelectData = mTagAdapter.getmDataList();
                if (mSelectData != null) {
                    setMemberTag();
                } else {
                    T.showShort(mContext, "请选择标签");
                }
                break;
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mData = new ArrayList<GetMamberTagListResult>();
        mSelectData = new ArrayList<GetMamberTagListResult>();

        mGroupId = getIntent().getIntExtra("groupId", 0);
        mUserId = getIntent().getIntExtra("userId", 0);
    }

    @Override
    protected void initViews() {
        mTagRv = (RecyclerView) findViewById(R.id.rv_group_tag);
        mTagRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void doMyOnCreate() {
        getMemberTagList();
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
        if (postHandler != null) {
            postHandler.removeMessages(MSG_POST_OK);
            postHandler.removeMessages(MSG_POST_ERROR);
        }
    }

    /**
     * 获取单个成员标签
     */
    private void getMemberTagList() {

        mDialogBuilder.showProgressDialog(mContext, "请稍候...", true);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PERSON_LABEL_LIST;
        RequestParams params = RequestParamsBuild.buildGetPersonLabelList(mContext,mGroupId, mUserId);
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
                mData = JSON.parseArray(Response, GetMamberTagListResult.class);
                if (mData != null) {
                    if (postHandler != null) {
                        Message msg = new Message();
                        msg.what = MSG_POST_OK;
                        msg.obj = Response;
                        postHandler.sendMessage(msg);
                    }
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
                if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }


    private void setMemberTag() {
        mDialogBuilder.showProgressDialog(mContext, "正在保存...", false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_PERSON_LABEL_LIST;

        StringBuffer mLabelIdList = new StringBuffer();
        for (int i = 0; i < mSelectData.size(); i++) {
            if (mSelectData.get(i).getHaslabel() == 1) {
                mLabelIdList.append( mSelectData.get(i).getId()+"," );
            }
        }
        if(mLabelIdList.length() > 0){
            mLabelIdList.deleteCharAt(mLabelIdList.length() - 1);
        }


        RequestParams params = RequestParamsBuild.buildUpdatePersonLabelList(mContext,mGroupId, mUserId, mLabelIdList.toString());

        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "保存成功。");
                finish();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }
}
