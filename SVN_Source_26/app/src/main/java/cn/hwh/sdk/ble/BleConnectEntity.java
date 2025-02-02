package cn.hwh.sdk.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.app.pao.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Administrator on 2016/8/15.
 */
public class BleConnectEntity {

    private static final String TAG = "BleConnectEntity";

    public static final String DEFAULT_ORIGINAL = "16777215,65535,65535,65535;16777215,65535,65535,65535;";

    private static final int MSG_SHOW_COUNT = 0x01;
    private static final int MSG_READ_C = 0x02;
    private static final int MSG_REPEAT_CONNECT = 0x03;
    private static final int MSG_WRITE_ODA_REPEAT = 0x04;

    private Context mContext;
    BluetoothGattCallback GattCallback;

    public String address;//连接的mac地址
    public boolean notifyOriginal = false;
    public int hrCount = 0;//原始数据数量
    public boolean isHwhDevice = false;

    public int lostCount = 0;//心率丢包数量
    private int lastHrSign = -1;//上一个心率末位标识

    public int lostOriginalCount = 0;//原始数据丢包数量
    private int lastOriginalSign = -1;//上一个原始数据的末位标识

    public ArrayList<byte[]> mOriginalList = new ArrayList<>();

    public String deviceName = "";
    public String editName = "";

    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器
    public BluetoothGatt mBluetoothGatt;

    BluetoothGattService mHrGattService;//心率服务
    BluetoothGattCharacteristic mHrCharacteristic;//心率特征

    BluetoothGattService mOriginalGattService;//原始数据服务
    BluetoothGattCharacteristic mOriginalCharacteristic;//原始数据特征

    /* 设备信息相关 */
    BluetoothGattCharacteristic mDeviceNameCharacteristic;//设备名称
    BluetoothGattCharacteristic mManufacturerCharacteristic;//生产厂家名称
    BluetoothGattCharacteristic mFirmwareCharacteristic;//firmware版本
    BluetoothGattCharacteristic mSoftwareCharacteristic;//软件版本
    BluetoothGattCharacteristic mAppTypeCharacteristic;//app类型

    BluetoothGattCharacteristic mBatteryCharacteristic;//电池特征


    BluetoothGattCharacteristic mODACharacteristic;//ODA升级的特征
    BluetoothGattCharacteristic mODARequestCharacteristic;//ODA升级的特征

    private int otaStep = -1;
    private int writeCount = 0;
    private byte[] otaData;
    private boolean needConnect = true;


    Handler mLocalHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_SHOW_COUNT) {
//                Log.e(TAG, "<" + address + ">" + "original count:" + hrCount);
                mLocalHandler.sendEmptyMessageDelayed(MSG_SHOW_COUNT, 1000);
            } else if (msg.what == MSG_READ_C) {
                BluetoothGattCharacteristic c = (BluetoothGattCharacteristic) msg.obj;
                c.getValue();
            } else if (msg.what == MSG_REPEAT_CONNECT) {
//                Log.v(TAG,"MSG_REPEAT_CONNECT");
                mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(address).connectGatt(mContext,
                        false, GattCallback);
            } else if (msg.what == MSG_WRITE_ODA_REPEAT) {
                if (!mBluetoothGatt.writeCharacteristic(mODACharacteristic)) {
                    mLocalHandler.sendEmptyMessageDelayed(MSG_WRITE_ODA_REPEAT, 500);
                }
            }
        }
    };

    public BleConnectEntity(Context context, final String mac, BluetoothAdapter mAdapter) {
        this.address = mac;
        this.mBluetoothAdapter = mAdapter;
        this.mContext = context;

        GattCallback = new BluetoothGattCallback() {
            //连接状态发生改变
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (status != BluetoothGatt.GATT_SUCCESS) { // 连接失败判断
                    Log.i(TAG, "<" + address + ">" + "Connect fail status:" + status);
                    EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_CONNECT_FAIL, address));
//                    mBluetoothGatt.close();
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
                    return;
                }
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i(TAG, "<" + address + ">" + "Connected to GATT server.");
                    Log.i(TAG, "<" + address + ">" + "Attempting to start service discovery");
//                    connected = true;
                    mBluetoothGatt.discoverServices();
                    EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_CONNECTED, address));
                    //连接断开
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED && needConnect) {
                    Log.i(TAG, "<" + address + ">" + "Disconnected from GATT server.");
                    EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_DISCONNECT, address));
//                    connected = false;
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
                    //重新连接
