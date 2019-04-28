package com.app.pao.activity.run;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.RaceWebViewActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.data.PreferencesData;
import com.app.pao.ui.widget.SwitchView;
import com.app.pao.utils.TTsUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Raul on 2015/12/13.
 * 跑步设置
 */
@ContentView(R.layout.activity_running_settings)
public class RunSettingsActivity extends BaseAppCompActivity implements View.OnClickListener {

    private static final String TAG = "RunSettingsActivity";

    /* local data */
    private int phoneType;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.sv_tts)
    private SwitchView mTTsSv;//语音设置按钮
    @ViewInject(R.id.sv_live)
    private SwitchView mLiveSv;//是否直播按钮
    @ViewInject(R.id.sv_voice)
    private SwitchView mVoiceSv;//是否直播按钮
    @ViewInject(R.id.ll_live)
    private LinearLayout mLiveLl;//直播按钮栏位
    @ViewInject(R.id.ll_voice)
    private LinearLayout mVoiceLl;//实时语音栏
    @ViewInject(R.id.sv_loc_mode)
    private SwitchView mLocModeSv;//定位模式选择

    /*q layout*/
    @ViewInject(R.id.ll_q_base)
    private LinearLayout mQBaseLl;
    @ViewInject(R.id.tv_q)
    private TextView mQTv;

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
        phoneType = PreferencesData.getPhoneType(mContext);
//        phoneType = AppEnum.PhoneType.HUAWEI;
    }

    @Override
    @OnClick(R.id.ll_q)
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (v.getId() == R.id.ll_q) {
            if (phoneType == AppEnum.PhoneType.HUAWEI) {
                bundle.putString("url", "http://mp.weixin.qq.com/s?__biz=MzI2ODA2MzI4NA==&mid=501535135&idx=1&sn=d4102ef04d098fda4521ab3f2352cf42&scene=1&srcid=0622F4C7a4jKL2uNKHb2IhHk#wechat_redirect");
            } else if (phoneType == AppEnum.PhoneType.XIAOMI) {
                bundle.putString("url", "http://mp.weixin.qq.com/s?__biz=MzI2ODA2MzI4NA==&mid=501535149&idx=1&sn=c63a2a583d0a047abf2642a066357fd1&scene=1&srcid=0622mE2k7Nc9GfAHCLoPBt88#wechat_redirect");
            } else if (phoneType == AppEnum.PhoneType.OPPO) {
                bundle.putString("url", "http://mp.weixin.qq.com/s?__biz=MzI2ODA2MzI4NA==&mid=501535158&idx=1&sn=d2e6ac045f94030fbd320d242de8731c&scene=1&srcid=0622wU2r2TWEh868P3EWnPbQ#wechat_redirect");
            }
            bundle.putInt("type", AppEnum.WebViewType.AdViewInto);
//            TTsUtils.perKm(RunSettingsActivity.this,234,54345,253);
//            TTsUtils.playHeartbeat(RunSettingsActivity.this,122);
            startActivity(RaceWebViewActivity.class, bundle);
        }
    }


    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);

        if (phoneType == AppEnum.PhoneType.HUAWEI
                || phoneType == AppEnum.PhoneType.XIAOMI
                || phoneType == AppEnum.PhoneType.OPPO) {
            mQBaseLl.setVisibility(View.VISIBLE);
            if (phoneType == AppEnum.PhoneType.HUAWEI) {
                mQTv.setText("华为手机跑步中为什么直播会断掉?");
            } else if (phoneType == AppEnum.PhoneType.XIAOMI) {
                mQTv.setText("小米手机跑步中为什么会丢点?");
            } else if (phoneType == AppEnum.PhoneType.OPPO) {
                mQTv.setText("OPPO手机跑步中为什么直播会断掉?");
            }
        }

        mTTsSv.setOpened(PreferencesData.getVoiceEnable(mContext));
        mLiveSv.setOpened(PreferencesData.getLiveEnable(mContext));
        if (!PreferencesData.getLiveEnable(mContext)) {
            mVoiceSv.setCanSwitch(false);
        }
        mVoiceSv.setOpened(PreferencesData.getRunningVoiceEnable(mContext));
        mLocModeSv.setOpened(PreferencesData.getOnlyIsGpsLocMode(mContext));
        mTTsSv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                mTTsSv.setOpened(true);
                PreferencesData.setVoiceEnable(mContext, true);
            }

            @Override
            public void toggleToOff(View view) {
                mTTsSv.setOpened(false);
                PreferencesData.setVoiceEnable(mContext, false);
            }
        });

        if (LocalApplication.getInstance().getLoginUser(mContext).getUserId() == AppEnum.DEFAULT_USER_ID) {
            mLiveLl.setVisibility(View.GONE);
            mVoiceLl.setVisibility(View.GONE);
            return;
        }

        mLiveSv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                mLiveSv.setOpened(true);
                mVoiceSv.setOpened(true);
                mVoiceSv.setCanSwitch(true);
                PreferencesData.setLiveEnable(mContext, true);
                PreferencesData.setRunningVoiceEnable(mContext, true);
            }

            @Override
            public void toggleToOff(View view) {
                mLiveSv.setOpened(false);
                mVoiceSv.setOpened(false);
                mVoiceSv.setCanSwitch(false);
                PreferencesData.setLiveEnable(mContext, false);
                PreferencesData.setRunningVoiceEnable(mContext, false);
            }
        });

        mVoiceSv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                mVoiceSv.setOpened(true);
                PreferencesData.setRunningVoiceEnable(mContext, true);
            }

            @Override
            public void toggleToOff(View view) {
                mVoiceSv.setOpened(false);
                PreferencesData.setRunningVoiceEnable(mContext, false);
            }
        });
        mLocModeSv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                mLocModeSv.setOpened(true);
                PreferencesData.setOnlyIsGpsLocMode(mContext, true);
            }

            @Override
            public void toggleToOff(View view) {
                mLocModeSv.setOpened(false);
                PreferencesData.setOnlyIsGpsLocMode(mContext, false);
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
