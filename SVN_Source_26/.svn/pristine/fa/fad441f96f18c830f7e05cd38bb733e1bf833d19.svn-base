package com.app.pao.activity.group;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ZoomBigImageActivity;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.adapter.GroupPartyMyPhotoAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyMyPhotoReqult;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 * 我的照片页面
 */
@ContentView(R.layout.activity_group_party_my_photo)
public class GroupPartyMyPhotoActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static String TAG = "GroupPartyMyPhotoActivity";

    private static int REQUEST_ADD_PHOTO = 1;

    private static int MSG_POST_ADD_PHOTO = 0;
    private static int MSG_POST_OK = 1;
    private static int MSG_POST_ERROR = 2;

    /* local view */
    private RecyclerView mPhotoRv;
    @ViewInject(R.id.tv_tip)
    private TextView mTipTv;//说明文本

    /* local data */
    private int mPartyId;
    private int mUserId;
    private String mPhotoUrl;
    private int mPhotoId;
    private List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList;
    private GroupPartyMyPhotoAdapter myPhotoAdapter;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ADD_PHOTO) {
                //新增一个显示数据
                GetGroupPartyMyPhotoReqult.UserPicture userPicture = new GetGroupPartyMyPhotoReqult.UserPicture();
                userPicture.setPhotoid(mPhotoId);
                userPicture.setUrl(mPhotoUrl);
                userPicture.setmCheckType(0);
                mDataList.add(userPicture);
                //刷新UI
                myPhotoAdapter.notifyDataSetChanged();
                if (mDataList.isEmpty()) {
                    mTipTv.setText("你尚未上传照片");
                } else {
                    mTipTv.setText("你共上传" + mDataList.size() + "张照片");
                }
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            } else if (msg.what == MSG_POST_OK) {
                myPhotoAdapter.notifyDataSetChanged();
                if (mDataList.isEmpty()) {
                    mTipTv.setText("你尚未上传照片");
                } else {
                    mTipTv.setText("你共上传" + mDataList.size() + "张照片");
                }
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
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
            case R.id.btn_add_photo:
                addPhoto(REQUEST_ADD_PHOTO);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_PHOTO) {
                mDialogBuilder.showProgressDialog(mContext, "正在上传照片..", false);
                ArrayList<String> mSelectImagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                Bitmap resultBmap = ImageUtils.compressImage(mSelectImagePathList.get(0));
                postAvatar(FileUtils.saveBigBitmap(resultBmap));
            }
        }
    }

    @Override
    protected void initData() {
        mDataList = new ArrayList<GetGroupPartyMyPhotoReqult.UserPicture>();
        mPartyId = getIntent().getIntExtra("partyId", 0);
        mUserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
    }

    @Override
    protected void initViews() {
        mPhotoRv = (RecyclerView) findViewById(R.id.rv_photos_list);
        mPhotoRv.setLayoutManager(new GridLayoutManager(this, 3));
        myPhotoAdapter = new GroupPartyMyPhotoAdapter(mContext, mDataList, new GroupPartyMyPhotoAdapter
                .onItemDeleteListener() {
            @Override
            public void deletePhoto(int pos) {
                deleteItemPhoto(pos);
            }

            @Override
            public void showPhoto(int pos) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", mDataList.get(pos).getUrl());
                startActivity(ZoomBigImageActivity.class, bundle);
            }
        });
        mPhotoRv.setAdapter(myPhotoAdapter);
    }

    @Override
    protected void doMyOnCreate() {
        getOnePersonPictureList();
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
        if (handler != null) {
            handler.removeMessages(MSG_POST_OK);
            handler.removeMessages(MSG_POST_ERROR);
            handler.removeMessages(MSG_POST_ADD_PHOTO);
        }
    }

    private void deleteItemPhoto(final int pos) {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "删除", "是否删除这张照片?", "取消", "删除");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                deletePhoto(pos);
            }

            @Override
            public void onCancel() {

            }
        });

    }

    private void deletePhoto(final int pos) {
        GetGroupPartyMyPhotoReqult.UserPicture photo = mDataList.get(pos);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_REMOVE_ONE_PICTURE;
        RequestParams params = RequestParamsBuild.buildRemoveOnPicture(mContext,mPartyId, photo.getPhotoid());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                mDataList.remove(pos);
                myPhotoAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 更换照片
     */
    private void addPhoto(int Request) {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, Request);
    }

    /**
     * 上传头像到服务器
     */
    private void postAvatar(File mAvatarF) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_AVATAR;
        RequestParams params = RequestParamsBuild.buildUploadAvatarRequest(mContext,mAvatarF);
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
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                mPhotoUrl = resultEntity.getUrl();
                uploadPicture();
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
     * 上传活动图片URL
     */
    private void uploadPicture() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_PICTURE;
        RequestParams params = RequestParamsBuild.buildUploadPicture(mContext,mPartyId, mPhotoUrl);
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
                try {
                    JSONObject jb = new JSONObject(Response);
                    mPhotoId = jb.optInt("id", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (handler != null) {
                    handler.sendEmptyMessage(MSG_POST_ADD_PHOTO);
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
                GetGroupPartyMyPhotoReqult reqult = JSON.parseObject(Response, GetGroupPartyMyPhotoReqult.class);
                if (reqult == null || reqult.getPicture() == null) {
                    return;
                }
                for (int i = 0; i < reqult.getPicture().size(); i++) {
                    GetGroupPartyMyPhotoReqult.UserPicture showPicture = reqult.getPicture().get(i);
                    showPicture.setmCheckType(0);
                    mDataList.add(showPicture);
                }

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


    /**
     * 删除单张图片
     */
    private void deleteOnePicture(int photoId) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_REMOVE_ONE_PICTURE;
        RequestParams params = RequestParamsBuild.buildRemoveOnPicture(mContext,mPartyId, photoId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {

            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }
}
