package com.app.pao.activity.settings;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.ScanBleDeviceAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.data.PreferencesData;
import com.app.pao.entity.model.BLEEntity;
import com.app.pao.entity.model.BLEStateEntity;
import com.app.pao.utils.T;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.hwh.sdk.ble.BLEConfig;
import cn.hwh.sdk.ble.BleManagerService;
import cn.hwh.sdk.ble.EventBleConnect;

@ContentView(R.layout.activity_ble_settings)
public class BleSettingsActivity extends BaseAppCompActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "BleSettingsActivity";
    private static final int REQUEST_BLE_PERMISION = 1;//蓝牙打开请求

    private static final int MSG_CONNECT_ANIM = 0x01;
    private static final int MSG_UPDATE_VIEW = 0x02;


    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.tv_ble_device_name)
    private TextView mBleDeviceNameTv;//设备名称
    @ViewInject(R.id.tv_ble_heartrate)
    private TextView mBleHeartRateTv;//心率值
    @ViewInject(R.id.ll_connect_device)
    private LinearLayout mConnectDeviceLl;//连接设备布局
    @ViewInject(R.id.tv_no_connect_device_tip)
    private TextView mNoConnectDeviceTipTv;//无连接设备提示
    @ViewInject(R.id.tv_ble_device_scan_tip)
    private TextView mScanBleDeviceTipTv;//扫描ble设备提示
    @ViewInject(R.id.pv_scanning)
    private ProgressView mScanningDevicePv;//扫描中进度条
    @ViewInject(R.id.lv_ble_device)
    private ListView mBleDeviceLv;  //ble设备列表
    @ViewInject(R.id.tv_no_scan_device_tip)
    private TextView mNoScanDeviceTv;//没有扫描或未扫描到ble设备提示
    @ViewInject(R.id.btn_scan)
    private Button mScanControlBtn;//开始扫描设备或停止扫描设备

    /* local data */
    private ScanBleDeviceAdapter mBleAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBleDevice;

    private boolean mIsScanning = false;//是否扫描状态
    private boolean mHasBindDevice;

    private List<BLEStateEntity> mBleStateList;

    private String mDeviceName;//蓝牙设备名称
    private String mDeviceMac;//蓝牙mac地址

    //连接中的文字变化
    private int mAnimCount = 0;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            BLEEntity bleEntity = new BLEEntity(device, rssi);
            listChange(bleEntity);
        }
    };

    Handler mLocalHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //文字发生变化事件
            if (msg.what == MSG_CONNECT_ANIM) {
                if (mAnimCount % 4 == 0) {
                    mBleHeartRateTv.setText("连接中");
                } else if (mAnimCount % 4 == 1) {
                    mBleHeartRateTv.setText("连接中.");
                } else if (mAnimCount % 4 == 2) {
                    mBleHeartRateTv.setText("连接中..");
                } else {
                    mBleHeartRateTv.setText("连接中...");
                }
                mAnimCount++;
                mLocalHandler.sendEmptyMessageDelayed(MSG_CONNECT_ANIM, 1000);
                //页面发生变化
            } else if (msg.what == MSG_UPDATE_VIEW) {
                mBleAdapter.notifyDataSetChanged();
                if (mNoScanDeviceTv.getVisibility() == View.VISIBLE) {
                    mNoScanDeviceTv.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    /**
     * Toolbar 点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    @OnClick({R.id.btn_scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (mIsScanning) {
                    stopScan();
                } else {
                    startScan();
                }
                break;
        }
    }

    /**
     * 接收到新的ble数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBleConnect event) {
        switch (event.msg) {
            //已连接
            case BleManagerService.MSG_CONNECTED:
                mLocalHandler.removeMessages(MSG_CONNECT_ANIM);
                mBleAdapter.setBleState(mDeviceMac, BLEStateEntity.STATE_CONNECTED);
                mBleHeartRateTv.setText("已连接，等待数据...");
                mScanControlBtn.setVisibility(View.INVISIBLE);
                break;
            //失去连接
            case BleManagerService.MSG_DISCONNECT:
                mLocalHandler.removeMessages(MSG_CONNECT_ANIM);
                mBleAdapter.setBleState(mDeviceMac, BLEStateEntity.STATE_UNCONNECT);
                mBleHeartRateTv.setText("未连接");
                mScanControlBtn.setVisibility(View.VISIBLE);
                break;
            //新的心率值
            case BleManagerService.MSG_NEW_HEARTBEAT:
                mLocalHandler.removeMessages(MSG_CONNECT_ANIM);
                int heartRate = event.heartbeat;
                mBleHeartRateTv.setText(heartRate + "");
                if (!mHasBindDevice) {
                    PreferencesData.setBleEnable(mContext, true);
                    mHasBindDevice = true;
                }
                break;
            case BleManagerService.MSG_FIND_CONNECT:
                if (event.connectEntity == null){
                    //断开之前的连接
                    Intent clearIntent = new Intent(BleSettingsActivity.this, BleManagerService.class);
                    clearIntent.putExtra("CMD", BleManagerService.CMD_CLEAR_ALL);
                    startService(clearIntent);
                    //增加一个新的连接
                    Intent connectIntent = new Intent(BleSettingsActivity.this, BleManagerService.class);
                    connectIntent.putExtra("CMD", BleManagerService.CMD_ADD_NEW_CONNECT);
                    connectIntent.putExtra("address", mDeviceMac);
                    startService(connectIntent);
                }
                break;
        }
    }


    @Override
    @OnItemClick(R.id.lv_ble_device)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mBleStateList != null && mBleStateList.size() > 0) {
            //停止扫描状态
            if (mIsScanning) {
                stopScan();
            }
            //改变当前状态
            BLEStateEntity connectBleEntity = mBleStateList.get(position);
            connectBleEntity.setmConnectState(BLEStateEntity.STATE_CONNECTING);
            mDeviceName = connectBleEntity.mBleDevice.Device.getName();
            mDeviceMac = connectBleEntity.mBleDevice.Device.getAddress();

            PreferencesData.setBlueToothDeviceMac(mContext, mDeviceMac, mDeviceName);

            mBleDeviceNameTv.setText(mDeviceName + "");
            mLocalHandler.sendEmptyMessage(MSG_CONNECT_ANIM);
            mConnectDeviceLl.setVisibility(View.VISIBLE);
            mNoConnectDeviceTipTv.setVisibility(View.INVISIBLE);
            mHasBindDevice = false;
            //开始连接
            startConnectGatt(mDeviceMac);
            //将点击移除位置后重新插入顶端
            mBleStateList.remove(position);
            //将其他状态重置
            for (int i = 0; i < mBleStateList.size(); i++) {
                mBleStateList.get(i).setmConnectState(BLEStateEntity.STATE_UNCONNECT);
            }
            List<BLEStateEntity> connectStateEntityList = new ArrayList<BLEStateEntity>();
            connectStateEntityList.addAll(mBleStateList);
            mBleStateList.clear();
            mBleStateList.add(connectBleEntity);
            mBleStateList.addAll(connectStateEntityList);

            mBleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {
        mDeviceName = PreferencesData.getBlueToothDeviceName(mContext);
        mDeviceMac = PreferencesData.getBlueToothDeviceMac(mContext);
        mBleStateList = new ArrayList<BLEStateEntity>();

        //初始化蓝牙及ble
        initBLE();

        mBleAdapter = new ScanBleDeviceAdapter(this, mBleStateList);
        mBleDeviceLv.setAdapter(mBleAdapter);

        mBleAdapter.setOnItemBtnClickListener(new ScanBleDeviceAdapter.OnItemBtnClickListener() {
            @Override
            public void onBtnClick(int position) {
                Intent clearIntent = new Intent(BleSettingsActivity.this, BleManagerService.class);
                clearIntent.putExtra("CMD", BleManagerService.CMD_CLEAR_ALL);
                startService(clearIntent);
                mBleAdapter.setBleState(mDeviceMac, BLEStateEntity.STATE_UNCONNECT);
                mBleHeartRateTv.setText("未连接");
                mScanControlBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);

        //若mac为空代表没有连接设备
        if (!mDeviceMac.equals(AppEnum.DEFAULT_BLE_MAC)) {
            if (mDeviceName == null) {
                mBleDeviceNameTv.setText("未知");
            } else {
                mBleDeviceNameTv.setText(mDeviceName);
            }
            mNoConnectDeviceTipTv.setVisibility(View.INVISIBLE);
            mConnectDeviceLl.setVisibility(View.VISIBLE);
            mHasBindDevice = true;
            mLocalHandler.sendEmptyMessage(MSG_CONNECT_ANIM);
        } else {
            mHasBindDevice = false;
            mNoConnectDeviceTipTv.setVisibility(View.VISIBLE);
            mConnectDeviceLl.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void doMyOnCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        // 若蓝牙未打开
        if (!mBluetoothAdapter.isEnabled()) {
            // 蓝牙未打开 请求蓝牙打开权限
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BLE_PERMISION);
        }
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //请求打开蓝牙
            case REQUEST_BLE_PERMISION:
                if (resultCode != RESULT_OK) {
                    finish();
                }
                break;
        }
    }

    /**
     * 初始化蓝牙
     */
    private void initBLE() {
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            T.showShort(this, "当前手机不支持BLE蓝牙");
            finish();
        }

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            T.showShort(this, "该设备不支持蓝牙");
            finish();
        }

        if (!mDeviceMac.equals(AppEnum.DEFAULT_BLE_MAC)) {
            Intent intent = new Intent(BleSettingsActivity.this,BleManagerService.class);
            intent.putExtra("CMD",BleManagerService.CMD_GET_CONNECT);
            intent.putExtra("address", mDeviceMac);
            startService(intent);
        }
    }


    /**
     * 当接收到新的数据时
     *
     * @param bleEntity
     */
    private void listChange(BLEEntity bleEntity) {
        //过滤重复的数据
        for (BLEStateEntity bleStateEntity : mBleStateList) {
            if ((bleStateEntity.mBleDevice.Device.getAddress().equals(bleEntity.Device.getAddress()))) {
                return;
            }
        }
        BLEStateEntity stateEntity = new BLEStateEntity(bleEntity, 0);
        mBleStateList.add(stateEntity);
        mLocalHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
    }


    /**
     * 开始扫描
     */
    private void startScan() {
        mLocalHandler.removeMessages(MSG_CONNECT_ANIM);
        mBleHeartRateTv.setText("未连接");
        mScanControlBtn.setText(getResources().getString(R.string.Btn_BleSettingsActivity_stop_scan));
        mScanningDevicePv.setVisibility(View.VISIBLE);
        mScanningDevicePv.start();

        mBluetoothAdapter.startLeScan(new UUID[]{BLEConfig.UUID_HEART_RATE_SERVICE}, mLeScanCallback);
        mIsScanning = true;

        if (mBleStateList != null && mBleStateList.size() > 0) {
            mBleStateList.clear();
            mBleAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 停止扫描
     */
    private void stopScan() {
        mScanControlBtn.setText(getResources().getString(R.string.Btn_ScanBleDeviceActivity_Restart_Scan));
        mScanningDevicePv.setVisibility(View.INVISIBLE);
        mScanningDevicePv.stop();

        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mIsScanning = false;
    }

    /**
     * 连接Ble设备
     */
    private void startConnectGatt(final String mac) {
        //断开之前的连接
        Intent clearIntent = new Intent(BleSettingsActivity.this, BleManagerService.class);
        clearIntent.putExtra("CMD", BleManagerService.CMD_CLEAR_ALL);
        startService(clearIntent);
        //增加一个新的连接
        Intent connectIntent = new Intent(BleSettingsActivity.this, BleManagerService.class);
        connectIntent.putExtra("CMD", BleManagerService.CMD_ADD_NEW_CONNECT);
        connectIntent.putExtra("address", mac);
        startService(connectIntent);
    }
}
