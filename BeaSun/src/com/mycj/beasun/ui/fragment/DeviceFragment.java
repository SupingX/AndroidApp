package com.mycj.beasun.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycj.beasun.BaseFragment;
import com.mycj.beasun.R;
import com.mycj.beasun.adapter.MyDeviceBindedAdapter;
import com.mycj.beasun.adapter.MyDeviceBindedAdapter.OnAddListener;
import com.mycj.beasun.adapter.MyDeviceBindedAdapter.OnItemConnectListener;
import com.mycj.beasun.adapter.MyDeviceBindedAdapter.OnItemDeleteListener;
import com.mycj.beasun.adapter.MyDeviceBindedAdapter.OnItemEditNameListener;
import com.mycj.beasun.adapter.MyDeviceBindedAdapter.ViewHolder;
import com.mycj.beasun.bean.DeviceBindedInfo;
import com.mycj.beasun.bean.DeviceJson;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.BluetoothJson;
import com.mycj.beasun.service.XplBluetoothService;
import com.mycj.beasun.service.util.DataUtil;
import com.mycj.beasun.service.util.FileUtil;
import com.mycj.beasun.ui.activity.DeviceActivity;
import com.mycj.beasun.ui.activity.EditNameActivity;
import com.mycj.beasun.ui.activity.MainActivity;
import com.mycj.beasun.ui.activity.MainActivity.OnBroadcastReceiverChange;

public class DeviceFragment extends BaseFragment implements OnClickListener {
	// private List<DeviceJson> deviceJsons = new ArrayList<>();
	private Handler mHandler = new Handler() {

	};
//	private AbstractSimpleBlueService mSimpleBlueService;
	private XplBluetoothService xplBluetoothService;
	public static final String ARGUMENT = "argument";
	private final int MAX_CONNECT_DEVICE = 4;

	/**
	 * 传入需要的参数，设置给arguments
	 * 
	 * @param argument
	 * @return
	 */
	public static DeviceFragment newInstance(String argument) {
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);
		DeviceFragment deviceFragment = new DeviceFragment();
		// setArguments方法必须在fragment创建以后，添加给Activity前完成
		deviceFragment.setArguments(bundle);
		return deviceFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View deviceView = inflater.inflate(R.layout.fragment_bea_device, container, false);
		// TextView tvAdd = (TextView) deviceView.findViewById(R.id.tv_add);
		// tvAdd.setOnClickListener(this);
		// TextView tvTest = (TextView) deviceView.findViewById(R.id.tv_test);
		// tvTest.setOnClickListener(this);

		// recyclerView 初始化
//		recyclerDevicesBinded = (RecyclerView) deviceView.findViewById(R.id.recycler_device_binded);
		// 竖直
//		recyclerDevicesBinded.setLayoutManager(new LinearLayoutManager(getActivity()));
		// recyclerDevicesBinded.addItemDecoration(new
		// DeviceScanItemDecoration(getActivity(),
		// LinearLayoutManager.VERTICAL));
		// 网格
		// recyclerDevicesBinded.setLayoutManager(new
		// GridLayoutManager(getActivity(),2));
		// recyclerDevicesBinded.addItemDecoration(new
		// DividerGridItemDecoration(getActivity()));
		// 瀑布
		// recyclerDevicesBinded.setLayoutManager(new
		// StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL));
		// recyclerDevicesBinded.addItemDecoration(new
		// DividerGridItemDecoration(getActivity()));

