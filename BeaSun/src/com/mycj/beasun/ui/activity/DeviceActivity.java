package com.mycj.beasun.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.adapter.DeviceScanAdapter;
import com.mycj.beasun.adapter.DeviceScanAdapter.OnSelectedItemListener;
import com.mycj.beasun.adapter.DeviceScanItemDecoration;
import com.mycj.beasun.bean.DeviceJson;
import com.mycj.beasun.broadcastreceiver.SimpleBluetoothBroadcastReceiver;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.BluetoothJson;
import com.mycj.beasun.service.XplBluetoothService;
import com.mycj.beasun.service.util.FileUtil;
import com.mycj.beasun.view.SearchViewRelativeLayout;
import com.mycj.beasun.view.SearchViewRelativeLayout.OnRotationStateChangeListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 查找添加设备
 * 
 * @author Administrator
 */
public class DeviceActivity extends BaseActivity implements OnClickListener, OnSelectedItemListener {
	// private AbstractSimpleBlueService mSimpleService;
	private XplBluetoothService xplBluetoothService;
	private RecyclerView recyclerDevice;
	private List<BluetoothDevice> showDevices = new ArrayList<BluetoothDevice>();// 显示的蓝牙列表
	private List<BluetoothDevice> selectedDevices = new ArrayList<BluetoothDevice>();// 选择连接的蓝牙列表
	private DeviceScanAdapter deviceScanAdapter; // RecyclerView的adapter
	private final int MAX_CONNECT_DEVICE = 4;
	private Handler mHandler = new Handler() {
	};
	private SimpleBluetoothBroadcastReceiver mSimpleBluetoothBroadcastReceiver = new SimpleBluetoothBroadcastReceiver() {
		public void doDeviceFound(final android.bluetooth.BluetoothDevice device, int rssi) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// 当列表有此设备信息，直接返回
					if (showDevices.contains(device)) {
						//Log.i("", "_____>列表已存在，不用添加 ");
					} else {
						// 过滤已保存的设备
						// 1。获取本地设备
						List<DeviceJson> savedDeviceList = BluetoothJson.loadJson(getApplicationContext());
						// 2。当本地设备为空，直接添加，返回。
						if (savedDeviceList == null || savedDeviceList.size() == 0) {
							//Log.i("", "_____>本地无保存蓝牙信息，直接添加 ");
							showDevices.add(device);
							deviceScanAdapter.notifyDataSetChanged();

						} else {
							// 3。当有保存的蓝牙 ，首先遍历保存的蓝牙信息，查看是否有地址一样，一样的话就不添加,返回。
							boolean isSaved = false;
							for (DeviceJson json : savedDeviceList) {
								if (json.getAddress().equals(device.getAddress())) {
									isSaved = true;
								}
							}
							if (isSaved) {
								//Log.i("", "_____>保存蓝牙信息存在此地址，不用添加 ");
							} else {
								showDevices.add(device);
								deviceScanAdapter.notifyDataSetChanged();
							}

						}
					}
					
			

				}
			});

		};
	};
	private SearchViewRelativeLayout searchView;
	private TextView tvDevicsInfo;
	private TextView tvDeviceBack;
	private ImageView imgDeviceConnect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		showDevices = new ArrayList<>();
		initViews();
		setListener();
	}

	private void setDevicesInfo(boolean flag) {
		if (flag) {
			tvDevicsInfo.setVisibility(View.VISIBLE);
			String info = getString(R.string.device_search_start) + showDevices.size() + getString(R.string.device_search_end);
			tvDevicsInfo.setText(info);
		} else {
			tvDevicsInfo.setVisibility(View.VISIBLE);
			String info = getString(R.string.device_searching);
			tvDevicsInfo.setText(info);
		}
	}

	private void initViews() {
		// 返回和连接
		tvDeviceBack = (TextView) findViewById(R.id.tv_device_back);
		imgDeviceConnect = (ImageView) findViewById(R.id.img_device_connect);
		// 搜索按钮
		searchView = (SearchViewRelativeLayout) findViewById(R.id.search);
		searchView.setOnRotationStateChangeListener(new OnRotationStateChangeListener() {
			@Override
			public void onChange(boolean isRotation) {
				if (isRotation) {
					showDevices.clear();
					// mSimpleService.scanDevice(true);
					// mSimpleService.startScan();
					if (xplBluetoothService!=null) {
						xplBluetoothService.scanDevice(true);
					}
//					xplBluetoothService.startScan();
					setDevicesInfo(false);
					deviceScanAdapter.notifyDataSetChanged();
				} else {
					// mSimpleService.scanDevice(false);
					// mSimpleService.stopScan();
					if (xplBluetoothService!=null) {
						xplBluetoothService.scanDevice(false);
					}
					setDevicesInfo(true);
					showDevices.clear();
					deviceScanAdapter.notifyDataSetChanged();

				}
			}
		});

		// 列表信息
		tvDevicsInfo = (TextView) findViewById(R.id.tv_device_info);
		// 设备列表
		recyclerDevice = (RecyclerView) findViewById(R.id.recycler_device_scan);
		// 设置布局
		recyclerDevice.setLayoutManager(new LinearLayoutManager(this));
		// 设置deviceScanAdapter
		deviceScanAdapter = new DeviceScanAdapter(showDevices, this);
		// 设置点击事件
		deviceScanAdapter.setOnSelectedItemListener(this);
		recyclerDevice.setAdapter(deviceScanAdapter);
		// recyclerDevice.setItemAnimator(new DefaultItemAnimator());
		recyclerDevice.addItemDecoration(new DeviceScanItemDecoration(this, LinearLayoutManager.VERTICAL));
		setDevicesInfo(false);
	}

	private void setListener() {
		tvDeviceBack.setOnClickListener(this);
		imgDeviceConnect.setOnClickListener(this);
	}

	/**
	 * 保存需要连接的蓝牙列表
	 * 
	 * @param selectedDevices
	 */
	private void saveDeviceToBeConnected(List<BluetoothDevice> selectedDevices) {

		// 取出存储的jsons
		// String json =
		// FileUtil.readFileData(AbstractSimpleBlueService.FILE_NAME_JSON,
		// this);
		// // 解析json--转化为DeviceJson对象
		// if (json != null && !json.equals("")) {
		// deviceJsons = BluetoothJson.jsonToList(json);
		// L.i(deviceJsons + "");
		// }
		List<DeviceJson> deviceJsons = BluetoothJson.loadJson(this);
		if (deviceJsons == null) {
			deviceJsons = new ArrayList<DeviceJson>();
		}

		// 遍历选择的蓝牙列表,判断本地是否存储，如果未存贮，则添加
		for (BluetoothDevice device : selectedDevices) {
			String name = device.getName();
			if (name == null || name.equals("")) {
				name = "--";
			}
			String address = device.getAddress();
			DeviceJson dJson = new DeviceJson(name, address);
			// if (!isContains(deviceJsons, device)) {
			// deviceJsons.add(dJson);
			// }
			if (!selectedDevices.contains(dJson)) {
				deviceJsons.add(dJson);
			}
		}
		// 如果保存的蓝牙个数大于4，返回
		if (deviceJsons.size() > MAX_CONNECT_DEVICE) {
			showShortToast(getString(R.string.device_save_max));
			return ;
		}

		// 得到新的json
		String json = BluetoothJson.listToJson(deviceJsons);
		// 保存json
		if (json != null) {
			FileUtil.writeFileData(BeaStaticValue.JSON_DEVICE, json, this);		
		} else {
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		// mSimpleService = getSimpleBlueService();
		xplBluetoothService = getXplBluetoothService();
		registerReceiver(mSimpleBluetoothBroadcastReceiver, XplBluetoothService.getIntentFilter());
		// mSimpleService.scanDevice(false);
		// mSimpleService.scanDevice(true);
		// mSimpleService.startScan();
		checkBlue();
		searchView.startRotation();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onActivityResult(int arg0, int result, Intent arg2) {

		// mHandler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		if (result == RESULT_OK) {
			// mSimpleBlueService.startScan();
			if (xplBluetoothService!=null) {
				xplBluetoothService.scanDevice(true);
			}
		} else {
		}
		// }
		// }, 2000);
		super.onActivityResult(arg0, result, arg2);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(mSimpleBluetoothBroadcastReceiver);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		showDevices.clear();
		selectedDevices.clear();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			break;
		case R.id.tv_device_back:
			ObjectAnimator backAnimation = clickAnimation(tvDeviceBack);
			backAnimation.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					finish();
				}
			});
			backAnimation.start();
			break;
		case R.id.img_device_connect:
			saveDeviceToBeConnected(selectedDevices);
			ObjectAnimator connectAnimation = clickAnimation(imgDeviceConnect);
			connectAnimation.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					// doConnectDevices();
					// mSimpleService.scanDevice(true);
					// xplBluetoothService.scanDevice(true);
					if (xplBluetoothService!=null) {
						xplBluetoothService.getSearchDevices().clear();
						xplBluetoothService.startScan();
					}
					finish();
				}
			});
			connectAnimation.start();
			break;

		default:
			break;
		}
	}

	@Override
	public void onSelect(int position, boolean isSelected) {
		
		BluetoothDevice device = showDevices.get(position);
		//Log.i("DeviceActivity", "selectedDevices.size() :" + selectedDevices!=null?("大小 :" +selectedDevices.size()):"空");
		if (isSelected) {
			if (!selectedDevices.contains(device)) {
				selectedDevices.add(device);
			}
		} else {
			if (selectedDevices.contains(device)) {
				selectedDevices.remove(device);
			}
		}
	}

}
