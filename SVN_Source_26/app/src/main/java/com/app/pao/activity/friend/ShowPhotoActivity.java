package com.app.pao.activity.friend;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 显示照片界面
 *
 * @author Raul
 */
@ContentView(R.layout.activity_show_photo)
public class ShowPhotoActivity extends BaseAppCompActivity {

    /**
     * contains
     **/
    private static final String TAG = "showPhotoFragment";
    private static final boolean DEBUG = false;

    /**
     * local data
     **/
    private String mImageUrl;
    private PhotoViewAttacher mAttacher;


    /**
     * local view
     **/
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.image)
    private PhotoView mImageView;


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
        setSupportActionBar(mToolbar);

        BitmapUtils bitmapUtils = new BitmapUtils(mContext);

        bitmapUtils.configDefaultLoadingImage(R.drawable.icon_user_photo);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon_user_photo);
        bitmapUtils.display(mImageView, mImageUrl);
        bitmapUtils.configDefaultAutoRotation(true);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setScaleType(ScaleType.CENTER_INSIDE);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                finish();
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

    }

}
