package com.app.pao.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.group.GroupPartyAllPhotoActivity;
import com.app.pao.adapter.GroupPartyEditSummaryAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyAllPhotoResult;
import com.app.pao.entity.network.GetGroupPartylistSummarySectionResult;
import com.app.pao.entity.network.GetPartyInfoResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/13.
 * <p/>
 * 活动总结编辑
 */
@ContentView(R.layout.activity_group_party_edit_summary)
public class GroupPartyEditSummaryActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final int REQUEST_PARTY_ALL_PHOTO = 0;

    private static final int MSG_POST_ERROR = 2;
    private static final int MSG_POST_OK = 3;

    /* local view */
    @ViewInject(R.id.rv_summary_photo)
    private RecyclerView mSummaryPhotoRv;
    @ViewInject(R.id.et_summary_title)
    private EditText mSummaryTitleEt;
    @ViewInject(R.id.et_summary_content)
    private EditText mSummaryContentEt;

    /* local data */
    private GroupPartyEditSummaryAdapter mAdapter;
    private List<GetGroupPartyAllPhotoResult.PartyPicture> mDataPhotoUrl;
    private GetGroupPartyAllPhotoResult.PartyPicture partyPicture;
    private int mPartyId;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            } else if (msg.what == MSG_POST_OK) {
                mAdapter = new GroupPartyEditSummaryAdapter(mContext, mDataPhotoUrl,
                        new GroupPartyEditSummaryAdapter.actionListener() {
                            @Override
                            public void DeletePhoto(int index) {
                                mDataPhotoUrl.remove(index);
                                mAdapter.notifyItemRemoved(index);
                            }
                        });
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PARTY_ALL_PHOTO) {
                partyPicture = (GetGroupPartyAllPhotoResult.PartyPicture) data.getSerializableExtra("photo");
                mDataPhotoUrl.add(partyPicture);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.btn_cancel, R.id.btn_submit, R.id.btn_add_photo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.btn_cancel:
                showTipDialog();
                break;
            case R.id.btn_submit:
                addSummarySection();
                break;
            case R.id.btn_add_photo:
                launchPhotoWall();
                break;
        }
    }

    @Override
    protected void initData() {
        mDataPhotoUrl = new ArrayList<GetGroupPartyAllPhotoResult.PartyPicture>();
//        mPartyInfo = (GetPartyInfoResult) getIntent().getExtras().get("party");
        mPartyId = getIntent().getIntExtra("partyid", 0);
    }

    @Override
    protected void initViews() {
        mSummaryPhotoRv.setLayoutManager(new FullyLinearLayoutManager(this));
        mAdapter = new GroupPartyEditSummaryAdapter(mContext, mDataPhotoUrl,
                new GroupPartyEditSummaryAdapter.actionListener() {
                    @Override
                    public void DeletePhoto(int index) {
                        mDataPhotoUrl.remove(index);
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mSummaryPhotoRv.setAdapter(mAdapter);
    }

    @Override
    protected void doMyOnCreate() {

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
     * 启动跳转至照片墙
     */
    private void launchPhotoWall() {
        Bundle bundle = new Bundle();
        bundle.putInt("partyId", mPartyId);
        startActivityForResult(GroupPartyAllPhotoActivity.class, bundle, REQUEST_PARTY_ALL_PHOTO);
    }

    private void showTipDialog() {
        String mTitleStr = mSummaryTitleEt.getText().toString();

        String mDescriptionStr = mSummaryContentEt.getText().toString();
        if (mTitleStr.isEmpty() || mDescriptionStr.isEmpty()) {
            finish();
            return;
        } else {

            mDialogBuilder.showChoiceTwoBtnDialog(mContext, "提示", "确定取消编辑?", "取消", "确定");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
                @Override
                public void onLeftBtnClick() {

                }

                @Override
                public void onRightBtnClick() {
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }


    /**
     * 新增活动总结
     */
    private void addSummarySection() {


        String mTitleStr = mSummaryTitleEt.getText().toString();
        if (mTitleStr.isEmpty()) {
            mSummaryTitleEt.setError("请输入标题!");
            return;
        }
        String mDescriptionStr = mSummaryContentEt.getText().toString();
        if (mDescriptionStr.isEmpty()) {
            mSummaryTitleEt.setError("请输入内容!");
            return;
        }

        mDialogBuilder.showProgressDialog(mContext, "请稍后...", false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_ADD_SUMMARY_SECTION;
        String idList = "";
        if (mDataPhotoUrl.size() > 0) {
            for (int i = 0; i < mDataPhotoUrl.size(); i++) {
                idList += mDataPhotoUrl.get(i).getPhotoid() + ",";
            }
            idList = idList.substring(0, idList.length() - 1);
        }

        RequestParams params = RequestParamsBuild.buildAddSummarySection(mContext,mPartyId, idList, mTitleStr, mDescriptionStr);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "新增成功。");
                finish();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                mDialogBuilder.Destroy();
            }
        });
    }

//    /**
//     * 获取活动的总结列表
//     */
//    private void getSummarySection(){
//        HttpUtils http=new HttpUtils();
//        String POST_URL = URLConfig.URL_LIST_SUMMARY_SECTION;
//        RequestParams params = RequestParamsBuild.buildListSummarySection(mPartyInfor.getId());
//        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
//            @Override
//            protected void onErrorResponse(int errorCode, String errorMsg) {
//                if (handler != null) {
//                    Message msg = new Message();
//                    msg.what = MSG_POST_ERROR;
//                    msg.obj = errorMsg;
//                    handler.sendMessage(msg);
//                }
//            }
//
//            @Override
//            protected void onRightResponse(String Response) {
//                GetGroupPartylistSummarySectionResult listSummarySection = JSON.parseObject(Response, GetGroupPartylistSummarySectionResult.class);
//                mDataSummary = listSummarySection.getSummarysection();
//
//                if (handler != null) {
//                    Message msg = new Message();
//                    msg.what = MSG_POST_OK;
//                    handler.sendMessage(msg);
//                }
//            }
//
//            @Override
//            protected void onFailureResponse(HttpException e, String s) {
//                if (handler != null) {
//                    Message msg = new Message();
//                    msg.what = MSG_POST_ERROR;
//                    msg.obj = s;
//                    handler.sendMessage(msg);
//                }
//            }
//
//            @Override
//            protected void onFinish() {
//
//            }
//        });
//    }

//    private void submitSummary(){
//        HttpUtils http =new  HttpUtils();
//        String POST_URL = URLConfig.URL_SUBMIT_SUMMARY;
//        int showRank = 1;
//        int showCheck = 1;
//        if(!mRankingListCb.isChecked()){
//            showRank = 0;
//        }
//        if(!mSignListCb.isChecked()){
//            showCheck = 0;
//        }
//
//        RequestParams params = RequestParamsBuild.buildSubmitSummary(mPartyInfor.getId(),showCheck,showRank);
//
//        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
//            @Override
//            protected void onErrorResponse(int errorCode, String errorMsg) {
//                T.showShort(mContext,errorMsg);
//            }
//
//            @Override
//            protected void onRightResponse(String Response) {
//                T.showShort(mContext,"提交成功");
//            }
//
//            @Override
//            protected void onFailureResponse(HttpException e, String s) {
//                T.showShort(mContext,s);
//            }
//
//            @Override
//            protected void onFinish() {
//
//            }
//        });
//    }
}
