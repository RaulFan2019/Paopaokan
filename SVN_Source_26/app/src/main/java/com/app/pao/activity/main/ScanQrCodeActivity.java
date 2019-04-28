package com.app.pao.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.activity.settings.UserBasicInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetParseQRCodeResult;
import com.app.pao.entity.network.GetUserInfoResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.jinlin.zxing.example.activity.CaptureActivity;
import com.jinlin.zxing.example.decode.DecodeThread;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Raul on 2015/11/25.
 * 扫描二维码界面
 */
@ContentView(R.layout.activity_scan_qrcode)
public class ScanQrCodeActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "ScanQrCodeActivity";

    private static final int REQUEST_QRCODE = 0;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.result_image)
    private ImageView mResultImage;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            updateQRView(data);
        } else {
            finish();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void doMyOnCreate() {
        startActivityForResult(CaptureActivity.class, REQUEST_QRCODE);
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
     * 更新二维码页面
     */
    private void updateQRView(Intent data) {
        int width = data.getExtras().getInt("width");
        int height = data.getExtras().getInt("height");

        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(width, height);
        lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
                .getDisplayMetrics());
        lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                .getDisplayMetrics());
        lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                .getDisplayMetrics());
        mResultImage.setLayoutParams(lps);

        String result = data.getExtras().getString("result");

        Bitmap barcode = null;
        byte[] compressedBitmap = data.getExtras().getByteArray(DecodeThread.BARCODE_BITMAP);
        if (compressedBitmap != null) {
            barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
            // Mutable copy:
            barcode = barcode.copy(Bitmap.Config.RGB_565, true);
        }

        mResultImage.setImageBitmap(barcode);

        checkQRCode(result);
    }

    /**
     * 通过服务器获取二维码用途
     */
    private void checkQRCode(String result) {
        mDialogBuilder.showProgressDialog(mContext, "正在识别..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_PARSE_QRCODE;
        RequestParams params = RequestParamsBuild.buildParseQrcodeRequest(mContext,result);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetParseQRCodeResult resultEntity = JSON.parseObject(Response, GetParseQRCodeResult.class);
                //用户
                if (resultEntity.getType() == AppEnum.QRType.PERSON) {
                    Log.v(TAG, "resultEntity.getId():" + resultEntity.getId());
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid", resultEntity.getId());
                    startActivity(UserInfoActivity.class,bundle);
                    finish();
//                    GetUserInfo(resultEntity.getId());
                    //跑团
                } else if (resultEntity.getType() == AppEnum.QRType.RUNGROUP) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("groupid", resultEntity.getId());
                    startActivity(GroupInfoActivity.class, bundle);
                    finish();
                    //活动
                } else if (resultEntity.getType() == AppEnum.QRType.PARTY) {
                    postCheckInParty(resultEntity.getId());
                    //其他情况
                } else {
                    T.showShort(mContext, "未知类型");
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
     * 获取用户个人信息
     */
    private void GetUserInfo(int userId) {
        //若扫描用户是自己
        if (userId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
            startActivity(UserBasicInfoActivity.class);
            return;
        } else {
            mDialogBuilder.showProgressDialog(mContext, "正在获取用户信息..", false);
            HttpUtils http = new HttpUtils();
            String POST_URL = URLConfig.URL_GET_USER_INFO;
            RequestParams params = RequestParamsBuild.buildGetUserInfoRequest(mContext,userId);
            http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

                @Override
                protected void onErrorResponse(int errorCode, String errorMsg) {
                    T.showShort(mContext, errorMsg);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                }

                @Override
                protected void onRightResponse(String Response) {
                    GetUserInfoResult resultEntity = JSON.parseObject(Response, GetUserInfoResult.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userinfo", resultEntity);
                    startActivity(UserInfoActivity.class,bundle);
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
    }


    /**
     * 正在发送签到
     */
    private void postCheckInParty(int partyid) {
        mDialogBuilder.showProgressDialog(mContext, "正在签到活动", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_CHECK_IN;
        RequestParams params = RequestParamsBuild.BuildCheckInParams(mContext,partyid);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "活动已签到");
                setResult(RESULT_OK);
                finish();
            }


            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    @OnClick(R.id.btn_scan)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_scan) {
            startActivityForResult(CaptureActivity.class, REQUEST_QRCODE);
        }
    }
}
