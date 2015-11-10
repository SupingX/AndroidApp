package com.mycj.beasun.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.R.layout;
import com.mycj.beasun.adapter.DeviceScanItemDecoration;
import com.mycj.beasun.adapter.MassageAdapter;
import com.mycj.beasun.adapter.MassageAdapter.OnSelectedItemListener;
import com.mycj.beasun.bean.MassageItem;
import com.mycj.beasun.bean.Music;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.MassageUtil;
import com.mycj.beasun.service.util.DataUtil;
import com.mycj.beasun.service.util.SharedPreferenceUtil;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MassageActivity extends BaseActivity implements OnClickListener {
	
	
	private List<MassageItem> massages;
	private TextView tvBack;
	private TextView tvSave;
	private int massageSaved_1 = 0b0000_0000;
	private int massageSaved_2 = 0b0000_0000;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_massage);
		initViews();
	}

	private void initViews() {
		tvBack = (TextView) findViewById(R.id.tv_back);
		tvSave = (TextView) findViewById(R.id.tv_save);
		tvBack.setOnClickListener(this);
		tvSave.setOnClickListener(this);
		
		RecyclerView massageRecyclerView = (RecyclerView) findViewById(R.id.recycler_massage);
		massageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		massages = new ArrayList<>();
//		massageSaved_1 = (int) SharedPreferenceUtil.get(this, BeaStaticValue.MASSAGE_MODEL_1, 0);
//		massageSaved_2 = (int) SharedPreferenceUtil.get(this, BeaStaticValue.MASSAGE_MODEL_2, 0);
		Intent intent = getIntent();
		if (intent!=null) {
			massageSaved_1 = intent.getExtras().getInt("model_1");
			massageSaved_2 = intent.getExtras().getInt("model_2");
		}
		massages = MassageUtil.getSavedMassageItems(massageSaved_1,massageSaved_2);
		
//		MassageItem item1 = new MassageItem(0,0x01);
//		MassageItem item2 = new MassageItem(0,0x02);
//		MassageItem item3 = new MassageItem(0,0x03);
//		MassageItem item4 = new MassageItem(0,0x04);
//		MassageItem item5 = new MassageItem(0,0x05);
//		MassageItem item6 = new MassageItem(0,0x06);
//		MassageItem item7 = new MassageItem(0,0x07);
//		MassageItem item8 = new MassageItem(0,0x08);
//		MassageItem item9 = new MassageItem(0,0x09);
//		MassageItem item10 = new MassageItem(0,0x0a);
//		
//		massages.add(item1);
//		massages.add(item2);
//		massages.add(item3);
//		massages.add(item4);
//		massages.add(item5);
//		massages.add(item6);
//		massages.add(item7);
//		massages.add(item8);
//		massages.add(item9);
//		massages.add(item10);
		
		MassageAdapter adapter = new MassageAdapter(massages, this);
		adapter.setOnSelectedItemListener(new OnSelectedItemListener() {

			@Override
			public void onSelect(int position,MassageItem massageItem) {
				int isChoose = massageItem.getIsChoose();
				//Log.i("", "选择后 isChoose : " + isChoose );
				switch (massageItem.getIndex()) {
				case 0x01:
					massageSaved_1 ^= (0b1);
					break;
				case 0x02:
					massageSaved_1 ^= ((0b1)<<1);
					break;
				case 0x03:
					massageSaved_1 ^= ((0b1)<<2);
					break;
				case 0x04:
					massageSaved_1^=((0b1)<<3);
					break;
				case 0x05:
					massageSaved_1^=((0b1)<<4);
					break;
				case 0x06:
					massageSaved_1^=((0b1)<<5);
					break;
				case 0x07:
					massageSaved_1^=((0b1)<<6);
					break;
				case 0x08:
					massageSaved_2^=((0b1));
					break;
				case 0x09:
					massageSaved_2^=((0b1)<<1);
					break;
				case 0x0a:
					massageSaved_2^=((0b1)<<2);
					break;

				default:
					break;
				}
				//Log.i("", "结束后 massageSaved : " + Integer.toBinaryString(massageSaved_1));
				//Log.i("", "结束后 massageSaved : " + Integer.toBinaryString(massageSaved_2));
			}
//			@Override
//			public void onSelect(int position, boolean isSelected) {
//				Integer name = massages.get(position);
//				if (isSelected) {
//					if (!selectedMassages.contains(name)) {
//						selectedMassages.add(name);
//					}
//				} else {
//					if (selectedMassages.contains(name)) {
//						selectedMassages.remove(name);
//					}
//				}
//			}
		});
		massageRecyclerView.addItemDecoration(new DeviceScanItemDecoration(this, LinearLayoutManager.VERTICAL));
		massageRecyclerView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
	case R.id.tv_save:
		//保存选择的模式
		//两种方法：一、点击一个item 就改变一次值
		//二、退出时遍历selectedMassages 通过对应index的对应isChoose拼凑值
		
//				SharedPreferenceUtil.put(this, BeaStaticValue.MASSAGE_MODEL_1, massageSaved_1);
//				SharedPreferenceUtil.put(this, BeaStaticValue.MASSAGE_MODEL_2, massageSaved_2);
				sendResultToCustomActivity();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 
	 * 
	 */
	private void sendResultToCustomActivity(){
		
		if (massageSaved_1==0 && massageSaved_2==0) {
			showShortToast("至少选择一种模式");
			showIosDialog("至少选择一种模式");
			return ;
		}
			Intent intent = new Intent(this,MassageControlActivity.class);
			intent.putExtra("model_1", massageSaved_1);
			intent.putExtra("model_2", massageSaved_2);
			setResult(RESULT_OK,intent);
			finish();
		
	
	}
}
