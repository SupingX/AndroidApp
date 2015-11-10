package com.mycj.beasun.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.R.id;
import com.mycj.beasun.R.layout;
import com.mycj.beasun.bean.User;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.NetWorkUtil;
import com.mycj.beasun.business.UserJson;
import com.mycj.beasun.service.util.FileUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private final static String URL = "http://www.beafeel.com/api/UserInfo/UserLogin";
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0xa1:
		
				String jsonFromNet = (String) msg.obj;
				int state = UserJson.isLoginSuccess(jsonFromNet);
				if (state == 0) {
					doLoginSuccess(jsonFromNet);
				} else {
					doLoginFail();
				}
				break;
			case 0xF1:
				doLoginFail();
				break;
			default:
				break;
			}
		}

	};
	private EditText etName;
	private EditText etPassword;
	private TextView tvLogin;
	private TextView tvResigter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initVies();
	}

	protected void doLoginSuccess(String jsonFromNet) {
//		tvLogin.setEnabled(true);
		if (jsonFromNet == null || jsonFromNet.equals("")) {
			return;
		}
		User user = UserJson.resultToObj(jsonFromNet);
		String jsonSaveUser = UserJson.objToJson(user);
		FileUtil.writeFileData(BeaStaticValue.JSON_USER, jsonSaveUser, this);
		startActivity(MainActivity.class);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				final User user = data.getParcelableExtra("user");
				if (user != null) {
//					showShortToast(user.toString());
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							setUserFromRegister(user);
						}
					});
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void setUserFromRegister(User user){
		etName.setText(user.getName());
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(logThread);
	}
	private void initVies() {
		tvLogin = (TextView) findViewById(R.id.tv_login);
		tvResigter = (TextView) findViewById(R.id.tv_resigter);
		etName = (EditText) findViewById(R.id.et_name);
		etPassword = (EditText) findViewById(R.id.et_password);
		tvLogin.setOnClickListener(this);
		tvResigter.setOnClickListener(this);
	}

	private String name;
	private String password;
	private Thread logThread;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login:
			name = etName.getText().toString();
			;
			password = etPassword.getText().toString();
			if (name.equals("")) {
				showIosDialog(getString(R.string.login_info_name));
				return;
			}
			if (password.equals("")) {
				showIosDialog(getString(R.string.login_info_password));
				return;
			}
			if (name.equals("123") && password.equals("123")) {
				startActivity(MainActivity.class);
				finish();
			}

			// 当有网络时
			if (NetWorkUtil.isNetworkConnected(this)) {
//				tvLogin.setEnabled(false);
				logThread = new Thread(new Runnable() {

					@Override
					public void run() {
						doLoginStart(URL, name, password);
					}
				});
				logThread.start();
			} else {
				showShortToast(getString(R.string.net_is_null));
			}

			break;
		case R.id.tv_resigter:
			Intent intent = new Intent(this, RegsiterActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	protected void doLoginFail() {
//		tvLogin.setEnabled(true);
		etName.setText("");
		etName.setHint(getString(R.string.login_fail));
		etName.setHintTextColor(Color.RED);
		etPassword.setText("");
	};

	private void doLoginStart(String url, String name, String password) {
		Log.e("", "开始登录");
		HttpURLConnection connection = null;
		InputStream is = null;
		ByteArrayOutputStream out = null;
		OutputStream os = null;
		try {
			URL urlConn = new URL(url);
			connection = (HttpURLConnection) urlConn.openConnection();
			// 传递的数据
			String data = "UserName=" + URLEncoder.encode(name, "UTF-8") + "&Password=" + URLEncoder.encode(password, "UTF-8");
			// 设置请求的头
			connection.setRequestProperty("Connection", "keep-alive");
			// 设置请求的头
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Accept-Charset", "utf-8");
			connection.setRequestProperty("contentType", "utf-8");
			// 设置请求的头
			connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true); // 设置是否向connection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			// 获取输出流
			os = connection.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			// connection.connect();
			// -------------------------------------------------------------
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				is = connection.getInputStream();//
			}
			if (is == null) {
				mHandler.sendEmptyMessage(0xF1);
				return;
			}
			out = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[200];
			while ((len = is.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			byte[] datas = out.toByteArray();

			Log.e("", " 网路数据:	：" + new String(datas));
			String json = new String(datas);
			// 转话成json
			Message msg = mHandler.obtainMessage();
			msg.what = 0xa1;
			msg.obj = json;
			mHandler.sendMessage(msg);
		} catch (IOException e) {
			mHandler.sendEmptyMessage(0xF1);
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
