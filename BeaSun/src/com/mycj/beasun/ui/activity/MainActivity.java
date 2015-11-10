package com.mycj.beasun.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.R.drawable;
import com.mycj.beasun.R.id;
import com.mycj.beasun.R.layout;
import com.mycj.beasun.adapter.MainViewPageAdapter;
import com.mycj.beasun.bean.DeviceJson;
import com.mycj.beasun.bean.MassageInfo;
import com.mycj.beasun.broadcastreceiver.SimpleBluetoothBroadcastReceiver;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.BluetoothJson;
import com.mycj.beasun.business.ProtoclWrite;
import com.mycj.beasun.service.XplBluetoothService;
import com.mycj.beasun.service.util.FileUtil;
import com.mycj.beasun.service.util.SharedPreferenceUtil;
import com.mycj.beasun.ui.fragment.DeviceFragment;
import com.mycj.beasun.ui.fragment.DeviceFragment.OnDeviceFragmentOnClickListener;
import com.mycj.beasun.ui.fragment.IntellgentMassageFragment;
import com.mycj.beasun.ui.fragment.MainFragment;
import com.mycj.beasun.ui.fragment.MainFragment.OnMainFragmentViewOnClickListener;
import com.mycj.beasun.ui.fragment.OtherFragment;
import com.mycj.beasun.ui.fragment.OtherFragment.OnOtherFragmentViewClickListener;
import com.mycj.beasun.view.ActionSheetDialog;
import com.mycj.beasun.view.ActionSheetDialog.OnSheetItemClickListener;
import com.mycj.beasun.view.ActionSheetDialog.SheetItemColor;
import com.mycj.beasun.view.NoScrollViewPager;

public class MainActivity extends BaseActivity implements OnClickListener {

	private MainFragment mainFragment;
	private DeviceFragment deviceFragment;
	private OtherFragment otherFragment;
	private TextView tvBeaMain;
	private TextView tvBeaDevice;
	private TextView tvBeaOther;
	private TextView tvBeaTitle;
	private NoScrollViewPager mainViewPager;
	private List<Fragment> fragments;
//	private AbstractSimpleBlueService mSimpleBlueService;
	private XplBluetoothService xplBluetoothService;
	private SimpleBluetoothBroadcastReceiver mSimpleBluetoothBroadcastReceiver = new SimpleBluetoothBroadcastReceiver() {
		public void doDeviceFound(android.bluetooth.BluetoothDevice device, int rssi) {

		};
		public void doRemoteRssi(int rssi, String address) {
			if (mOnBroadcastReceiverChange != null) {
				mOnBroadcastReceiverChange.onRemoteRssiChange(rssi, address);
			}
		}
		
		public void doDiscoveredWriteService(BluetoothDevice device) {
			if (mOnBroadcastReceiverChange != null) {
				mOnBroadcastReceiverChange.onDiscoverWriteService(device);
			}
		};
		@Override
		public void doBlueDisconnect(int state,String address) {
			if (mOnBroadcastReceiverChange != null) {
				mOnBroadcastReceiverChange.onBlueDisconnect(state, address);
			}
		}
		
		public void doElectChange(int elect, String address) {
			if (mOnBroadcastReceiverChange != null) {
				mOnBroadcastReceiverChange.onElectChange(elect, address);
			}
		};
		
	};
	
	private OnBroadcastReceiverChange mOnBroadcastReceiverChange;
	public void setOnBroadcastReceiverChange(OnBroadcastReceiverChange l) {
		this.mOnBroadcastReceiverChange = l;
	}
	/**
	 * 蓝牙变化情况
	 * @author Administrator
	 *
	 */
	public interface OnBroadcastReceiverChange {
		public void onRemoteRssiChange(int rssi, String address);
		public void onDiscoverWriteService(BluetoothDevice device);
		public void onBlueDisconnect(int state,String address);
		public void onElectChange(int state,String address);
		
	}
		
	
	

	/**
	 * 设备页面点击事件
	 */
	private OnDeviceFragmentOnClickListener mOnDeviceFragmentOnClickListener = new OnDeviceFragmentOnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_add:
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 打开智能模式
	 */
	private boolean isOpen;
	/**
	 * 放松吧页面点击事件
	 */
	private OnMainFragmentViewOnClickListener mOnMainFragmentViewOnClickListener = new OnMainFragmentViewOnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId()==0) {
				openIntellgent(1);
			}else{
				openMassageControl(v.getId());
			}
