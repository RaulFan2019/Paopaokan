package com.app.pao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.model.BLEStateEntity;
import com.rey.material.widget.Button;

import java.util.List;

/**
 *	扫描蓝牙设备适配器
 */
public class ScanBleDeviceAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<BLEStateEntity> bleStateList;

	private OnItemBtnClickListener onItemBtnClickListener;

	public ScanBleDeviceAdapter(Context context, List<BLEStateEntity> bleStateList) {
		this.mInflater = LayoutInflater.from(context);
		this.bleStateList = bleStateList;
	}

	public void setOnItemBtnClickListener(OnItemBtnClickListener onItemBtnClickListener) {
		this.onItemBtnClickListener = onItemBtnClickListener;
	}

	@Override
	public int getCount() {
		return bleStateList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return bleStateList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BleViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_scan_ble_device, null);
			holder = new BleViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (BleViewHolder) convertView.getTag();
		}

		BLEStateEntity stateEntity = bleStateList.get(position);

		String device = stateEntity.mBleDevice.Device.getName();
		String mac = stateEntity.getmBleDevice().getDevice().getAddress();
		holder.mDeviceMacTv.setText("地址: " + mac);
		if (device == null || device.isEmpty()) {
			holder.mDeviceNameTv.setText("未知");
		} else {
			holder.mDeviceNameTv.setText(device);
		}

		if(stateEntity.mConnectState == BLEStateEntity.STATE_CONNECTING){
			holder.mConnectingTv.setVisibility(View.VISIBLE);
			holder.mDisConnectBtn.setVisibility(View.GONE);
		}else if(stateEntity.mConnectState == BLEStateEntity.STATE_CONNECTED){
			holder.mConnectingTv.setVisibility(View.GONE);
			holder.mDisConnectBtn.setVisibility(View.VISIBLE);
			holder.mDisConnectBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(onItemBtnClickListener != null){
						onItemBtnClickListener.onBtnClick(position);
					}
				}
			});
		}else{
			holder.mConnectingTv.setVisibility(View.GONE);
			holder.mDisConnectBtn.setVisibility(View.GONE);
		}

		return convertView;
	}

	/**
	 * 设置连接设备的状态显示
	 * @param deviceMac
	 * @param state
	 */
	public void setBleState(String deviceMac,int state){
		int pos = 0;
		boolean mHasDevice = false;
		for (int i= 0;i<bleStateList.size();i++){
			if(bleStateList.get(i).getmBleDevice().getDevice().getAddress().equals(deviceMac)){
				mHasDevice = true;
				pos = i;
				break;
			}
		}
		if(mHasDevice) {
			bleStateList.get(pos).setmConnectState(state);
			notifyDataSetChanged();
		}
	}


	class BleViewHolder {
		TextView mDeviceNameTv;
		TextView mDeviceMacTv;
		TextView mConnectingTv;
		Button mDisConnectBtn;
		FrameLayout mStateFl;


		public BleViewHolder(View convertView){
			mDeviceNameTv = (TextView) convertView.findViewById(R.id.tv_name);
			mDeviceMacTv = (TextView) convertView.findViewById(R.id.tv_mac);
			mConnectingTv = (TextView) convertView.findViewById(R.id.tv_connecting);
			mDisConnectBtn = (Button) convertView.findViewById(R.id.btn_disconnect);
			mStateFl = (FrameLayout) convertView.findViewById(R.id.fl_state);
		}
	}

	public interface OnItemBtnClickListener{
		public void onBtnClick(int position);
	}
}
