package com.mycj.beasun.view;

import com.mycj.beasun.R;
import com.mycj.beasun.service.util.DisplayUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.GetChars;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 侧滑View 添加至item
 * 
 * @author Administrator
 *
 */
public class NewSideView extends HorizontalScrollView {

	private int screenWidth;
	private View itemView;

	public NewSideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public NewSideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public NewSideView(Context context) {
		super(context);
		initView(context);
	}
	
	float hideX = 2*60;
	private void initView(Context context) {
		itemView = LayoutInflater.from(context).inflate(R.layout.item_device_binded, this, false);
		LayoutParams params = new LayoutParams(screenWidth + DisplayUtil.dip2px(context, hideX), DisplayUtil.dip2px(context, 60));
		itemView.setLayoutParams(params);
		addView(itemView);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 屏幕宽度
		int size = 10;
		screenWidth = DisplayUtil.getScreenMetrics(getContext()).x;
		//代表整个view分成10等分，屏幕宽度占8份
		measureChild(getChildAt(0), (int)(screenWidth*size*1.0f/(size-3+1)), MeasureSpec.getSize(heightMeasureSpec));
		setMeasuredDimension((int)(screenWidth*size*1.0f/(size-3+1)), MeasureSpec.getSize(heightMeasureSpec));
		this.scrollTo((int) (screenWidth*10f/8), 0);
	}
}
