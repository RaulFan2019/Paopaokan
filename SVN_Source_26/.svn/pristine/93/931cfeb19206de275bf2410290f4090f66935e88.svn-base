package com.app.pao.entity.model;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * BLE蓝牙扫描实体类
 *
 * @author Raul
 */
public class BLEEntity implements Serializable{

    public BluetoothDevice Device;// 蓝牙设备
    public int rssi;// rssi


    public BLEEntity(BluetoothDevice Device, int rssi) {
        this.Device = Device;
        this.rssi = rssi;
    }

    public BluetoothDevice getDevice() {
        return Device;
    }

    public void setDevice(BluetoothDevice device) {
        Device = device;
    }
}
