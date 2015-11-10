package com.mycj.beasun.view;

import com.mycj.beasun.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class MovingDotsView extends RelativeLayout{

	public MovingDotsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initVies(context);
	}

	public MovingDotsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVies(context);
	}

	public MovingDotsView(Context context) {
		super(context);
		
		initVies(context);
	}

	private void initVies(Context context) {
		ImageView imgReduce = new ImageView(context);
		imgReduce.setImageResource(R.drawable.ic_beasun_minus_btn);
		imgReduce.setScaleType(ScaleType.CENTER);
		RelativeLayout.LayoutParams paramsReduce = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		paramsReduce.addRule(RelativeLayout.ALIGN_PARENT_LEFT|RelativeLayout.CENTER_VERTICAL);
		imgReduce.setLayoutParams(paramsReduce);
		
		ImageView imgIncrease = new ImageView(context);
		imgIncrease.setImageResource(R.drawable.ic_beasun_plus_btn);
		imgIncrease.setScaleType(ScaleType.CENTER);
		RelativeLayout.LayoutParams paramsIncrease = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		paramsIncrease.addRule(RelativeLayout.ALIGN_PARENT_RIGHT|RelativeLayout.CENTER_VERTICAL);
		imgIncrease.setLayoutParams(paramsIncrease);
		
		DotsView dotsView = new DotsView(context);
		RelativeLayout.LayoutParams paramsDots = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		paramsDots.addRule(RelativeLayout.CENTER_IN_PARENT|RelativeLayout.CENTER_VERTICAL);
		dotsView.setLayoutParams(paramsDots);
		
		addView(dotsView);
		addView(imgReduce);
		addView(imgIncrease);
	}
	
	
	

}
