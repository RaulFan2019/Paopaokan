package com.app.pao.activity.main;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivityV2;
import com.app.pao.data.PreferencesData;
import com.app.pao.fragment.main.SplashFragment;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ViewHelper;
import com.rey.material.widget.Button;

/**
 * 引导页界面
 * 只在版本升级后显示
 * Created by Raul on 2015/9/28.
 */
@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseAppCompActivityV2 implements ViewPager.OnPageChangeListener, View.OnClickListener {

    /**
     * contains
     **/
    private static final String TAG = "SplashActivity";
    private static final int PAGER_NUM = 4;

    boolean isOpaque = true;

    private int mUserId;//用户ID
    private String mUserPassword;//用户密码

    /**
     * local view
     **/
    @ViewInject(R.id.viewpager)
    private ViewPager mVpager;//引导页划动控件
    @ViewInject(R.id.btn_splash)
    private Button mSplashBtn; //进入按钮

    /**
     * adapter
     */
    private SplashAdapter splashAdapter;

    private SplashFragment mSplashFragment1;
    private SplashFragment mSplashFragment2;
    private SplashFragment mSplashFragment3;
    private SplashFragment mSplashFragment4;

    /**
     * ViewPager 监听
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == PAGER_NUM - 2 && positionOffset > 0) {
            if (isOpaque) {
                mVpager.setBackgroundColor(Color.TRANSPARENT);
                isOpaque = false;
            }
        } else {
            if (!isOpaque) {
                mVpager.setBackgroundColor(getResources().getColor(R.color.primary_material_light));
                isOpaque = true;
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == PAGER_NUM - 1) {
            mSplashBtn.setVisibility(View.VISIBLE);
        } else {
            mSplashBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    @OnClick({R.id.btn_splash})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_splash:
                finish();
                launchMain();
                break;
        }
    }

    /**
     * back 按钮监听
     */
    @Override
    public void onBackPressed() {
        if (mVpager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mVpager.setCurrentItem(mVpager.getCurrentItem() - 1);
        }
    }

    /**
     * init data
     */
    @Override
    protected void initData() {
        splashAdapter = new SplashAdapter(getSupportFragmentManager());

        mUserId = PreferencesData.getUserId(getApplicationContext());
        mUserPassword = PreferencesData.getPassword(getApplicationContext());

        mSplashFragment1 = SplashFragment.newInstance(R.layout.fragment_splash_one);
        mSplashFragment2 = SplashFragment.newInstance(R.layout.fragment_splash_two);
        mSplashFragment3 = SplashFragment.newInstance(R.layout.fragment_splash_three);
        mSplashFragment4 = SplashFragment.newInstance(R.layout.fragment_splash_four);
    }

    @Override
    protected void initViews() {
        mVpager.setAdapter(splashAdapter);
        mVpager.setPageTransformer(true, new SlidePageTransformer());//设置过渡动画
        mVpager.addOnPageChangeListener(this);
    }

    @Override
    protected void doMyOnCreate() {
        getWindow().setBackgroundDrawable(null);

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        mVpager.removeOnPageChangeListener(this);
    }

    /**
     * 启动APP的主要内容
     */
    private void launchMain() {
        startActivity(AdActivity.class);
    }

    /**
     * ViewPager Adapter 内部类
     */
    class SplashAdapter extends FragmentStatePagerAdapter {

        public SplashAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mSplashFragment1;
                case 1:
                    return mSplashFragment2;
                case 2:
                    return mSplashFragment3;
                case 3:
                    return mSplashFragment4;
            }
            return mSplashFragment1;
        }

        @Override
        public int getCount() {
            return PAGER_NUM;
        }
    }

    /**
     * 定义滑动时逻辑动画 内部类
     */
    class SlidePageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            //初始化控件
            View backgroundView = page.findViewById(R.id.fl_splash);
            View titleText = page.findViewById(R.id.tv_splash_title);
            View explain = page.findViewById(R.id.tv_splash_explain);

            //向左滑动
            if (0 <= position && position < 1) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }

            //向右滑动
            if (-1 < position && position < 0) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }

            if (position <= -1.0f || position >= 1.0f) {

            } else if (position == 0.0f) {

            } else {
                if (backgroundView != null) {
                    ViewHelper.setAlpha(backgroundView, 1.0f - Math.abs(position));
                }

                if (titleText != null) {
                    ViewHelper.setTranslationX(titleText, pageWidth * position);
                    ViewHelper.setAlpha(titleText, 1.0f - Math.abs(position));
                }

                if (explain != null) {
                    ViewHelper.setTranslationX(explain, pageWidth * position);
                    ViewHelper.setAlpha(explain, 1.0f - Math.abs(position));
                }
            }

        }
    }

}
