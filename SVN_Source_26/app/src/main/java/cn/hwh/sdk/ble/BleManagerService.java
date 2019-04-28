package cn.hwh.sdk.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 */
public class BleManagerService extends Service {

    private static final String TAG = "BleManagerService";

    public static final int CMD_CLEAR_ALL = 0x01;//清除所有连接
    public static final int CMD_ADD_NEW_CONNECT = 0x02;//增加一个新的连接
    public static final int CMD_ORIGINAL_NOTIFY = 0x03;//设置原始数据notify
    public static final int CMD_GET_CONNECT = 0x04;
    public static final int CMD_EDIT_DEVICE_NAME = 0x05;//修改设备名称
    public static final int CMD_READ_BATTERY = 0x06;//读取电池数据
    public static final int CMD_READ_FIRMWARE_REVISION = 0x07;//读取firmware 版本
    public static final int CMD_READ_SOFTWARE_REVISION = 0x08;//读取软件版本
    public static final int CMD_ODA = 0x09;//写入ODA升级
    public static final int CMD_WRITE_PHONE_TYPE = 0x10;//写入APP类型
    public static final int CMD_READ_DEVICE_NAME = 0x11;//读取设备名称


    public final static int MSG_NONE = -1;
    public final static int MSG_CONNECTING = 0;//正在连接
    public final static int MSG_CONNECTED = 1;//已连接
    public final static int MSG_DISCONNECT = 2;//断开连接
    public final static int MSG_CONNECT_FAIL = 3;//连接失败
    public final static int MSG_NEW_HEARTBEAT = 4;//新的BLE数据
    public final static int MSG_FIND_ORIGINAL = 5;//发现原始数据支持的服务
    public final static int MSG_NEW_ORIGINAL = 6;//新的原始数据
    public final static int MSG_NEW_BATTERY = 7;//新的电池信息
    public final static int MSG_GET_FIRMWARE_REVISION = 8;//获取firmware版本
    public final static int MSG_GET_SOFTWARE_REVISION = 9;//获取软件版本
    public final static int MSG_OAD = 10;//开始升级OAD
    public final static int MSG_OAD_FINISH = 11;//升级结束
    public final static int MSG_OAD_ERROR = 12;//升级结束
    public final static int MSG_LOST_HEARTBEAT = 13;//心率丢包数量
    public final static int MSG_GET_DEVICE_NAME = 14;//读取设备名称
    public final static int MSG_FIND_CONNECT = 15;//找到连接


    ArrayList<BleConnectEntity> mConnectList = new ArrayList<>();//连接列表


