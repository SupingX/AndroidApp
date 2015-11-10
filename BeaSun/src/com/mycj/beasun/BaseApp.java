package com.mycj.beasun;


import com.mycj.beasun.broadcastreceiver.BluetoothStateBroadcastReceiver;
import com.mycj.beasun.service.MusicService;
import com.mycj.beasun.service.XplBluetoothService;
import com.mycj.beasun.service.XplBluetoothService.XplBinder;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class BaseApp extends Application{

//private AbstractSimpleBlueService mSimpleBlueService;
	
//	private ServiceConnection mSimpleBlueServiceConn = new ServiceConnection() {
//		
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			mSimpleBlueService = null;
//		}
//		
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			mSimpleBlueService =  (AbstractSimpleBlueService) (((AbstractSimpleBlueService.MyBinder)service).getService());
//			BluetoothStateBroadcastReceiver mBluetoothStateBroadcastReceiver = new BluetoothStateBroadcastReceiver(getApplicationContext(), mSimpleBlueService);
//			mBluetoothStateBroadcastReceiver.registerBoradcastReceiverForCheckBlueToothState(getApplicationContext());
////			if (!mSimpleBlueService.isEnable()) {
////				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////				enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				startActivity(enableBtIntent);
////			// showIosDialog();
////			}else{
//////				mSimpleBlueService.startScan();
//////				mSimpleBlueService.scanDevice(true);
////				
////			}
//			
//		}
//	};
	
	private MusicService mMusicService;
	private ServiceConnection mMusicServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mMusicService.stop();
			mMusicService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMusicService = ((MusicService.MyBinder)service).getMusicService();
			mMusicService.setPlayMode(MusicService.MODE_XUNHUAN);
		}
	};
	
	private XplBluetoothService xplBluetoothService;
	private ServiceConnection xplServiceConnection = new ServiceConnection() {
		


		@Override
		public void onServiceDisconnected(ComponentName name) {
			xplBluetoothService=null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			XplBinder xplBinder = (com.mycj.beasun.service.XplBluetoothService.XplBinder) service;
			xplBluetoothService = xplBinder.getXplBluetoothService();
				
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
//		Intent liteIntent = new Intent(this, SimpleBlueService.class);
//		bindService(liteIntent, mSimpleBlueServiceConn, Context.BIND_AUTO_CREATE);
		Intent musicIntent = new Intent(this, MusicService.class);
		bindService(musicIntent, mMusicServiceConnection, Context.BIND_AUTO_CREATE);
		
		Intent xplIntent = new Intent(this, XplBluetoothService.class);
		bindService(xplIntent, xplServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onTerminate() {
//		mSimpleBlueService.closeAll();
//		unbindService(mSimpleBlueServiceConn);
		xplBluetoothService.closeAll();
		getMusicService().stop();
		unbindService(mMusicServiceConnection);
		unbindService(xplServiceConnection);
		onTerminate();
	}
	
//	public AbstractSimpleBlueService getSimpleBlueService(){
//		return this.mSimpleBlueService;
//	}
	public MusicService getMusicService(){
		return this.mMusicService;
	}
	public XplBluetoothService getXplBluetoothService(){
		return this.xplBluetoothService;
	}
	
}
