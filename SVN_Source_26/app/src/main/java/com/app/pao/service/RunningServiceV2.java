package com.app.pao.service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.run.RunningActivityV2;
import com.app.pao.activity.run.RunningLockActivity;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.RunningData;
import com.app.pao.data.db.HeartrateData;
import com.app.pao.data.db.LapData;
import com.app.pao.data.db.LocationData;
import com.app.pao.data.db.OriginalPositionData;
import com.app.pao.data.db.SpliteData;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.db.DBEntityLap;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntityOriginalPosition;
import com.app.pao.entity.db.DBEntitySplite;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventNewWorkout;
import com.app.pao.entity.event.EventRuningHeartrate;
import com.app.pao.entity.event.EventRuningLocation;
import com.app.pao.entity.event.EventRuningTime;
import com.app.pao.entity.event.EventRunningComment;
import com.app.pao.entity.event.EventRunningThumb;
import com.app.pao.utils.CalorieUtils;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.EventUtils;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.GpsUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.TTsUtils;
import com.app.pao.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.hwh.sdk.ble.BleManagerService;
import cn.hwh.sdk.ble.EventBleConnect;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul.Fan on 2016/9/11.
 */
public class RunningServiceV2 extends Service implements AMapLocationListener {

    /* contains */
    private static final String TAG = "RunningServiceV2";

    // 跑步命令
    public static final int CMD_PAUSE = 1;// 暂停命令
    public static final int CMD_CONTINUE = 2;// 继续命令
    public static final int CMD_FINISH = 3;// 结束命令

