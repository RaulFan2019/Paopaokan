package com.app.pao.activity.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.ClipImage.ClipZoomImageView;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/2/17.
 */
@ContentView(R.layout.activity_zoom_big_image)
public class ZoomBigImageActivity extends BaseAppCompActivity implements View.OnClickListener{

    /* contains */
    private static final int MSG_LOAD_IMAGE = 1;
    private static final int MSG_LOAD_IMAGE_F = 2;
    private static final int MSG_LOAD_IMAGE_EX = 3;

    /* local view */
    @ViewInject(R.id.cziv_zoom_big_image)
    private ClipZoomImageView mBigImageCziv;
    @ViewInject(R.id.ll_big_image_p)
    private LinearLayout mBigImageLl;

    /* local data */
    private String mImageUrl;
    private Bitmap mLoadBitmap;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_LOAD_IMAGE) {
                mBigImageCziv.setImageBitmap(mLoadBitmap);
            } else if (msg.what == MSG_LOAD_IMAGE_F) {
                mBigImageCziv.setImageResource(R.drawable.icon_user_photo);
//                T.showShort(mContext, "请检查你的网络");
            }else if(msg.what == MSG_LOAD_IMAGE_EX){
                T.showShort(mContext, "请检查你的网络");
            }

            if (mDialogBuilder.progressDialog != null) {
                mDialogBuilder.progressDialog.dismiss();
            }
        }
    };

    private Thread mLoadImageThread = new Thread() {
        @Override
        public void run() {
            super.run();
            getNetWorkBitmap(mImageUrl);
        }
    };

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
    protected void initData() {
        mImageUrl = getIntent().getExtras().getString("avatar");
    }

    @Override
    protected void initViews() {
//        mBigImageCziv = new ClipZoomImageView(mContext);
//        android.view.ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
//                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
//
//        mBigImageLl.addView(mBigImageCziv, lp);

//        mBigImageCziv.setHorizontalPadding(100);
//        mBigImageCziv.setImageResource(R.drawable.icon_user_photo);
//        BitmapUtils bitmapUtils = new BitmapUtils(mContext);
//        bitmapUtils.configDefaultLoadingImage(R.drawable.icon_user_photo);
//        bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon_user_photo);
//        bitmapUtils.display(mBigImageCziv, mImageUrl);
//        mBigImageCziv.setImageBitmap(getNetWorkBitmap(mImageUrl));
        mDialogBuilder.showProgressDialog(mContext, "正在加载图片", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {

            }

            @Override
            public void onCancel() {
                finish();
            }
        });
        //启动线程开始下载图片
        mLoadImageThread.start();
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
        if (handler != null) {
            handler.removeMessages(MSG_LOAD_IMAGE);
            handler.removeMessages(MSG_LOAD_IMAGE_F);
            handler.removeMessages(MSG_LOAD_IMAGE_EX);
            handler = null;
            mLoadImageThread.interrupt();
        }
    }

    /**
     * 开始获取图片
     *
     * @param urlString
     */
    public void getNetWorkBitmap(String urlString) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        if(urlString == null){
            handler.sendEmptyMessage(MSG_LOAD_IMAGE_F);
            return;
        }
        if(urlString.isEmpty()){
            handler.sendEmptyMessage(MSG_LOAD_IMAGE_F);
            return;
        }
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl
                    .openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (handler != null) {
                handler.sendEmptyMessage(MSG_LOAD_IMAGE_F);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (handler != null) {
                handler.sendEmptyMessage(MSG_LOAD_IMAGE_EX);
            }
        }

        mLoadBitmap = bitmap;
        if (handler != null) {
            handler.sendEmptyMessage(MSG_LOAD_IMAGE);
        }

    }


    @Override
    @OnClick(R.id.ll_big_image_p)
    public void onClick(View v) {
        if(v.getId() == R.id.ll_big_image_p){
            finish();
        }
    }
}
