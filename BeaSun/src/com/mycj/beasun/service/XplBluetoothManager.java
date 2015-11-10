package com.mycj.beasun.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mycj.beasun.service.util.DataUtil;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * 蓝牙控制
 * 
 * @author zeej
 *
 */
public class XplBluetoothManager {
	private final String TAG = XplBluetoothManager.class.getSimpleName();
	public final static String UUID_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
	public final static String UUID_CHARACTERISTIC_NOTIFY = "0000fff1-0000-1000-8000-00805f9b34fb";
	public final static String UUID_CHARACTERISTIC_WRITE = "0000fff2-0000-1000-8000-00805f9b34fb";
	public final static String UUID_DESC_CCC = "00002902-0000-1000-8000-00805f9b34fb";
	public final int REQUEST_ENABLE_BLUETOOTH = 0xF1;
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mbleBluetoothAdapter;
	private Context mContext;
	private boolean isScanning;
	/** 已经连接好的并且服务找到的gatt列表 **/
	private HashMap<String, BluetoothGatt> connectedDeviceMap = new HashMap<>();

	// private HashMap<String,BluetoothGatt> preDeviceMap = new HashMap<>();
	// public HashMap<String,BluetoothGatt> getPreDeviceMap(){
	// return preDeviceMap;
	// }
	// private Handler mHandler;
	public XplBluetoothManager(Context context) {
		mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		mbleBluetoothAdapter = mBluetoothManager.getAdapter();
		this.mContext = context;
	}

	public HashMap<String, BluetoothGatt> getConnectDeviceMap() {
		return this.connectedDeviceMap;
	}

	public boolean isEmpty() {
		return connectedDeviceMap == null || connectedDeviceMap.size() == 0;
	}

	/**
	 * 
	 * @return BluetoothManager
	 */
	public BluetoothManager getBluetoothManager() {
		return this.mBluetoothManager;
	}

	/**
	 * <p>
	 * 获取BluetoothAdapter
	 * </p>
	 * 注：这里通过getSystemService获取BluetoothManager，
	 * 再通过BluetoothManager获取BluetoothAdapter。BluetoothManager在Android4.3以上支持(API
	 * level 18)。
	 * 
	 * @return BluetoothAdapter
	 */
	public BluetoothAdapter getBluetoothAdapter() {
		return this.mbleBluetoothAdapter;
	}

