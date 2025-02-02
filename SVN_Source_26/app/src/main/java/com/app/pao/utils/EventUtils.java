package com.app.pao.utils;

import com.app.pao.LocalApplication;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/7/4.
 * Event事件工具
 */
public class EventUtils {

    /**
     * 发送前台事件
     */
    public static void sendForegroundEvent(final Object event) {
        if (LocalApplication.getInstance().isActive) {
            EventBus.getDefault().post(event);
        }
    }


    /**
     * 发送事件
     */
    public static void sendEvent(final Object event) {
        EventBus.getDefault().post(event);
    }
}
