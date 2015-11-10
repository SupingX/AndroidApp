package com.mycj.beasun.ui.activity;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.R.layout;
import com.mycj.beasun.bean.User;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.UserJson;
import com.mycj.beasun.service.util.FileUtil;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SpalishActivity extends BaseActivity {
	private Handler mHandler = new Handler() {
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spanish);

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				checkUser();
		
			}
		}, 2000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// checkBlue();
	}

	private void checkUser() {
		String json = FileUtil.readFileData(BeaStaticValue.JSON_USER, this);
		User user = UserJson.jsonToObj(json);
		if (user==null ||user.getName()==null||user.getName().equals("")) {
			//没有json就需要登录
			startActivity(new Intent(SpalishActivity.this, LoginActivity.class));
		}else{
			//否则直接进入主页面
			startActivity(new Intent(SpalishActivity.this, MainActivity.class));
		}
		finish();
	}

	
	
		//	****************************************************//
	boolean isOnceEnter = true;
//	/**
//	 * 检查蓝牙
//	 */
//	private void checkBlue() {
//		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//			if (isOnceEnter) {
//				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				// startActivityForResult(enableBtIntent, 1);
//				startActivity(enableBtIntent);
//				// showIosDialog();
//				isOnceEnter = false;
//			}
//		} else {
//
//		}
//	}

	@Override
	protected void onActivityResult(int arg0, int result, Intent arg2) {

		// mHandler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		if (result == RESULT_OK) {
			startActivity(new Intent(SpalishActivity.this, MainActivity.class));
			finish();
		} else {
			startActivity(new Intent(SpalishActivity.this, MainActivity.class));
			finish();
		}
		// }
		// }, 2000);
		super.onActivityResult(arg0, result, arg2);
	}
}