		// adapter
		// adapter = new DeviceBindedAdapter(devicesBinded, getActivity());
		// recyclerDevicesBinded.setAdapter(adapter);
		deviceBindedInfos = new ArrayList<DeviceBindedInfo>();
		lvDevicesBinded = (ListView) deviceView.findViewById(R.id.lv_device);
		myAdapter = new MyDeviceBindedAdapter(deviceBindedInfos,getActivity().getApplicationContext());
		
//		adapter2 = new ImpBindedDeviceRecyclerViewAdapter();
//		adapter2.setOnItemDeleteListener(new OnItemDeleteListener() {
		myAdapter.setOnItemDeleteListener(new OnItemDeleteListener() {
			@Override
			public void onDelete(MyDeviceBindedAdapter.ViewHolder v, int position) {
				try {
//					xplBluetoothService.scanDevice(false);//关闭搜索
					//Log.e("", "deviceBindedInfos大小  ：" + deviceBindedInfos.size());
					if (deviceBindedInfos==null || deviceBindedInfos.size()==2) {
						//Log.e("", "关闭");
						if (xplBluetoothService!=null) {
							xplBluetoothService.stopMassage();
						}
						getMusicService().stop();
					}
					final String address = deviceBindedInfos.get(position).getAddress();
					updateJson(position);
					deviceBindedInfos.remove(position);
					
					myAdapter.notifyDataSetChanged();
					
//					adapter2.remove(position);
					//必须放在删除json后面 自动搜索连接会重新连接上
					if (xplBluetoothService!=null) {
						xplBluetoothService.close(address);
					}
//					mHandler.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							xplBluetoothService.scanDevice(true);//开启搜索
//						}
//					}, 500);
					// 连接状态时，关闭对应蓝牙的gatt连接
					/*	mSimpleBlueService.close(address);
						if (mSimpleBlueService.getConnectedGattCount()==0) {
							mSimpleBlueService.stopMassage();
							getMusicService().stop();
					}*/
				
					
					// 更新json
					// updateJson(deviceJsons.get(position));
				} catch (Exception e) {
					e.printStackTrace();
				}
		
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
////						loadDatas();
//					}
//				}, 1000);
				
			}

		});
//		adapter2.setOnItemConnectListener(new OnItemConnectListener() {
		myAdapter.setOnItemConnectListener(new OnItemConnectListener() {
		
			@Override
			public void onConnect(MyDeviceBindedAdapter.ViewHolder v, int position) {
				/*if (mSimpleBlueService != null) {
					final DeviceBindedInfo deviceBindedInfo = deviceBindedInfos.get(position);
					mSimpleBlueService.close(deviceBindedInfo.getAddress());
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mSimpleBlueService.connectAddress(deviceBindedInfo.getAddress());
						}
					}, 500);
				}*/
				
				if (xplBluetoothService!=null) {
					DeviceBindedInfo deviceBindedInfo = deviceBindedInfos.get(position);
//					mHandler.postDelayed(new Runnable() {
//						@Override
//						public void run() {
							xplBluetoothService.connect(deviceBindedInfo.getAddress());
//						}
//					}, 500);
				}
				
			}
		});
		
		myAdapter.setOnAddListener(new OnAddListener() {
			
			@Override
			public void OnAdd() {
				List<DeviceJson> deviceJsons = BluetoothJson.loadJson(getActivity());
				if (deviceJsons != null && deviceJsons.size() >= MAX_CONNECT_DEVICE) {
					Toast.makeText(getActivity(), getString(R.string.fragment_device_max_add_device), Toast.LENGTH_SHORT).show();
					return;
				}
				// 添加更多
				startActivity(new Intent(getActivity(), DeviceActivity.class));
			}
		});
//		adapter2.setOnAddMoreClickListener(new OnAddMoreClickListener() {
//		myAdapter.setOnAddMoreClickListener(new MyDeviceBindedAdapter.OnAddMoreClickListener() {
//
//			@Override
//			public void onClick(ViewHolder v) {
//				List<DeviceJson> deviceJsons = BluetoothJson.loadJson(getActivity());
//				if (deviceJsons != null && deviceJsons.size() >= 6) {
//					Toast.makeText(getActivity(), getString(R.string.fragment_device_max_add_device), Toast.LENGTH_SHORT).show();
//					return;
//				}
//				adapter2.removeAll();
//				// 添加更多
//				startActivity(new Intent(getActivity(), DeviceActivity.class));
//			}
//		});
		
//		adapter2.setOnItemShutDownListener(new OnItemShutDownListener() {
			myAdapter.setOnItemShutDownListener(new MyDeviceBindedAdapter.OnItemShutDownListener() {

			@Override
			public void onShutDown(MyDeviceBindedAdapter.ViewHolder v, int position,String address) {
				DeviceBindedInfo deviceBindedInfo = deviceBindedInfos.get(position);
				if (xplBluetoothService!=null) {
					xplBluetoothService.shutDown(deviceBindedInfo.getAddress());
				}
//				mSimpleBlueService.shutDown(deviceBindedInfo.getAddress());
				if (xplBluetoothService!=null && deviceBindedInfos==null || deviceBindedInfos.size()==2) {//只有一个时就关比按摩
					//Log.e("", "关闭");
					xplBluetoothService.stopMassage();
					getMusicService().stop();
				}
				
			
//				
				for (int i = 0; i < deviceBindedInfos.size() - 1; i++) {
					DeviceBindedInfo deviceInfo = deviceBindedInfos.get(i);
					if (deviceInfo.getAddress().equals(address)) {
						deviceInfo.setBlueState(-1);
						myAdapter.notifyDataSetChanged();
						break;
					}
				}
//				loadDatas();
			}
			
		});
		
//		adapter2.setOnItemEditNameListener(new OnItemEditNameListener() {
		myAdapter.setOnItemEditNameListener(new OnItemEditNameListener() {

			@Override
			public void onEditName(ViewHolder v,int position) {
				Intent intent = new Intent(getActivity(), EditNameActivity.class);
				intent.putExtra("position", position);
				startActivityForResult(intent, 0xaa);
			}
		});
//		recyclerDevicesBinded.setAdapter(adapter2);
		
