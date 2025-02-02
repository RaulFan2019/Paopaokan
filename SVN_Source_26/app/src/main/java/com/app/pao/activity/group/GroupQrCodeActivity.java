package com.app.pao.activity.group;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ScanQRCodeActivityReplace;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.PageLiaghtUtils;
import com.app.pao.utils.QrCodeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

/**
 * Created by Raul on 2015/12/8.
 * 二维码界面
 */
@ContentView(R.layout.activity_group_qrcode)
public class GroupQrCodeActivity extends BaseAppCompActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.iv_user_avatar)
    private ImageView mAvatarIv;
    @ViewInject(R.id.tv_user_nickname)
    private TextView mNicknameTv;
    @ViewInject(R.id.iv_qrcode)
    private ImageView mQRCodeIv;
    @ViewInject(R.id.ll_my_qrcode_sys)
    private LinearLayout mQRCodeSys;

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
        ImageUtils.loadUserImage( mAvatar, mAvatarIv);
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

    @OnClick({R.id.ll_my_qrcode_sys})
    public void onClick(View v) {
        if(v.getId() == R.id.ll_my_qrcode_sys){
            Bundle bundle = new Bundle();
            bundle.putInt("hasScanSys",0);
            startActivity(ScanQRCodeActivityReplace.class,bundle);
        }
    }
}
