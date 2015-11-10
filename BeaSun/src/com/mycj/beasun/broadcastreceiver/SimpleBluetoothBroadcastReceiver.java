package com.mycj.beasun.broadcastreceiver;


import android.bluetooth.BluetoothDevice;

/**
 * 蓝牙服务的各种action 广播
 * @author Administrator
 *
 */
public  class SimpleBluetoothBroadcastReceiver extends AbstractSimpleBluetoothBroadcastReceiver{
	/**
	 * 蓝牙连接
	 * @param state
	 */
	@Override
	public void doBlueConnect(int state,String address) {
		
	}
	
	/**
	 * 蓝牙断开
	 * @param state
	 */
	@Override
	public void doBlueDisconnect(int state,String address) {
		
	}
	
	/**
	 * 找到匹配设备
	 */
	@Override
	public void doDiscoveredWriteService(BluetoothDevice device) {
		
	}
	
	/**
	 * 搜索到设备
	 * @param device
	 */
	@Override
	public void doDeviceFound(BluetoothDevice device,int rssi) {
		
	}
	
	/**
	 * 断线提醒（重连5秒后）
	 */
	@Override
	public void doDisconnectRemind() {
		
	}
	
	/**
	 * 拍照
	 */
	@Override
	public void doCamera() {
		
	}
	
	/**
	 * 计步 0卡路里1 时间2	 
	 */
	@Override
	public void doStepAndCalReceiver(long[] data) {
		
	}

	/**
	 * 同步开始
	 */
	@Override
	public void doSyncStart() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 同步结束
	 */
	@Override
	public void doSyncEnd() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 链接失败
	 */
	@Override
	public void doDiscoveredWrongService() {
		
	}

	@Override
	public void doRemoteRssi(int rssi,String address) {
		
	}

	@Override
	public void doElectChange(int elect, String address) {
		
	}

	@Override
	public void doTimeChange(int time) {
		
	}
	
	

}
