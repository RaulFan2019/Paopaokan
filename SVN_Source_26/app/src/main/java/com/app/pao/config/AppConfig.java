package com.app.pao.config;

import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by Raul on 2015/9/25.
 * APP配置类
 */
public class AppConfig {

    /**
     * 存放默认下载位置
     */
    public final static String DEFAULT_SAVE_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator
            + "Run2See" + File.separator + "download" + File.separator;

    public final static String DEFAULT_SAVE_CUT_BITMAP = Environment.getExternalStorageDirectory() + File.separator
            + "Run2See" + File.separator + "pic" + File.separator;

    public final static String CRASH_PATH = Environment.getExternalStorageDirectory() + File.separator + "Run2See" + File.separator + "crash";

    public final static String RECORD_PATH = Environment.getExternalStorageDirectory() + File.separator + "Run2See" + File.separator + "record";

    public final static String DEFAULT_AD_FILE_NAME = "ad";

    public final static Uri cutFileUri = Uri.parse("file://" + "/" + AppConfig.DEFAULT_SAVE_CUT_BITMAP + "/" + "small.jpg");
    /**
     * Build类型
     * BUILD_TYPE_ONLINE 对外上线版本
     * BUILD_TYPE_TEST 对内发布版本
     */
    public static final int BUILD_TYPE = AppEnum.BuildType.BUILD_BATE;

    /**
     * App 的版本信息.0
     * 用于发送给服务器版本信息
     */
    public static final String Version = "V 2.6.2.1";


    /**
     * 数据库版本号
     */
    public static final int DB_VERSION = 127;


    /**
     * 是否抓取报错
     */
    public static final boolean CATCH_EXCEPTION = true;

}
