package com.app.pao.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.activity.settings.MyQRCodeActivity;
import com.app.pao.activity.settings.UserBasicInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.network.GetParseQRCodeResult;
import com.app.pao.entity.network.GetUserInfoResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.app.pao.utils.handler.CaptureActivityHandler;
import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.jinlin.zxing.example.camera.CameraManager;
import com.jinlin.zxing.example.decode.BitmapDecoder;
import com.jinlin.zxing.example.decode.DecodeThread;
import com.jinlin.zxing.example.utils.BeepManager;
import com.jinlin.zxing.example.utils.BitmapUtils;
import com.jinlin.zxing.example.utils.InactivityTimer;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by LY on 2016/4/5.
 */
public class ScanQRCodeActivityReplace extends BaseAppCompActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = ScanQRCodeActivityReplace.class.getSimpleName();

    private static final int REQUEST_CODE = 100;

    private static final int PARSE_BARCODE_FAIL = 300;

    private static final int PARSE_BARCODE_SUC = 200;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Toolbar mToolbar;

    private Rect mCropRect = null;

    private boolean isFlashlightOpen;

    private DBUserEntity mUserEntity;

    private LinearLayout mMyQRcodeLl;

    private int hasScanSys;


    /**
     * 图片的路径
     */
    private String photoPath;

    private Handler mHandler = new MyHandler(this);

    static class MyHandler extends Handler {

        private WeakReference<Activity> activityReference;

        public MyHandler(Activity activity) {
            activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PARSE_BARCODE_SUC: // 解析图片成功
                    Toast.makeText(activityReference.get(),
                            "解析成功，结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case PARSE_BARCODE_FAIL:// 解析图片失败
                    Toast.makeText(activityReference.get(), "解析图片失败",
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void initData() {
        hasScanSys = getIntent().getIntExtra("hasScanSys", 0);
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);

        scanPreview = (SurfaceView) findViewById(com.jinlin.zxing.example.camera.R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(com.jinlin.zxing.example.camera.R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(com.jinlin.zxing.example.camera.R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(com.jinlin.zxing.example.camera.R.id.capture_scan_line);
        mMyQRcodeLl = (LinearLayout) findViewById(R.id.ll_my_qrcode);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        if(hasScanSys==0){
            mMyQRcodeLl.setVisibility(View.GONE);
        }

        mMyQRcodeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar",mUserEntity.getAvatar());
                bundle.putString("nickName",mUserEntity.getNickname());
                bundle.putString("qrcode", mUserEntity.getQrcode());
                startActivity(MyQRCodeActivity.class,bundle);
            }
        });
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
        inactivityTimer.shutdown();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
//        Intent intent = new Intent();
//        bundle.putInt("width", mCropRect.width());
//        bundle.putInt("height", mCropRect.height());
//        bundle.putString("result", rawResult.getText());
//        intent.putExtras(bundle);
//        setResult(RESULT_OK, intent);
        String result = rawResult.getText();
        checkQRCode(result);
        finish();
//        startActivity(new Intent(CaptureActivity.this, ResultActivity.class).putExtras(bundle));
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(com.jinlin.zxing.example.camera.R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(4, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == RESULT_OK) {
            final ProgressDialog progressDialog;
            switch (requestCode) {
                case REQUEST_CODE:

                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(
                            intent.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("正在扫描...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Bitmap img = BitmapUtils
                                    .getCompressedBitmap(photoPath);

                            BitmapDecoder decoder = new BitmapDecoder(
                                    ScanQRCodeActivityReplace.this);
                            Result result = decoder.getRawResult(img);

                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = ResultParser.parseResult(result)
                                        .toString();
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                mHandler.sendMessage(m);
                            }
                            progressDialog.dismiss();
                        }
                    }).start();
                    break;
            }
        }
    }

    /**
     * 更新二维码页面
     */
    private void updateQRView(String result) {
//        int width = data.getExtras().getInt("width");
//        int height = data.getExtras().getInt("height");
//
//        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(width, height);
//        lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
//                .getDisplayMetrics());
//        lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
//                .getDisplayMetrics());
//        lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
//                .getDisplayMetrics());
//        mResultImage.setLayoutParams(lps);

//        String result = data.getExtras().getString("result");

//        Bitmap barcode = null;
//        byte[] compressedBitmap = data.getExtras().getByteArray(DecodeThread.BARCODE_BITMAP);
//        if (compressedBitmap != null) {
//            barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
//            // Mutable copy:
//            barcode = barcode.copy(Bitmap.Config.RGB_565, true);
//        }

//        mResultImage.setImageBitmap(barcode);

        checkQRCode(result);
    }

    /**
     * 通过服务器获取二维码用途
     */
    public void checkQRCode(String result) {
        mDialogBuilder.showProgressDialog(mContext, "正在识别..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_PARSE_QRCODE;
        RequestParams params = RequestParamsBuild.buildParseQrcodeRequest(mContext, result);
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
    @OnClick({R.id.ll_my_qrcode})
    public void onClick(View v) {
        switch (v.getId()){
            //开启二维码界面
            case R.id.ll_my_qrcode:
                Bundle bundle = new Bundle();
                bundle.putString("avatar",mUserEntity.getAvatar());
                bundle.putString("nickName",mUserEntity.getNickname());
                bundle.putString("qrcode", mUserEntity.getQrcode());
                bundle.putInt("hasSys", 1);
                startActivity(MyQRCodeActivity.class, bundle);
                break;
        }
//        final int id = v.getId();
//         图片识别
//        if (id == R.id.capture_scan_photo) {
//            // 打开手机中的相册
//            Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
//            innerIntent.setType("image/*");
//            Intent wrapperIntent = Intent.createChooser(innerIntent,
//                    "选择二维码图片");
//            this.startActivityForResult(wrapperIntent, REQUEST_CODE);
//        } else if (id == R.id.capture_flashlight) {
//            if (isFlashlightOpen) {
//                cameraManager.setTorch(false); // 关闭闪光灯
//                isFlashlightOpen = false;
//            } else {
//                cameraManager.setTorch(true); // 打开闪光灯
//                isFlashlightOpen = true;
//            }
//        }
    }
}