    //通知相关
    private static final Class<?>[] mSetForegroundSignature = new Class[]{
            boolean.class};
    private static final Class<?>[] mStartForegroundSignature = new Class[]{
            int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[]{
            boolean.class};
    private static final int NOTIFICATION_ID = 1;
    private static boolean mReflectFlg = false;

    //间隔时间参数
    private static final int alarmWakeUpInterval = 15 * 60 * 1000;
    private static final int alarmSaveUploadInterval = 60 * 1000;
    private static final int alarmCheckDaemonInterval = 15 * 1000;
    private static final int heartbeatVoiceInterval = 2 * 60;//心率播报频率(秒)

    private static final int MSG_WAKE_UP = 0x11;
    private static final int MSG_SAVE_UPLOAD = 0x12;
    private static final int MSG_CHECK_DAEMON = 0x13;

    //GPS获取参数
    private static final int GPSInterval = 2000;//获取GPS信息时间频率 单位:毫秒
    private static final int GpsAccuracy = 250;//精度控制
    private static final int LIMIT_MIN_PACE = 50;//速度过滤参数(最快速度)

    /* local data */

    /* 通知相关 */
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    private NotificationManager mNM;

    //计时器
    private static Handler mTimerHandler = null;
    private static Runnable mTimerRa = null;
    private static long mStartTime;
    private static long NextTime = 0;

    /* 定位相关 */
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption = null;
    private LatLng mLastLLatLng;// 用于计算距离

    //当前状态参数
    private boolean isRunning = false;
    private float mCalorie = 0; // 消耗的卡路里值
    public long mRunningTime = 0;// 跑步用时
    public float mRunningLength = 0;// 跑步距离
    private int mPace;// 可靠速度
    private long mLastTimeForPace;// 用于计算速度的时间
    private int mLocSizeInMin = 0;//每分钟的获取点的数量
    private int mNullLocationTime = 0;
    private int mNetworkGpsCount = 0;

    //蓝牙状态
    private int mCurrHeartbeat = 0;//当前心率
    private int mCurrCadence = 0;
    private String mConnectDeviceMac;
    private int mHeartbeatSaveSize = 0;// 每3个心率保存一次

    // 跑步历史参数
    private DBUserEntity mUserEntity;//用户信息
    private DBEntityWorkout mDBWorkout; // 数据库中保存的跑步历史

    // lap参数
    private long mLapTime = 0;// lap 用时
    private float mLapLength = 0;// lap 距离
    private DBEntityLap mDBLap;// 数据库lap信息

    // split参数
    private float mSplitLength = 0;
    private long mSplitTime = 0;
    private DBEntitySplite mDBSplit;

    /* 播放相关 */
    private MediaPlayer mVoicePlayer;
    private ArrayList<String> mVoiceList = new ArrayList<>();
    private boolean playVoiceNow = false;

    //接收器
    private RunningReceiver mLockReceiver;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CHECK_DAEMON:
                    x.task().post(new Runnable() {
                        @Override
                        public void run() {
                            checkDaemon();
                        }
                    });
                    break;
                case MSG_SAVE_UPLOAD:
                    x.task().post(new Runnable() {
                        @Override
                        public void run() {
                            saveUploadInfoData();
                        }
                    });
                    break;
                case MSG_WAKE_UP:
                    x.task().post(new Runnable() {
                        @Override
                        public void run() {
                            brightKeyguard();
                        }
                    });
                    break;
            }
        }
    };

    /**
     * 广播接收器
     */
    class RunningReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.v(TAG, "RunningLockReceiver onReceive");
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                launchLockActivity(context);
            }
        }
    }


    /**
     * 收到跑步的消息或命令
     */
    Handler runningHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 收到暂停
                case CMD_PAUSE:
                    isRunning = false;
                    mTimerHandler.removeCallbacks(mTimerRa);
                    mHandler.removeMessages(MSG_SAVE_UPLOAD);
                    stopLocation();
                    finishLap();
                    break;
                // 收到继续
                case CMD_CONTINUE:
                    isRunning = true;
                    mStartTime = System.currentTimeMillis();
                    mTimerRa.run();
                    mHandler.sendEmptyMessage(MSG_SAVE_UPLOAD);
                    initGps();
                    if (mDBLap.status == AppEnum.RunningStatus.FINISH) {
                        mDBLap = RunningData.createNewLap(RunningServiceV2.this, mDBWorkout.starttime, mUserEntity.userId);
                        mLapTime = 0;
                    }
                    break;
                // 收到完成
                case CMD_FINISH:
                    resumeOtherService();
                    unregisterReceiver(mLockReceiver);
                    mHandler.removeMessages(MSG_WAKE_UP);
                    mHandler.removeMessages(MSG_CHECK_DAEMON);
                    mVoicePlayer.release();
                    finishWorkout();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 接收到新的ble数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBleConnect event) {
        if (event.msg == BleManagerService.MSG_NEW_HEARTBEAT) {
            mHeartbeatSaveSize++;
            mCurrHeartbeat = event.heartbeat;
            mCurrCadence = event.cadence;
            EventUtils.sendForegroundEvent(new EventRuningHeartrate(BleManagerService.MSG_NEW_HEARTBEAT, mCurrHeartbeat));
            if (mHeartbeatSaveSize > 2 && isRunning) {
                DBEntityHeartrate heartrateEntity = new DBEntityHeartrate(System.currentTimeMillis(),
                        mDBWorkout.name, mDBLap.lapId, mDBSplit.getId(), mRunningTime / 1000, mCurrHeartbeat,
                        AppEnum.UploadStatus.START, event.cadence);
                HeartrateData.saveHeartrate(RunningServiceV2.this, heartrateEntity);
                mHeartbeatSaveSize = 0;
                if (mDBWorkout.startStep == -1) {
                    mDBWorkout.startStep = event.stepCount;
                }
                if (mDBWorkout.endStep == -1) {
                    mDBWorkout.endStep = event.stepCount;
                }
            }
        } else if (event.msg == BleManagerService.MSG_FIND_CONNECT) {
            if (event.connectEntity == null) {
                //断开之前的连接
                Intent clearIntent = new Intent(RunningServiceV2.this, BleManagerService.class);
                clearIntent.putExtra("CMD", BleManagerService.CMD_CLEAR_ALL);
                startService(clearIntent);
                //增加一个新的连接
                Intent connectIntent = new Intent(RunningServiceV2.this, BleManagerService.class);
                connectIntent.putExtra("CMD", BleManagerService.CMD_ADD_NEW_CONNECT);
                connectIntent.putExtra("address", mConnectDeviceMac);
                startService(connectIntent);
            }
        }
    }

    /**
     * 接收到workoutid信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventNewWorkout event) {
        if (mDBWorkout.starttime.equals(event.workoutname)) {
            mDBWorkout.setWorkoutId(event.id);
            WorkoutData.updateWorkout(RunningServiceV2.this, mDBWorkout);
        }
    }

    /**
     * 收到点赞
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRunningThumb event) {
        if (PreferencesData.getRunningVoiceEnable(RunningServiceV2.this)) {
            mVoiceList.add("thumb");
            if (!playVoiceNow) {
                playVoice();
            }
        }
    }

    /**
     * 收到语音消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRunningComment event) {
//        Log.v(TAG, "EventRunningComment");
        if (PreferencesData.getRunningVoiceEnable(RunningServiceV2.this)) {
            for (int i = 0; i < event.commentsEntities.size(); i++) {
                mVoiceList.add(event.commentsEntities.get(i).getMediaurl());
            }
            if (!playVoiceNow) {
                playVoice();
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (!isRunning) {
            return;
        }
        //保存原始数据
        DBEntityOriginalPosition originalPosition = new DBEntityOriginalPosition(System.currentTimeMillis(),
                mDBWorkout.getStarttime(), location.getLatitude(), location.getLongitude(), location.getAccuracy(),
                mRunningTime / 1000, location.getErrorCode(), AppEnum.UploadStatus.START);
        OriginalPositionData.saveLocation(RunningServiceV2.this, originalPosition);
        //定位不成功，扔掉
        if (location.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
            return;
        }
        //缓存定位结果扔掉
        if (location.getLocationType() == AMapLocation.LOCATION_TYPE_FIX_CACHE) {
            return;
        }
        //若定位来源是基站，并且定位数量小于2，那么扔掉
        if (location.getLocationType() == AMapLocation.LOCATION_TYPE_CELL) {
            mNetworkGpsCount++;
            if (mNetworkGpsCount < 2) {
                return;
            }
        } else {
            mNetworkGpsCount = 0;
        }

        if (location != null && isRunning) {
            if (location.getLatitude() > 10
                    && location.getLongitude() > 10
                    && location.getAccuracy() < GpsAccuracy) {
                updateLocation(location);
            }
        }
    }

    /**
     * 初始化
     */
    @Override
    public void onCreate() {
        super.onCreate();
        bindNotification();
        destroyOtherService();
        initRunParams();
        initAlarmData();
        mHandler.sendEmptyMessage(MSG_WAKE_UP);
        mHandler.sendEmptyMessage(MSG_CHECK_DAEMON);
        initVoicePlayer();
        initRunningReceiver();
        initBleManager();
        EventBus.getDefault().register(this);
        //记录跑步开始的状态
        writeRunningLog("RUNNING SERVICE START\n");
    }

    /**
     * on Start Command
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int cmd = CMD_CONTINUE;
        if (intent != null && intent.hasExtra("CMD")) {
            cmd = intent.getIntExtra("CMD", CMD_CONTINUE);
        }
        runningHandler.sendEmptyMessage(cmd);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mDBWorkout.status != AppEnum.RunningStatus.RUNNING) {
            Intent intent = new Intent();
            intent.setAction("listener");
            sendBroadcast(intent);
        } else {
            //取消前台通知
            stopForegroundCompat(NOTIFICATION_ID);
            EventBus.getDefault().unregister(this);
            //结束的时候发送状态日志
            writeRunningLog("DESTROY RUNNING SERVICE\n");
        }
        super.onDestroy();
    }

    /**
     * 服务邦定通知服务
     */
    private void bindNotification() {
        mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mStartForeground = RunningServiceV2.class.getMethod("startForeground", mStartForegroundSignature);
            mStopForeground = RunningServiceV2.class.getMethod("stopForeground", mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground = mStopForeground = null;
        }
        try {
            mSetForeground = getClass().getMethod("setForeground",
                    mSetForegroundSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "OS doesn't have Service.startForeground OR Service.setForeground!");
        }
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, RunningActivityV2.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("123Sports");
        builder.setContentText("正在采集Gps信息..");
        Notification notification = builder.build();

        startForegroundCompat(NOTIFICATION_ID, notification);
    }


    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
        if (mReflectFlg) {
            // If we have the new startForeground API, then use it.
            if (mStartForeground != null) {
                mStartForegroundArgs[0] = Integer.valueOf(id);
                mStartForegroundArgs[1] = notification;
                invokeMethod(mStartForeground, mStartForegroundArgs);
                return;
            }

            // Fall back on the old API.
            mSetForegroundArgs[0] = Boolean.TRUE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
            mNM.notify(id, notification);
        } else {
            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法startForeground设置前台运行，
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground设置前台运行 */
            if (Build.VERSION.SDK_INT >= 5) {
                startForeground(id, notification);
            } else {
                // Fall back on the old API.
                mSetForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
                mNM.notify(id, notification);
            }
        }
    }

    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    void stopForegroundCompat(int id) {
        if (mReflectFlg) {
            // If we have the new stopForeground API, then use it.
            if (mStopForeground != null) {
                mStopForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mStopForeground, mStopForegroundArgs);
                return;
            }

            // Fall back on the old API.  Note to cancel BEFORE changing the
            // foreground state, since we could be killed at that point.
            mNM.cancel(id);
            mSetForegroundArgs[0] = Boolean.FALSE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
        } else {
            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法stopForeground停止前台运行，
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground停止前台运行 */

            if (Build.VERSION.SDK_INT >= 5) {
                stopForeground(true);
            } else {
                // Fall back on the old API.  Note to cancel BEFORE changing the
                // foreground state, since we could be killed at that point.
                mNM.cancel(id);
                mSetForegroundArgs[0] = Boolean.FALSE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
            }
        }
    }

    /**
     * 和通知相关的 API
     **/
    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
