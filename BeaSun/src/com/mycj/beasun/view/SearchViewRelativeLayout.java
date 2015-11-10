package com.mycj.beasun.view;

import com.mycj.beasun.R;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 查找蓝牙 动态图标
 * 包含一个查找图标 一个圆环 和一个背景
 * @author Administrator
 *
 */
public class SearchViewRelativeLayout extends RelativeLayout{
	
	private ImageView imgBg;
	private ImageView imgMain;
	private ImageView imgCircle;
	private boolean isRotation = false;
	
	
	public SearchViewRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public SearchViewRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SearchViewRelativeLayout(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		imgBg = new ImageView(context);
		imgBg.setImageResource(R.drawable.ic_device_search_bg);
		imgMain = new ImageView(context);
		imgMain.setImageResource(R.drawable.ic_device_search_main);
		imgCircle = new ImageView(context);
		imgCircle.setImageResource(R.drawable.ic_device_search_circle);
		RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imgBg.setLayoutParams(params);
		imgMain.setLayoutParams(params);
		imgCircle.setLayoutParams(params);
		this.addView(imgBg);
		this.addView(imgMain);
		this.addView(imgCircle);
		updateUI(isRotation);
		setClickable(true);
	}
	
	private void updateUI(boolean isShow){
		if (isRotation) {
			imgMain.setVisibility(View.VISIBLE);
			imgCircle.setVisibility(View.VISIBLE);
	
		
		}else{
			imgMain.setVisibility(View.GONE);
			imgCircle.setVisibility(View.GONE);
		}
		
	}
	
	private void rotation(){
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imgCircle, "rotation", 0,360f);
		objectAnimator.setDuration(3000);
		objectAnimator.setInterpolator(new LinearInterpolator());
		objectAnimator.setRepeatCount(-1);
		objectAnimator.start();
//		imgCircle.animate().rotation(360f);
	}

	public void startRotation(){
		rotation();
		isRotation = true;
		updateUI(isRotation);
		if (mOnRotationStateChangeListener!=null) {
			mOnRotationStateChangeListener.onChange(isRotation);
		}
	}
	
	public void stopRotation(){
		imgCircle.animate().cancel();
		isRotation = false;
		updateUI(isRotation);
		if (mOnRotationStateChangeListener!=null) {
			mOnRotationStateChangeListener.onChange(isRotation);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (isRotation) {
				stopRotation();
			}else{
				startRotation();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	
	public interface OnRotationStateChangeListener {
		void onChange( boolean isRotation);
	}
	private OnRotationStateChangeListener mOnRotationStateChangeListener;
	public void setOnRotationStateChangeListener(OnRotationStateChangeListener l){
		this.mOnRotationStateChangeListener = l;
	}

}
