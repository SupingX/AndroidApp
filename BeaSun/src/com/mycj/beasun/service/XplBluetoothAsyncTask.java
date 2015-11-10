package com.mycj.beasun.service;

import java.util.UUID;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.AsyncTask;
import android.util.Log;

public class XplBluetoothAsyncTask extends AsyncTask<byte[], Void, Void> {
	private BluetoothGatt gatt;
	private long sleepTime = 500L;
	public XplBluetoothAsyncTask(BluetoothGatt gatt){
		this.gatt = gatt;
	}
	public XplBluetoothAsyncTask(BluetoothGatt gatt,long sleepTime){
		this.gatt = gatt;
		this.sleepTime = sleepTime;
	}
	@Override
	protected Void doInBackground(byte[]... params) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BluetoothGattService service = gatt.getService(UUID.fromString(XplBluetoothManager.UUID_SERVICE));
		BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(XplBluetoothManager.UUID_CHARACTERISTIC_WRITE));
		characteristic.setValue(params[0]);
		
		gatt.writeCharacteristic(characteristic);
		return null;
	}

}
