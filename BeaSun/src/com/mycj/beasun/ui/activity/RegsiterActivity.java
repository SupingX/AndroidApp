package com.mycj.beasun.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.R.id;
import com.mycj.beasun.R.layout;
import com.mycj.beasun.bean.User;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.NetWorkUtil;
import com.mycj.beasun.business.UserJson;
import com.mycj.beasun.service.util.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class RegsiterActivity extends BaseActivity implements OnClickListener {
	private static final String URL = "http://www.beafeel.com/api/UserInfo/UserRegister";
	private EditText etName;
	private EditText etPhone;
	private EditText etEmail;
	private EditText etPassword;
	private EditText etCall;
	private EditText etWeixin;
	private EditText etAddress;
	private EditText etQq;
	private TextView tvBack;
	private TextView tvPost;
	private Handler mHandler = new Handler (){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0xa1:
				switch (msg.arg1) {
				case -1:
					//注册失败
					doRegisterFail();
					break;
				case 0:
					//注册成功
					doRegisterSuccess((User)msg.obj);
					break;
				case 1:
				case 2:
					//已被注册
					doRegisterAgain();
					break;
					
				default:
					break;
				}
				break;
			case 0xF1:
				doRegisterFail();
				break;
			default:
				break;
			}
		};
	};
	private TextView tvRegisterInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regsiter);
		initViews();
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_save:
			doRegister();
			break;
		default:
			break;
		}
	}

	protected void doRegisterAgain() {
		tvRegisterInfo.setVisibility(View.VISIBLE);
		tvRegisterInfo.setText(getString(R.string.register_all_ready));
	}

	protected void doRegisterSuccess(User user) {
		tvRegisterInfo.setVisibility(View.INVISIBLE);
		Intent intent = new Intent(this,LoginActivity.class);
		intent.putExtra("user", user);
		setResult(RESULT_OK, intent);
		finish();
	}

	protected void doRegisterFail() {
		tvRegisterInfo.setVisibility(View.VISIBLE);
		tvRegisterInfo.setText(getString(R.string.register_fail));
	}

	protected void initViews() {
		etName = (EditText) findViewById(R.id.et_name);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etEmail = (EditText) findViewById(R.id.et_email);
		etPassword = (EditText) findViewById(R.id.et_password);
		etCall = (EditText) findViewById(R.id.et_call);
		etWeixin = (EditText) findViewById(R.id.et_weixin);
		etAddress = (EditText) findViewById(R.id.et_address);
		etQq = (EditText) findViewById(R.id.et_qq);
		tvBack = (TextView)findViewById(R.id.tv_back);
		tvPost = (TextView)findViewById(R.id.tv_save);
		tvRegisterInfo = (TextView)findViewById(R.id.tv_register_info);
		tvBack.setOnClickListener(this);
		tvPost.setOnClickListener(this);
	}

	protected void doRegister() {
		String name = etName.getText().toString();
		String phone = etPhone.getText().toString();
		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString();
		String call = etCall.getText().toString();
		String weixin = etWeixin.getText().toString();
		String address = etAddress.getText().toString();
		String qq = etQq.getText().toString();
		
		if (name.equals("")) {
			showIosDialog(getString(R.string.register_alert_name_is_null));
			return ;
		}
		if (phone.equals("")) {
			showIosDialog(getString(R.string.register_alert_phone_is_null));
			return ;
		}
		if (email.equals("")) {
			showIosDialog(getString(R.string.register_alert_email_is_null));
			return ;
		}
		if (password.equals("")) {
			showIosDialog(getString(R.string.register_alert_password_is_null));
			return ;
		}
		
//		if (name!=null && name.length()>15) {
//			showIosDialog(getString(R.string.register_alert_name_match_fail));
//			return ;
//		}
//		if (!name.matches("^[A-Za-z0-9]{9,15}$|(^[\u4e00-\u9fa5]{1,10}$")) {
//		if (!name.matches("[\u4e00-\u9fa5_a-zA-Z0-9_]{4,10}")) {
			if (!name.matches("^(\\w{9,12}|-|[\u4E00-\u9FA5]{1,6})$")) {
				showIosDialog(getString(R.string.register_alert_name_match_fail));
			return;
		}
		if (!phone.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$")) {
			showIosDialog(getString(R.string.register_alert_phone_match_fail));
			return;
		}
		if (!email.matches("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
			showIosDialog(getString(R.string.register_alert_email_match_fail));
			return ;
		}
		if (!password.matches("^[0-9a-zA-Z]{6,16}$")) {
			showIosDialog(getString(R.string.register_alert_password_match_fail));
			return ;
		}
//		if (!call.matches("(\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$")) {
//			showIosDialog("请输入正确的电话号码!");
//			return ;
//		}
//		if (!qq.matches("^\\d{5,10}$")) {
//			showIosDialog("请输入正确的QQ号码!");
//			return ;
//		}
		if (address!=null && address.length()>50) {
			showIosDialog(getString(R.string.register_alert_address_match_fail));
			return ;
		}
		if (weixin!=null && weixin.length()>50) {
			showIosDialog(getString(R.string.register_alert_weixin_match_fail));
			return ;
		}
	
		
		
		final User user = new User(name, password, phone, email, call, qq, weixin, address);
		//当有网络时
		if (NetWorkUtil.isNetworkConnected(this)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					doResigerStart(user, URL);
				}
			}).start();
		}else{
			showShortToast(getString(R.string.net_is_null));
		}
	}
	
	protected void doResigerStart(User user,String url) {
		//Log.e("", "开始注册");
		HttpURLConnection connection = null;
		InputStream is = null;
		ByteArrayOutputStream out = null;
		OutputStream os =null;
		try {
			URL urlConn = new URL(url);
			connection = (HttpURLConnection) urlConn.openConnection();
			// 传递的数据
			String data = "UserName=" + URLEncoder.encode(user.getName(), "UTF-8") 
					+ "&Password=" + URLEncoder.encode(user.getPassword(), "UTF-8")
					+ "&Phone=" + URLEncoder.encode(user.getPhone(), "UTF-8")
					+ "&TelePhone=" + URLEncoder.encode(user.getCall(), "UTF-8")
					+ "&Mail=" + URLEncoder.encode(user.getEmail(), "UTF-8")
					+ "&QQ=" + URLEncoder.encode(user.getQq(), "UTF-8")
					+ "&WeChat=" + URLEncoder.encode(user.getWeixin(), "UTF-8")
					+ "&Address=" + URLEncoder.encode(user.getAddress(), "UTF-8")
					;
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
//			connection.connect();
			//-------------------------------------------------------------
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				is = connection.getInputStream();//
			}
			if (is==null) {
				//Log.e("", "is 为null");
				mHandler.sendEmptyMessage(0xF1);
				return;
			}
			out = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			byte[] datas = out.toByteArray();

			//Log.e("", " 网路数据:	：" + new String(datas));
			//转话成json
//			boolean success = UserJson.isSuccess(new String(datas));
//			if (success) {
//				User user = UserJson.resultToObj(new String(datas));
//				String json = UserJson.objToJson(user);
//				FileUtil.writeFileData(BeaStaticValue.JSON_USER, json, this);
			int registerState = UserJson.getRegisterState(new String(datas));
			Message message = mHandler.obtainMessage();
			message.what=0xa1;
			message.obj = user;
			message.arg1 = registerState;
			mHandler.sendMessage(message);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
//			connection.disconnect();
			if (is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os!=null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