//            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.
//            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }

    /**
     * 保存地址信息
     *
     * @param location
     */
    private void updateLocation(AMapLocation location) {
        double orgLa = location.getLatitude();
        double orgLo = location.getLongitude();
        // 若上次位置信息不为空
        if (mLastLLatLng != null) {
            float tempLength = AMapUtils.calculateLineDistance(mLastLLatLng,
                    new LatLng(location.getLatitude(), location.getLongitude()));

            // 保存splite海拔信息
            if (location.hasAltitude()) {
                double Altitude = location.getAltitude();
                if (Altitude > mDBSplit.getMaxaltitude()) {
                    mDBSplit.setMaxaltitude(Altitude);
                }
                if (Altitude < mDBSplit.getMinaltitude()) {
                    mDBSplit.setMinaltitude(Altitude);
                }
                // 保存跑步历史海拔信息
                if (Altitude > mDBWorkout.getMaxHeight()) {
                    mDBWorkout.setMaxHeight(Altitude);
                }
                if (Altitude < mDBWorkout.getMinHeight()) {
                    mDBWorkout.setMaxHeight(Altitude);
                }
            }
            int lPace = 0;
            if (tempLength == 0) {
                lPace = 0;
            } else {
                lPace = (int) ((mRunningTime - mLastTimeForPace) / tempLength);
            }
//            }
            //速度不符合正常速度的
            if (lPace < LIMIT_MIN_PACE) {
                lPace = 0;
            }
//            若速度是0 不做任何记录
            if (lPace == 0) {
                return;
            }
            mPace = lPace;

            // 保存速度信息
            mRunningLength += tempLength;
            mLapLength += tempLength;
            mSplitLength += tempLength;
            mLastTimeForPace = mRunningTime;

            // 保存距离信息
            mDBWorkout.setLength(mRunningLength);
            mDBLap.setLength(mLapLength);
        }

        // 保存split信息
        if (mDBSplit.getStatus() == AppEnum.RunningStatus.RUNNING) {
            if (mSplitLength < 1000) {
                mDBSplit.setLength(mSplitLength);
                mDBSplit.setDuration(mSplitTime / 1000);
                SpliteData.updateSplite(RunningServiceV2.this, mDBSplit);
                // 分段结束
            } else {
                saveSplit(mLastLLatLng, location);
            }
        }
        mLastLLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mDBWorkout.setDuration(mRunningTime / 1000);
        mDBLap.setDuration(mLapTime / 1000);
        WorkoutData.updateWorkout(RunningServiceV2.this, mDBWorkout);
        LapData.updateLap(RunningServiceV2.this, mDBLap);

        // 保存location信息到DB
        DBEntityLocation templocation = new DBEntityLocation(System.currentTimeMillis(), mDBWorkout.starttime,
                mDBLap.starttime, location.getLatitude(), location.getLongitude(), location.getAltitude(),
                location.getAccuracy(), location.getAccuracy(), (mLapTime / 1000), mPace,
                AppEnum.UploadStatus.START, orgLa, orgLo);
        LocationData.saveLocation(RunningServiceV2.this, templocation);

        EventUtils.sendForegroundEvent(new EventRuningLocation(mRunningLength, mPace));
    }

    /**
     * 保存分段
     *
     * @param LastLLatLng
     * @param location
     */
    private void saveSplit(LatLng LastLLatLng, AMapLocation location) {
        int splitSize = (int) (mSplitLength / 1000);
        long time = (long) (mSplitTime * 1000 / mSplitLength);
        double LatitudeChange = (location.getLatitude() - LastLLatLng.latitude) / splitSize;
        double LongitudeChange = (location.getLongitude() - LastLLatLng.longitude) / splitSize;
        for (int i = 0; i < splitSize; i++) {
            finishSplit(mDBSplit, 1000, time, (LatitudeChange * (i + 1) + LastLLatLng.latitude),
                    (LongitudeChange * (i + 1) + LastLLatLng.longitude), false);
            long lastSplitId = mDBSplit.getId();
            mDBSplit = new DBEntitySplite(System.currentTimeMillis(), (lastSplitId + 1),
                    mDBWorkout.getName(), 0, 0, 0, 0.00d, 0.00d, AppEnum.RunningStatus.RUNNING, 0, 0);
            mSplitTime = mSplitTime - time;
            mSplitLength = mSplitLength - 1000;
        }
        // Log.v(TAG, "创建新的splite信息");
        SpliteData.saveSplite(RunningServiceV2.this, mDBSplit);
    }

    /**
     * 结束split
     */
    private void finishSplit(DBEntitySplite DBSplit, float length, long time,
                             double Latitude, double Longitude, boolean lastSplit) {
        // Log.v(TAG, "结束splite");
        int avgHeartRate = HeartrateData.getAvgheartrateInSplite(RunningServiceV2.this, mDBWorkout.starttime, DBSplit.id);
        DBSplit.length = length;
        DBSplit.duration = (time / 1000);
        DBSplit.status = AppEnum.RunningStatus.FINISH;
        DBSplit.avgheartrate = avgHeartRate;
        DBSplit.Latitude = Latitude;
        DBSplit.Longitude = (Longitude);

        int speed = 0;
        if (DBSplit.length != 0) {
            speed = (int) (DBSplit.duration * 1000 / DBSplit.length);
            if (speed != 0) {
                if (mDBWorkout.maxSpeed < speed) {
                    mDBWorkout.maxSpeed = speed;
                }
                if (mDBWorkout.minSpeed == 0) {
                    mDBWorkout.minSpeed = speed;
                }
                if (mDBWorkout.minSpeed > speed) {
                    mDBWorkout.minSpeed = speed;
                }
            }
        }
        SpliteData.updateSplite(RunningServiceV2.this, DBSplit);
        UploadData.finishSplite(RunningServiceV2.this, DBSplit, mUserEntity.userId);
        if (!lastSplit) {
            TTsUtils.perKm(RunningServiceV2.this, (int) (mRunningLength / 1000), (int) (mRunningTime / 1000), speed);
        }
    }

    /**
     * 结束跑步历史
     */
    private void finishWorkout() {
        isRunning = false;
        // 保存位置信息
        if (mUserEntity.userId != AppEnum.DEFAULT_USER_ID) {
            UploadData.saveLocationInfo(RunningServiceV2.this, mDBWorkout, mDBLap, mUserEntity.userId);
        }
        // 结束lap
        if (mDBLap.getStatus() != AppEnum.RunningStatus.FINISH) {
            finishLap();
        }
        // 保存split 信息
        finishSplit(mDBSplit, mSplitLength, mSplitTime, 0, 0, true);
        // 保存跑步历史信息
        mDBWorkout.status = AppEnum.RunningStatus.FINISH;
        mDBWorkout.duration = (mRunningTime / 1000);
        if (mDBWorkout.length > 0) {
            mDBWorkout.avgPace = ((int) (mDBWorkout.duration * 1000 / mDBWorkout.length));// 计算平均配速
        } else {
            mDBWorkout.avgPace = 0;// 计算平均配速
        }
        // 计算卡路里
        mCalorie = CalorieUtils.getCalorie(RunningServiceV2.this, 0, mDBWorkout.duration, mDBWorkout.length * 1.0f);
        mDBWorkout.calorie = mCalorie;
        //计算平均心率,最大心率,最小心率
        int avgHeartrate = 0;
        int maxHeartrate = 0;
        int minHeartrate = 0;
        int total = 0;
        List<DBEntityHeartrate> heartlist = HeartrateData.getAllHeartrateFromWork(RunningServiceV2.this, mDBWorkout.starttime);
        if (heartlist.size() != 0) {
            for (DBEntityHeartrate entity : heartlist) {
                total += entity.getBmp();
                if (maxHeartrate < entity.bmp) {
                    maxHeartrate = entity.bmp;
                }
                if (minHeartrate > entity.bmp) {
                    minHeartrate = entity.bmp;
                }
            }
            avgHeartrate = total / heartlist.size();
        }

        mDBWorkout.setAvgHeartrate(avgHeartrate);
        mDBWorkout.setMaxHeartrate(maxHeartrate);
        mDBWorkout.setMinHeartrate(minHeartrate);

        WorkoutData.updateWorkout(RunningServiceV2.this, mDBWorkout);
        //若登录的人不是游客,创建上传信息
        if (mUserEntity.userId != AppEnum.DEFAULT_USER_ID) {
            UploadData.finishWorkout(RunningServiceV2.this, mDBWorkout, mUserEntity.userId);
        }
        // 若距离小于100米, 距离太短不记录
        if (mDBWorkout.length < 100) {
            WorkoutData.deleteWorkout(RunningServiceV2.this, mDBWorkout.starttime);
        }
        stopSelf();
    }

    /**
     * 结束lap信息
     */
    private void finishLap() {
        if (mDBLap.status == AppEnum.RunningStatus.FINISH) {
            return;
        }
        mDBLap.duration = (mLapTime / 1000);
        mDBLap.status = AppEnum.RunningStatus.FINISH;
        UploadData.saveOriginalLocationInfo(RunningServiceV2.this, mDBWorkout, mUserEntity.userId);
        LapData.updateLap(RunningServiceV2.this, mDBLap);
        mLastLLatLng = null;
        //若用户不是游客
        if (mUserEntity.userId != AppEnum.DEFAULT_USER_ID) {
            RunningData.FinishLap(RunningServiceV2.this, mUserEntity.userId, mDBWorkout.starttime, mDBLap);
        }
    }

    /**
     * 写入跑步日志
     */
    private void writeRunningLog(final String Title) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // Save the log on SD card if available
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    String sdcardPath = AppConfig.CRASH_PATH;
                    File crashPath = new File(sdcardPath);
                    if (!crashPath.exists()) {
                        crashPath.mkdir();
                    }
                    //Gps状态
                    String GpsStatus = "Gps状态: " + GpsUtils.getStatus(RunningServiceV2.this) + "\n";
                    //内存
                    String memStr = "系统内存:" + DeviceUtils.getTotalMemory() + "\n";
                    //电量
                    Intent batteryInfoIntent = DeviceUtils.getBatterPower(RunningServiceV2.this);
                    String powerStatus = "电量: " + batteryInfoIntent.getIntExtra("level", 0) + "\n";

                    String logInfo = Title + GpsStatus + memStr + powerStatus;
                    FileUtils.writeLog(logInfo, sdcardPath + "/");
                }
            }
        });
    }

    /**
     * 启动锁屏界面
     *
     * @param context
     */
    private void launchLockActivity(Context context) {
        KeyguardManager km = (KeyguardManager)
                context.getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            Intent alarmLockIntent = new Intent(context,
                    RunningLockActivity.class);
            alarmLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            alarmLockIntent.putExtra("time", mRunningTime / 1000);
            alarmLockIntent.putExtra("length", mRunningLength);
            alarmLockIntent.putExtra("speed", mPace);
            context.startActivity(alarmLockIntent);
        }
    }

    /**
     * 销毁其他服务
     */
    private void destroyOtherService() {
        //停止剪贴板监听服务
        LocalApplication.getInstance().setIsActive(true);
        Intent clipboardService = new Intent(RunningServiceV2.this, ClipboardService.class);
        stopService(clipboardService);
        JPushInterface.stopPush(getApplicationContext());
    }

    /**
     * 恢复其他服务
     */
    private void resumeOtherService() {
        //启动剪贴板监听服务
        LocalApplication.getInstance().setIsActive(true);
        Intent clipboardService = new Intent(RunningServiceV2.this, ClipboardService.class);
        startService(clipboardService);
        if (PreferencesData.getJpushEnable(getApplicationContext())) {
            JPushInterface.resumePush(getApplicationContext());
        }

    }

    /**
     * 初始化跑步参数
     */
    private void initRunParams() {
        boolean timeCorrect = false;//是否需要时间校准
        //获取用户信息
        mUserEntity = LocalApplication.getInstance().getLoginUser(RunningServiceV2.this);
        mDBWorkout = WorkoutData.getUnFinishWorkout(RunningServiceV2.this, mUserEntity.userId);
        //若跑步历史为空,创建一个新的跑步
        if (mDBWorkout == null) {
            mDBWorkout = RunningData.createNewWorkout(RunningServiceV2.this, mUserEntity.userId);
        } else {
            if (mDBWorkout.length != 0) {
                timeCorrect = true;
            }
        }
        //若lap为空，创建一个新的lap
        mDBLap = LapData.getUnFinishLap(RunningServiceV2.this, mDBWorkout.starttime);
        if (mDBLap == null) {
            mDBLap = RunningData.createNewLap(RunningServiceV2.this, mDBWorkout.starttime, mUserEntity.userId);
        } else {
            DBEntityLocation dbEntityLocation = LocationData.getLocationByLap(RunningServiceV2.this, mDBLap, false);
            if (dbEntityLocation != null) {
                mLastLLatLng = new LatLng(dbEntityLocation.latitude, dbEntityLocation.longitude);
            }
        }
        //若split为空，创建一个新的split
        mDBSplit = SpliteData.getUnFinishSplite(RunningServiceV2.this, mDBWorkout.starttime);
        if (mDBSplit == null) {
            mDBSplit = RunningData.createNewSplit(RunningServiceV2.this, mDBWorkout.length, mDBWorkout.starttime);
        }

        // 初始化时间参数
        mRunningTime = mDBWorkout.duration * 1000;
        mSplitTime = mDBSplit.duration * 1000;
        mLapTime = mDBLap.duration * 1000;
        mCalorie = mDBWorkout.calorie;
        // 初始化距离参数
        mRunningLength = mDBWorkout.length;
        mLapLength = mDBLap.length;
        mSplitLength = mDBSplit.length;
        // 速度参数
        mPace = 0;
        mLastTimeForPace = mRunningTime;
        // 初始化信使
        isRunning = true;
        //时间修正
        if (timeCorrect) {
            long changeTime = TimeUtils.getTimeDifference(mDBLap.starttime, TimeUtils.NowTime());
            long correctTime = (changeTime * 1000) - mLapTime;
            mRunningTime += correctTime;
            mSplitTime += correctTime;
            mLapTime += correctTime;
        }
    }

    /**
     * 初始化定时事件
     */
    private void initAlarmData() {
        //计时器
        mTimerHandler = new Handler();
        mTimerRa = new Runnable() {
            @Override
            public void run() {
                updateTimer();
            }
        };
        mTimerHandler.removeCallbacks(mTimerRa);
    }


    /**
     * 初始化蓝牙管理
     */
    private void initBleManager() {
        //初始化蓝牙
        if (!PreferencesData.getBlueToothDeviceMac(RunningServiceV2.this).equals("")) {
            mConnectDeviceMac = PreferencesData.getBlueToothDeviceMac(RunningServiceV2.this);
            Intent intent = new Intent(RunningServiceV2.this, BleManagerService.class);
            intent.putExtra("CMD", BleManagerService.CMD_GET_CONNECT);
            intent.putExtra("address", mConnectDeviceMac);
            startService(intent);
        }
    }

    /**
     * 初始化声音播放器
     */
    private void initVoicePlayer() {
        mVoicePlayer = new MediaPlayer();
        mVoicePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Log.v(TAG,"onCompletion");
                mVoicePlayer.release();
                mVoicePlayer = null;
                mVoiceList.remove(0);
                playVoice();
            }
        });
        mVoicePlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mVoicePlayer = null;

                return false;
            }
        });
    }

    /**
     * 播放下一个音频
     */
    private void playVoice() {
        initVoicePlayer();
        playVoiceNow = true;
        if (mVoicePlayer != null && mVoiceList != null && mVoiceList.size() > 0) {
            try {
                String url = mVoiceList.get(0);
                if (url.equals("thumb")) {
                    AssetFileDescriptor fileDescriptor = RunningServiceV2.this.getAssets().openFd("running_voice.mp3");
                    mVoicePlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                } else {
                    mVoicePlayer.setDataSource(mVoiceList.get(0));
                }
                mVoicePlayer.prepare();
                mVoicePlayer.start();
            } catch (IllegalArgumentException e) {
                mVoicePlayer = null;
                mVoiceList.remove(0);
                playVoice();
                return;
            } catch (IOException e) {
                mVoicePlayer = null;
                mVoiceList.remove(0);
                playVoice();
                return;
            }
        } else {
            playVoiceNow = false;
        }
    }

    /**
     * 初始化跑步接收器
     */
    private void initRunningReceiver() {
        mLockReceiver = new RunningReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mLockReceiver, filter);
    }

    /**
     * 更新时间
     */
    private void updateTimer() {
//        Log.v(TAG, "updateTimer isRunning:" + isRunning);
        if (!isRunning) {
            return;
        }
        long disparityTime = System.currentTimeMillis() - mStartTime;// 时间
        mRunningTime += disparityTime;
        mLapTime += disparityTime;
        mSplitTime += disparityTime;
        // 这里发送到activity
        EventUtils.sendForegroundEvent(new EventRuningTime(mRunningTime / 1000));
        //执行下次计时
        long now = SystemClock.uptimeMillis();
        if (NextTime == now + (1000 - now % 1000)) {
            return;
        }
        NextTime = now + (1000 - now % 1000);
        mStartTime = System.currentTimeMillis();
        mTimerHandler.postAtTime(mTimerRa, NextTime);
        //若符合心率播报要求，播报心率
        if ((mRunningTime / 1000) % heartbeatVoiceInterval == 0
                && mCurrHeartbeat != 0 && (mRunningTime / 1000) != 0) {
            TTsUtils.playHeartbeat(RunningServiceV2.this, mCurrHeartbeat,mCurrCadence);
        }
    }

    /**
     * 保存上传内容
     */
    private void saveUploadInfoData() {
        if (!isRunning) {
            return;
        }
        mDBWorkout.duration = (mRunningTime / 1000);
        WorkoutData.updateWorkout(RunningServiceV2.this, mDBWorkout);
        if (mUserEntity.userId != AppEnum.DEFAULT_USER_ID) {
            mLocSizeInMin = UploadData.saveLocationInfo(RunningServiceV2.this, mDBWorkout, mDBLap, mUserEntity.userId);
            UploadData.saveHeartrateInfo(RunningServiceV2.this, mDBWorkout, mDBLap, mUserEntity.userId);
            UploadData.saveOriginalLocationInfo(RunningServiceV2.this, mDBWorkout, mUserEntity.userId);
            if (mLocSizeInMin == 0) {
                mNullLocationTime++;
                //3分钟都没定位 重启
                if (mNullLocationTime > 2) {
                    mNullLocationTime = 0;
                    stopLocation();
                    initGps();
                    writeRunningLog("RESET GPS\n");
                }
            } else {
                mNullLocationTime = 0;
            }
        }

        long now = SystemClock.uptimeMillis();
        long nextTime = now + (alarmSaveUploadInterval - now % alarmSaveUploadInterval);
        mHandler.sendEmptyMessageAtTime(MSG_SAVE_UPLOAD, nextTime);
    }

    /**
     * 电量屏幕
     */
    private void brightKeyguard() {
        if (!isRunning) {
            return;
        }
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取电源管理器对象
        PowerManager.WakeLock wl;
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager
                .SCREEN_DIM_WAKE_LOCK, "RunningServiceV2");
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        wl.acquire();
        //点亮屏幕
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        //得到键盘锁管理器对象
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        kl.reenableKeyguard();
        //重新启用自动加锁
        wl.release();
        //下次启动时间
        long now = SystemClock.uptimeMillis();
        long nextTime = now + (alarmWakeUpInterval - now % alarmWakeUpInterval);
        mHandler.sendEmptyMessageAtTime(MSG_WAKE_UP, nextTime);
    }

    /**
     * 初始化GPS取点服务
     */
    private void initGps() {
        locationClient = new AMapLocationClient(RunningServiceV2.this);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为仅设备模式
        if (!PreferencesData.getOnlyIsGpsLocMode(RunningServiceV2.this)) {
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        } else {
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        }
        // 设置定位监听
        locationClient.setLocationListener(this);

        locationOption.setInterval(GPSInterval);//定位间隔
        locationOption.setNeedAddress(false);//不需要返回位置信息
        locationOption.setOnceLocation(false);//持续定位
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    private void stopLocation() {
        if (null != locationClient) {
            locationClient.stopLocation();
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 检查守护进程
     */
    private void checkDaemon() {
        if (!DeviceUtils.isWorked(RunningServiceV2.this, "com.app.pao.service.ListenerService")) {
            //启动监听服务
            Intent listenerIntent = new Intent(RunningServiceV2.this, ListenerService.class);
            startService(listenerIntent);
        }
        long now = SystemClock.uptimeMillis();
        long nextTime = now + (alarmCheckDaemonInterval - now % alarmCheckDaemonInterval);
        mHandler.sendEmptyMessageAtTime(MSG_CHECK_DAEMON, nextTime);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
