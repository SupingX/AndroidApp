package com.mycj.beasun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mycj.beasun.bean.User;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.UserJson;
import com.mycj.beasun.service.MusicService;
import com.mycj.beasun.service.XplBluetoothService;
import com.mycj.beasun.service.util.FileUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class BaseActivity extends FragmentActivity {
protected BaseApp mApplication;
	
//	public boolean isConnected(){
//		AbstractSimpleBlueService mSimpleBlueService = getSimpleBlueService();
//		return 	 (null!=mSimpleBlueService&&!mSimpleBlueService.isGattEmpty() );
//	}
	
//	protected FlippingLoadingDialog mLoadingDialog;
//	public void showIosDialog(Context context,String msg){
//		new com.suping.i2_watch.view.AlertDialog(context).builder().setMsg(msg).setCancelable(true).show();
//	}
//	public void showIosDialog(Context context,String msg,String title){
//		new com.suping.i2_watch.view.AlertDialog(context).builder().setMsg(msg).setCancelable(true).show();
//	}
	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	protected List<AsyncTask<Void, Void, Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (BaseApp) getApplication();
//		mLoadingDialog = new FlippingLoadingDialog(this, "请求提交中");
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
	}

	@Override
	protected void onDestroy() {
		clearAsyncTask();
		super.onDestroy();
	}
	/**
	 * 等待框
	 * @param msg
	 * @return
	 */
	protected ProgressDialog showProgressDialog(String msg) {
		ProgressDialog pDialog;
		pDialog = new ProgressDialog(this);
			pDialog.setCancelable(false);
			pDialog.setMessage(msg);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
			return pDialog;
	}
	protected User loadUserJson() {
		String json = FileUtil.readFileData(BeaStaticValue.JSON_USER, this);
		if (json == null) {
			return null;
		}
		if (json.equals("")) {
			return null;
		}
		return UserJson.jsonToObj(json);
	}
	
	protected void checkBlue(){
		if (getXplBluetoothService()!=null) {
			
		
		if (!getXplBluetoothService().isBluetoothEnable()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(enableBtIntent);
		// showIosDialog();
		}	else {
			getXplBluetoothService().scanDevice(true);
//			getXplBluetoothService().startScan();
//			getSimpleBlueService().scanDevice(true);
		}
		}
	}
	
	/**
	 * 等待框
	 * @param msg
	 * @return
	 */
	protected ProgressDialog showProgressDialog(String msg,boolean icancel) {
		ProgressDialog pDialog;
		pDialog = new ProgressDialog(this);
			pDialog.setCancelable(icancel);
			pDialog.setMessage(msg);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
			return pDialog;
	}

	protected void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
		mAsyncTasks.add(asyncTask.execute());
	}

	protected void clearAsyncTask() {
		Iterator<AsyncTask<Void, Void, Boolean>> iterator = mAsyncTasks
				.iterator();
		while (iterator.hasNext()) {
			AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();
			if (asyncTask != null && !asyncTask.isCancelled()) {
				asyncTask.cancel(true);
			}
		}
		mAsyncTasks.clear();
	}

//	protected void showLoadingDialog(String text) {
//		if (text != null) {
//			mLoadingDialog.setText(text);
//		}
//		mLoadingDialog.show();
//	}

//	protected void dismissLoadingDialog() {
//		if (mLoadingDialog.isShowing()) {
//			mLoadingDialog.dismiss();
//		}
//	}

	/** 短暂显示Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** 短暂显示Toast提示(来自String) **/
	protected void showShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	protected void showLongToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
	}

	/** 长时间显示Toast提示(来自String) **/
	protected void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	/** 显示自定义Toast提示(来自res) **/
	protected void showCustomToast(int resId) {
		View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
				R.layout.common_toast, null);
		((TextView) toastRoot.findViewById(R.id.toast_text))
				.setText(getString(resId));
		Toast toast = new Toast(BaseActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
				R.layout.common_toast, null);
		((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(BaseActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** Debug输出//Log日志 **/
	protected void showDebug(String tag, String msg) {
		Log.d(tag, msg);
	}

	/** Error输出//Log日志 **/
	protected void showError(String tag, String msg) {
		Log.e(tag, msg);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	protected com.mycj.beasun.view.AlertDialog showIosDialog(String message) {
		final com.mycj.beasun.view.AlertDialog setMsg = new com.mycj.beasun.view.AlertDialog(this).builder()
				.setMsg(message)
				.setPositiveButton(getString(R.string.positive), new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});setMsg.show();
		return setMsg;
	}
	
	/** 含有标题和内容的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).show();
		return alertDialog;
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 含有标题、内容、图标、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			int icon, String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(icon)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 默认退出 **/
	protected void defaultFinish() {
		super.finish();
	}
	
//	protected AbstractSimpleBlueService getSimpleBlueService() {
//		return ((BaseApp)getApplication()).getSimpleBlueService();
//	}
	protected XplBluetoothService getXplBluetoothService() {
		return ((BaseApp)getApplication()).getXplBluetoothService();
	}
	
	protected MusicService getMusicService() {
		return ((BaseApp)getApplication()).getMusicService();
	}
	
	public ObjectAnimator clickAnimation(View v){
		ObjectAnimator animator =  ObjectAnimator.ofFloat(v, "alpha", 1f,0.8f,0.6f,0.2f,0.6f,1f);
		animator.setDuration(200);
		return animator;
	}

	
}
