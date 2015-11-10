package com.mycj.beasun.ui.activity;

import java.util.List;

import android.content.Intent;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.bean.MassageInfo;
import com.mycj.beasun.broadcastreceiver.SimpleBluetoothBroadcastReceiver;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.MassageJson;
import com.mycj.beasun.business.MassageUtil;
import com.mycj.beasun.business.MusicLoader;
import com.mycj.beasun.business.ProtoclWrite;
import com.mycj.beasun.service.MusicService;
import com.mycj.beasun.service.XplBluetoothService;
import com.mycj.beasun.service.util.FileUtil;
import com.mycj.beasun.service.util.SharedPreferenceUtil;
import com.mycj.beasun.view.DotsView;
import com.mycj.beasun.view.TimeArcView;
import com.mycj.beasun.view.TimeArcView.OnTimePointChangeListener;
import com.mycj.beasun.view.TimeArcView.onTouchCancelListener;

/**
 * 按摩控制
 * 
 * @author Administrator
 *
 */
public class MassageControlActivity extends BaseActivity implements OnClickListener {
	/** 力度选择View **/
	private DotsView dotsView;
	/** 增加 **/
	private ImageView imgIncrease;
	/** 减少 **/
	private ImageView imgReduce;
	/** 按摩器开关 **/
	private CheckBox cbOnOff;
	/** 音乐开关 **/
	private ImageView imgMusicOnOFF;
	/** 倒计时View **/
	private TimeArcView timeArcView;
	/** 返回 **/
	private TextView tvBack;
	/** 音乐名称 **/
	private TextView tvMusicName;
	/** 当前按摩信息 **/
	private MassageInfo currentMassageInfo;
	/** 当前activity标题 **/
	private TextView tvTitle;
	/** 音乐服务 **/
	private MusicService musicService;
	/** 蓝牙服务 **/
	// private AbstractSimpleBlueService mSimpleBlueService;
	private XplBluetoothService xplBluetoothService;
	private Handler mHandler = new Handler() {

	};
	private HorizontalScrollView srcoll;
	private RadioGroup rgModel;
	private SimpleBluetoothBroadcastReceiver mReceiver = new SimpleBluetoothBroadcastReceiver() {
		public void doTimeChange(final int time) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					updateUIWhenTimeChange(time);
				}
			});
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_massage_control);
		musicService = getMusicService();
		// mSimpleBlueService = getSimpleBlueService();
		xplBluetoothService = getXplBluetoothService();
		registerReceiver(mReceiver, XplBluetoothService.getIntentFilter());
		loadIntentData();
		initViews();
		showHorizontalRecyclerView();
		freshUI();
		setListener();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		currentMassageInfo = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle b = data.getExtras();
				if (b != null) {
					if (musicService.isPlaying()) {
						imgMusicOnOFF.setImageResource(R.drawable.ic_sound_on);
					} else {
						imgMusicOnOFF.setImageResource(R.drawable.ic_sound_off);
					}

					long music = b.getLong("musicId");
					currentMassageInfo.setMusic(music);
					String musicName = b.getString("musicTitle");
					tvMusicName.setText(musicName);
					List<MassageInfo> massageInfosFromJson = MassageJson.loadJson(this);
					if (music != 0) {
						int index = currentMassageInfo.getIndex();
						if (massageInfosFromJson != null && massageInfosFromJson.size() > 0 && index >= 11 && index <= 11 + massageInfosFromJson.size()) {
							massageInfosFromJson.get(index - 11).setMusic(music);
							FileUtil.writeFileData(BeaStaticValue.JSON_MASSAGE, MassageJson.listToJson(massageInfosFromJson), this);
						} else {

							switch (index) {
							case 0xF1:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_ROUNIE, music);
								break;
							case 0xF2:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_TUINA, music);
								break;
							case 0xF3:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_GUASHA, music);
								break;
							case 0xF4:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_SHOUSHEN, music);
								break;
							case 0xF5:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_QINGFU, music);
								break;
							case 0xF6:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_ZHENJIU, music);
								break;
							case 0xF7:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_CHUIJI, music);
								break;
							case 0xF8:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_ZHIYA, music);
								break;
							case 0xF9:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_JINGZHUI, music);
								break;
							case 0xFA:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_HUOGUAN, music);
								break;
							default:
								SharedPreferenceUtil.put(this, BeaStaticValue.MUSIC_INTELGENT_ + currentMassageInfo.getIndex(), music);
								break;
							}
						}
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private RadioButton rbQinfu;
	private long lastTime;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_reduce:
			long currentTimeMillis = System.currentTimeMillis();
			if (currentTimeMillis - lastTime < 500) {
				return;
			}
			lastTime = currentTimeMillis;
			dotsView.reduce();

			break;
		case R.id.img_increase:
			// if (!cbOnOff.isChecked()) {
			// return;
			// }
			long currentTimeMillis1 = System.currentTimeMillis();
			if (currentTimeMillis1 - lastTime < 500) {
				return;
			}
			lastTime = currentTimeMillis1;
			dotsView.increase();

			break;
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_music_name:
			startActivityForResult(new Intent(this, MusicActivity.class), 1);
			break;
		case R.id.img_music_voice:
			if (musicService.isPlaying()) {
				musicService.stop();
				imgMusicOnOFF.setImageResource(R.drawable.ic_sound_off);
			} else {
				musicService.play(MusicLoader.getMusicUriById(currentMassageInfo.getMusic()));
				imgMusicOnOFF.setImageResource(R.drawable.ic_sound_on);
			}

			break;

		default:
			break;
		}
	}

	private void showHorizontalRecyclerView() {
		if (currentMassageInfo.getIndex() == 0) {
			srcoll.setVisibility(View.VISIBLE);
		} else {
			srcoll.setVisibility(View.GONE);
		}
	}

	private long last = 0;
	private long deff = 800;

	private void setListener() {
		dotsView.setOnDotChangeListener(new DotsView.OnDotChangeListener() {

			@Override
			public void onDotChange(int currentDot) {
				if (!cbOnOff.isChecked()) {
					return ;
				}
				currentMassageInfo.setPower(currentDot+1);
				final long now = System.currentTimeMillis();
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (now - last >= deff) {
							last = now;
							// 连接蓝牙时
							// if (!mSimpleBlueService.isGattEmpty()) {
							if (xplBluetoothService != null && xplBluetoothService.getConnectedGattSize() > 0) {
								// 只有同一模式下才能继续写入操作
								if (xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
									// mSimpleBlueService.writeCharacteristic(ProtoclWrite.instance().protoclWriteForLevel(dotsView.getLevel()));
									// mSimpleBlueService.setMassageInfo(currentMassageInfo);
									// xplBluetoothService.writeCharacteristicForMapAsync(ProtoclWrite.instance().protoclWriteForLevel(dotsView.getLevel()));
									// xplBluetoothService.setMassageInfo(currentMassageInfo);

									MassageInfo massageInfo = xplBluetoothService.getMassageInfo();
									// massageInfo.setPower(currentDot + 1);
									massageInfo.setPower(dotsView.getLevel());
									// 力度必须在模式之前设置 否则无效
									xplBluetoothService.setMassageInfo(massageInfo);
									xplBluetoothService.startMassage();

								}
							}
						}
					}
				}, deff + 100);
			}
		});

		// 经典模式选择
		rgModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (!cbOnOff.isChecked()) {
					return ;
				}
		
				final long now = System.currentTimeMillis();
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (now - last >= deff) {
							//Log.e("!!!", "2秒后的状态：" + dotsView.getLevel());
							last = now;

							if (xplBluetoothService != null && xplBluetoothService.getConnectedGattSize() > 0) {
								if (xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
									//Log.v("", "=================================切换模式============================================");
									int model_1 = 0;
									int model_2 = 0;
									int index =0xF5;
									switch (rgModel.getCheckedRadioButtonId()) {
									case R.id.rb_model_chuiji:
										//Log.v("", "=====锤击====");
										index = 0xF7;
										model_1 = 0b100;
										model_2 = 0b0;

										break;
									case R.id.rb_model_qinfu:
										//Log.v("", "=====轻抚====");
										model_1 = 0b0;
										model_2 = 0b10;
										index = 0xF5;
										break;
									case R.id.rb_model_rounie:
										//Log.v("", "=====揉捏====");
										model_1 = 0b01;
										model_2 = 0b0;
										index = 0xF1;
										break;
									case R.id.rb_model_tuina:
										//Log.v("", "=====推拿====");
										model_1 = 0b10;
										model_2 = 0b0;
										index = 0xF2;
										break;
									case R.id.rb_model_zhenjiu:
										//Log.v("", "=====针灸====");
										model_1 = 0b10000;
										model_2 = 0b0;
										index = 0xF6;
										break;
									case R.id.rb_model_zhiya:
										//Log.v("", "=====指压====");
										model_1 = 0b100000;
										model_2 = 0b0;
										index = 0xF8;
										break;

									default:
										break;
									}
									MassageInfo massageInfo = xplBluetoothService.getMassageInfo();
									massageInfo.setIndex(index);
									massageInfo.setModel_1(model_1);
									massageInfo.setModel_2(model_2);
									// 力度必须在模式之前设置 否则无效
									xplBluetoothService.setMassageInfo(massageInfo);
									xplBluetoothService.startMassage();
//									currentMassageInfo = xplBluetoothService.getMassageInfo();

									// xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForLevel(massageInfo.getPower()));
									//
									// if (currentMassageInfo.getIsPix() == 1) {
									// // 混合模式
									// xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForIntelligent(model_1,
									// model_2));
									// } else {
									// // 单一模式
									// String singleHex =
									// MassageUtil.getSingleHex(model_1,
									// model_2);
									// xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForSingle(singleHex));
									// }
								}
							}

						}
					}
				}, deff + 50);

				/*
				 * if (!mSimpleBlueService.isGattEmpty()) { if
				 * (isSameMassageInfo) { // 同步模式 MassageInfo massageInfo =
				 * mSimpleBlueService.getMassageInfo();
				 * massageInfo.setModel_1(model_1);
				 * massageInfo.setModel_2(model_2);
				 * mSimpleBlueService.writeCharacteristic
				 * (ProtoclWrite.instance().protoclWriteForSingle(model_1,
				 * model_2));
				 * mSimpleBlueService.writeCharacteristic(ProtoclWrite
				 * .instance().protoclWriteForLevel(massageInfo.getPower())); }
				 * }
				 */

				// if (xplBluetoothService.getConnectedGattSize() > 0) {
				// if
				// (xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
				// //Log.v("",
				// "=================================切换模式============================================");
				// MassageInfo massageInfo =
				// xplBluetoothService.getMassageInfo();
				// massageInfo.setModel_1(model_1);
				// massageInfo.setModel_2(model_2);
				// // 力度必须在模式之前设置 否则无效
				//
				// xplBluetoothService.setMassageInfo(massageInfo);
				// xplBluetoothService.startMassage();
				//
				// //
				// xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForLevel(massageInfo.getPower()));
				// //
				// // if (currentMassageInfo.getIsPix() == 1) {
				// // // 混合模式
				// //
				// xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForIntelligent(model_1,
				// // model_2));
				// // } else {
				// // // 单一模式
				// // String singleHex = MassageUtil.getSingleHex(model_1,
				// // model_2);
				// //
				// xplBluetoothService.writeCharacteristicForMap(ProtoclWrite.instance().protoclWriteForSingle(singleHex));
				// // }
				// }
				// }
			}
		});
		timeArcView.setonTouchCancelListener(new onTouchCancelListener() {

			@Override
			public void onCancel(int time) {
				//Log.e("", "time :" + time);
				currentTime = time;
//				currentMassageInfo.setTime(time);
				if (xplBluetoothService != null && xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
					// mSimpleBlueService.updateTime(time);
					xplBluetoothService.updateTime(time);
					;
					currentMassageInfo = xplBluetoothService.getMassageInfo();
				} else {
				}
	

			}
		});

		// ps：已在service更新
		// timeArcView.setOnProgressListener(new OnProgressListener() {
		// @Override
		// public void onChange(int progress) {// 分钟
		// currentMassageInfo.setTime(progress);
		// if (!mSimpleBlueService.isGattEmpty()) {
		// if (mSimpleBlueService.isSameMassageInfo(currentMassageInfo)) {
		// mSimpleBlueService.setMassageInfo(currentMassageInfo);
		// }
		// }
		// }
		// });
		cbOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//Log.v("", "开");
					if (xplBluetoothService != null && xplBluetoothService.getConnectedGattSize() <= 0) {
						showIosDialog(getString(R.string.control_least_one_device));
						cbOnOff.setChecked(false);
						return;
					}

					if (xplBluetoothService != null && !xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {// 当前按摩信息不同时，可以开启开关。
						// if (xplBluetoothService.getMassageState() ==
						// XplBluetoothService.MASSAGE_STATE_STOP) {//当前是按摩停止时

						if (currentMassageInfo.getTime() == 0) {
							return;
						}

						imgMusicOnOFF.setImageResource(R.drawable.ic_sound_on);
						// 开启音乐
						if (currentMassageInfo.getMusic() != -1) {
							musicService.play(MusicLoader.getMusicUriById(currentMassageInfo.getMusic()));
						} else {
							if (musicService.isPlaying()) {
								musicService.stop();
							}
						}
						// mSimpleBlueService.setMassageInfo(currentMassageInfo);
						currentMassageInfo.setPower(dotsView.getLevel());
						int index = currentMassageInfo.getIndex();
						
						if (index==0) {
							
					
						int model_1 = 0;
						int model_2 = 0;
						int  modelIndex = rgModel.getCheckedRadioButtonId();
//						//Log.e("", "modelIndex	:" + modelIndex);
						switch (modelIndex) {
						case R.id.rb_model_chuiji:
							//Log.v("", "=====锤击====");
							index = 0xF7;
							model_1 = 0b100;
							model_2 = 0b0;

							break;
						case R.id.rb_model_qinfu:
							//Log.v("", "=====轻抚====");
							model_1 = 0b0;
							model_2 = 0b10;
							index = 0xF5;
							break;
						case R.id.rb_model_rounie:
							//Log.v("", "=====揉捏====");
							model_1 = 0b01;
							model_2 = 0b0;
							index = 0xF1;
							break;
						case R.id.rb_model_tuina:
							//Log.v("", "=====推拿====");
							model_1 = 0b10;
							model_2 = 0b0;
							index = 0xF2;
							break;
						case R.id.rb_model_zhenjiu:
							//Log.v("", "=====针灸====");
							model_1 = 0b10000;
							model_2 = 0b0;
							index = 0xF6;
							break;
						case R.id.rb_model_zhiya:
							//Log.v("", "=====指压====");
							model_1 = 0b100000;
							model_2 = 0b0;
							index = 0xF8;
							break;

						default:
							break;
						}
						currentMassageInfo.setIndex(index);
						currentMassageInfo.setModel_1(model_1);
						currentMassageInfo.setModel_2(model_2);
						}
						
						// 模式	
						if (currentMassageInfo.getIndex()==0) {//经典模式时，默认模式为轻抚模式
							currentMassageInfo.setIndex(0xF5); 
						}
						
						
						currentMassageInfo.setTime(timeArcView.getTimePoint());
					
						xplBluetoothService.setMassageInfo(currentMassageInfo);
						// 开启按摩
						// mSimpleBlueService.startMassage();
						xplBluetoothService.startMassage();
						// 点击无效
						// timeArcView.setIsCanTouch(false);
						// timeArcView.startTimer();//改为由service计时，每隔1分钟返回一个通知更新计时器
						// }
					}
				} else {
					//Log.v("", "按钮 关");
					if (xplBluetoothService != null && xplBluetoothService.getMassageInfo() != null) {
						if (xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
							 xplBluetoothService.stopMassage();
//							xplBluetoothService.writeCharacteristicForMapAsync(ProtoclWrite.instance().protoclWriteForControl(0x03));
							// currentMassageInfo.setPower(1);
							// currentMassageInfo.setTime(20);
							// int model_1 = 0b0;
							// int model_2 = 0b10;
							// currentMassageInfo.setModel_1(model_1);
							// currentMassageInfo.setModel_2(model_2);
							// dotsView.setCurrentDot(xplBluetoothService.getMassageInfo().getPower()-1);
							// timeArcView.setTimePoint(xplBluetoothService.getMassageInfo().getTime());
						}
					}
					if (musicService.isPlaying()) {
						musicService.stop();
						imgMusicOnOFF.setImageResource(R.drawable.ic_sound_off);
					}
//					dotsView.setCurrentDot(0);
//					rbQinfu.setChecked(true);
					// timeArcView.setTimePoint(20);
					// } else {
					// 不是同一个 模式 按钮本来就是关闭状态
					// timeArcView.setTimePoint(20);
					// }
				}
			}

			// }

		});
		imgMusicOnOFF.setOnClickListener(this);
	}

