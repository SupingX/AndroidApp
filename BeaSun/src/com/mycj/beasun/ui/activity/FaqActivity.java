package com.mycj.beasun.ui.activity;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FaqActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		initViews();
	}
	private void initViews() {
		TextView tvBack = (TextView) findViewById(R.id.tv_back);
		RelativeLayout rlFaq1 = (RelativeLayout) findViewById(R.id.rl_faq_1);
		RelativeLayout rlFaq2 = (RelativeLayout) findViewById(R.id.rl_faq_2);
		RelativeLayout rlFaq3 = (RelativeLayout) findViewById(R.id.rl_faq_3);
		RelativeLayout rlFaq4 = (RelativeLayout) findViewById(R.id.rl_faq_4);
		RelativeLayout rlFaq5 = (RelativeLayout) findViewById(R.id.rl_faq_5);
		RelativeLayout rlFaq6 = (RelativeLayout) findViewById(R.id.rl_faq_6);
		tvBack.setOnClickListener(this);
		rlFaq1.setOnClickListener(this);
		rlFaq2.setOnClickListener(this);
		rlFaq3.setOnClickListener(this);
		rlFaq4.setOnClickListener(this);
		rlFaq5.setOnClickListener(this);
		rlFaq6.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		String[] faqs = getResources().getStringArray(R.array.faqs);
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.rl_faq_1:
			showIosDialog(faqs[0]);
			break;
		case R.id.rl_faq_2:
			showIosDialog(faqs[1]);
			break;
		case R.id.rl_faq_3:
			showIosDialog(faqs[2]);
			break;
		case R.id.rl_faq_4:
			showIosDialog(faqs[3]);
			break;
		case R.id.rl_faq_5:
			showIosDialog(faqs[4]);
			break;
		case R.id.rl_faq_6:
			showIosDialog(faqs[5]);
			break;

		default:
			break;
		}
	}
}
