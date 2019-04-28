package cn.hwh.sdk.ble;

/**
 * Created by Administrator on 2016/8/15.
 */
public class EventBleConnect {

    public int msg;
    public int heartbeat;
    public String address;
    public String originalData;
    public int count;
    public int lostCount;
    public String info;
    public BleConnectEntity connectEntity;
    public int cadence;//步频
    public int stepCount;//步数

    public EventBleConnect(int msg, String address) {
        this.msg = msg;
        this.address = address;
    }

    public EventBleConnect(int msg, int heartbeat, String address) {
        this.msg = msg;
        this.heartbeat = heartbeat;
        this.address = address;
        this.cadence = cadence;
        this.stepCount = stepCount;
    }

    public EventBleConnect(int msg, int heartbeat, int cadence, int stepCount, String address) {
        this.msg = msg;
        this.heartbeat = heartbeat;
        this.address = address;
        this.cadence = cadence;
        this.stepCount = stepCount;
    }

    public EventBleConnect(int msg, String originalData, String address, int count, int lostCount) {
        this.msg = msg;
        this.originalData = originalData;
        this.address = address;
        this.count = count;
        this.lostCount = lostCount;
    }

    public EventBleConnect(int msg, String info, String address) {
        this.msg = msg;
        this.info = info;
        this.address = address;
    }

    public EventBleConnect(int msg, int heartbeat, String address, int count) {
        this.msg = msg;
        this.heartbeat = heartbeat;
        this.address = address;
        this.count = count;
    }

    public EventBleConnect(int msg, BleConnectEntity connectEntity) {
        this.msg = msg;
        this.connectEntity = connectEntity;
    }
}
