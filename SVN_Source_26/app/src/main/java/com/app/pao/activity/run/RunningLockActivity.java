package com.app.pao.activity.run;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.entity.event.EventRuningLocation;
import com.app.pao.entity.event.EventRuningTime;
import com.app.pao.ui.widget.SlideUnlockView;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 锁屏界面
 *
 * @author Raul
 */

@ContentView(R.layout.activity_running_lock)
public class RunningLockActivity extends Activity implements OnClickListener {

    /**
     * local data
     **/
    private long exitTime = 0;

    //	private PowerManager pm;
    //	private WakeLock mWakelock;
    //声明键盘管理器
    KeyguardManager mKeyguardManager = null;
    //声明键盘锁
    private KeyguardManager.KeyguardLock mKeyguardLock = null;

    /**
     * local view
     **/
    @ViewInject(R.id.ll_lock)
    private LinearLayout mLockLl;
    // @ViewInject(R.id.tv_running_map_timespeed_value_min)
    // private TextView mTimeSpeedMinTv;
    // @ViewInject(R.id.tv_running_map_timespeed_value_sec)
    // private TextView mTimeSpeedSecTv;
    @ViewInject(R.id.tv_running_map_timespeed_value)
    private TextView mTimeSpeedTv;
    @ViewInject(R.id.tv_running_map_time)
    private TextView mTImeTv;
    @ViewInject(R.id.tv_running_map_length)
    private TextView mLengthTv;
    @ViewInject(R.id.slideUnlockView)
    private SlideUnlockView mSlideLock;

//    private RunningReceiver mRunningReceiver;
//    private IntentFilter Runningfilter;
//

    /**
     * on Create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//		final Window win = getWindow();
//		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        ViewUtils.inject(this);
        ActivityStackManager.getAppManager().addActivity(this);
//		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,
//				"SimpleTimer");
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        long time = getIntent().getLongExtra("time", 0);
        float length = getIntent().getFloatExtra("length", 0.0f);
        int speed = getIntent().getIntExtra("speed", 0);

        mSlideLock.setOnUnLockListener(new SlideUnlockView.OnUnLockListener() {
            @Override
            public void setUnLocked(boolean unLock) {
                // 如果是true，证明解锁
                if (unLock) {
                    // 启动震动器 100ms
//                    vibrator.vibrate(100);
                    // 当解锁的时候，执行逻辑操作，在这里仅仅是将图片进行展示
//                    imageView.setVisibility(View.VISIBLE);
                    // 重置一下滑动解锁的控件
//                    mSlideLock.reset();
                    // 让滑动解锁控件消失
//                    slideUnlockView.setVisibility(View.GONE);
                    //初始化键盘锁，可以锁定或解开键盘锁
                    mKeyguardLock = mKeyguardManager.newKeyguardLock("");
                    //禁用显示键盘锁定
                    mKeyguardLock.disableKeyguard();

                    Intent i = new Intent(RunningLockActivity.this, RunningActivityV2.class);
                    startActivity(i);
                    finish();

                }
            }
        });

        if (mTImeTv == null) {
            return;
        }
        // 时间
        mTImeTv.setText(TimeUtils.formatDurationStr(time));

        // 速度
        String speedValues = TimeUtils.formatSecondsToSpeedTime(speed);
        mTimeSpeedTv.setText(speedValues);

        // 距离
        float lengthtemp = length / 1000.0f;
        BigDecimal lengthb = new BigDecimal(lengthtemp);
        mLengthTv.setText(lengthb.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "");

        mLockLl.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    /**
     * on Click
     */
    @Override
    @OnClick({R.id.btn_lock_close})
    public void onClick(View v) {
        if (v.getId() == R.id.btn_lock_close) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //若APP从后台到前台
        if (!LocalApplication.getInstance().isActive) {
            LocalApplication.getInstance().isActive = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isAppOnForeground()) {
            //app 进入后台
            LocalApplication.getInstance().isActive = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ActivityStackManager.getAppManager().finishActivity(this);
    }

    /**
     * 接收到时间
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRuningTime event) {
        long time = event.getTime();
        if (mTImeTv == null) {
            return;
        }
        if (time != -1) {
            mTImeTv.setText(TimeUtils.formatDurationStr(time));
        }
    }

    /**
     * 收到位置变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRuningLocation event) {
        int speed = event.getSpeed();
        if (mTimeSpeedTv == null) {
            return;
        }
        String speedValues = TimeUtils.formatSecondsToSpeedTime(speed);
        mTimeSpeedTv.setText(speedValues);
        // mTimeSpeedMinTv.setText(speed / 60 + "");
        // mTimeSpeedSecTv.setText(speed % 60 + "");
        // 距离
        float lengthtemp = event.getLength() / 1000.0f;
        BigDecimal lengthb = new BigDecimal(lengthtemp);
        mLengthTv.setText(lengthb.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "");
    }

    /**
     * 按2次退出本页面
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次进入跑步页面", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //初始化键盘锁，可以锁定或解开键盘锁
            mKeyguardLock = mKeyguardManager.newKeyguardLock("");
            //禁用显示键盘锁定
            mKeyguardLock.disableKeyguard();

            Intent i = new Intent(RunningLockActivity.this, RunningActivityV2.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
