package com.app.pao.activity.group;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupPartyPhotoWallAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetPartyPhotoWallListResult;
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

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
@ContentView(R.layout.activity_group_party_photo_wall)
public class GroupPartyPhotoWallActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "GroupPartyPhotoWallActivity";
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    /* local view */
    @ViewInject(R.id.id_photo_wall)
    private RecyclerView mPhotoWallRv;
    @ViewInject(R.id.ll_none)
    private LinearLayout mNoneLl;//没有照片显示的界面

    /* local data */
    private GroupPartyPhotoWallAdapter mPhotoWallAdapter;
    private int mPartyId;
    private List<GetPartyPhotoWallListResult.UserPicture> mPhotoWallList;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            } else if (msg.what == MSG_POST_OK) {

                mPhotoWallAdapter = new GroupPartyPhotoWallAdapter(mContext, mPhotoWallList);
                mPhotoWallRv.setAdapter(mPhotoWallAdapter);
                mPhotoWallAdapter.setOnItemOnClickListener(new GroupPartyPhotoWallAdapter.OnItemOnClickListener() {
                    @Override
                    public void onItemOnClick(int position) {
                        GetPartyPhotoWallListResult.UserPicture photoWall = mPhotoWallList.get(position);
                        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
                        if (photoWall.getUserid() == userId) {
                            launchMyPhoto();
                        } else {
                            launchUserPhoto(photoWall.getUserid(), photoWall.getNickname());
                        }
                    }
                });
                if(mPhotoWallList.isEmpty()){
                    mNoneLl.setVisibility(View.VISIBLE);
                }else {
                    mNoneLl.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.btn_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.btn_edit:
                launchMyPhoto();
                break;
        }
    }

    @Override
    protected void initData() {
        mPartyId = getIntent().getIntExtra("partyId", 0);
    }

    @Override
    protected void initViews() {
        mPhotoWallRv.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        getPictureList();
    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (handler != null) {
            handler.removeMessages(MSG_POST_ERROR);
            handler.removeMessages(MSG_POST_OK);
        }
    }

    public void launchMyPhoto() {
        Bundle b = new Bundle();
        b.putInt("partyId", mPartyId);
        startActivity(GroupPartyMyPhotoActivity.class, b);
    }

    private void launchUserPhoto(int userId, String userNickName) {
        Bundle b = new Bundle();
        b.putInt("partyId", mPartyId);
        b.putInt("userId", userId);
        b.putString("userNickName", userNickName);
        startActivity(GroupPartyUserPhotoActivity.class, b);
    }

    /**
     * 获取照片墙数据
     */
    private void getPictureList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PICTURE_WALL_LIST;
        RequestParams params = RequestParamsBuild.buildGetPictureWallList(mContext,mPartyId);
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
                GetPartyPhotoWallListResult photos = JSON.parseObject(Response, GetPartyPhotoWallListResult.class);
                mPhotoWallList = photos.getUsers();

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
