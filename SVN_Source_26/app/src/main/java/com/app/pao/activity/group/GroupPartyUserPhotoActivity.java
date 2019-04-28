package com.app.pao.activity.group;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ZoomBigImageActivity;
import com.app.pao.adapter.GroupPartyUserPhotoAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyMyPhotoReqult;
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
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
@ContentView(R.layout.activity_group_party_user_photo)
public class GroupPartyUserPhotoActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    /* local view */
    @ViewInject(R.id.tv_title_group_party_user_photo)
    private TextView mTitleTv;
    @ViewInject(R.id.rv_photos_list)
    private RecyclerView mPhotosRv;
    @ViewInject(R.id.tv_tip)
    private TextView mTipTv;//说明文本


    /* local data */
    private int mPartyId;
    private int mUserId;
    private String mUserNickName;
    private GroupPartyUserPhotoAdapter mUserPhotoAdapter;

    private List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            } else if (msg.what == MSG_POST_OK) {
                mUserPhotoAdapter.notifyDataSetChanged();
                if (mDataList.isEmpty()) {
                    mTipTv.setText(mUserNickName + "尚未上传照片");
                } else {
                    mTipTv.setText(mUserNickName + "共上传" + mDataList.size() + "张照片");
                }
            }
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.btn_add_photo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            //增加图片
            case R.id.btn_add_photo:
                break;
        }
    }

    @Override
    protected void initData() {
        mDataList = new ArrayList<GetGroupPartyMyPhotoReqult.UserPicture>();
        mPartyId = getIntent().getIntExtra("partyId", 0);
        mUserId = getIntent().getIntExtra("userId", 0);
        mUserNickName = getIntent().getStringExtra("userNickName");
    }

    @Override
    protected void initViews() {
        mPhotosRv.setLayoutManager(new GridLayoutManager(this, 3));
        mTitleTv.setText(mUserNickName + "的活动图片");
        mUserPhotoAdapter = new GroupPartyUserPhotoAdapter(mContext, mDataList, new GroupPartyUserPhotoAdapter.onClickListener() {
            @Override
            public void showPhoto(int pos) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", mDataList.get(pos).getUrl());
                startActivity(ZoomBigImageActivity.class, bundle);
            }
        });
        mPhotosRv.setAdapter(mUserPhotoAdapter);
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        getOnePersonPictureList();
    }

    @Override
    protected void destroy() {

    }

    /**
     * 获取个人图片List
     */
    private void getOnePersonPictureList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_ONE_PERSON_PICTURE_LIST;
        RequestParams params = RequestParamsBuild.buildGetOnePersonPictureList(mContext,mPartyId, mUserId);
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
                if (mDataList != null && mDataList.size() > 0) {
                    mDataList.clear();
                }
                GetGroupPartyMyPhotoReqult reqult = JSON.parseObject(Response, GetGroupPartyMyPhotoReqult.class);
                mDataList.addAll(reqult.getPicture());
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
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
