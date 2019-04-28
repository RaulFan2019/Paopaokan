package cn.hwh.sdk.ble;

import android.os.Environment;

import java.io.File;
import java.util.UUID;


/**
 * BLE 的配置
 */
public class BLEConfig {
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    /* 心率相关的 service 和 characteristic */
    public static String HEART_RATE_SERVICE = "0000180d-0000-1000-8000-00805f9b34fb";
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String HEART_RATE_CHARACTERISTIC = "00002a38-0000-1000-8000-00805f9b34fb";

    public final static UUID UUID_HEART_RATE_SERVICE = UUID
            .fromString(HEART_RATE_SERVICE);
    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
            .fromString(HEART_RATE_MEASUREMENT);
    public final static UUID UUID_HEART_RATE_CHARACTERISTIC = UUID
            .fromString(HEART_RATE_CHARACTERISTIC);

    /* original 相关的 service 和 characteristic */
    public static final String HR_ORIGINAL_SERVICE = "0000c0e0-0000-1000-8000-00805f9b34fb";
    public static final String HR_ORIGINAL_MEASUREMENT = "0000c0e1-0000-1000-8000-00805f9b34fb";
    public static final String HR_ORIGINAL_CHARACTERISTIC = "00002a38-0000-1000-8000-00805f9b34fb";

    public final static UUID UUID_HR_ORIGINAL_SERVICE = UUID
            .fromString(HR_ORIGINAL_SERVICE);
    public final static UUID UUID_HR_ORIGINAL_MEASUREMENT = UUID
            .fromString(HR_ORIGINAL_MEASUREMENT);
    public final static UUID UUID_HR_ORIGINAL_CHARACTERISTIC = UUID
            .fromString(HR_ORIGINAL_CHARACTERISTIC);

//    public static final String HR_ORIGINAL_SERVICE = "0000c0e0-0000-1000-8000-00805f9b34fc";
//    public static final String HR_ORIGINAL_MEASUREMENT = "0000c0e1-0000-1000-8000-00805f9b34fc";
//    public static final String HR_ORIGINAL_CHARACTERISTIC = "0000c0e1-0000-1000-8000-00805f9b34fc";

    /* ODA 升级相关 */
    public static final String OAD_SERVICE = "F000FFC0-0451-4000-B000-000000000000";
    public static final String OAD_NOTIFY = "F000FFC1-0451-4000-B000-000000000000";
    public static final String OAD_REQUEST = "F000FFC2-0451-4000-B000-000000000000";

    public final static UUID UUID_OAD_SERVICE = UUID
            .fromString(OAD_SERVICE);
    public final static UUID UUID_OAD_NOTIFY = UUID
            .fromString(OAD_NOTIFY);
    public final static UUID UUID_OAD_REQUEST = UUID
            .fromString(OAD_REQUEST);

    public static final int TYPE_FIRMWARE = 2;
    public static final int TYPE_SOFTWARE = 1;

    /* Device Info 相关*/
    public static final String DEVICE_INFO_SERVICE  = "0000180a-0000-1000-8000-00805f9b34fb";
    public static final String DEVICE_NAME = "00002a00-0000-1000-8000-00805f9b34fb";
    public static final String SYSTEM_ID = "00002a23-0000-1000-8000-00805f9b34fb";
    public static final String MODEL_NUMBER = "00002a24-0000-1000-8000-00805f9b34fb";
    public static final String SERIAL_NUMBER = "00002a25-0000-1000-8000-00805f9b34fb";
    public static final String FIRMWARE_REVISION = "00002a26-0000-1000-8000-00805f9b34fb";
    public static final String HARDWARE_REVISION = "00002a27-0000-1000-8000-00805f9b34fb";
    public static final String SOFTWARE_REVISION = "00002a28-0000-1000-8000-00805f9b34fb";
    public static final String MANUFACTURER_NAME = "00002a29-0000-1000-8000-00805f9b34fb";
    public static final String PNP_ID = "00002a50-0000-1000-8000-00805f9b34fb";

    public final static UUID UUID_DEVICE_INFO_SERVICE = UUID
            .fromString(DEVICE_INFO_SERVICE);
    public final static UUID UUID_DEVICE_NAME= UUID
            .fromString(DEVICE_NAME);
    public final static UUID UUID_SYSTEM_ID = UUID
            .fromString(SYSTEM_ID);
    public final static UUID UUID_MODEL_NUMBER= UUID
            .fromString(MODEL_NUMBER);
    public final static UUID UUID_SERIAL_NUMBER = UUID
            .fromString(SERIAL_NUMBER);
    public final static UUID UUID_FIRMWARE_REVISION= UUID
            .fromString(FIRMWARE_REVISION);
    public final static UUID UUID_HARDWARE_REVISION = UUID
            .fromString(HARDWARE_REVISION);
    public final static UUID UUID_SOFTWARE_REVISION= UUID
            .fromString(SOFTWARE_REVISION);
    public final static UUID UUID_MANUFACTURER_NAME = UUID
            .fromString(MANUFACTURER_NAME);
    public final static UUID UUID_PNP_ID = UUID
            .fromString(PNP_ID);

    /* 私有服务 相关 */

    public static final String PRIVATE_SERVICE = "f000ff00-0451-4000-b000-000000000000";
    public static final String APP_TYPE = "0000ff01-0000-1000-8000-00805f9b34fb";
    public static final String BATTERY = "0000ff06-0000-1000-8000-00805f9b34fb";



    public static final UUID  UUID_PRIVATE_SERVICE = UUID.fromString(PRIVATE_SERVICE);

    public final static String STANDARD_DEVICE_NAME = "Polar";
//    public final static String STANDARD_DEVICE_NAME = "MIO";

    public final static UUID UUID_BATTERY = UUID
            .fromString(BATTERY);

    public final static UUID UUID_APP_TYPE = UUID
            .fromString(APP_TYPE);

    public final static String SAVE_PATH = Environment.getExternalStorageDirectory() + File.separator + "123Test";
}
