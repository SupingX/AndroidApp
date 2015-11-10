package com.mycj.beasun.broadcastreceiver;


import com.mycj.beasun.R;
import com.mycj.beasun.service.XplBluetoothService;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 蓝牙服务的各种action
 * @author Administrator
 *
 */
public abstract class AbstractSimpleBluetoothBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(XplBluetoothService.ACTION_DEVICE_FOUND)) {
			BluetoothDevice device = intent.getParcelableExtra(XplBluetoothService.EXTRA_DEVICE);
			int rssi = intent.getExtras().getInt(XplBluetoothService.EXTRA_RSSI);
			doDeviceFound(device,rssi);
		} else if (action.equals(XplBluetoothService.ACTION_SERVICE_DISCOVERED)) {
//			Toast.makeText(context, context.getString(R.string.connected), Toast.LENGTH_SHORT).show();
			BluetoothDevice device = intent.getParcelableExtra(XplBluetoothService.EXTRA_DEVICE);
			doDiscoveredWriteService(device);
		} else if (action.equals(XplBluetoothService.ACTION_CONNECT_STATE)) {
			int state = intent.getExtras().getInt(XplBluetoothService.EXTRA_CONNECT_STATE);
			BluetoothDevice device = intent.getParcelableExtra(XplBluetoothService.EXTRA_DEVICE);
			String address = device.getAddress();
			
			switch (state) {
			case BluetoothGatt.STATE_CONNECTED:
				doBlueConnect(state,address);
				break;
			case BluetoothGatt.STATE_DISCONNECTED:
				doBlueDisconnect(state,address);
				break;

			default:
				break;
			}

		} else if (action.equals(XplBluetoothService.ACTION_ELECTRICITY)) {
			int elect  = intent.getExtras().getInt(XplBluetoothService.EXTRA_ELECT);
			String address  = intent.getExtras().getString(XplBluetoothService.EXTRA_ADDRESS);
			doElectChange(elect, address);
		}else if (action.equals(XplBluetoothService.ACTION_SERVICE_MASSAGE_TIME)) {
			int time  = intent.getExtras().getInt(XplBluetoothService.EXTRA_TIME);
			doTimeChange(time);
		}
		
	}
	public abstract void doTimeChange(int time);
	public abstract void doElectChange(int elect,String address);
	public abstract void doRemoteRssi(int rssi,String address);
	public abstract void doStepAndCalReceiver(long[] data);

	/**
	 * 拍照
	 */
	public abstract void doCamera();

	/**
	 * 断线提醒（重连5秒后）
	 */
	public abstract void doDisconnectRemind();

	/**
	 * 蓝牙连接
	 * 
	 * @param state
	 */
	public abstract void doBlueConnect(int state,String address);

	/**
	 * 蓝牙断开
	 * 
	 * @param state
	 */
	public abstract void doBlueDisconnect(int state,String address);

	/**
	 * 找到匹配设备
	 */
	public abstract void doDiscoveredWriteService(BluetoothDevice device);
	public abstract void doDiscoveredWrongService();

	/**
	 * 搜索到设备
	 * 
	 * @param device
	 */
	public abstract void doDeviceFound(BluetoothDevice device,int rssi);
	public abstract void doSyncStart();
	public abstract void doSyncEnd();
	

}