//                    mLocalHandler.sendEmptyMessage()

                }
            }

            //发现服务
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//                Log.v(TAG,"onServicesDiscovered status:" + status);
                // 发现服务失败
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    Log.v(TAG, "Discover ble fail");
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
                    return;
                }
                //扫描服务
                for (BluetoothGattService gattService : gatt.getServices()) {
                    Log.e(TAG, "<" + address + ">" + "UUID " + gattService.getUuid().toString());

                    //发现ODA升级 service
                    if (gattService.getUuid().equals(BLEConfig.UUID_OAD_SERVICE)) {
                        mODACharacteristic = gattService.getCharacteristic(BLEConfig.UUID_OAD_NOTIFY);
                        mODARequestCharacteristic = gattService.getCharacteristic(BLEConfig.UUID_OAD_REQUEST);
                    }

                    //发现私有服务
                    if (gattService.getUuid().equals(BLEConfig.UUID_PRIVATE_SERVICE)) {
                        mBatteryCharacteristic = gattService.getCharacteristic(BLEConfig.UUID_BATTERY);
                        mAppTypeCharacteristic = gattService.getCharacteristic(BLEConfig.UUID_APP_TYPE);
                    }

                    //发现device info service
                    if (gattService.getUuid().equals(BLEConfig.UUID_DEVICE_INFO_SERVICE)) {
                        mFirmwareCharacteristic = gattService.getCharacteristic(BLEConfig.UUID_FIRMWARE_REVISION);
                        mSoftwareCharacteristic = gattService.getCharacteristic(BLEConfig.UUID_SOFTWARE_REVISION);
                        mDeviceNameCharacteristic = gattService.getCharacteristic(BLEConfig.UUID_DEVICE_NAME);
                        mManufacturerCharacteristic = gattService.getCharacteristic(BLEConfig.UUID_MANUFACTURER_NAME);
                    }

                    //发现心率服务
                    if (gattService.getUuid().equals(BLEConfig.UUID_HEART_RATE_SERVICE)) {
//                        Log.v(TAG, "find heart beat service");
                        mHrGattService = gattService;
                        mHrCharacteristic = mHrGattService.getCharacteristic(BLEConfig.UUID_HEART_RATE_MEASUREMENT);
                    }

                    //发现123Sports原始数据的 service
                    if (gattService.getUuid().equals(BLEConfig.UUID_HR_ORIGINAL_SERVICE)) {
                        mOriginalGattService = gattService;
                        mOriginalCharacteristic = mOriginalGattService.getCharacteristic(BLEConfig.UUID_HR_ORIGINAL_MEASUREMENT);
                    }
                }
                //读取心率设备生产厂家
                if (mManufacturerCharacteristic != null) {
                    readManufacturer();
                } else {
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
                    return;
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int
                    status) {
//                Log.e(TAG, "<" + address + ">" + "characteristic--：" + "onCharacteristicRead:" + characteristic.getUuid() +",status:" + status);
                readCharacteristic(characteristic);

            }

            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//                Log.e(TAG, "<" + address + ">" + "characteristic--：" + "onCharacteristicChanged");
                readCharacteristicOnChange(characteristic);
            }

//            @Override
//            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//                Log.e(TAG, "<" + address + ">" + "onDescriptorWrite ：" + "status:" + status);
//                super.onDescriptorWrite(gatt, descriptor, status);
//            }

            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                         int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    Log.i(TAG, descriptor.getValue() + "");
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.v(TAG, "Write error status:" + status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    writeSuccess(characteristic);
                } else {
                    if (characteristic.getUuid().equals(BLEConfig.UUID_APP_TYPE)) {
                        mBluetoothGatt.disconnect();
                        mBluetoothGatt.close();
                        mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
                    }
                    if (characteristic.getUuid().equals(BLEConfig.UUID_DEVICE_NAME)) {
                        EventBus.getDefault().post(new EventBleEdit(EventBleEdit.STATE_FAIL, EventBleEdit.EDIT_DEVICE_NAME, mac, ""));
                    }
                    if (characteristic.getUuid().equals(BLEConfig.UUID_OAD_NOTIFY)) {
//                        Log.v(TAG, "oda 升级失败");
                    }
                }
            }
        };
        mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(mac).connectGatt(context,
                false, GattCallback);
    }


    /**
     * 设置当指定characteristic值变化时，发出通知
     *
     * @param characteristic
     * @param enable
     */
    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {

        mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
        if (enable) {
            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(UUID.fromString(BLEConfig.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * 解析读取的信息
     *
     * @param characteristic
     */
    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        //读取设备名称
        if (BLEConfig.UUID_DEVICE_NAME.equals(characteristic.getUuid())) {
            deviceName = characteristic.getStringValue(0).trim();
            EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_GET_DEVICE_NAME, characteristic.getStringValue(0).trim(), address));
            return;
        }
        if (BLEConfig.UUID_FIRMWARE_REVISION.equals(characteristic.getUuid())) {
            EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_GET_FIRMWARE_REVISION, characteristic.getStringValue(0).trim(), address));
            return;
        }
        if (BLEConfig.UUID_SOFTWARE_REVISION.equals(characteristic.getUuid())) {
            EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_GET_SOFTWARE_REVISION, characteristic.getStringValue(0).trim(), address));
            return;
        }
        if (BLEConfig.UUID_BATTERY.equals(characteristic.getUuid())) {
            if (characteristic.getValue() != null) {
                final int battery = characteristic.getValue()[0];
                EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_NEW_BATTERY, battery, address));
            }
            return;
        }
        //读取心率数据
        if (mHrCharacteristic != null) {
            setCharacteristicNotification(mHrCharacteristic, true);
        } else {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
            return;
        }
        //5000a0000000e803
        //31323353706f727473
        //读取生产厂家
