package com.app.pao.entity.event;

/**
 * Created by Raul.Fan on 2016/4/11.
 * 切换Fragment事件
 */
public class EventSwitchFragment {

    //直播相关
    public static final int TYPE_LIVE = 0x01;
    public static final int EVENT_LIVE_DATA_SMALL_DATA = 0x11;//显示小数据界面
    public static final int EVENT_LIVE_DATA_BIG_DATA = 0x12;//显示大数据界面
    public static final int EVENT_LIVE_ROTATE_TO_MAIN = 0x13;//切换主数据页面
    public static final int EVENT_LIVE_ROTATE_TO_SPLIT = 0x14;//切换Split页面


    //跑步历史相关
    public static final int TYPE_HISTORY = 0x02;
    public static final int EVENT_HISTORY_SPLIT = 0x21;//显示分段页面
    public static final int EVENT_HISTORY_HEARTRATE = 0x22;//显示心率页面

    public int type;//Activity类型
    public int event;//行为类型
    public String extra;//附带参数


    public EventSwitchFragment(int type, int event) {
        this.type = type;
        this.event = event;
    }

    public EventSwitchFragment(int type, int event, String extra) {
        this.type = type;
        this.event = event;
        this.extra = extra;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
