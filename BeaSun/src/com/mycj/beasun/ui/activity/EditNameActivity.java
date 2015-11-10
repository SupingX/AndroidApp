package com.mycj.beasun.ui.activity;

import java.util.List;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.bean.DeviceJson;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.BluetoothJson;
import com.mycj.beasun.service.util.FileUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class EditNameActivity extends BaseActivity implements OnClickListener {

	private EditText tvName;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_name);
		Intent intent = getIntent();
		if (intent!=null) {
			position = intent.getExtras().getInt("position");
		}
		initViews();
	}

	private void initViews() {
		TextView tvBack = (TextView) findViewById(R.id.tv_back);
		TextView tvSave = (TextView) findViewById(R.id.tv_save);
		tvBack.setOnClickListener(this);
		tvSave.setOnClickListener(this);
		tvName = (EditText) findViewById(R.id.et_name);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_save:
			String name = tvName.getText().toString();
			if (name==null ||name.length()==0) {
//				showShortToast("不能为空");
				return ;
			}
			
			List<DeviceJson> deviceJsons = BluetoothJson.loadJson(this);
			deviceJsons.get(position).setName(name);
			FileUtil.writeFileData(BeaStaticValue.JSON_DEVICE, BluetoothJson.listToJson(deviceJsons), this);
			
			finish();
			break;

		default:
			break;
		}
	}
}
