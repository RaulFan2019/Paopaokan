package cn.hwh.sdk.ble;

/**
 * Created by Administrator on 2016/8/18.
 */
public class EventBleEdit {

    public static final int STATE_SUCCESS = 0x01;
    public static final int STATE_FAIL = 0x02;

    public static final int EDIT_DEVICE_NAME = 0x03;

    public int state;
    public int editType;
    public String address;

    public String editName;//修改的设备名称

    public EventBleEdit(int state, int editType, String address, String name) {
        this.state = state;
        this.editType = editType;
        this.address = address;
        this.editName = name;
    }
}