//			switch (v.getId()) {
//		
//			case 0:
//				break;
//			case 1:
//			
////				Intent intent = new Intent(MainActivity.this, MassageControlActivity.class);
////				Bundle b = new Bundle();
////				//Log.e("", info.toString());
////				b.putParcelable("massage", info);
////				intent.putExtras(b);
////				startActivity(intent);
//				break;
//			case 5:
//				startActivity(MassageControlActivity.class);
//				break;
//			case 6:
//				startActivity(TestActivity.class);
//				break;
//			default:
//				break;
//			}
			
		
		}
	};
	
	
	private void openMassageControl(int i){
		MassageInfo info = new MassageInfo();
		int model_1 = 0;
		int model_2 = 0;
		long music = -1L;
		int index=-1;
		String text="";
		switch (i) {
		case 1:
			index= 0xF1;
			model_1 = 0b01;
			model_2 = 0b0;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_ROUNIE),-1L);
//			text="揉捏按摩";
			text=getString(R.string.massage_kneading);
			break;
		case 2:
			index= 0xF2;
			model_1 = 0b10;
			model_2 = 0b0;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_TUINA),-1L);
//			text="推拿按摩";
			text=getString(R.string.massage_manipulation);
			break;
		case 3:
			index= 0xF3;
			model_1 = 0b1000;
			model_2 = 0b0;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_GUASHA),-1L);
//			text="刮痧按摩";
			text=getString(R.string.massage_scrapping);
			break;
		case 4:
			index= 0xF4;
			model_1 = 0b0;
			model_2 = 0b1;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_SHOUSHEN),-1L);
//			text="瘦身按摩";
			text=getString(R.string.massage_slimming);
			break;
		case 5:
			
			index= 0xF5;
			model_1 = 0b0;
			model_2 = 0b10;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_QINGFU),-1L);
//			text="轻抚按摩";
			text=getString(R.string.massage_gently);
			break;
		case 6:
			index= 0xF6;
			model_1 = 0b10000;
			model_2 = 0b0;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_ZHENJIU),-1L);
//			text="针灸按摩";
			
			text=getString(R.string.massage_acupuncture);
			break;
		case 7:
			index= 0xF7;
			model_1 = 0b100;
			model_2 = 0b0;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_CHUIJI),-1L);
//			text="锤击按摩";
			text=getString(R.string.massage_hammer);
			break;
		case 8:
			index= 0xF8;
			model_1 = 0b10000;
			model_2 = 0b0;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_ZHIYA),-1L);
//			text="指压按摩";
			text=getString(R.string.massage_acupressure);
			break;
		case 9:
			index= 0xF9;
			model_1 = 0b00000;
			model_2 = 0b100;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_JINGZHUI),-1L);
//			text="颈椎按摩";
			text=getString(R.string.massage_neck);
			break;
		case 10:
			index= 0xFA;
			model_1 = 0b1000000;
			model_2 = 0b000;
			music = (long) SharedPreferenceUtil.get(this, (BeaStaticValue.MUSIC_HUOGUAN),-1L);
