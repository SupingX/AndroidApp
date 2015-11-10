//package com.mycj.beasun.service;
//
//import java.io.IOException;
//
//
//
//
//import com.mycj.beasun.R;
//import com.mycj.beasun.bean.MassageInfo;
//
//import android.app.ProgressDialog;
//import android.content.IntentFilter;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnPreparedListener;
//
//public class SimpleBlueService extends AbstractSimpleBlueService {
//
//	public final static String ACTION_DATA_STEP_AND_CAL = "lite_data_step";
//	public final static String ACTION_DATA_SYNC_TIME = "lite_data_sync_time";
//	public final static String ACTION_CAMERA = "lite_data_camera";
//	public final static String ACTION_DATA_MUSIC = "lite_data_music";
//	public final static String ACTION_DATA_REMIND = "lite_data_remind";
//	public final static String ACTION_DATA_HEART_RATE = "lite_data_heart_rate";
//	public final static String ACTION_DATA_HISTORY_HEART_RATE = "lite_data_history_heart_rate";
//	public final static String ACTION_DATA_HISTORY_STEP = "lite_data_history_step";
//	public final static String ACTION_DATA_HISTORY_SLEEP = "lite_data_history_sleep";
//	public final static String ACTION_DATA_HISTORY_DISTANCE = "lite_data_history_distance";
//	public final static String ACTION_DATA_HISTORY_CAL = "lite_data_history_cal";
//	public final static String ACTION_DATA_HISTORY_SPORT_TIME = "lite_data_history_sport_time";
//	public final static String ACTION_DATA_HISTORY_SLEEP_FOR_TODAY = "lite_data_history_sleep_for_today";
//	public final static String ACTION_SYNC_START = "ACTION_SYNC_START";
//	public final static String ACTION_SYNC_END = "ACTION_SYNC_END";
//
//	public final static String EXTRA_STEP_AND_CAL = "extra_step";
//	public final static String EXTRA_SLEEP = "extra_sleep";
//	public final static String EXTRA_HEART_RATE = "EXTRA_HEART_RATE";
//	public final static String EXTRA_CAMERA = "EXTRA_CAMERA";
//
//	public static IntentFilter getIntentFilter() {
//		IntentFilter intentFilter = AbstractSimpleBlueService.getIntentFilter();
//		intentFilter.addAction(ACTION_DATA_STEP_AND_CAL);
//		intentFilter.addAction(ACTION_DATA_SYNC_TIME);
//		intentFilter.addAction(ACTION_CAMERA);
//		intentFilter.addAction(ACTION_DATA_MUSIC);
//		intentFilter.addAction(ACTION_DATA_REMIND);
//		intentFilter.addAction(ACTION_DATA_HEART_RATE);
//		intentFilter.addAction(ACTION_DATA_HISTORY_HEART_RATE);
//		intentFilter.addAction(ACTION_DATA_HISTORY_STEP);
//		intentFilter.addAction(ACTION_DATA_HISTORY_SLEEP);
//		intentFilter.addAction(ACTION_DATA_HISTORY_DISTANCE);
//		intentFilter.addAction(ACTION_DATA_HISTORY_CAL);
//		intentFilter.addAction(ACTION_DATA_HISTORY_SPORT_TIME);
//		intentFilter.addAction(ACTION_DATA_HISTORY_SLEEP_FOR_TODAY);
//		intentFilter.addAction(ACTION_SYNC_START);
//		intentFilter.addAction(ACTION_SYNC_END);
//		return intentFilter;
//	}
//
//	@Override
//	public void parseData(byte[] data) {
//
//	}
//
//
//	@Override
//	public void onReconnectedOverTimeOut() {
//		// 断线提醒
////		prepareRing();
////		mHander.postDelayed(new Runnable() {
////
////			@Override
////			public void run() {
////				if (player != null && player.isLooping()) {
////					player.stop();
////					player.release();
////				}
////			}
////		}, 5000);
////
////		if (dialogSyncSetting != null && dialogSyncSetting.isShowing()) {
////			dialogSyncSetting.dismiss();
////		}
//	}
//
//	private MediaPlayer player;
//	private ProgressDialog dialogSyncSetting;
//
//	/**
//	 * 获取断线铃音
//	 * 
//	 * @return
//	 * @throws IllegalStateException
//	 * @throws Exception
//	 * @throws IOException
//	 */
//	public void prepareRing() {
//		try {
//			player = MediaPlayer.create(getApplicationContext(), R.raw.a1);
//			player.setLooping(true);
//			player.setOnPreparedListener(new OnPreparedListener() {
//				@Override
//				public void onPrepared(MediaPlayer arg0) {
//					player.start();
//				}
//			});
//			player.prepare();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void onServicediscoveredSuccess() {
//	}
//
//	@Override
//	public void onWriteOver() {
//	}
//	
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		if (dialogSyncSetting != null && dialogSyncSetting.isShowing()) {
//			dialogSyncSetting.dismiss();
//		}
//	}
//
//	@Override
//	public void onServicediscoveredFail() {
//		
//	}
//	
//	
//	private void loadConfig(){
//		
//	}
//}
