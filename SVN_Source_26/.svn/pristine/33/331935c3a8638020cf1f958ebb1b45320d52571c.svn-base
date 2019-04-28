package com.app.pao.activity.group;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Raul on 2015/11/25.
 * 创建跑团(身份验证)
 */
@ContentView(R.layout.activity_create_group_check)
public class CreateGroupCheckActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "CreateGroupCheckActivity";


    private static final int REQUEST_USER_PHOTO = 1;
    private static final int REQUEST_PARTY_PHOTO = 2;
    private static final int REQUEST_CUT_USER = 3;
    private static final int REQUEST_CUT_PHOTO = 4;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.et_name)
    private EditText mNameEt;//姓名输入
    @ViewInject(R.id.et_email)
    private EditText mEmailEt;//邮箱输入
    @ViewInject(R.id.et_phone)
    private EditText mPhoneEt;//手机号码输入
    @ViewInject(R.id.view_user_photo)
    private ImageView mUserPhotoV;//用户照片
    @ViewInject(R.id.view_party_photo)
    private ImageView mPartyPhotoV;//活动照片


    /* local data */
    private String mName;//用户姓名
    private String mPhone;//用户手机号
    private String mEmail;//用户邮箱
    private String mCountryIso;//国际号


    private File mUserFile;//用户图片文件
    private File mPartyFile;//活动图片文件
    private String mUserAvatar = null;//用户照片地址
    private String mPartyAvatar = null;//活动照片地址

    private BitmapUtils mBitmapUtils;
    private ArrayList<String> mSelectImagePathList;

    /**
     * Toolbar 点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.btn_next, R.id.view_user_photo, R.id.view_party_photo})
    public void onClick(View v) {
        switch (v.getId()) {
            //点击下一步
            case R.id.btn_next:
                checkUserInfo();
                break;
            //点击活动照片
            case R.id.view_party_photo:
                changePhoto(REQUEST_PARTY_PHOTO);
                break;
            //点击用户照片
            case R.id.view_user_photo:
                changePhoto(REQUEST_USER_PHOTO);
                break;
        }
    }


    /**
     * on Activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //去裁剪用户头像
            if (requestCode == REQUEST_USER_PHOTO) {
                mSelectImagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                ImageUtils.startPhotoCut(this, Uri.fromFile(new File(mSelectImagePathList.get(0))), REQUEST_CUT_USER);
                ///去裁剪活动照片
            } else if (requestCode == REQUEST_PARTY_PHOTO) {
                mSelectImagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                ImageUtils.startPhotoCut(this,Uri.fromFile(new File(mSelectImagePathList.get(0))),REQUEST_CUT_PHOTO);
            }else if(requestCode == REQUEST_CUT_USER){
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(AppConfig.cutFileUri));
                    Bitmap resultBmap = ImageUtils.compressBitmap(bitmap);
                    mUserFile = FileUtils.saveBigBitmap(resultBmap);
                    postAvatar(REQUEST_USER_PHOTO, mUserFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == REQUEST_CUT_PHOTO){
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(AppConfig.cutFileUri));
                    Bitmap resultBmap = ImageUtils.compressBitmap(bitmap);
                    mPartyFile = FileUtils.saveBigBitmap(resultBmap);
                    postAvatar(REQUEST_PARTY_PHOTO, mPartyFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    protected void initData() {
        ActivityStackManager.getAppManager().addTempActivity(this);
        mBitmapUtils = new BitmapUtils(mContext);
        mCountryIso = DeviceUtils.GetCountryIso(mContext);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
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
        ActivityStackManager.getAppManager().finishTempActivity(this);
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
    private void postAvatar(final int request, File mAvatarF) {
        mDialogBuilder.showProgressDialog(mContext, "正在上传照片..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_AVATAR;
        RequestParams params = RequestParamsBuild.buildUploadAvatarRequest(mContext,mAvatarF);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                String avatar = resultEntity.getUrl();
                //更新活动照片
                if (request == REQUEST_PARTY_PHOTO) {
                    mPartyAvatar = avatar;
                    mBitmapUtils.display(mPartyPhotoV, String.valueOf(Uri.fromFile(mPartyFile)));
                    //更新个人照片
                } else {
                    mUserAvatar = avatar;
                    mBitmapUtils.display(mUserPhotoV, String.valueOf(Uri.fromFile(mUserFile)));
                }
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
     * 检查用户信息
     */
    private void checkUserInfo() {
        //检查姓名
        mName = mNameEt.getText().toString();
        String error = StringUtils.checkNickNameError(mContext, mName);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mNameEt.setError(error);
            return;
        }
        //检查邮件
        mEmail = mEmailEt.getText().toString();
        error = StringUtils.checkEmailError(mContext, mEmail);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mEmailEt.setError(error);
            return;
        }
        //检查手机
        mPhone = mPhoneEt.getText().toString();
        error = StringUtils.checkPhoneNumInputError(mContext, mPhone, mCountryIso);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mPhoneEt.setError(error);
            return;
        }
        //检查头像
        if (mUserAvatar == null) {
            T.showShort(mContext, "请选择个人照片");
            return;
        }
        //活动照片
        if (mPartyAvatar == null) {
            T.showShort(mContext, "请选择活动照片");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("name", mName);
        bundle.putString("email", mEmail);
        bundle.putString("phone", mPhone);
        bundle.putString("useravatar", mUserAvatar);
        bundle.putString("partyavatar", mPartyAvatar);
        startActivity(CreateGroupCommitActivity.class, bundle);
    }

}
