package com.mycj.beasun.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mycj.beasun.service.util.SharedPreferenceUtil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.util.Log;

public abstract class AbstractSimpleBluetooth implements IBluetooth {
	public final static String DEVICE_NAME = "DEVICE_NAME";
	public final static String DEVICE_ADDRESS = "DEVICE_ADDRESS";
//	public final static String BLE_SERVICE = "00001523-1212-efde-1523-785feabcd123";
//	public final static String BLE_CHARACTERISTIC_NOTIFY = "00001524-1212-efde-1523-785feabcd123";
//	public final static String BLE_CHARACTERISTIC_WRITE = "00001525-1212-efde-1523-785feabcd123";
	public final static String BLE_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
	public final static String BLE_CHARACTERISTIC_NOTIFY = "0000fff1-0000-1000-8000-00805f9b34fb";
	public final static String BLE_CHARACTERISTIC_WRITE = "0000fff2-0000-1000-8000-00805f9b34fb";
	public final static String DESC_CCC = "00002902-0000-1000-8000-00805f9b34fb";
	private BluetoothAdapter mBluetoothAdapter;
	private Context context;
	private SimpleLeScanCallback scanCallBack = new SimpleLeScanCallback();
	/** 待连接的device **/
	private List<BluetoothDevice> devices =  new ArrayList<BluetoothDevice>();
	/** 连接好的gatt **/
	private List<BluetoothGatt> connectedGatts = new ArrayList<BluetoothGatt>();
	public void addToConnectedGatts(BluetoothGatt gatt){
		if (!connectedGatts.contains(gatt)) {
			this.connectedGatts.add(gatt);
		}
	}
	public List<BluetoothGatt> getConnectedGatts(){
		return this.connectedGatts;	
	}
	
	
	public List<BluetoothDevice> getDevices(){
		return this.devices;
	}
	public void addDevices(BluetoothDevice device){
		if (device==null ) {
			return ;
		}
		if (!devices.contains(device)) {
			devices.add(device);
		}
	}
	

	public AbstractSimpleBluetooth(Context context,BluetoothAdapter mBluetoothAdapter) {
		this.mBluetoothAdapter = mBluetoothAdapter;
		this.context = context;
	}

