package com.app.pao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.multidex.MultiDexApplication;

import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.network.GetParseInviteTextResult;
import com.app.pao.exception.BaseExceptionHandler;
import com.app.pao.exception.LocalExceptionHandler;
import com.app.pao.service.ListenerReceiver;
import com.app.pao.service.ListenerService;
import com.app.pao.service.DaemonReceiver;
import com.app.pao.service.DaemonService;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.marswin89.marsdaemon.DaemonClient;
import com.marswin89.marsdaemon.DaemonConfigurations;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul on 2015/9/28.
 * APP运行实例
 */
public class LocalApplication extends MultiDexApplication {


    /* contains */
    private static final String TAG = "LocalApplication";

    /* local data */
    public static Context applicatonContext;//整个APP的上下文
    private static LocalApplication instance;//Application 对象
    public SharedPreferences mSharedPreferences;//sharedPreferences存储管理对象
    public DbUtils mDbUtils;//数据库操作工具
    public UploadData mUploadData;//需要上传的数据库操作类

    public DBUserEntity mLoginUser;//当前登录的用户

    public boolean isActive;//是否在前台运行
    public GetParseInviteTextResult clipresult;//剪贴板内容

    private DaemonClient mDaemonClient;

    /**
     * 整个 APP的创建
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //Jpush初始化
        JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化
//
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.drawable.icon_nf;
        JPushInterface.setDefaultPushNotificationBuilder(builder);
        if (!PreferencesData.getJpushEnable(this)) {
            JPushInterface.stopPush(this);
        }
        applicatonContext = getApplicationContext();

        //捕捉错误日志机制
        if (AppConfig.CATCH_EXCEPTION) {
            if (getDefaultUncaughtExceptionHandler() == null) {
                Thread.setDefaultUncaughtExceptionHandler(new LocalExceptionHandler(applicatonContext));
            } else {
                Thread.setDefaultUncaughtExceptionHandler(getDefaultUncaughtExceptionHandler());
            }
        }

        // 初始化键值对存储
        mSharedPreferences = getSharedPreferences("123Go", MODE_PRIVATE);

        //创建存储空间
        File Crashf = new File(AppConfig.CRASH_PATH);
        if (!Crashf.exists()) {
            Crashf.mkdirs();
        }
        File picF = new File(AppConfig.DEFAULT_SAVE_CUT_BITMAP);
        if (!picF.exists()) {
            picF.mkdirs();
        }

        File recordF = new File(AppConfig.RECORD_PATH);
        if (!recordF.exists()) {
            recordF.mkdirs();
        }

        File downloadF = new File(AppConfig.DEFAULT_SAVE_FILE_PATH);
        if (!downloadF.exists()) {
            downloadF.mkdirs();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
        mDaemonClient.onAttachBaseContext(base);
    }


    private DaemonConfigurations createDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.app.pao:process",
                ListenerService.class.getCanonicalName(),
                ListenerReceiver.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.app.pao:daemon",
                DaemonService.class.getCanonicalName(),
                DaemonReceiver.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
//        return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }


    /**
     * 获取异常捕获器
     *
     * @return
     */
    public BaseExceptionHandler getDefaultUncaughtExceptionHandler() {
        return new LocalExceptionHandler(applicatonContext);
    }

    /**
     * 获取 LocalApplication
     *
     * @return
     */
    public static LocalApplication getInstance() {
        if (instance == null) {
            instance = new LocalApplication();
        }
        return instance;
    }

    /**
     * 获取数据库操作类
     *
     * @return
     */
    public DbUtils getDbUtils(Context context) {
        if (mDbUtils == null) {
            String DBName = AppEnum.DB_NAME;
            mDbUtils = DbUtils.create(context, DBName, AppConfig.DB_VERSION, new DbUtils.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbUtils dbUtils, int oldversion, int newVersion) {
//                    Log.v(TAG, "onUpgrade");
                    if (newVersion > oldversion) {
                        try {
                            List<String> dbFildsList = new ArrayList<String>();
                            String str = "select * from user";
                            Cursor cursor = dbUtils.execQuery(str);
                            int count = cursor.getColumnCount();
                            for (int i = 0; i < count; i++) {
                                dbFildsList.add(cursor.getColumnName(i));
                            }
                            if (!dbFildsList.contains("clockcount")) {
                                String updateString = "ALTER TABLE user ADD clockcount INTEGER";
                                dbUtils.execNonQuery(updateString);
                            }

                            if (!dbFildsList.contains("friendcount")) {
                                String updateString = "ALTER TABLE user ADD friendcount INTEGER";
                                dbUtils.execNonQuery(updateString);
                            }

                            if (!dbFildsList.contains("groupcount")) {
                                String updateString = "ALTER TABLE user ADD groupcount INTEGER";
                                dbUtils.execNonQuery(updateString);
                            }

                            if (!dbFildsList.contains("lastShowInPerperLength")) {
                                String updateString = "ALTER TABLE user ADD lastShowInPerperLength INTEGER";
                                dbUtils.execNonQuery(updateString);
                            }
                            dbFildsList.clear();
                            str = "select * from workout";
                            cursor = dbUtils.execQuery(str);
                            count = cursor.getColumnCount();
                            for (int i = 0; i < count; i++) {
                                dbFildsList.add(cursor.getColumnName(i));
                            }
                            if (!dbFildsList.contains("startStep")) {
                                String updateString = "ALTER TABLE workout ADD startStep INTEGER";
                                dbUtils.execNonQuery(updateString);
                            }
                            if (!dbFildsList.contains("endStep")) {
                                String updateString = "ALTER TABLE workout ADD endStep INTEGER";
                                dbUtils.execNonQuery(updateString);
                            }
                            dbFildsList.clear();
                            str = "select * from heartrate";
                            cursor = dbUtils.execQuery(str);
                            count = cursor.getColumnCount();
                            for (int i = 0; i < count; i++) {
                                dbFildsList.add(cursor.getColumnName(i));
                            }
                            if (!dbFildsList.contains("cadence")) {
                                String updateString = "ALTER TABLE heartrate ADD cadence INTEGER";
                                dbUtils.execNonQuery(updateString);
                            }
                        } catch (DbException e) {
//                            Log.v(TAG, "DbException:" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
        return mDbUtils;
    }


    public void setDbUtils(DbUtils DbUtils) {
        this.mDbUtils = DbUtils;
    }

    /**
     * 获取需要上传数据库操作对象
     *
     * @return
     */
    public UploadData getUploadData() {
        if (mUploadData == null) {
            mUploadData = new UploadData();
        }
        return mUploadData;
    }


    /**
     * 设置正在登录的用户对象
     *
     * @param userEntity
     */
    public void setLoginUser(DBUserEntity userEntity) {
        this.mLoginUser = userEntity;
    }


    /**
     * 获取正在登录的用户对象
     *
     * @return
     */
    public DBUserEntity getLoginUser(Context context) {
        if (mLoginUser == null) {
            if (context != null) {
                mLoginUser = UserData.getUserById(context, PreferencesData.getUserId(context));
            }
        }
        return mLoginUser;
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
//            //启动监听服务ListenerService
//            Intent listenerIntent = new Intent(getInstance(), ListenerService.class);
//            startService(listenerIntent);
        }
    }


    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public GetParseInviteTextResult getClipresult() {
        return clipresult;
    }

    public void setClipresult(GetParseInviteTextResult clipresult) {
        this.clipresult = clipresult;
    }
}
