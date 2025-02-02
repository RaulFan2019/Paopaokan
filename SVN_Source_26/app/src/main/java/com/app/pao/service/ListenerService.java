package com.app.pao.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.app.pao.LocalApplication;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.utils.DeviceUtils;

import java.util.ArrayList;

/**
 * This Service is Persistent Service. Do some what you want to do here.<br/>
 * <p>
 * Created by Mars on 12/24/15.
 */
public class ListenerService extends Service {


    Handler myHandler;
    private int timer = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
//                    Toast.makeText(ListenerService.this, "我还活着:" + timer, Toast.LENGTH_SHORT).show();
                    timer++;
                    if (timer % 5 == 0
                            && LocalApplication.getInstance().getLoginUser(ListenerService.this) != null
                            && WorkoutData.getUnFinishWorkout(ListenerService.this, LocalApplication.getInstance().getLoginUser(ListenerService.this).userId) != null) {
                        if (!DeviceUtils.isWorked(ListenerService.this, "com.app.pao.service.RunningServiceV2")) {
//                            Toast.makeText(ListenerService.this, "跑步要重启了:" + timer, Toast.LENGTH_SHORT).show();
                            Intent runServiceIntent = new Intent(ListenerService.this, RunningServiceV2.class);
                            startService(runServiceIntent);
                        }else {
//                            Toast.makeText(ListenerService.this, "跑步活着:" + timer, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(timer % 20 == 0){
                        if (!DeviceUtils.isWorked(ListenerService.this, "com.app.pao.service.UploadWatcherService")) {
//                            Toast.makeText(ListenerService.this, "跑步要重启了:" + timer, Toast.LENGTH_SHORT).show();
                            Intent runServiceIntent = new Intent(ListenerService.this, UploadWatcherService.class);
                            startService(runServiceIntent);
                        }
                    }
                    myHandler.sendEmptyMessageDelayed(1, 3000);
                }
            }
        };
        myHandler.sendEmptyMessage(1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
