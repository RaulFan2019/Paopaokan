package com.app.pao.activity.group;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.adapter.GroupPartyAllPhotoAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyAllPhotoResult;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 * 活动所有照片
 */
@ContentView(R.layout.activity_group_party_all_photo)
public class GroupPartyAllPhotoActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "GroupPartyAllPhotoActivity";

    private static final int REQUEST_ADD_PHOTO = 1;
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;
    private static final int MSG_POST_ADD_PHOTO=2;

    private RecyclerView mPhotoRv;

    private GroupPartyAllPhotoAdapter mAdapter;
    private List<GetGroupPartyAllPhotoResult.PartyPicture> mDataList;

    private int mPartyId;
    private int mPhotoId;
    private String mPhotoUrl;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取图片列表成功
            if (msg.what == MSG_POST_OK) {
                if(mDataList.size() == 0){
                    T.showShort(mContext,"该活动目前没有照片上传");
                    finish();
                }
                mAdapter = new GroupPartyAllPhotoAdapter(GroupPartyAllPhotoActivity.this, mDataList);
                mPhotoRv.setAdapter(mAdapter);
                //获取图片错误
            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            }else if(msg.what == MSG_POST_ADD_PHOTO){
                GetGroupPartyAllPhotoResult.PartyPicture newPhoto = new GetGroupPartyAllPhotoResult.PartyPicture();
                newPhoto.setPhotoid(mPhotoId);
                newPhoto.setUrl(mPhotoUrl);
                mDataList.add(newPhoto);
                if(mAdapter == null) {
                    mAdapter = new GroupPartyAllPhotoAdapter(GroupPartyAllPhotoActivity.this, mDataList);
                    mPhotoRv.setAdapter(mAdapter);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu,R.id.btn_add_photo})
    public void onClick(View v) {
        if (v.getId() == R.id.title_bar_left_menu) {
            finish();
        } else if (v.getId() == R.id.btn_add_photo) {
            changePhoto(REQUEST_ADD_PHOTO);
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
        mDataList = new ArrayList<GetGroupPartyAllPhotoResult.PartyPicture>();
        mPartyId = getIntent().getIntExtra("partyId", 0);
    }

    @Override
    protected void initViews() {
        mPhotoRv = (RecyclerView) findViewById(R.id.id_all_photo);
        mPhotoRv.setLayoutManager(new GridLayoutManager(this, 3));
        getBriefPictureList();
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
        cancelHandler();
    }

    private void cancelHandler(){
        if(handler != null){
            handler.removeMessages(MSG_POST_ADD_PHOTO);
            handler.removeMessages(MSG_POST_ERROR);
            handler.removeMessages(MSG_POST_OK);
        }
    }

    /**
     * 更换照片
     */
    private void changePhoto(int Request) {
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
                T.showShort(mContext, errorMsg);
                errFinish();
            }

            @Override
            protected void onRightResponse(String Response) {
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                mPhotoUrl = resultEntity.getUrl();
                uploadPicture();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
                errFinish();
            }

            @Override
            protected void onFinish() {

            }

            private void errFinish() {
                if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                    mDialogBuilder.progressDialog.dismiss();
                }
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
                T.showShort(mContext,errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                try {
                    JSONObject jb = new JSONObject(Response);
                    mPhotoId = jb.optInt("id", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(handler != null) {
                    handler.sendEmptyMessage(MSG_POST_ADD_PHOTO);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,s);
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
     * 获取图片数据
     */
    private void getBriefPictureList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_BRIEF_PICTURE_LIST;
        RequestParams params = RequestParamsBuild.buildGetBriefPictureList(mContext,mPartyId);
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
//                Log.v(TAG,"Response:" + Response);
                GetGroupPartyAllPhotoResult allPhotoResult = JSON.parseObject(Response, GetGroupPartyAllPhotoResult.class);
                mDataList = allPhotoResult.getPicture();
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
