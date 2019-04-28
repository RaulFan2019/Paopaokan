package com.app.pao.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.ui.widget.ClipImage.ClipImageLayout;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

/**
 * Created by Administrator on 2016/2/16.
 */
@ContentView(R.layout.activity_image_cut)
public class ImageCutActivity extends BaseAppCompActivity implements View.OnClickListener{

    @ViewInject(R.id.cil_cut_layout)
    private ClipImageLayout mCutIil;
    @ViewInject(R.id.btn_confirm_cut)
    private Button mConfirmCutBtn;

    private Uri mZoomImageUri;


    @Override
    @OnClick({R.id.btn_confirm_cut,R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm_cut:
                doCutImage();
                break;
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mZoomImageUri = getIntent().getData();
    }

    @Override
    protected void initViews() {
        if(mZoomImageUri != null) {
            mCutIil.setZoomImageUri(mZoomImageUri);
            mCutIil.setHorizontalPadding(120);
        }
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

    private void doCutImage(){
        Bitmap bitmap = mCutIil.clip();
        Bitmap resultBitmap = ImageUtils.compressBitmap(bitmap, 40);
        File f = FileUtils.saveBitmap(resultBitmap);
        Intent i = new Intent();
        Bundle b  = new Bundle();
        b.putString("data",f.getPath());
        i.putExtras(b);
        setResult(RESULT_OK,i);
        finish();
    }
}
