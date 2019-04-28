package com.app.pao.exception;

import android.content.Context;
import android.content.Intent;
import com.app.pao.service.SendCrashLogService;
import com.app.pao.service.UploadWatcherService;

import cn.hwh.sdk.ble.BleManagerService;


public class LocalExceptionHandler extends BaseExceptionHandler {

    /**
     * contains
     **/
    private static final String TAG = "LocalFileHandler";
    private static final boolean DEBUG = false;

    private Context context;

    public LocalExceptionHandler(Context context) {
        this.context = context;
    }


    /**
     * 捕捉异常后的处理
     *
     * @param throwable
     * @return
     */
    @Override
    public boolean handleExeption(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
//        brightKeyguard();
        //结束可能在运行的Service
        Intent i = new Intent(context, UploadWatcherService.class);
        context.stopService(i);
        i = new Intent(context, BleManagerService.class);
        context.stopService(i);
        i = new Intent(context, SendCrashLogService.class);
        context.stopService(i);

        //退出程序
        System.exit(0);
        return false;
    }
}
