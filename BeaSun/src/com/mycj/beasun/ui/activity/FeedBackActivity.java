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
import com.mycj.beasun.bean.User;
import com.mycj.beasun.business.NetWorkUtil;
import com.mycj.beasun.business.UserJson;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class FeedBackActivity extends BaseActivity implements OnClickListener {
	private static final String URL="http://www.beafeel.com/api/UserInfo/UserMessage";
	private EditText etTitle;
	private EditText etContent;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0xa1:
				String jsonFromNet =(String) msg.obj;//来自服务器端的状态
				int state = UserJson.getRegisterState(jsonFromNet);
				if (state==0) {
					doFeedBackSuccess();
				}else{
					doFeedBackFail();
				}
				break;
			case 0xF1:
				doFeedBackFail();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		initViews();
	}

	private void initViews() {
		etTitle = (EditText) findViewById(R.id.et_feed_back_title);
		etContent = (EditText) findViewById(R.id.et_feed_back_content);
		TextView tvBack = (TextView) findViewById(R.id.tv_back);
		TextView tvSend = (TextView) findViewById(R.id.tv_send);
		tvBack.setOnClickListener(this);
		tvSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_send:
			final String title = etTitle.getText().toString();
			if (title.equals("")) {
				showIosDialog(getString(R.string.feedback_title_empty));
				return;
			}
			final String content = etContent.getText().toString();
			if (content.equals("")) {
				showIosDialog(getString(R.string.feedback_content_empty));
				return ;
			}
			if (NetWorkUtil.isNetworkConnected(this)) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						doFeedBackStart(URL,title,content);
					}
				}).start();
			}
			break;
		default:
			break;
		}
	}

	private void doFeedBackSuccess() {
		showShortToast(getString(R.string.feedback_send_success));
		finish();
	}
	private void doFeedBackFail() {
		showShortToast(getString(R.string.feedback_send_fail));
	}
	
	private int getUserInfoId(){
		User user = loadUserJson();
		if (user==null) {
			return -1;
		}else{
			return user.getId();
		}
	}
	
	private void doFeedBackStart(String url, String title, String content) {
		//Log.e("", "开始提交意见");
		HttpURLConnection connection = null;
		InputStream is = null;
		ByteArrayOutputStream out = null;
		OutputStream os =null;
		try {
			URL urlConn = new URL(url);
			connection = (HttpURLConnection) urlConn.openConnection();
			// 传递的数据
			String data = "Title=" + URLEncoder.encode(title, "UTF-8") 
						+ "&Content=" + URLEncoder.encode(content, "UTF-8")
						+ "&FormType=0" 
						+ "&UserInfoId=" + getUserInfoId()
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

			//Log.e("", " 网路数据:	：" + new String(datas));
			String json =  new String(datas);
			//转话成json
			Message msg = mHandler.obtainMessage();
			msg.what=0xa1;
			msg.obj =json;
			mHandler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
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
