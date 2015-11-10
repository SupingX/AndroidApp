//package com.mycj.beasun.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import com.mycj.beasun.bean.DeviceJson;
//import com.mycj.beasun.bean.MassageInfo;
//import com.mycj.beasun.business.BluetoothJson;
//import com.mycj.beasun.business.MassageUtil;
//import com.mycj.beasun.business.ProtoclNotify;
//import com.mycj.beasun.business.ProtoclWrite;
//import com.mycj.beasun.service.util.L;
//
//import android.app.Service;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothManager;
//import android.bluetooth.BluetoothProfile;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Binder;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.util.Log;
//
///**
// * 多连 蓝牙服务
// * 
// * @author Administrator
// *
// */
//public abstract class AbstractSimpleBlueService extends Service {
//	private final String TAG = "SimpleBlueService";
//	public final static String ACTION_DEVICE_FOUND = "ACTION_DEVICE_FOUND";
//	public final static String ACTION_SERVICE_DISCOVERED_WRITE_DEVICE = "ACTION_SERVICE_DISCOVERED_WRITE_DEVICE";
//	public final static String ACTION_SERVICE_MASSAGE_TIME = "ACTION_SERVICE_MASSAGE_STOP";
//	public final static String ACTION_SERVICE_DISCOVERED_WRONG_DEVICE = "ACTION_SERVICE_DISCOVERED_WRONG_DEVICE";
//	public final static String ACTION_REMOTE_RSSI = "ACTION_REMOTE_RSSI";
//	public final static String ACTION_CONNECTION_STATE = "ACTION_CONNECTION_STATE";
//	public final static String ACTION_ELECTRICITY = "ACTION_ELECTRICITY";
//	public final static String ACTION_SERVICE_DISCONNECTED_FOR_REMIND = "ACTION_SERVICE_DISCONNECTED_FOR_REMIND";
//	public final static String EXTRA_DEVICE = "EXTRA_DEVICE";
//	public final static String EXTRA_TIME = "EXTRA_TIME";
//	public final static String EXTRA_RSSI = "EXTRA_RSSI";
//	public final static String EXTRA_ELECT = "EXTRA_ELEC";
//	public final static String EXTRA_ADDRESS = "EXTRA_ADDRESS";
//	public final static String EXTRA_CONNECT_STATE = "EXTRA_CONNECT_STATE";
//	public final static String FILE_NAME_JSON = "json.ds";
//	private boolean isPrint = false;
//	private long lastDiscoveredServiceTime = 0L;
//	private BluetoothManager mBluetoothManager;
//	private BluetoothAdapter mBluetoothAdapter;
//	private AbstractSimpleBluetooth mSimpleBluetooth;
//	private MyBinder myBinder = new MyBinder();
//	private boolean isScanning;
//	public Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0xF1:
//				stopScan();
//				startScan();
//				break;
//			case 0xFC:
//				if (currentMassageInfo != null) {
//					int timePoint = currentMassageInfo.getTime();
//					Log.e("", "===========================================");
//					Log.e("", "=== 	当前剩余按摩时间： " + timePoint);
//					Log.e("", "===========================================");
//					timePoint--;
//					if (timePoint <= 0) {
//						stopTimer();
//						writeCharacteristic(ProtoclWrite.instance().protoclWriteForControl(0x03));
//						massageState = 0;
//						currentMassageInfo = null;
//						timePoint = 0;
//						sendBroadcastForMassageStop(timePoint);
//						return ;
//					}
//					currentMassageInfo.setTime(timePoint);
//					sendBroadcastForMassageStop(timePoint);
//					mHandler.postDelayed(runTimer, 60 * 1000);
//				}
//				break;
//			default:
//				break;
//			}
//		};
//	};
//	private MassageInfo currentMassageInfo;
//	private Runnable runTimer = new Runnable() {
//		public void run() {
//			Message message = new Message();
//			message.what = 0xFC;
//			mHandler.sendMessage(message);
//		}
//	};
//
//	public void startTimer() {
//		mHandler.removeCallbacks(runTimer);
//		mHandler.postDelayed(runTimer, 60 * 1000);
//	}
//
//	public void stopTimer() {
//		mHandler.removeCallbacks(runTimer);
//		writeCharacteristic(ProtoclWrite.instance().protoclWriteForControl(0x03));
//		sendBroadcastForMassageStop(0);
//	}
//
//	public void setMassageInfo(MassageInfo info) {
//		// 当相同时按摩信息时，不替换
//		// if (currentMassageInfo!=null && currentMassageInfo.equals(info)) {
//		// Log.i("", "同一个按摩信息，不用替换");
//		// return;
//		// }
//		Log.i("", "替换按摩信息");
//		this.currentMassageInfo = info;
//	}
//	
//	public void writeListByte(){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//			writeCharacteristic(ProtoclWrite.instance().protoclWriteForSingle("F5"));
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				// 设置强度
//			writeCharacteristic(ProtoclWrite.instance().protoclWriteForLevel(3));
//			try {
//				Thread.sleep(1500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//				// 开始按摩
//				writeCharacteristic(ProtoclWrite.instance().protoclWriteForControl(0x01));
//				// 更新状态
//			
//			}
//		}).start();
//	}
//	
//	
//	public MassageInfo getMassageInfo() {
//		return this.currentMassageInfo;
//	}
//
//	public void updateTime(int time){
//		this.currentMassageInfo.setTime(time);
//		startTimer();
//	}
//	
//	public boolean isSameMassageInfo(MassageInfo info) {
//		if (currentMassageInfo == null || info == null) {
//			return false;
//		}
//		Log.i("", "info" + info.getIndex());
//		Log.i("", "currentMassageInfo" + currentMassageInfo.getIndex());
//		return currentMassageInfo.equals(info);
//	}
//
//	/**
//	 * 开始1 结束0 停止2
//	 */
//	private int massageState;
//	
//
//	public int getMassageState() {
//		return this.massageState;
//	}
//
//	/**
//	 * 获取某个地址的连接状态
//	 * 
//	 * @param address
//	 * @return
//	 */
//	public int getGattState(String address) {
//		List<BluetoothGatt> gatts = mSimpleBluetooth.getConnectedGatts();
//		if (gatts != null && gatts.size() > 0) {
//			for (BluetoothGatt gatt : gatts) {
//				BluetoothDevice device = gatt.getDevice();
//				if (device.getAddress().equals(address)) {
//
//					Log.i("", "地址-->address的蓝牙状态是 ：" + 0);
//					return 0;
//				}
//			}
//		}
//		return -1;
//	}
//
//	/**
//	 * 按摩 开始 根据当前保存的currentMassageInfo
//	 */
//	public void startMassage() {
//		if (currentMassageInfo == null) {
//			return;
//		}
//
//		// 设置模式
//		int model_1 = currentMassageInfo.getModel_1();
//		int model_2 = currentMassageInfo.getModel_2();
//		if (currentMassageInfo.getIsPix() == 1) {
//			// 混合模式
//			logV("按摩---------混合模式 即将开始");
//			writeCharacteristic(ProtoclWrite.instance().protoclWriteForIntelligent(model_1, model_2));
//		} else {
//			// 单一模式
//			logV("按摩---------单一模式 即将开始");
//			String singleHex = MassageUtil.getSingleHex(model_1, model_2);
//			writeCharacteristic(ProtoclWrite.instance().protoclWriteForSingle(singleHex));
//		}
//		// 设置强度
//		writeCharacteristic(ProtoclWrite.instance().protoclWriteForLevel(currentMassageInfo.getPower()));
//		// 开始按摩
//		writeCharacteristic(ProtoclWrite.instance().protoclWriteForControl(0x01));
//		// 更新状态
//		massageState = 1;
//		// 计时
//		startTimer();
//	}
//
//	/**
//	 * 按摩 停止
//	 */
//	public void stopMassage() {
//		// 开始按摩
//		writeCharacteristic(ProtoclWrite.instance().protoclWriteForControl(0x03));
//		massageState = 0;
//		currentMassageInfo = null;
//		stopTimer();
//	}
//
//	/**
//	 * 按摩 暂停
//	 */
//	public void pauseMassage() {
//		// 暂停按摩
//		writeCharacteristic(ProtoclWrite.instance().protoclWriteForControl(0x02));
//		massageState = 2;
//	}
//	
//	public int getConnectedGattCount(){
//		List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//		if (connectedGatts!=null) {
//			return connectedGatts.size();
//		}
//		return 0;
//	}
//	
//	/**
//	 * 写数据
//	 * 
//	 * @param order
//	 */
//	public void writeCharacteristic(byte[] order) {
//		// 遍历所有gatt
//		// 逐一写数据
//		// 因为每一个gatt在onServicesDiscoveredCallBack()中已经设置notify write 。
//		// 只需在gatt中取出write
//		List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//		if (connectedGatts == null
//		// || connectedGatts.size() == 0
//		) {
//			return;
//		}
//		for (BluetoothGatt gatt : connectedGatts) {
//			if (gatt != null) {
//				BluetoothGattService service = gatt.getService(UUID.fromString(AbstractSimpleBluetooth.BLE_SERVICE));
//				BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(AbstractSimpleBluetooth.BLE_CHARACTERISTIC_WRITE));
//				characteristic.setValue(order);
//				logV("gatt : " + gatt + " , 开始写Characteristic : " + order);
//				gatt.writeCharacteristic(characteristic);
//			}
//		}
//	}
//
//	public boolean isGattEmpty() {
//		List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//		return connectedGatts == null || connectedGatts.size() == 0;
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return myBinder;
//	}
//
//	private Runnable runScan = new Runnable() {
//
//		@Override
//		public void run() {
//			scanDevice(true);
//			// 30秒后暂停重新搜索
//			mHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					mHandler.sendEmptyMessage(0xF1);
//				}
//			}, 10 * 1000);
//		}
//	};
//
//	public void startScan() {
//		runScan = new Runnable() {
//			@Override
//			public void run() {
//				scanDevice(true);
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						mHandler.sendEmptyMessage(0xF1);
//					}
//				}, 10*1000);
//			}
//		};
//		mHandler.postDelayed(runScan, 1000);
//	}
//
//	public void stopScan() {
//		scanDevice(false);
//		mHandler.removeCallbacks(runScan);
//		runScan= null;
//
//	}
//
//	public void scanDevice(boolean scan) {
//		if (scan) {
//			isScanning = true;
//			mSimpleBluetooth.startScan();
//		} else {
//			isScanning = false;
//			mSimpleBluetooth.stopScan();
//		}
//	}
//
//	public boolean isScanning() {
//		return this.isScanning;
//	}
//
//	public void closeAll() {
//		// mSimpleBluetooth.close();
//		List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//		if (connectedGatts == null || connectedGatts.size() == 0) {
//			return;
//		}
//		for (BluetoothGatt gatt : connectedGatts) {
//			gatt.close();
//			gatt=null;
//		}
//		connectedGatts.clear();
//	}
//
//	public void close(String address) {
//		Log.i("", "关闭连接 :" + address);
//		List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//		if (connectedGatts == null || connectedGatts.size() == 0) {
//			return;
//		}
//		for (BluetoothGatt gatt : connectedGatts) {
//			if (gatt.getDevice().getAddress().equals(address)) {
//				gatt.disconnect();
//				gatt.close();
//				connectedGatts.remove(gatt);
//			}
//		}
//
//	}
//	
//	public void shutDown(String address) {
//		Log.i("", "关闭连接 :" + address);
//		List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//		if (connectedGatts == null || connectedGatts.size() == 0) {
//			return;
//		}
//		for (BluetoothGatt gatt : connectedGatts) {
//			if (gatt.getDevice().getAddress().equals(address)) {
//				BluetoothGattService service = gatt.getService(UUID.fromString(AbstractSimpleBluetooth.BLE_SERVICE));
//				BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(AbstractSimpleBluetooth.BLE_CHARACTERISTIC_WRITE));
//				characteristic.setValue(ProtoclWrite.instance().protoclWriteForShutDown());
//				logV("address : " + address + "  关机 ");
//				gatt.writeCharacteristic(characteristic);
//			}
//		}
//
//	}
//
//	public boolean isEnable() {
//		return mSimpleBluetooth.isEnable();
//	}
//
//	public void setIsPrint(boolean ip) {
//		this.isPrint = ip;
//	}
//
//	public void connectAddress(String address) {
//		mHandler.removeCallbacks(checkisServiceFindRunnable);
//		checkisServiceFindRunnable = null;
//		final BluetoothGatt gatt = mSimpleBluetooth.connectAddress(address);
//		checkisServiceFindRunnable = new Runnable() {
//			@Override
//			public void run() {
//				checkIsConnectService(gatt);
//			};
//		};
//		mHandler.postDelayed(checkisServiceFindRunnable, 10 * 1000);
//	}
//
//	public void connectDevice(BluetoothDevice device) {
//		mHandler.removeCallbacks(checkisServiceFindRunnable);
//		checkisServiceFindRunnable = null;
//		final BluetoothGatt gatt = mSimpleBluetooth.connect(device);
//		checkisServiceFindRunnable = new Runnable() {
//			@Override
//			public void run() {
//				checkIsConnectService(gatt);
//			};
//		};
//		mHandler.postDelayed(checkisServiceFindRunnable, 10 * 1000);
//	}
//
//	public class MyBinder extends Binder {
//		public AbstractSimpleBlueService getService() {
//			return AbstractSimpleBlueService.this;
//		}
//	}
//
//	@Override
//	public void onCreate() {
//		logV("onCreate");
//		if (this.mBluetoothManager == null) {
//			mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
//		}
//		if (mBluetoothAdapter == null) {
//			// mBluetoothAdapter = mBluetoothManager.getAdapter();
//			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		}
//		mSimpleBluetooth = new SimpleBluetooth(getApplicationContext(), mBluetoothAdapter);
//		super.onCreate();
//	}
//
//	/**
//	 * 检查 是否 找到服务 。没有则关闭 gatt。
//	 * 
//	 * @param gatt
//	 */
//	private void checkIsConnectService( BluetoothGatt gatt) {
//		if (!isServiceFound) {
//			gatt.close();// 清空Gatt
//			// scanDevice(true); // 开始搜索
//			// 断开连接时，先遍历已经连接的Gatts，从中删除掉线的gatt
//			List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//			if (connectedGatts.contains(gatt)) {
//				connectedGatts.remove(gatt);
//				gatt=null;
//			}
//			// 发送通知
//			sendBroadcastForRemind();
//			onReconnectedOverTimeOut();
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		logE("service销毁！");
//		stopMassage();
//		currentMassageInfo = null;
//		closeAll();
//		
//		super.onDestroy();
//	}
//
//	public static IntentFilter getIntentFilter() {
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(ACTION_DEVICE_FOUND);
//		intentFilter.addAction(ACTION_SERVICE_DISCOVERED_WRITE_DEVICE);
//		intentFilter.addAction(ACTION_SERVICE_DISCOVERED_WRONG_DEVICE);
//		intentFilter.addAction(ACTION_REMOTE_RSSI);
//		intentFilter.addAction(ACTION_CONNECTION_STATE);
//		intentFilter.addAction(ACTION_SERVICE_DISCONNECTED_FOR_REMIND);
//		intentFilter.addAction(ACTION_ELECTRICITY);
//		intentFilter.addAction(ACTION_SERVICE_MASSAGE_TIME);
//		return intentFilter;
//	}
//
//	private void logV(String msg) {
//		Log.v(TAG, "**   " + msg + "  **");
//	}
//
//	private void logE(String msg) {
//		Log.e(TAG, "**   " + msg + "  **");
//	};
//
//	protected void sendBroadcastForDeviceFound(BluetoothDevice device, int rssi) {
//		Intent intent = new Intent(ACTION_DEVICE_FOUND);
//		intent.putExtra(EXTRA_DEVICE, device);
//		intent.putExtra(EXTRA_RSSI, rssi);
//		sendBroadcast(intent);
//	}
//
//	protected void doPrintDescoveredServices(List<BluetoothGattService> services) {
//		if (isPrint) {
//			if (services != null && services.size() > 0) {
//				Log.i("", "_____________________service________________________");
//				for (BluetoothGattService service : services) {
//					Log.i("", "__service : " + service.getUuid());
//					List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
//					if (characteristics != null && characteristics.size() > 0) {
//						Log.i("", "____________characteristic_____________");
//						for (BluetoothGattCharacteristic characteristic : characteristics) {
//							Log.i("", "_____characteristic : " + characteristic.getUuid());
//						}
//					}
//				}
//			}
//		}
//	}
//
//	protected void sendBroadcastForCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//		byte[] value = characteristic.getValue();
//		parseData(value);
//	}
//
//	protected void sendBroadcastForConnectionState(BluetoothGatt gatt, int newState) {
//		Intent intent = new Intent(ACTION_CONNECTION_STATE);
//		intent.putExtra(EXTRA_ADDRESS, gatt.getDevice().getAddress());
//		intent.putExtra(EXTRA_CONNECT_STATE, newState);
//		sendBroadcast(intent);
//
//	}
//
//	protected void sendBroadcastForRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//		Intent intent = new Intent(ACTION_REMOTE_RSSI);
//		String address = gatt.getDevice().getAddress();
//		intent.putExtra(EXTRA_ADDRESS, address);
//		intent.putExtra(EXTRA_RSSI, rssi);
//		sendBroadcast(intent);
//	}
//
//	protected void sendBroadcastForServiceDiscoveredWriteDevice(BluetoothGatt gatt, int status) {
//		Intent intent = new Intent(ACTION_SERVICE_DISCOVERED_WRITE_DEVICE);
//		intent.putExtra(EXTRA_DEVICE, gatt.getDevice());
//		sendBroadcast(intent);
//	}
//
//	protected void sendBroadcastForMassageStop(int time) {
//		Intent intent = new Intent(ACTION_SERVICE_MASSAGE_TIME);
//		intent.putExtra(EXTRA_TIME, time);
//		sendBroadcast(intent);
//	}
//
//	/**
//	 * 连接了错误的设备 不匹配service
//	 */
//	protected void sendBroadcastForServiceDiscoveredWrongDevice() {
//		Intent intent = new Intent(ACTION_SERVICE_DISCOVERED_WRONG_DEVICE);
//		sendBroadcast(intent);
//	}
//
//	/**
//	 * 断连连接超过5秒 ， 发送通知
//	 */
//	protected void sendBroadcastForRemind() {
//		Intent intent = new Intent(ACTION_SERVICE_DISCONNECTED_FOR_REMIND);
//		sendBroadcast(intent);
//	}
//
//	/**
//	 * 抽象方法 子类来实现 解析数据 并分发通知
//	 * 
//	 * @param value
//	 */
//	protected abstract void parseData(byte[] value);
//
//	/** 找到匹配的设备 **/
//	protected abstract void onServicediscoveredSuccess();
//
//	/** 重新连接超时 **/
//	protected abstract void onReconnectedOverTimeOut();
//
//	/** 写数据结束 **/
//	protected abstract void onWriteOver();
//
//	protected abstract void onServicediscoveredFail();
//
//	private boolean isServiceFound;
//	private Runnable checkisServiceFindRunnable;
//
//	private class SimpleBluetooth extends AbstractSimpleBluetooth {
//
//		public SimpleBluetooth(Context context, BluetoothAdapter mBluetoothAdapter) {
//			super(context, mBluetoothAdapter);
//		}
//
//		@Override
//		public void onLeScanCallBack(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//			sendBroadcastForDeviceFound(device, rssi);		
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					//存在时就不连接。
//					List<BluetoothGatt> gatts = mSimpleBluetooth.getConnectedGatts();
//					if (gatts != null) {
//						for (BluetoothGatt gatt : mSimpleBluetooth.getConnectedGatts()) {
//							if (gatt.getDevice().equals(device.getAddress())) {
//								Log.e("", "gatt已经连接");
//								return ;
//							}
//						}
//					}
//					List<DeviceJson> deviceJsons = BluetoothJson.loadJson(getApplicationContext());
//					if (deviceJsons != null && deviceJsons.size() > 0) {
//						for (DeviceJson deviceJson : deviceJsons) {
//							if (device.getAddress().equals(deviceJson.getAddress())) {
//								Log.e("", "发现本地保存设备 ，开始链接");
//								mHandler.postDelayed(new Runnable() {
//									@Override
//									public void run() {
//										connectDevice(device);
//									}
//								}, 1000);
//
//							}
//						}
//					}
//				}
//			}).start();
//
//		}
//
//		@Override
//		public void onServicesDiscoveredCallBack(final BluetoothGatt gatt, int status) {
//		if (status == BluetoothGatt.GATT_SUCCESS) {
//			L.i("onServicesDiscoveredCallBack() 成功！！！！！！！！");
//			doPrintDescoveredServices(gatt.getServices());
//			BluetoothGattService mBluetoothGattService = gatt.getService(UUID.fromString(AbstractSimpleBluetooth.BLE_SERVICE));
//			if (mBluetoothGattService != null) {
//				L.i("匹配service");
//				// 获取write
//				final BluetoothGattCharacteristic characteristicWrite = mBluetoothGattService.getCharacteristic(UUID.fromString(BLE_CHARACTERISTIC_WRITE));
//				// 获取notify
//				BluetoothGattCharacteristic characteristicNotify = mBluetoothGattService.getCharacteristic(UUID.fromString(BLE_CHARACTERISTIC_NOTIFY));
//				// 匹配成功 系列操作
//				if (characteristicWrite != null && characteristicNotify != null) {
//					mHandler.removeCallbacks(checkisServiceFindRunnable);
//					L.i("", "匹配characteristicWrite");
//					L.i("", "匹配characteristicNotify");
//					// 1。设置write
//					characteristicWrite.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);// 不带相应
//					// 2。设置notify
//					gatt.setCharacteristicNotification(characteristicNotify, true);
//					BluetoothGattDescriptor descriptor = characteristicNotify.getDescriptor(UUID.fromString(DESC_CCC));
//					descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//					gatt.writeDescriptor(descriptor);
//					;// 3。添加到map
//					mSimpleBluetooth.addToConnectedGatts(gatt);
//					// 4.保存json
//					// saveDevice(device);
//					// saveGattsToSdcard();
//					// 发送通知
//					sendBroadcastForServiceDiscoveredWriteDevice(gatt, status);
//					mHandler.postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							if (currentMassageInfo != null) {
//								
//								// 设置模式
//								int model_1 = currentMassageInfo.getModel_1();
//								int model_2 = currentMassageInfo.getModel_2();
//								if (currentMassageInfo.getIsPix() == 1) {
//									// 混合模式
//									logV("按摩---------混合模式 即将开始");
//									characteristicWrite.setValue(ProtoclWrite.instance().protoclWriteForIntelligent(model_1, model_2));
//									gatt.writeCharacteristic(characteristicWrite);
//								} else {
//									// 单一模式
//									logV("按摩---------单一模式 即将开始");
//									String singleHex = MassageUtil.getSingleHex(model_1, model_2);
//									characteristicWrite.setValue(ProtoclWrite.instance().protoclWriteForSingle(singleHex));
//									gatt.writeCharacteristic(characteristicWrite);
//								}
//								
//								// 设置强度
//								characteristicWrite.setValue(ProtoclWrite.instance().protoclWriteForLevel(currentMassageInfo.getPower()));
//								gatt.writeCharacteristic(characteristicWrite);
//								// 开始按摩
//								characteristicWrite.setValue(ProtoclWrite.instance().protoclWriteForControl(0x01));
//								gatt.writeCharacteristic(characteristicWrite);
//								}
//						}
//					}, 500);
//					
//					
//					isServiceFound = true;
//					/** ----------------耗时操作用？？？------------- **/
////					long currentTimeMillis = System.currentTimeMillis();
////					// 如果本次链接服务 和上次链接服务的时间差大于5秒
////					if (currentTimeMillis - lastDiscoveredServiceTime > 5000) {
////						L.i("服务距离上次链接超过5秒，所以需要重新同步手机设置");
////						lastDiscoveredServiceTime = currentTimeMillis;
////						onServicediscoveredSuccess();
////					} else {
////						L.i("服务距离上次链接没有超过5秒，所以不需要重新同步手机设置");
////					}
//					/** ----------------耗时操作用？？？------------- **/
//
//					// 停止搜索？
//					// scanDevice(false);
//				} else {
//					// isServiceFound = false;
//					// L.i("没有找到匹配服务，关闭Gatt");
//					// // 没有连接匹配的服务,断开连接.防止连接不匹配的蓝牙?
//					// gatt.disconnect();
//					// gatt.close();
//					// sendBroadcastForServiceDiscoveredWrongDevice();
//					// onServicediscoveredFail();
//				}
//			} else {
//				// isServiceFound = false;
//				// L.i("没有找到匹配服务，关闭Gatt");
//				// gatt.disconnect();
//				// gatt.close();
//				// sendBroadcastForServiceDiscoveredWrongDevice();
//				// onServicediscoveredFail();
//			}
//		}else{
//			L.i("onServicesDiscoveredCallBack() 失败！！！！！！！！");
//		}
//		}
//
//		@Override
//		public void onReadRemoteRssiCallBack(BluetoothGatt gatt, int rssi, int status) {
//			sendBroadcastForRemoteRssi(gatt, rssi, status);
//		}
//
//		@Override
//		public void onConnectionStateChangeCallBack(BluetoothGatt gatt, int status, int newState) {
//			logE("-->  onConnectionStateChangeCallBack : [ newState : " + newState + "]");
//			sendBroadcastForConnectionState(gatt, newState);
//			
//			if (status==BluetoothGatt.GATT_SUCCESS) {
//				
//			
//			switch (newState) {
//		
//			case BluetoothProfile.STATE_CONNECTED:
//				gatt.discoverServices();
//				break;
//			case BluetoothProfile.STATE_DISCONNECTING:
//				logV("断开中");
//				break;
//			case BluetoothProfile.STATE_CONNECTING:
//				logV("连接中");
//				break;
//			case BluetoothProfile.STATE_DISCONNECTED:
//				logV("已断开");
//				isServiceFound = false;
//				gatt.close();// 清空Gatt
//				List<BluetoothGatt> connectedGatts = mSimpleBluetooth.getConnectedGatts();
//				if (connectedGatts.contains(gatt)) {
//					connectedGatts.remove(gatt);
//				}
//				gatt=null;
//				break;
//			default:
//				break;
//			}
//			}
//		}
//
//		@Override
//		public void onCharacteristicWriteCallBack(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//		}
//
//		@Override
//		public void onCharacteristicChangedCallBack(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//			sendBroadcastForCharacteristicChanged(gatt, characteristic);
//			parseData(gatt, characteristic);
//		}
//
//	}
//	
//	
//
//	public void parseData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//		ProtoclNotify notify = ProtoclNotify.instance();
//		int parseData = notify.parseData(characteristic.getValue());
//		if (parseData == 3) {
//			// 电量
//			int electricity = notify.getElectricity(characteristic.getValue());
//			sendBroadcastForElectricity(electricity, gatt);
//		}
//	}
//
//	protected void sendBroadcastForElectricity(int electricity, BluetoothGatt gatt) {
//		Intent intent = new Intent(ACTION_ELECTRICITY);
//		intent.putExtra(EXTRA_ELECT, electricity);
//		intent.putExtra(EXTRA_ADDRESS, gatt.getDevice().getAddress());
//		sendBroadcast(intent);
//	}
//}