//	private void whenStop() {
//		// if (!mSimpleBlueService.isGattEmpty() && isSameMassageInfo) {
//		// //Log.e("-------------------------->",
//		// "xplBluetoothService.getMassageInfo() :========"+
//		// xplBluetoothService.getMassageInfo());
//		// if (xplBluetoothService.getMassageInfo()==null)
//		// {//service没有按摩信息,这时候关闭
//		// dotsView.setCurrentDot(0);
//
//		// }else{//service有按摩信息时,表示现在正在按摩？
//		// if (xplBluetoothService.isSameMassageInfo(currentMassageInfo))
//		// {//1。先判断当前页面和service里面是否是同一个按摩信息，是则停止按摩
//		// xplBluetoothService.stopMassage();
//		// // xplBluetoothService.setMassageInfo(null);
//		// //
//		// xplBluetoothService.setMassageState(XplBluetoothService.MASSAGE_STATE_STOP);
//		// cbOnOff.setChecked(false);
//		// dotsView.setCurrentDot(0);
//		// timeArcView.setTimePoint(20);
//		// if (musicService.isPlaying()) {
//		// musicService.stop();
//		// imgMusicOnOFF.setImageResource(R.drawable.ic_sound_off);
//		// }
//		// }else{
//		//
//		// }
//		// }
//	}

	private void initViews() {
		imgIncrease = (ImageView) findViewById(R.id.img_increase);
		imgReduce = (ImageView) findViewById(R.id.img_reduce);
		dotsView = (DotsView) findViewById(R.id.dots);
		tvBack = (TextView) findViewById(R.id.tv_back);
		tvTitle = (TextView) findViewById(R.id.tv_control_title);
		tvMusicName = (TextView) findViewById(R.id.tv_music_name);

		srcoll = (HorizontalScrollView) findViewById(R.id.srcoll);

		rgModel = (RadioGroup) findViewById(R.id.rg_model);
		rbQinfu = (RadioButton) findViewById(R.id.rb_model_qinfu);

		rgModel.check(R.id.rb_model_qinfu);

		tvMusicName.setOnClickListener(this);
		tvBack.setOnClickListener(this);
		imgIncrease.setOnClickListener(this);
		imgReduce.setOnClickListener(this);
		// 计时器
		timeArcView = (TimeArcView) findViewById(R.id.time_arc);

		// 按摩器开关
		cbOnOff = (CheckBox) findViewById(R.id.cb_on_off);

		imgMusicOnOFF = (ImageView) findViewById(R.id.img_music_voice);

		// radio
		RadioButton rb = (RadioButton) findViewById(R.id.rb_model_chuiji);
		rb.setCompoundDrawablePadding(2);
	}

	/**
	 * 接受数据
	 * 
	 */
	private void loadIntentData() {
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if (b != null) {
			currentMassageInfo = b.getParcelable("massage");
			//Log.v("", "-----------------currentMassageInfo 接受到intent的按摩模式信息---------------------" + "\n" + currentMassageInfo);
		}
	}

	/**
	 * 根据当前currentMassageInfo 加载视图
	 */
	private void freshUI() {
		// 1.检查当前MassageInfo 是否与service里的一样
		// isSameMassageInfo =
		// mSimpleBlueService.isSameMassageInfo(currentMassageInfo);

		// 如果当前视图 和 service上的视图 一样，则更新当前currentMassageInfo一些参数
		if (xplBluetoothService != null && xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
			// if (mSimpleBlueService.getMassageState() == 1) {
			if (xplBluetoothService.getMassageState() == XplBluetoothService.MASSAGE_STATE_START) {
				// timeArcView.setIsCanTouch(false);
			}
			// currentMassageInfo = mSimpleBlueService.getMassageInfo();
			currentMassageInfo = xplBluetoothService.getMassageInfo();
		}

		// 开关
		// if (isSameMassageInfo) {
		if (xplBluetoothService != null && xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
			// cbOnOff.setChecked(mSimpleBlueService.getMassageState() == 1 ?
			// true : false);
			cbOnOff.setChecked(xplBluetoothService.getMassageState() == XplBluetoothService.MASSAGE_STATE_START ? true : false);

		} else {
			cbOnOff.setChecked(false);
		}

		if (xplBluetoothService != null && xplBluetoothService.isSameMassageInfo(currentMassageInfo)) {
			if (musicService.isPlaying()) {
				imgMusicOnOFF.setImageResource(R.drawable.ic_sound_on);
			} else {
				imgMusicOnOFF.setImageResource(R.drawable.ic_sound_off);
			}
		}

		// 力度
		dotsView.setCurrentDot(currentMassageInfo.getPower() - 1);
		// 音乐
		if (currentMassageInfo.getMusic() == -1L) {
			tvMusicName.setText(getString(R.string.custom_choose_music));
		} else {
			String musicName = MusicLoader.getTitleFromId(currentMassageInfo.getMusic());
			if (musicName != null) {
				tvMusicName.setText(musicName);
			} else {
				tvMusicName.setText(getString(R.string.custom_choose_music));
			}
		}
		// 标题
		tvTitle.setText(currentMassageInfo.getText());
//		// 模式	
//		if (currentMassageInfo.getIndex()==0) {//经典模式时，默认模式为轻抚模式
//			currentMassageInfo.setIndex(0xF5); 
//		}
	
//		switch (currentMassageInfo.getIndex()) {
//		case 0:
//			int model_1 = 0;
//			int model_2 = 0;
//			switch (rgModel.getCheckedRadioButtonId()) {
//			case R.id.rb_model_chuiji:
//				model_1 = 0b100;
//				model_2 = 0b0;
//				break;
//			case R.id.rb_model_qinfu:
//				model_1 = 0b0;
//				model_2 = 0b10;
//				break;
//			case R.id.rb_model_rounie:
//				model_1 = 0b01;
//				model_2 = 0b0;
//				break;
//			case R.id.rb_model_tuina:
//				model_1 = 0b10;
//				model_2 = 0b0;
//				break;
//			case R.id.rb_model_zhenjiu:
//				model_1 = 0b10000;
//				model_2 = 0b0;
//				break;
//			case R.id.rb_model_zhiya:
//				model_1 = 0b100000;
//				model_2 = 0b0;
//				break;
//
//			default:
//				break;
//			}
//			currentMassageInfo.setModel_1(model_1);
//			currentMassageInfo.setModel_2(model_2);
//			break;
//		case 0xF1:
//		case 0xF2:
//		case 0xF3:
//		case 0xF4:
//		case 0xF5:
//		case 0xF6:
//		case 0xF7:
//		case 0xF8:
//		case 0xF9:
//		case 0xFA:
//			break;
//		default:
//			break;
//		}
		// 倒计时
		timeArcView.setTimePoint(currentMassageInfo.getTime());

	}
	
	private int currentTime = 20;
	
	/**
	 * 根据service传来的时间更新UI
	 * 
	 * @param time
	 */
	private void updateUIWhenTimeChange(int time) {
		if (!cbOnOff.isChecked()) {
			return;
		}
		//Log.e("", "currentMassageInfo.getTime();	" + currentMassageInfo.getTime());
		timeArcView.setTimePoint(time);
		if (time == 0) {
	
			dotsView.setCurrentDot(currentMassageInfo.getPower() - 1);
			//Log.e("", "currentMassageInfo :" +currentMassageInfo.getTime());
//			timeArcView.setTimePoint(currentMassageInfo.getTime());
			timeArcView.setTimePoint(currentTime);
			cbOnOff.setChecked(false);
			if (musicService.isPlaying()) {
				musicService.stop();
				imgMusicOnOFF.setImageResource(R.drawable.ic_sound_off);
			}
		}


	}
}
