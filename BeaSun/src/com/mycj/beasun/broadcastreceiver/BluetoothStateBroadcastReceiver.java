package com.mycj.beasun.broadcastreceiver;



import com.mycj.beasun.R;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class BluetoothStateBroadcastReceiver extends AbstractBluetoothStateBroadcastReceiver {

	private Context context;

	public BluetoothStateBroadcastReceiver() {

	}

	public BluetoothStateBroadcastReceiver(Context context) {
		this.context = context;
	}

	@Override
	public void onBluetoothChange(int state, int previousState) {
		switch (state) {
		case BluetoothAdapter.STATE_ON:
			//Log.i("", "蓝牙已打开");
//			Toast.makeText(context, context.getString(R.string.blue_on), Toast.LENGTH_SHORT).show();
//			if (mSimpleBlueService.isScanning()) {
//				mSimpleBlueService.scanDevice(false);
//			}
//			mSimpleBlueService.scanDevice(true);
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
			//Log.i("", "蓝牙关闭中。。。");
			break;
		case BluetoothAdapter.STATE_TURNING_ON:
			//Log.i("", "蓝牙打开中。。。");
			break;
		case BluetoothAdapter.STATE_OFF:
			//Log.i("", "蓝牙已关闭");
//			Toast.makeText(context, context.getString(R.string.blue_off), Toast.LENGTH_SHORT).show();
//			if (mSimpleBlueService.isScanning()) {
//				mSimpleBlueService.scanDevice(false);
//			}
//			mSimpleBlueService.close();
//			showdialog("蓝牙已关闭");
			break;
		default:
			break;
		}
	}
	
	

}