		lvDevicesBinded.setAdapter(myAdapter);
//		loadDatas();
		return deviceView;
	}
	


	/**
	 * 删除JSON后更新JSON并存贮到本地
	 * 
	 * @param deviceJson
	 */
	// private void updateJson(DeviceJson deviceJson) {
	// if (deviceJsons.contains(deviceJson)) {
	// deviceJsons.remove(deviceJson);
	// String json = BluetoothJson.listToJson(deviceJsons);
	// if (json != null) {
	// FileUtil.writeFileData(BeaStaticValue.JSON_DEVICE, json, getActivity());
	// }else{
	// FileUtil.writeFileData(BeaStaticValue.JSON_DEVICE, "", getActivity());
	// }
	// }
	// }
	private void updateJson(int position) {
		List<DeviceJson> deviceJsons = BluetoothJson.loadJson(getActivity());
		deviceJsons.remove(position);
		String json = BluetoothJson.listToJson(deviceJsons);
		FileUtil.writeFileData(BeaStaticValue.JSON_DEVICE, json, getActivity());
		;
	}

	/**
	 * 获取数据
	 */
	private void loadDatas() {
		deviceBindedInfos.clear();
		
		// 获取本地存储设备json
		// String json = FileUtil.readFileData(BeaStaticValue.JSON_DEVICE,
		// getActivity());
		// L.e(json);
		// // 解析json--转化为DeviceJson对象
		// if (json != null && !json.equals("")) {
		// deviceJsons = BluetoothJson.jsonToList(json);
		// L.i(deviceJsons + "");
		// }
		// BluetoothJson
		List<DeviceJson> deviceJsons = BluetoothJson.loadJson(getActivity());
		if (deviceJsons != null && deviceJsons.size() > 0) {
			//Log.e("", "deviceJsons :" + deviceJsons.size());
			// 创建DeviceBindedInfo对象集合
			for (DeviceJson deviceJson : deviceJsons) {
				// if (!isContains(deviceJson)) {
				DeviceBindedInfo deviceBindedInfo = new DeviceBindedInfo();
				deviceBindedInfo.setName(deviceJson.getName());
				deviceBindedInfo.setAddress(deviceJson.getAddress());
//				int gattState = mSimpleBlueService.getGattState(deviceJson.getAddress());
				int gattState = xplBluetoothService!=null &&xplBluetoothService.isBluetoothConnected(deviceJson.getAddress())?0:-1;
				
				if (gattState==0) {
					deviceBindedInfo.setBlueState(0);
				}else{
					deviceBindedInfo.setBlueState(-1);
				}
				deviceBindedInfos.add(deviceBindedInfo);
				// }
			}
		}
		deviceBindedInfos.add(new DeviceBindedInfo()); // 为了添加 更多
		// 数据添加至adapter
//		myAdapter.setList(deviceBindedInfos);
		myAdapter.notifyDataSetChanged();
//		adapter2.replace(deviceBindedInfos);
		
	}

	/**
	 * 本地保存的蓝牙中，是否在当前显示的蓝牙列表中
	 * 
	 * @param deviceBindedInfo
	 * @return
	 */
	private boolean isContains(DeviceJson deviceJson) {
		// 当前显示的蓝牙列表为空或数量为0时，返回false 不包含
		if (deviceBindedInfos == null || deviceBindedInfos.size() == 0) {
			return false;
		}
		// 遍历当前显示的蓝牙列表，只要有一个匹配，则包含，返回true。
		for (DeviceBindedInfo info : deviceBindedInfos) {
			if (info.getAddress().equals(deviceJson.getAddress())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onStart() {
		super.onStart();
//		mSimpleBlueService = getSimpleBlueService();
		xplBluetoothService = getxplBluetoothService();
		setBroadCastReceiverFromMainActivity();
	
	}

	@Override
	public void onResume() {
		super.onResume();
		loadDatas();
//		xplBluetoothService.scanDevice(false);
//		xplBluetoothService.scanDevice(true);

	}

	private void setBroadCastReceiverFromMainActivity() {
		OnBroadcastReceiverChange s = new OnBroadcastReceiverChange() {
			@Override
			public void onRemoteRssiChange(int rssi, String address) {
//				L.i("信号：" + rssi + ",地址：" + address);
//				for (DeviceBindedInfo device : deviceBindedInfos) {
//					if (device.getAddress().equals(address)) {
//						deviceBindedInfos.indexOf(device);
//					}
//				}
			}

			@Override
			public void onDiscoverWriteService(BluetoothDevice device) {
				for (int i = 0; i < deviceBindedInfos.size() - 1; i++) {

					if (deviceBindedInfos.get(i).getAddress().equals(device.getAddress())) {
						// 遍历确定是哪个Device连接上了，改变蓝牙状态
						deviceBindedInfos.get(i).setBlueState(0);
						myAdapter.setList(deviceBindedInfos);
						myAdapter.notifyDataSetChanged();
						break;
					}
					
				}
//				loadDatas();
			}

			@Override
			public void onBlueDisconnect(int state, final String address) {
				//Log.e("", "======================="+address);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						
						for (int i = 0; i < deviceBindedInfos.size() - 1; i++) {
							DeviceBindedInfo deviceInfo = deviceBindedInfos.get(i);
							if (deviceInfo.getAddress().equals(address)) {
								// 遍历确定是哪个Device失去了连接，改变蓝牙状态
//								adapter2.remove(i);
						
								deviceInfo.setBlueState(-1);
//								adapter2.append(deviceInfo);
//								myAdapter.setList(deviceBindedInfos);
								myAdapter.notifyDataSetChanged();
								break;
							}
						}
//						loadDatas();
					}
				});
			}

			@Override
			public void onElectChange(final int state, final String address) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < deviceBindedInfos.size() - 1; i++) {
							DeviceBindedInfo deviceInfo = deviceBindedInfos.get(i);
							if (deviceInfo.getAddress().equals(address)) {
								// 设置电量
//								deviceInfo.setBlueState(0);
								if(deviceInfo.getDianliang()==state){
									return ;
								}
								deviceInfo.setDianliang(state);
								myAdapter.notifyDataSetChanged();
								break;
							}
						}
					}
				});
			}
		};
		((MainActivity) getActivity()).setOnBroadcastReceiverChange(s);
	}

	private byte[] getByteForSyncTime(Date date) {
		StringBuffer sb = new StringBuffer();
		sb.append("F4");
		// 解析日期
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		//
		sb.append(DataUtil.toHexString(year - 1900));
		sb.append(DataUtil.toHexString(month));
		sb.append(DataUtil.toHexString(day));
		sb.append(DataUtil.toHexString(hour));
		sb.append(DataUtil.toHexString(minute));
		sb.append(DataUtil.toHexString(second));

		return DataUtil.hexStringToByte(sb.toString());
	}

	@Override
	public void onClick(View v) {
		if (mOnDeviceFragmentOnClickListener != null) {
			mOnDeviceFragmentOnClickListener.onClick(v);
		}
		switch (v.getId()) {

		case R.id.tv_add:

			break;
		// case R.id.tv_test:
		// byte[] byteForSyncTime = getByteForSyncTime(new Date());
		// if (mSimpleBlueService!=null) {
		// mSimpleBlueService.writeCharacteristic(byteForSyncTime);
		// }
		// break;
		default:
			break;
		}
	}

	/**
	 * 设备fragment 空间点击回调
	 * 
	 * @author Administrator
	 */
	public interface OnDeviceFragmentOnClickListener {
		public void onClick(View v);
	}

	private OnDeviceFragmentOnClickListener mOnDeviceFragmentOnClickListener;
//	private RecyclerView recyclerDevicesBinded;
	private ListView lvDevicesBinded;
//	private ImpBindedDeviceRecyclerViewAdapter adapter2;
	private List<DeviceBindedInfo> deviceBindedInfos = new ArrayList<DeviceBindedInfo>();
	private MyDeviceBindedAdapter myAdapter;

	public void setOnDeviceFragmentOnClickListener(OnDeviceFragmentOnClickListener l) {
		this.mOnDeviceFragmentOnClickListener = l;
	}
}
