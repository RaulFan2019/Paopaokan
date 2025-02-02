package com.app.pao.activity.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ScanQRCodeActivityReplace;
import com.app.pao.activity.main.ScanQrCodeActivity;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.PageLiaghtUtils;
import com.app.pao.utils.QrCodeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

/**
 * Created by LY on 2016/4/5.
 */
@ContentView(R.layout.activity_my_qrcode)
public class MyQRCodeActivity extends BaseAppCompActivity implements View.OnClickListener {
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.iv_user_avatar)
    private ImageView mAvatarIv;//头像
    @ViewInject(R.id.tv_user_nickname)
    private TextView mNicknameTv;//昵称
    @ViewInject(R.id.iv_qrcode)
    private ImageView mQRCodeIv;//二维码
    @ViewInject(R.id.ll_my_qrcode_sys)
    private LinearLayout mQRCodeSys;//扫一扫按钮

    private String mAvatar;
    private String mNickname;
    private String mQRCode;
    private int hasSys;
    private BitmapUtils mBitmapUtils;

    @Override
    protected void initData() {
        mBitmapUtils = new BitmapUtils(mContext);
        mAvatar = getIntent().getStringExtra("avatar");
        mNickname = getIntent().getStringExtra("nickName");
        mQRCode = getIntent().getStringExtra("qrcode");
        hasSys = getIntent().getIntExtra("hasSys",1);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        ImageUtils.loadUserImage(mAvatar,mAvatarIv);
        mNicknameTv.setText(mNickname);
        //绘制二维码
        if (mQRCode != null && !mQRCode.isEmpty()) {
            mQRCodeIv.setImageBitmap(QrCodeUtils.create2DCode(mQRCode));
        }
        if(hasSys==0){
            mQRCodeSys.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重写Toolbar方法
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onResume() {
        super.onResume();
        PageLiaghtUtils.changeToLightMax(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PageLiaghtUtils.backToLight(mContext);
    }

    @Override
    @OnClick({R.id.ll_my_qrcode_sys})
    public void onClick(View v) {
        if(v.getId() == R.id.ll_my_qrcode_sys){
            Bundle bundle = new Bundle();
            bundle.putInt("hasScanSys",0);
            startActivity(ScanQRCodeActivityReplace.class,bundle);
        }
    }
}
