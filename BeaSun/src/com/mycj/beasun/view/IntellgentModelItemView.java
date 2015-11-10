package com.mycj.beasun.view;

import com.mycj.beasun.R;
import com.mycj.beasun.service.util.DisplayUtil;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class IntellgentModelItemView extends FrameLayout{
	
	private ImageView imgFront;
	private TextView tvTitle;
	private int resId;
	private String text;
	private final int MAG = 8;
	
	public IntellgentModelItemView(Context context) {
		super(context);
		init(context);
	}

	public IntellgentModelItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public IntellgentModelItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public void setImageResource(int resId){
		this.resId = resId;
	}
	public void setText(String text){
		this.text =text;
	}
	
	public ImageView getImageView(){
		return this.imgFront;
	}
	
	public TextView getTextView()	{
		return this.tvTitle;
	}
	
	private void init(Context context) {
		Point screenMetrics = DisplayUtil.getScreenMetrics(context);
		//图片
		imgFront = new ImageView(context);
		imgFront.setImageResource(resId);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		params.bottomMargin=MAG;
		params.leftMargin=MAG;
		params.rightMargin=MAG;
		params.topMargin=MAG;
		params.width = screenMetrics.x/3-MAG*2;
		params.height=screenMetrics.x/3-MAG*2;
		imgFront.setLayoutParams(params);
		this.addView(imgFront);
		//文字
		tvTitle = new TextView(context);
		tvTitle.setText(text);
		tvTitle.setTextSize(16);
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setBackgroundColor(Color.parseColor("#88000000"));
		tvTitle.setGravity(Gravity.CENTER);
		LayoutParams paramsTextView = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		paramsTextView.gravity = Gravity.BOTTOM;
		paramsTextView.bottomMargin=MAG;
		paramsTextView.leftMargin=MAG;
		paramsTextView.rightMargin=MAG;
		paramsTextView.topMargin=MAG;
		tvTitle.setLayoutParams(paramsTextView);
		this.addView(tvTitle);
		
	}

	
	
	
}