    /**
     * blue tooth
     */
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBluetooth();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra("CMD")) {
//                Log.v(TAG, "CMD:" + intent.getIntExtra("CMD", CMD_CLEAR_ALL));
                // 根据命令执行不同操作
                switch (intent.getIntExtra("CMD", CMD_CLEAR_ALL)) {
                    case CMD_CLEAR_ALL:
                        clearConnectList();
                        break;
                    case CMD_ADD_NEW_CONNECT:
                        addNewConnect(intent.getStringExtra("address"));
                        break;
                    case CMD_ORIGINAL_NOTIFY:
                        setOriginalNotify(intent.getStringExtra("address"), intent.getBooleanExtra("notify", true));
                        break;
                    case CMD_GET_CONNECT:
                        EventBus.getDefault().post(new EventBleConnect(MSG_FIND_CONNECT,findBleConnect(intent.getStringExtra("address"))));
                        break;
                    //修改设备名称
                    case CMD_EDIT_DEVICE_NAME:
                        String address = intent.getStringExtra("address");
                        String editName = intent.getStringExtra("editName");
                        editDeviceName(address, editName);
                        break;
                    //更新电池数据
                    case CMD_READ_BATTERY:
                        readBattery(intent.getStringExtra("address"));
                        break;
                    //读取firmware 版本
                    case CMD_READ_FIRMWARE_REVISION:
                        readFirmwareRevision(intent.getStringExtra("address"));
                        break;
                    //读取软件版本
                    case CMD_READ_SOFTWARE_REVISION:
                        readSoftwareRevision(intent.getStringExtra("address"));
                        break;
                    //ODA升级
                    case CMD_ODA:
//                        Log.v(TAG,"start oda");
                        String mac = intent.getStringExtra("mac");
                        byte[] head = intent.getByteArrayExtra("head");
                        byte[] data = intent.getByteArrayExtra("data");
                        writeOta(mac, head, data);
                        break;
                    //写入APP类型
                    case CMD_WRITE_PHONE_TYPE:
                        writePhoneType(intent.getStringExtra("address"));
                        break;
                    //读取设备名称
                    case CMD_READ_DEVICE_NAME:
                        readDeviceName(intent.getStringExtra("address"));
                        break;

                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化蓝牙
     */
    private void initBluetooth() {
        bluetoothManager = (BluetoothManager) getSystemService(this.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }


    /**
     * 清除连接状态
     */
    public void clearConnectList() {
        for (int i = 0, size = mConnectList.size(); i < size; i++) {
            BleConnectEntity entity = mConnectList.get(i);
            if (entity.mBluetoothGatt != null) {
                entity.disconnect();
            }
        }
        mConnectList.clear();
    }

    /**
     * 增加一个新的连接
     *
     * @param address
     */
    private void addNewConnect(final String address) {
        for (BleConnectEntity entity : mConnectList) {
            if (entity.address.equals(address)) {
                return;
            }
        }
        mConnectList.add(new BleConnectEntity(BleManagerService.this, address, mBluetoothAdapter));
    }


    /**
     * notify原始数据
     *
     * @param address
     * @param notify
     */
    private void setOriginalNotify(final String address, final boolean notify) {
//        Log.v(TAG, "setOriginalNotify <" + address + ">" + notify);
        int i = 0;

        for (int size = mConnectList.size(); i < size; i++) {
            if (mConnectList.get(i).address.equals(address)) {
                mConnectList.get(i).setNotifyOriginal(notify);
            }
        }
    }

    /**
     * 更新电池数据
     *
     * @param address
     */
    private void readBattery(final String address) {
        int i = 0;
        for (int size = mConnectList.size(); i < size; i++) {
            if (mConnectList.get(i).address.equals(address)) {
                mConnectList.get(i).readBattery();
            }
        }
    }

    private void readFirmwareRevision(final String address) {
        for (int i = 0, size = mConnectList.size(); i < size; i++) {
            if (mConnectList.get(i).address.equals(address)) {
                mConnectList.get(i).readFirmwareRevision();
            }
        }
    }

    private void readSoftwareRevision(final String address) {
        for (int i = 0, size = mConnectList.size(); i < size; i++) {
            if (mConnectList.get(i).address.equals(address)) {
                mConnectList.get(i).readSoftwareRevision();
            }
        }
    }

    private void readDeviceName(final String address){
        for (int i = 0, size = mConnectList.size(); i < size; i++) {
            if (mConnectList.get(i).address.equals(address)) {
                mConnectList.get(i).readDeviceName();
            }
        }
    }

    /**
     * 获取连接对象
     *
     * @param address
     * @return
     */
    public BleConnectEntity findBleConnect(final String address) {
        for (BleConnectEntity entity : mConnectList) {
            if (entity.address.equals(address)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 修改设备名称
     *
     * @param address
     * @param editName
     */
    private void editDeviceName(final String address, final String editName) {
        BleConnectEntity entity = findBleConnect(address);
        entity.editDeviceName(editName);
    }


    private void writeOta(String mac, byte[] head, byte[] data) {
        //写head
        BleConnectEntity entity = findBleConnect(mac);
        entity.writeOta(head, data);
    }

    /**
     * 写入android 设备类型
     *
     * @param address
     */
    private void writePhoneType(final String address) {
        BleConnectEntity entity = findBleConnect(address);
        if (entity != null){
            entity.writeAndroidPhoneType();
        }
    }
}