//        if (BLEConfig.UUID_MANUFACTURER_NAME.equals(characteristic.getUuid())) {
//            String manuFacturerName = characteristic.getStringValue(0).trim();
////            Log.v(TAG,"manuFacturerName:"+StringUtils.bytesToHexString(characteristic.getValue()));
////            Log.v(TAG, "manuFacturerName:" + manuFacturerName);
//            //读取心率数据
////            if (mHrCharacteristic != null) {
////                setCharacteristicNotification(mHrCharacteristic, true);
////            } else {
////                mBluetoothGatt.disconnect();
////                mBluetoothGatt.close();
////                mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
////                return;
////            }
////
////            //支持原始数据
////            if (manuFacturerName.equals("123Sports") && mOriginalCharacteristic != null) {
////                EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_FIND_ORIGINAL, address));
////                if (notifyOriginal) {
////                    setCharacteristicNotification(mOriginalCharacteristic, true);
////                }
////            }
//
//            //需要写入设备类型的方式
//            if (manuFacturerName.equals("123Sports")) {
//                isHwhDevice = true;
//                if (mAppTypeCharacteristic != null) {
//                    writeAndroidPhoneType();
//                    return;
//                } else {
//                    mBluetoothGatt.disconnect();
//                    mBluetoothGatt.close();
//                    mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
//                    return;
//                }
//            } else {
//                //读取心率数据
//                if (mHrCharacteristic != null) {
//                    setCharacteristicNotification(mHrCharacteristic, true);
//                } else {
//                    mBluetoothGatt.disconnect();
//                    mBluetoothGatt.close();
//                    mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
//                    return;
//                }
//            }
//        }
//        if (BLEConfig.UUID_APP_TYPE.equals(characteristic.getUuid())) {
//            if (characteristic.getValue() != null) {
////                Log.v(TAG, "app type:" + StringUtils.bytesToHexString(characteristic.getValue()));
//            }
//            return;
//        }

    }

    /**
     * 若写入设备名称成功
     * 512
     * 8320
     *
     * @param characteristic
     */
    private void writeSuccess(BluetoothGattCharacteristic characteristic) {
        if (BLEConfig.UUID_DEVICE_NAME.equals(characteristic.getUuid())) {
            EventBus.getDefault().post(new EventBleEdit(EventBleEdit.STATE_SUCCESS, EventBleEdit.EDIT_DEVICE_NAME, address, editName));
            deviceName = editName;
            //写入APP TYPE 成功
        } else if (BLEConfig.UUID_APP_TYPE.equals(characteristic.getUuid())) {
//            mBluetoothGatt.readCharacteristic(mAppTypeCharacteristic);
            if (mHrCharacteristic != null) {
                setCharacteristicNotification(mHrCharacteristic, true);
            } else {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mLocalHandler.sendEmptyMessage(MSG_REPEAT_CONNECT);
                return;
            }

            //支持原始数据
            if (mOriginalCharacteristic != null) {
                EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_FIND_ORIGINAL, address));
                if (notifyOriginal) {
                    setCharacteristicNotification(mOriginalCharacteristic, true);
                }
            }
        }
    }

    /**
     * 解析心率返回值w
     */
    private void readCharacteristicOnChange(BluetoothGattCharacteristic characteristic) {
        //心率数据
        if (BLEConfig.UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                // Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                // Log.d(TAG, "Heart rate format UINT8.");
            }

            if (characteristic.getValue() != null) {
                byte[] data = characteristic.getValue();
                int heartRate = data[1] & 0xff;
                int stepCount = 0;
                int cadence = 0;
                if (data.length > 4) {
                    stepCount = ((data[2] & 0xff) | ((data[3] & 0xff) << 8));
                    cadence = data[4] & 0xff;
                    if (cadence > 0){
                        cadence += 50;
                    }
//                    Log.v(TAG, "heartRate:" + heartRate + ",stepCount:" + stepCount + ",cadence:" + cadence);
                }

//                Log.d(TAG, "<" + mac + ">" + String.format("1 Received heart rate: %d", heartRate));

                EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_NEW_HEARTBEAT, heartRate, cadence, stepCount, address));

            }
        }
        //这是 Original 的数据
        if (BLEConfig.UUID_HR_ORIGINAL_MEASUREMENT.equals(characteristic.getUuid())) {
            hrCount++;
            mOriginalList.add(characteristic.getValue());
//            Log.e(TAG, "<" + address + "> mOriginalCharacteristic:" + StringUtils.bytesToHexString(characteristic.getValue()));
//            Log.e(TAG, "<" + address + "> mOriginalList.size():" + mOriginalList.size());
            if (mOriginalList.size() == 16) {
//                Log.e(TAG, "<" + address + "> hrCount:" + hrCount);

                List<byte[]> tempList = new ArrayList<>();
                tempList.addAll(mOriginalList);
                analysisOriginalData(tempList);
                mOriginalList.clear();
            }
        }

        //OTA升级的信息返回
        if (BLEConfig.UUID_OAD_REQUEST.equals(characteristic.getUuid())) {

            byte[] data = characteristic.getValue();
            byte[] tep = new byte[data.length];
            tep[0] = data[1];
            tep[1] = data[0];
//
//            Log.v(TAG, "data.length:" + data.length);
//            Log.d(TAG, "<" + address + "> mOtaCharacteristic:" + StringUtils.bytesToHexString(tep));
            if (characteristic.getValue() != null) {
                otaStep = StringUtils.byteToInt2(tep);
//                Log.e(TAG, "otaStep:" + otaStep);
                writeStep();
//                final String origin = bytesToHexString(characteristic.getValue());
//                EventBus.getDefault().post(new EventBLE(MSG_NEW_ORIGIN, origin, mac));
            }
        }
    }

    /**
     * 分析原始数据
     */
    private void analysisOriginalData(final List<byte[]> tempList) {
//        Log.v(TAG, "analysisOriginalData");
        String originalData = "";
//        Log.v(TAG, "tempList.size():" + tempList.size());
        for (int j = 0, size = tempList.size(); j < size; j++) {
            byte src[] = tempList.get(j);
            int currSign = src[src.length - 1] & 0xFF;
            if (lastOriginalSign == -1 || lastOriginalSign - currSign == 1 || (currSign == 255 && lastOriginalSign == 0)) {
                //没有丢包
            } else {
                int lost = 0;
                if (lastOriginalSign > currSign) {
                    lost += (lastOriginalSign - currSign - 1);
                } else {
                    lost += 255 - currSign + lastOriginalSign;
                }
//                Log.v(TAG, "lost:" + lost);
                for (int k = 0; k < lost; k++) {
                    originalData += DEFAULT_ORIGINAL;
                }
                lostOriginalCount += lost;
            }
            lastOriginalSign = currSign;
            originalData += StringUtils.parseOriginalData(src);
        }
//        Log.v(TAG, "originalData:" + originalData);
        if (!originalData.equals("")) {
            originalData = originalData.substring(0, originalData.length() - 1);
//            Log.v(TAG, "EventBleConnect hrCount:" + hrCount);
            EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_NEW_ORIGINAL,
                    originalData, address, hrCount, lostOriginalCount));
        }
    }

    /**
     * 是否接受原始数据
     * 08-16 13:40:16.095 13831-13844/cn.hwh.phone.bletest E/BleConnectEntity: <A0:E6:F8:2F:3E:83> hrCount:1
     * 08-16 13:46:48.037 13831-13893/cn.hwh.phone.bletest E/BleConnectEntity: <A0:E6:F8:2F:3E:83> hrCount:19589
     * <p>
     * 08-16 14:02:13.264 32627-32717/cn.hwh.phone.bletest E/BleConnectEntity: <A0:E6:F8:2F:3E:83> hrCount:1
     * 08-16 14:02:49.294 32627-32642/cn.hwh.phone.bletest E/BleConnectEntity: <A0:E6:F8:2F:3E:83> hrCount:1801
     *
     * @param notify
     */
    public void setNotifyOriginal(final boolean notify) {
//        hrCount = 0;
        mOriginalList.clear();
        setCharacteristicNotification(mOriginalCharacteristic, notify);
        notifyOriginal = notify;
    }


    /**
     * 更新电池信息
     */
    public void readBattery() {
        if (mBatteryCharacteristic != null) {
            mBluetoothGatt.readCharacteristic(mBatteryCharacteristic);
        }
    }

    /**
     * 读取firm ware 版本信息
     */
    public void readFirmwareRevision() {
        mBluetoothGatt.readCharacteristic(mFirmwareCharacteristic);
    }

    /**
     * 读取软件版本信息
     */
    public void readSoftwareRevision() {
        mBluetoothGatt.readCharacteristic(mSoftwareCharacteristic);
    }

    /**
     * 读取设备名称
     */
    public void readDeviceName() {
//        Log.v(TAG,"readDeviceName");
        mBluetoothGatt.readCharacteristic(mDeviceNameCharacteristic);
    }

    /**
     * 读取生产厂家
     */
    public void readManufacturer() {
        mBluetoothGatt.readCharacteristic(mManufacturerCharacteristic);
    }

    /**
     * 写入android类型
     */
    public void writeAndroidPhoneType() {
//        Log.v(TAG,"writeAndroidPhoneType");
        byte[] type = new byte[]{(byte) 0xa2};
        mAppTypeCharacteristic.setValue(type);
        mBluetoothGatt.writeCharacteristic(mAppTypeCharacteristic);
    }


    /**
     * 修改设备名称
     *
     * @param deviceName
     */
    public void editDeviceName(final String deviceName) {
        if (mDeviceNameCharacteristic != null) {
            editName = deviceName;
            mDeviceNameCharacteristic.setValue(deviceName.getBytes());
            mBluetoothGatt.writeCharacteristic(mDeviceNameCharacteristic);
        }
    }

    /**
     * 开始写入OTA 升级数据
     *
     * @param head
     * @param data
     */
    public void writeOta(byte[] head, byte[] data) {
        Log.v(TAG, "start writeOta");
//        setCharacteristicNotification(mHrCharacteristic, false);
        setCharacteristicNotification(mODARequestCharacteristic, true);
        //写head
        writeCount = 1;
        otaStep = -1;
        otaData = data;
//        Log.v(TAG, "writeOta block:" + otaStep + "od:" + StringUtils.bytesToHexString(head));
        mODACharacteristic.setValue(head);
        if ((mODACharacteristic.getProperties()
                & (BluetoothGattCharacteristic.PROPERTY_WRITE
                | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0) {
            Log.v(TAG, "write head");
            if (!mBluetoothGatt.writeCharacteristic(mODACharacteristic)) {
                mLocalHandler.sendEmptyMessageDelayed(MSG_WRITE_ODA_REPEAT, 500);
            }
        }

    }

    //06-17 14:35:22.891 15374-15374/cn.hwh.phone.bletest V/BLEGattEntity: start writeOta
    //06-17 14:38:08.001 15374-15557/cn.hwh.phone.bletest V/BLEGattEntity: otaStep:7679
    public void writeStep() {
        //写到结尾了
        if (otaStep == 61166) {
//            Log.v(TAG, "finish writeOAD");
            EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_OAD_FINISH, address));
            return;
        }
        if (otaStep == 65535) {
//            Log.v(TAG, "finish writeOAD error");
            EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_OAD_ERROR, address));
            return;
        }
        EventBus.getDefault().post(new EventBleConnect(BleManagerService.MSG_OAD, otaStep, address));
        writeCount++;
        byte[] od = new byte[18];
        od[0] = (byte) (otaStep & 0xFF);
        od[1] = (byte) ((otaStep >> 8) & 0xFF);
        System.arraycopy(otaData, otaStep * 16, od, 2, 16);
//        Log.v(TAG, "writeOta block:" + otaStep + "od:" + StringUtils.bytesToHexString(od));
        mODARequestCharacteristic.setValue(od);
        mBluetoothGatt.writeCharacteristic(mODARequestCharacteristic);
    }

    /**
     * 主动断开连接
     */
    public void disconnect() {
        needConnect = false;
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
    }
}
