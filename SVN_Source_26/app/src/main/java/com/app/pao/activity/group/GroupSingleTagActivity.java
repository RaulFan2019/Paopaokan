package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupSingleTagAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetLabelMemberListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 * <p/>
 * 单个标签的人员列表
 */
@ContentView(R.layout.activity_group_single_tag)
public class GroupSingleTagActivity extends BaseAppCompActivity implements View.OnClickListener {

    private static final String TAG = "GroupSingleTagActivity";
    private static final int MSG_POST_OK = 0;//列表请求成功
    private static final int MSG_POST_ERROR = 1;//列表请求失败
    private static final int MSG_POST_DELETE_OK = 2;//删除成功
    private static final int MSG_POST_DELETE_ERROR = 3;//删除失败

    //   @ViewInject(R.id.rv_single_tag_member)
    private RecyclerView mGroupTagMemberRv;
    @ViewInject(R.id.btn_confirm_save)
    private Button mConfirmSaveBtn;
    @ViewInject(R.id.btn_delete_tag)
    private Button mDeleteTagBtn;

    private int mLabelId;

    private GroupSingleTagAdapter mSingleTagAdapter;
    private List<GetLabelMemberListResult> mMemberList;//成员列表
    private List<GetLabelMemberListResult> mSelectMemberList;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_OK) {
                mSingleTagAdapter = new GroupSingleTagAdapter(mMemberList);
                mGroupTagMemberRv.setAdapter(mSingleTagAdapter);
            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            } else if (msg.what == MSG_POST_DELETE_OK) {
                T.showShort(mContext, "已删除");
                finish();
            } else if (msg.what == MSG_POST_DELETE_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            }
        }
    };

    @Override
    @OnClick({R.id.btn_confirm_save, R.id.btn_delete_tag, R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_save:
                mSelectMemberList = mSingleTagAdapter.getDatas();
                if (mSelectMemberList != null) {
                    setLabelMemberList();
                } else {
                    T.showShort(mContext, "请选择成员");
                }
                break;
            case R.id.btn_delete_tag:
                showDeleteTagDialog();
                break;
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }


    @Override
    protected void initData() {
        mMemberList = new ArrayList<GetLabelMemberListResult>();
        mSelectMemberList = new ArrayList<GetLabelMemberListResult>();
        mLabelId = getIntent().getIntExtra("labelId", 0);
    }

    @Override
    protected void initViews() {
        mGroupTagMemberRv = (RecyclerView) findViewById(R.id.rv_single_tag_member);
        mGroupTagMemberRv.setLayoutManager(new LinearLayoutManager(this));
        //  mGroupTagMemberRv.addItemDecoration(new DividerItemDecoration());
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        getTagPersonList();
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (handler != null) {
            handler.removeMessages(MSG_POST_OK);
            handler.removeMessages(MSG_POST_ERROR);
            handler.removeMessages(MSG_POST_DELETE_OK);
            handler.removeMessages(MSG_POST_DELETE_ERROR);
        }
    }

    /**
     * 提示是否删除标签
     */
    private void showDeleteTagDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "提示", "确认删除标签 ?", "取消", "确定");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                removeLabel();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    /**
     * 删除标签
     */
    private void removeLabel() {
        mDialogBuilder.showProgressDialog(mContext, "正在删除..", false);

        HttpUtils http = new HttpUtils();
        final String POST_URL = URLConfig.URL_REMOVE_LABEL;
        RequestParams params = RequestParamsBuild.buildRemoveGroupLabel(mContext,mLabelId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_DELETE_ERROR;
                    msg.obj = errorCode;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_DELETE_OK;
                    msg.obj = Response;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_DELETE_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
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

    /**
     * 设置标签的人员列表
     */
    public void setLabelMemberList() {
        mDialogBuilder.showProgressDialog(mContext, "保存中...", false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_LABEL_MEMBER_LIST;

        StringBuffer mMemberList = new StringBuffer();
        for (int i = 0; i < mSelectMemberList.size(); i++) {
            if (mSelectMemberList.get(i).getHasLabel() == 1) {
                mMemberList.append(mSelectMemberList.get(i).getUserid() + ",");
            }
        }
        if (mMemberList.length() > 0) {
            mMemberList.deleteCharAt(mMemberList.length() - 1);
        }

        RequestParams params = RequestParamsBuild.buildUpdateLabelMemberList(mContext,mLabelId, mMemberList.toString());
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

    /**
     * 获取标签人员数据
     */
    public void getTagPersonList() {
        HttpUtils http = new HttpUtils();
        final String POST_URL = URLConfig.URL_GET_LABEL_MEMBER_LIST;
        RequestParams params = RequestParamsBuild.buildGetLabelMemberList(mContext,mLabelId);
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
                mMemberList = JSON.parseArray(Response, GetLabelMemberListResult.class);

                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    msg.obj = Response;
                    handler.sendMessage(msg);
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
}
