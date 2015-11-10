package com.mycj.beasun.service;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public interface IBluetooth {

	/**
	 * 蓝牙是否可用
	 * @return
	 */
	public boolean isEnable();
	/**
	 * 开始搜索
	 */
	public void startScan();
	
	/**
	 * 结束搜索
	 */
	public void stopScan();
	/**
	 * 连接蓝牙
	 * @return BluetoothGatt 一个设备 返回 一个 gatt
	 * @param device
	 */
	public BluetoothGatt connect(BluetoothDevice device);
	/**
	 * 连接蓝牙 多连
	 * @param device
	 */
	public boolean connectDevices();
	
	/**
	 * 断开连接
	 */
	public void disconnect();
	/**
	 * 关闭
	 */
	public void close();
	/**
	 * 关闭 多连
	 */
	public void closeAll();
	
	/**
	 * 
	 */
	public void write(BluetoothGattCharacteristic characteristic);
	/**
	 * 写 多连
	 */
	public void writeMap(BluetoothGattCharacteristic characteristic);
	
	/**
	 * 
	 */
	public void readRemoteRssi();
	
	/**
	 * save
	 */
	public void saveDevice(BluetoothDevice device);
	public void unSaveDevice();
	
	public boolean isBinded();
	public String getBindedName();
	public String getBindedAddress();
	
	
}
