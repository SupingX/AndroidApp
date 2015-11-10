package com.mycj.beasun.ui.activity;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.bean.User;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.service.util.FileUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class UserInfoActivity extends BaseActivity {

	private TextView tvBack;
	private TextView tvUserName;
	private TextView tvUserPhone;
	private TextView tvUserEmail;
	private TextView tvUserCall;
	private TextView tvUserQq;
	private TextView tvUserWeixin;
	private TextView tvUserAddress;
	private TextView tvLogOff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUi(loadUserJson());
	}


	protected void updateUi(User user) {
		if (user==null) {
			tvLogOff.setVisibility(View.INVISIBLE);
			return ;
		}
		tvLogOff.setVisibility(View.VISIBLE);
		tvUserName.setText(user.getName());
		tvUserPhone.setText(user.getPhone());
		tvUserEmail.setText(user.getEmail());
		tvUserCall.setText(user.getCall());
		tvUserQq.setText(user.getQq());
		tvUserWeixin.setText(user.getWeixin());
		tvUserAddress.setText(user.getAddress());
	}
	protected void initViews() {
		tvBack = (TextView) findViewById(R.id.tv_back);
		tvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvLogOff = (TextView) findViewById(R.id.tv_log_off);
		tvLogOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (loadUserJson()!=null) {
					FileUtil.writeFileData(BeaStaticValue.JSON_USER, "", UserInfoActivity.this);
					finish();
				}
			}
		});
		tvUserName = (TextView) findViewById(R.id.tv_user_name);
		tvUserPhone = (TextView) findViewById(R.id.tv_user_phone);
		tvUserEmail = (TextView) findViewById(R.id.tv_user_email);
		tvUserCall = (TextView) findViewById(R.id.tv_user_call);
		tvUserQq = (TextView) findViewById(R.id.tv_user_qq);
		tvUserWeixin = (TextView) findViewById(R.id.tv_user_weixin);
		tvUserAddress = (TextView) findViewById(R.id.tv_user_address);
	}
}