	/**
	 * <p>
	 * 是否支持 Ble
	 * </p>
	 * <b> {uses-feature android:name="android.hardware.bluetooth_le"
	 * android:required="true"/></B> required为true时，则应用只能在支持BLE的Android设备上安装运行；<br/>
	 * required为false时，Android设备均可正常安装运行，需要在代码运行时判断设备是否支持BLE feature： <br/>
	 * 
	 * @return isSupport
	 */
	public boolean isSupportBle() {
		if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(mContext, "设备不支持低功耗蓝牙", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * <p>
	 * 判断是否打开蓝牙
	 * </p>
	 * 
	 * @return isEnable
	 */
	public boolean isBluetoothEnable() {
		if (mbleBluetoothAdapter == null || !mbleBluetoothAdapter.isEnabled()) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 通过 intent打开 蓝牙
	 * </p>
	 * 
	 * 在onActivityResult获取结果
	 * 
	 * @param Activity
	 */
	public void enableBluetooth(Activity ac) {
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		ac.startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
	}

	/**
	 * <p>
	 * 搜索设备
	 * </p>
	 * 
	 * @param enable
	 */
	public void scanDevice(boolean enable) {
		if (!isSupportBle()) {
			return;
		}
		if (!isBluetoothEnable()) {
			return;
		}
		if (enable) {
			//////Log.d(TAG, "开始搜索");
			mbleBluetoothAdapter.stopLeScan(mLeScanCallback);
			isScanning = true;
			mbleBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			//////Log.d(TAG, "结束搜索");
			isScanning = false;
			mbleBluetoothAdapter.stopLeScan(mLeScanCallback);
		}

		doScanStateChangeCallBack(isScanning);
	}

	/**
	 * <p>
	 * 是否在搜索
	 * </p>
	 * 
	 * @return enable
	 */
	public boolean isScanning() {
		return this.isScanning;
	}

	/**
	 * <p>
	 * 搜索指定serviceUuids设备
	 * </p>
	 * 
	 * @param serviceUuids
	 */
	public void scanDevice(UUID[] serviceUuids) {
		if (!isSupportBle()) {
			return;
		}
		if (!isBluetoothEnable()) {
			return;
		}
		//////Log.d(TAG, "结束搜索 指定serviceUuid ：" + serviceUuids.toString());

		mbleBluetoothAdapter.startLeScan(serviceUuids, mLeScanCallback);
		isScanning = true;
		doScanStateChangeCallBack(isScanning);
	}

	/**
	 * <p>
	 * 单连
	 * </p>
	 * 
	 * @param device
	 * @return
	 */
	public BluetoothGatt connectOne(final BluetoothDevice device) {
		BluetoothGatt gatt = null;
		if (connectedDeviceMap.size() > 0) {
			closeAll();
		}
		gatt = connect(device);
		return gatt;
	}

	/**
	 * <p>
	 * 连接设备
	 * </p>
	 * 
	 * @param device
	 * @return BluetoothGatt
	 */
	public BluetoothGatt connect(final BluetoothDevice device) {
		if (!isSupportBle()) {
			return null;
		}
		if (!isBluetoothEnable()) {
			return null;
		}
		if (device == null) {
			return null;
		}

		//////Log.d(TAG, "开始连接");
		//////Log.d(TAG, "结束连接" + device);
		BluetoothGatt gatt = device.connectGatt(mContext, true, mBluetoothGattCallback);
		//////Log.d(TAG, "结束连接" + gatt);
		// preDeviceMap.put(device.getAddress(), gatt);

		return gatt;
	}

	/**
	 * <p>
	 * 连接地址
	 * </p>
	 * 
	 * @param String
	 *            蓝牙地址
	 * @return BluetoothGatt
	 */
	public BluetoothGatt connect(String address) {
		if (address == null) {
			return null;
		}
		boolean checkBluetoothAddress = BluetoothAdapter.checkBluetoothAddress(address);
		if (!checkBluetoothAddress) {
			return null;
		}
		BluetoothDevice remoteDevice = mbleBluetoothAdapter.getRemoteDevice(address);
		BluetoothGatt gatt = connect(remoteDevice);
		return gatt;
	}

	/**
	 * <p>
	 * 获取蓝牙连接的gatt数量
	 * </p>
	 * 
	 * @return
	 */
	public int getConnectedGattSize() {
		if (connectedDeviceMap == null) {
			return 0;
		}
		return this.connectedDeviceMap.size();
	}

	/**
	 * <p>
	 * 获得指定设备是否连接
	 * </p>
	 * 
	 * @param device
	 * @return
	 */
	public boolean isBluetoothConnected(BluetoothDevice device) {
		if (device == null) {
			return false;
		}
		return this.isBluetoothConnected(device.getAddress());
	}

	/**
	 * 已连接的gatt中 是否存在对应地址
	 * 
	 * @param address
	 * @return
	 */
	private boolean isAddressExist(String address) {
		return connectedDeviceMap.containsKey(address);
	}

	/**
	 * 已连接的gatt中 是否存在对应地址
	 * 
	 * @param address
	 * @return
	 */
	public boolean isGattExist(BluetoothGatt gatt) {
		return connectedDeviceMap.containsValue(gatt);
	}

	/**
	 * <p>
	 * 获得指定地址是否连接
	 * </p>
	 * 
	 * @param device
	 * @return
	 */
	public boolean isBluetoothConnected(String address) {
		if (!checkBluetoothAddress(address)) {
			return false;
		}
		if (isAddressExist(address)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 检测地址是否合法
	 * </p>
	 * 
	 * @param address
	 * @return
	 */
	private boolean checkBluetoothAddress(String address) {
		return BluetoothAdapter.checkBluetoothAddress(address) || address == null;
	}

	/**
	 * <p>
	 * 关闭对应地址的gatt
	 * </p>
	 * 
	 * @param address
	 */
	public void close(String address) {
		printConnectGatt();
		if (!checkBluetoothAddress(address)) {
			return;
		}
		BluetoothGatt bluetoothGatt = connectedDeviceMap.get(address);
		if (bluetoothGatt != null) {
			bluetoothGatt.disconnect();
			bluetoothGatt.close();
			removeGattFromMap(bluetoothGatt);
			bluetoothGatt = null;
		}
		printConnectGatt();
	}

	/**
	 * <p>
	 * 关闭对应地址的gatt
	 * </p>
	 * 
	 * @param address
	 */
	public void close(BluetoothDevice device) {
		if (device == null) {
			return;
		}
		close(device.getAddress());
	}

	/**
	 * <p>
	 * 关闭所有gatt
	 * </p>
	 */
	public void closeAll() {
		if (!isBluetoothEnable()) {
			return;
		}
		Collection<BluetoothGatt> gatts = connectedDeviceMap.values();
		for (BluetoothGatt gatt : gatts) {
			gatt.close();
			gatt.disconnect();
			gatt = null;
		}
		connectedDeviceMap.clear();
		// preDeviceMap.clear();
	}

	/**
	 * <p>
	 * 写属性
	 * </p>
	 * 
	 * @param gatt
	 * @param order
	 */
	public void writeCharacteristic(BluetoothGatt gatt, byte[] order) {
		if (!isGattExist(gatt)) {
			return;
		}
		BluetoothGattService service = gatt.getService(UUID.fromString(UUID_SERVICE));
		BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_WRITE));
		characteristic.setValue(order);
		gatt.writeCharacteristic(characteristic);
		//////Log.d("writeCharacteristic", "writeCharacteristic :" + DataUtil.byteToHexString(order));
	}

	/**
	 * <p>
	 * 异步任务写属性
	 * </p>
	 * 
	 * @param gatt
	 * @param order
	 */
	public void writeCharacteristicAsync(BluetoothGatt gatt, byte[] order) {
		if (!isGattExist(gatt)) {
			return;
		}
		//////Log.d("writeCharacteristic", "order :" + DataUtil.byteToHexString(order));
		XplBluetoothAsyncTask task = new XplBluetoothAsyncTask(gatt, 200);
		task.execute(order);
	}

	/**
	 * <p>
	 * 根据地址写数据
	 * </p>
	 * 如果地址在已连接的 gatt中 存在对应的 地址 ，则写数据 。
	 * 
	 * @param address
	 * @param order
	 */
	public void writeCharacteristic(String address, byte[] order) {
		if (!isBluetoothEnable()) {
			return;
		}
		if (!checkBluetoothAddress(address)) {
			return;
		}
		if (!isAddressExist(address)) {
			return;
		}
		BluetoothGatt gatt = connectedDeviceMap.get(address);
		writeCharacteristic(gatt, order);
	}

	/**
	 * <p>
	 * 根据地址写数据
	 * </p>
	 * 如果地址在已连接的 gatt中 存在对应的 地址 ，则写数据 。
	 * 
	 * @param address
	 * @param order
	 */
	public void writeCharacteristicAsync(String address, byte[] order) {
		if (!isBluetoothEnable()) {
			return;
		}
		if (!checkBluetoothAddress(address)) {
			return;
		}
		if (!isAddressExist(address)) {
			return;
		}
		BluetoothGatt gatt = connectedDeviceMap.get(address);
		writeCharacteristicAsync(gatt, order);
	}

	/**
	 * <p>
	 * 多个gatt写数据
	 * <p>
	 * 
	 * @param order
	 */
	public void writeCharacteristicForMap(byte[] order) {
		if (connectedDeviceMap != null && connectedDeviceMap.size() > 0) {
			Collection<BluetoothGatt> gatts = connectedDeviceMap.values();
			for (BluetoothGatt gatt : gatts) {
				writeCharacteristic(gatt, order);
			}
		}
	}

	/**
	 * <p>
	 * 多个gatt写数据异步
	 * <p>
	 * 
	 * @param order
	 */
	public void writeCharacteristicForMapAsync(byte[] order) {
		if (connectedDeviceMap != null && connectedDeviceMap.size() > 0) {
			Collection<BluetoothGatt> gatts = connectedDeviceMap.values();
			//////Log.d("", "gatts size()" + gatts.size());
			for (BluetoothGatt gatt : gatts) {
				writeCharacteristicAsync(gatt, order);
			}
		}
	}

	/**
	 * <p>
	 * 添加gatt到已连接的gatt中
	 * </p>
	 * 
	 * @param gatt
	 */
	protected void addGattToMap(BluetoothGatt gatt) {
		if (gatt == null) {
			return;
		}
		String address = gatt.getDevice().getAddress();
		if (!isAddressExist(address)) {
			connectedDeviceMap.put(address, gatt);
		}
	}

	/**
	 * <p>
	 * 已连接的gatt中 删除gatt
	 * </p>
	 * 
	 * @param gatt
	 */
	protected void removeGattFromMap(BluetoothGatt gatt) {
		if (gatt == null) {
			return;
		}
		String address = gatt.getDevice().getAddress();
		if (isAddressExist(address)) {
			connectedDeviceMap.remove(address);
		}
	}

	/**
	 * <p>
	 * 已连接的gatt中 删除gatt
	 * </p>
	 * 
	 * @param gatt
	 */
	protected void removeGattFromMap(String address) {
		if (address == null) {
			return;
		}
		if (isAddressExist(address)) {
			connectedDeviceMap.remove(address);
		}
	}

	/**
	 * <p>
	 * 打印已连接的 Gatt
	 * </p>
	 */
	protected void printConnectGatt() {
		//////Log.d(TAG, "==	connectedDeviceMap : " + connectedDeviceMap + "	==");
		if (connectedDeviceMap != null && connectedDeviceMap.size() > 0) {
			//////Log.d(TAG, "==	size :	" + connectedDeviceMap.size() + "	==");
			Collection<BluetoothGatt> gatts = connectedDeviceMap.values();
			for (BluetoothGatt gatt : gatts) {
				//////Log.d(TAG, "==	address :	" + gatt.getDevice().getAddress() + "	==");
			}
		}
	}

	/**
	 * <p>
	 * 搜索回调
	 * </p>
	 * 
	 */
	private LeScanCallback mLeScanCallback = new LeScanCallback() {

		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			doLeScanCallBack(device, rssi, scanRecord);
		}
	};
	/**
	 * <p>
	 * gatt回调
	 * </p>
	 * 
	 */
	private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

		@Override
		public void onCharacteristicChanged(final BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//			//////Log.d(TAG, ">>> onCharacteristicChanged() <<<");
			doCharacteristicChangedCallback(gatt, characteristic);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			doCharacteristicReadCallback(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onCharacteristicRead()成功 <<<");
			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onCharacteristicRead()失败 <<<");
			}
			super.onCharacteristicRead(gatt, characteristic, status);
		}

		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//			doCharacteristicWriteCallback(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onCharacteristicWrite() 成功 <<<  " +DataUtil.byteToHexString(characteristic.getValue()));
			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onCharacteristicWrite() 失败 <<<");
			}
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			doConnectionStateChangeCallback(gatt, newState, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onConnectionStateChange() 成功 <<<");

			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onConnectionStateChange() 失败 <<<");
			}
			super.onConnectionStateChange(gatt, status, newState);
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			doDescriptorReadCallback(gatt, descriptor, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onDescriptorRead() 成功 <<<");

			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onDescriptorRead() 失败 <<<");
			}
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			doDescriptorWriteCallback(gatt, descriptor, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onDescriptorWrite() 成功 <<<");
			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onDescriptorWrite() 失败 <<<");
			}
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			doReadRemoteRssiCallback(gatt, rssi, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onReadRemoteRssi() 成功 <<<");
			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onReadRemoteRssi() 失败 <<<");
			}
			super.onReadRemoteRssi(gatt, rssi, status);
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
			doReliableWriteCompletedCallback(gatt, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onReliableWriteCompleted() 成功 <<<");
			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onReliableWriteCompleted() 失败 <<<");
			}
			super.onReliableWriteCompleted(gatt, status);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			doServicesDiscoveredCallback(gatt, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//////Log.d(TAG, ">>> onServicesDiscovered() 成功 <<<");
			} else if (status == BluetoothGatt.GATT_FAILURE) {
				//////Log.d(TAG, ">>> onServicesDiscovered() 失败 <<<");
			}
			super.onServicesDiscovered(gatt, status);
		}

	};

	// ************************ some callbacks ****************************//
	/**
	 * <p>
	 * 搜索设备回调
	 * </p>
	 * 
	 * @param device
	 * @param rssi
	 * @param scanRecord
	 */
	protected void doLeScanCallBack(BluetoothDevice device, int rssi, byte[] scanRecord) {

	}

	/**
	 * <p>
	 * 搜索状态改变时回调
	 * </p>
	 * 
	 * @param isScanning
	 */
	protected void doScanStateChangeCallBack(boolean isScanning) {

	}

	/**
	 * <p>
	 * 收到characteristic时的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param characteristic
	 */
	protected void doCharacteristicChangedCallback(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

	}

	/**
	 * <p>
	 * 读取characteristic的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param characteristic
	 * @param status
	 */
	protected void doCharacteristicReadCallback(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

	}

	/**
	 * <p>
	 * 写入characteristic时的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param characteristic
	 * @param status
	 */
	protected void doCharacteristicWriteCallback(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
		//Log.d("characteristic", "doCharacteristicWriteCallback" + DataUtil.byteToHexString(characteristic.getValue()));
	}

	/**
	 * <p>
	 * 蓝牙状态改变时的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param newState
	 * @param status
	 */
	protected void doConnectionStateChangeCallback(BluetoothGatt gatt, int newState, int status) {

	}

	/**
	 * <p>
	 * 读取特征值时的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param descriptor
	 * @param status
	 */

	protected void doDescriptorReadCallback(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

	}

	/**
	 * <p>
	 * 写入特征值时的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param descriptor
	 * @param status
	 */
	protected void doDescriptorWriteCallback(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

	}

	/**
	 * <p>
	 * 读取信号时的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param rssi
	 * @param status
	 */
	protected void doReadRemoteRssiCallback(BluetoothGatt gatt, int rssi, int status) {

	}

	/**
	 * <p>
	 * 写入完成的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param status
	 */
	protected void doReliableWriteCompletedCallback(BluetoothGatt gatt, int status) {

	}

	/**
	 * <p>
	 * 发现服务的回调
	 * </p>
	 * 
	 * @param gatt
	 * @param status
	 */
	protected void doServicesDiscoveredCallback(BluetoothGatt gatt, int status) {

	}
	// ************************ some callbacks ****************************//

}