//			text="火罐按摩";
			text=getString(R.string.massage_cupping);
			break;

		default:
			break;
		}
	
		
		info.setIndex(index);
		info.setMusic(music);
		info.setModel_1(model_1);
		info.setText(text);
		info.setModel_2(model_2);
		Intent intent = new Intent(MainActivity.this, MassageControlActivity.class);
		Bundle b = new Bundle();
		//Log.e("", info.toString());
		b.putParcelable("massage", info);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	private MainViewPageAdapter mainViewPageAdapter;
	private TextView tvMainBack;
	private IntellgentMassageFragment intellgentMassageFragment;
	
	
	private void openIntellgent(int open){
		if (open==1) {
			if (intellgentMassageFragment==null) {
				intellgentMassageFragment = IntellgentMassageFragment.newInstance("IntellgentMassageFragment");
			}
		fragments.remove(0);
		fragments.add(0, intellgentMassageFragment);
		mainViewPageAdapter.notifyDataSetChanged();
		tvMainBack.setVisibility(View.VISIBLE);
		isOpen = true;
		tvBeaTitle.setText(getString(R.string.massage_intelligent));
		}else{
			if (mainFragment==null) {
				mainFragment = MainFragment.newInstance("main");
			}
			fragments.remove(0);
			fragments.add(0, mainFragment);
			mainViewPageAdapter.notifyDataSetChanged();
			tvMainBack.setVisibility(View.GONE);
			isOpen = false;
			tvBeaTitle.setText(getString(R.string.bea_long));
		}

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		setListener();
		updateTab(1, 0, 0);
		mainViewPager.setCurrentItem(0);
		tvBeaTitle.setText(getString(R.string.bea_long));
		

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(mSimpleBluetoothBroadcastReceiver, XplBluetoothService.getIntentFilter());
//		mSimpleBlueService = getSimpleBlueService();
//		mSimpleBlueService.startScan();
		xplBluetoothService= getXplBluetoothService();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//检查有没有登录
		if (loadUserJson()==null) {
			startActivity(SpalishActivity.class);
			finish();
		}
		checkBlue();
	}
	@Override
	protected void onActivityResult(int arg0, int result, Intent arg2) {

		// mHandler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		if (result == RESULT_OK) {
			if (xplBluetoothService!=null) {
				xplBluetoothService.startScan();
			}
//			xplBluetoothService.scanDevice(true);
		} else {
		}
		// }
		// }, 2000);
		super.onActivityResult(arg0, result, arg2);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mSimpleBluetoothBroadcastReceiver);
	}

	private void setListener() {
		tvBeaMain.setOnClickListener(this);
		tvBeaDevice.setOnClickListener(this);
		tvBeaOther.setOnClickListener(this);
		tvMainBack.setOnClickListener(this);
		
	}

	private void initViews() {
		tvBeaMain = (TextView) findViewById(R.id.tv_bea_main);
		tvBeaDevice = (TextView) findViewById(R.id.tv_bea_device);
		tvBeaOther = (TextView) findViewById(R.id.tv_bea_other);
		tvBeaTitle = (TextView) findViewById(R.id.tv_bea_top_title);
		tvMainBack = (TextView) findViewById(R.id.tv_main_back);
		mainViewPager = (NoScrollViewPager) findViewById(R.id.vp_main);
		fragments = new ArrayList<Fragment>();

		mainFragment = MainFragment.newInstance("main");
		mainFragment.setOnMainFragmentViewOnClickListener(mOnMainFragmentViewOnClickListener);
		deviceFragment = DeviceFragment.newInstance("device");
		deviceFragment.setOnDeviceFragmentOnClickListener(mOnDeviceFragmentOnClickListener);
		otherFragment = OtherFragment.newInstance("other");
		otherFragment.setOnOtherFragmentViewClickListener(new OnOtherFragmentViewClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.rl_user_info:
					startActivity(UserInfoActivity.class);
					break;
				case R.id.rl_faq:
					startActivity(FaqActivity.class);
					break;
				case R.id.rl_feedback:
					startActivity(FeedBackActivity.class);
					break;

				default:
					break;
				}
			}
		});
		intellgentMassageFragment = IntellgentMassageFragment.newInstance("IntellgentMassageFragment");
		
		fragments.add(mainFragment);
		fragments.add(deviceFragment);
		fragments.add(otherFragment);
		mainViewPageAdapter = new MainViewPageAdapter(getSupportFragmentManager(), fragments);
		mainViewPager.setAdapter(mainViewPageAdapter);
		mainViewPager.setCurrentItem(0);
	}

	/**
	 * 更新tab 1：选中 0：未选中
	 * 
	 * @param tab1
	 * @param tab2
	 * @param tab3
	 */
	private void updateTab(int tab1, int tab2, int tab3) {
		if (tab1 == 0) {
			tvBeaMain.setAlpha(0.4f);
			setDrawable(tvBeaMain, R.drawable.ic_tab_beasun, 100);
		} else {
			tvBeaMain.setAlpha(1f);
			setDrawable(tvBeaMain, R.drawable.ic_tab_beasun, 255);
		}
		if (tab2 == 0) {
			tvBeaDevice.setAlpha(0.4f);
			setDrawable(tvBeaDevice, R.drawable.ic_tab_device, 100);
		} else {
			tvBeaDevice.setAlpha(1f);
			setDrawable(tvBeaDevice, R.drawable.ic_tab_device, 255);
		}
		if (tab3 == 0) {
			tvBeaOther.setAlpha(0.4f);
			setDrawable(tvBeaOther, R.drawable.ic_tab_other, 100);
		} else {
			tvBeaOther.setAlpha(1f);
			setDrawable(tvBeaOther, R.drawable.ic_tab_other, 255);
		}
	}

	/**
	 * 设置TextView 图片
	 * 
	 * @param tv
	 * @param resourceid
	 */
	private void setDrawable(TextView tv, int resourceid, int alpha) {
		Resources res = getResources();
		Drawable img = res.getDrawable(resourceid);
		img.setAlpha(alpha);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		tv.setCompoundDrawables(null, img, null, null); //
	}

	/**
	 * 更新底部选中状态
	 * 
	 * @param id
	 */
	// private void updateFragment(int id) {
	// updateTab(0, 0, 0);
	// FragmentTransaction beginTransaction =
	// getSupportFragmentManager().beginTransaction();
	// switch (id) {
	// case 0:
	// if (mainFragment == null) {
	// mainFragment = new MainFragment();
	// }
	// tvBeaTitle.setText("贝尔顺智能放松器");
	// beginTransaction.replace(R.id.fr_bea_fragment, mainFragment);
	// updateTab(1, 0, 0);
	// break;
	// case 1:
	// updateTab(0, 1, 0);
	// if (deviceFragment == null) {
	// deviceFragment = new DeviceFragment();
	// }
	// deviceFragment.setOnDeviceFragmentOnClickListener(mOnDeviceFragmentOnClickListener);
	// tvBeaTitle.setText("设备");
	// beginTransaction.replace(R.id.fr_bea_fragment, deviceFragment);
	// break;
	// case 2:
	// updateTab(0, 0, 1);
	// if (otherFragment == null) {
	// otherFragment = new OtherFragment();
	// }
	// tvBeaTitle.setText("其他");
	// beginTransaction.replace(R.id.fr_bea_fragment, otherFragment);
	// break;
	// default:
	// break;
	// }
	// // beginTransaction.addToBackStack(null);
	// beginTransaction.commitAllowingStateLoss();
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_bea_main:
			// String json = AssetsUtil.getJson(this);
			// L.e(json);
			// try {
			// List<DeviceJson> deviceJsons = AssetsUtil.getDeviceJson(json);
			// L.i(deviceJsons + "");
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// String json = "fafafafa";
			// FileUtil.writeFileData("json.txt", json, this);
			// updateFragment(0);

			updateTab(1, 0, 0);
			tvBeaTitle.setText(getString(R.string.bea_long));
			mainViewPager.setCurrentItem(0);
			if (isOpen) {
				tvMainBack.setVisibility(View.VISIBLE);
				tvBeaTitle.setText(getString(R.string.massage_intelligent));
			}
			break;
		case R.id.tv_bea_device:
			// updateFragment(1);
			// String readFileData = FileUtil.readFileData("json.txt", this);
			// L.e(readFileData);
			updateTab(0, 1, 0);
			tvBeaTitle.setText(getString(R.string.menu_device));
			mainViewPager.setCurrentItem(1);
			tvMainBack.setVisibility(View.GONE);
			break;
		case R.id.tv_bea_other:
			// updateFragment(2);
			updateTab(0, 0, 1);
			tvBeaTitle.setText(getString(R.string.menu_other));
			tvMainBack.setVisibility(View.GONE);
			mainViewPager.setCurrentItem(2);
			break;
		case R.id.tv_main_back:
			openIntellgent(0);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
//		mSimpleBlueService.writeListByte();
//		mSimpleBlueService.writeCharacteristic(ProtoclWrite.instance().protoclWriteForSingle("F5"));
//		// 设置强度
//		mSimpleBlueService.writeCharacteristic(ProtoclWrite.instance().protoclWriteForLevel(3));
//		// 开始按摩
//		mSimpleBlueService.writeCharacteristic(ProtoclWrite.instance().protoclWriteForControl(0x01));
//		// 更新状态
		
//		xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForControl(1));
//		xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForIntelligent(0b01, 0b1));
//		xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForLevel(2));
//		xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForSingle("F2"));
		
		ActionSheetDialog exitDialog = new ActionSheetDialog(this).builder();
		exitDialog.setTitle(getString(R.string.exit));
		exitDialog.addSheetItem(getString(R.string.positive), SheetItemColor.Red, new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
//				if (mSimpleBlueService!=null) {
//					mSimpleBlueService.closeAll();
//				}
				if (xplBluetoothService!=null) {
					xplBluetoothService.stopMassage();
					xplBluetoothService.closeAll();
				}
				if (getMusicService()!=null) {
					getMusicService().stop();
				}
				finish();
			}
		}).
		
		show();
	}

}