	@Override
	public boolean isEnable() {
		if (mBluetoothAdapter != null) {
			return mBluetoothAdapter.isEnabled();
		} else {
			return false;
		}
	}
	
	
	@Override
	public void startScan() {
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.startLeScan(scanCallBack);
		}
	}

	@Override
	public void stopScan() {
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.stopLeScan(scanCallBack);
		}
	}
	@Override
	public void readRemoteRssi() {
	}
	public void readRemoteRssi(BluetoothGatt gatt) {
		if (gatt != null) {
			gatt.readRemoteRssi();
		}
	}
	
	public boolean connectDevices() {
		return this.connectDevices(devices);
	}
	public boolean connectDevices( List<BluetoothDevice> devices ) {
		
		if (devices==null || mBluetoothAdapter == null) {
			return false;
		}
		if (devices.size()<=0) {
			return false;
		}
		for (BluetoothDevice device : devices) {
			device.connectGatt(context, true, new SimpleBluetoothGattCallBack());
		}
		return true;
	}
	
	@Override
	public BluetoothGatt connect(BluetoothDevice device) {
		Log.e("", "连接开始");
		if (device == null || mBluetoothAdapter == null) {
			Log.e("", "device空");
			return null;
		}
		BluetoothGatt connectGatt = device.connectGatt(context, false, new SimpleBluetoothGattCallBack());
		return connectGatt;
		
	}
	
	/**
	 * @param address 蓝牙地址 
	 * @return BluetoothGatt 一个蓝牙地址返回一个 gatt
	 */
	public BluetoothGatt connectAddress(String address) {
		BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(address);
		//连接前gatt.close() 并删除？
		for (BluetoothGatt gatt:connectedGatts) {
			if(gatt.getDevice().getAddress().equals(remoteDevice.getAddress())){
				gatt.close();
				connectedGatts.remove(gatt);
			}
		}
		if (remoteDevice!=null) {
			return connect(remoteDevice);
		}
		return null;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public void close() {
		
	}
	public boolean isBinded(){
//		String name = (String) SharedPreferenceUtil.get(context, DEVICE_NAME, "");
		String address = (String) SharedPreferenceUtil.get(context, DEVICE_ADDRESS, "--");
		if (!address.equals("--") ) {//地址 空则 返回false
			return true;
		}else{
			return false;
		}
	};
	
	public String getBindedName(){
		return (String) SharedPreferenceUtil.get(context, DEVICE_NAME, "--");
	};
	public String getBindedAddress(){
		return (String) SharedPreferenceUtil.get(context, DEVICE_ADDRESS, "--");
	};

	@Override
	public void write(BluetoothGattCharacteristic characteristic) {
	}
	
	@Override
	public void saveDevice(BluetoothDevice device){
		if (device!=null) {
			SharedPreferenceUtil.put(context, DEVICE_NAME,device.getName() );
			SharedPreferenceUtil.put(context, DEVICE_ADDRESS,device.getAddress() );
		}
	};
	
	

	@Override
	public void unSaveDevice() {
			SharedPreferenceUtil.put(context, DEVICE_NAME,"--" );
			SharedPreferenceUtil.put(context, DEVICE_ADDRESS,"--");
	}



	/**
	 * 连接CallBack
	 * @author Administrator
	 *
	 */
	private class SimpleBluetoothGattCallBack extends BluetoothGattCallback{

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			onCharacteristicChangedCallBack(gatt,characteristic);
			Log.e("AbstractSimpleBluetooth", ">>>>>>>> onCharacteristicChanged <<<<<<<<<<<");
			super.onCharacteristicChanged(gatt, characteristic);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			
			super.onCharacteristicRead(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			Log.e("AbstractSimpleBluetooth", ">>>>>>>> onCharacteristicWrite <<<<<<<<<<<");
			onCharacteristicWriteCallBack(gatt,characteristic,status);
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			Log.e("AbstractSimpleBluetooth", ">>>>>>>> onConnectionStateChange <<<<<<<<<<< newState:" + newState);
			
//			if (!gatt.equals(mBluetoothGatt)) {
//				return;
//			}
//			
			onConnectionStateChangeCallBack(gatt,status,newState);
			super.onConnectionStateChange(gatt, status, newState);
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			Log.e("AbstractSimpleBluetooth", ">>>>>>>> onReadRemoteRssi <<<<<<<<<<<  rssi" + rssi);
			onReadRemoteRssiCallBack(gatt,rssi,status);
			super.onReadRemoteRssi(gatt, rssi, status);
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
			Log.e("AbstractSimpleBluetooth", ">>>>>>>> onReliableWriteCompleted <<<<<<<<<<<");
			Log.e("", "写成功了？onReliableWriteCompleted()");
			super.onReliableWriteCompleted(gatt, status);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.e("AbstractSimpleBluetooth", ">>>>>>>> onServicesDiscovered <<<<<<<<<<<");
	           if (status == BluetoothGatt.GATT_SUCCESS) {
	        	   onServicesDiscoveredCallBack(gatt,status);
	           }
			super.onServicesDiscovered(gatt, status);
		}
		
	}
	/**
	 * 搜索CallBack
	 * @author Administrator
	 *
	 */
	private class SimpleLeScanCallback implements LeScanCallback {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			Log.e("AbstractSimpleBluetooth", ">>>>>>>> onLeScan <<<<<<<<<<<" );
			onLeScanCallBack(device, rssi, scanRecord);
		}
	}
	
	
	
	// some callbacks
	public abstract void onLeScanCallBack(BluetoothDevice device, int rssi, byte[] scanRecord);

	public abstract void onServicesDiscoveredCallBack(BluetoothGatt gatt, int status) ;

	public abstract void onReadRemoteRssiCallBack(BluetoothGatt gatt, int rssi, int status) ;

	public abstract void onConnectionStateChangeCallBack(BluetoothGatt gatt, int status, int newState) ;

	public abstract void onCharacteristicWriteCallBack(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) ;

	public abstract void onCharacteristicChangedCallBack(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) ;


	@Override
	public void closeAll() {
		if (connectedGatts.size()>0) {
			for(BluetoothGatt gatt: connectedGatts){
				gatt.disconnect();
				gatt.close();
			}
		}
	}

	@Override
	public void writeMap(BluetoothGattCharacteristic characteristic) {
		if (connectedGatts.size()>0) {
			for(BluetoothGatt gatt: connectedGatts){
				gatt.writeCharacteristic(characteristic);
			}
		}
	}
}
