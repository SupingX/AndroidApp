package com.mycj.beasun.view;

import com.mycj.beasun.R;
import com.mycj.beasun.service.util.DisplayUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 侧滑View 添加至item
 * 
 * @author Administrator
 *
 */
public class SideView extends LinearLayout {

	private float downX;
	private float hideX = 60 * 2;

	public SideView(Context context) {
		super(context);
		initView(context);
	}

	public SideView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public SideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 设置水平放置
		setOrientation(LinearLayout.HORIZONTAL);
		itemView = LayoutInflater.from(context).inflate(R.layout.item_device_binded, this, false);
	
		// 屏幕宽度
		screenWidth = DisplayUtil.getScreenMetrics(context).x;
		// 设置参数 宽度=屏幕宽度+右边刷出的宽度 高度=60dp
		LayoutParams params = new LayoutParams(screenWidth + DisplayUtil.dip2px(context, hideX), DisplayUtil.dip2px(context, 60));
		// 右边margin-60dp
		params.rightMargin = -DisplayUtil.dip2px(context, hideX);
		itemView.setLayoutParams(params);
		addView(itemView);

		setClickable(true);
		setFocusable(true);
		setLongClickable(true);

		setListener();
	}

	private void setListener() {

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOpen) {
					mHandler.post(closeRunnable);
				}
			}
		});
	}

	private boolean isOpen = false;
	private int screenWidth;
	private Handler mHandler = new Handler() {

	};

	/**
	 * 打开
	 */
	private Runnable openRunnable = new Runnable() {

		@Override
		public void run() {
			//Log.e("", "增大" +currentDiff);
			if (currentDiff < DisplayUtil.dip2px(getContext(), hideX)) {
				currentDiff += 4;
				scrollTo(((int) currentDiff), 0);
				mHandler.postDelayed(openRunnable, 1);
			} else {
				mHandler.removeCallbacks(openRunnable);
				isOpen = true;
				currentDiff = DisplayUtil.dip2px(getContext(), hideX);
				isMoving = false;
			}
		}
	};

	/**
	 * 关闭
	 */
	private Runnable closeRunnable = new Runnable() {

		@Override
		public void run() {
			//Log.e("", "减少" +currentDiff);
			if (!(currentDiff <= 0)) {
				currentDiff -= 4;
				scrollTo(((int) currentDiff), 0);
				mHandler.postDelayed(closeRunnable, 1);
				// mHandler.postDelayed(closeRunnable, 1);
			} else {
				mHandler.removeCallbacks(closeRunnable);
				isOpen = false;
				currentDiff = 0;
				isMoving = false;
			}
		}
	};

	private float currentDiff;
	private View itemView;
	private boolean isMoving;
	private boolean isCanOpen = true;
	public void setIsCanOpen(boolean isCanOpen){
		this.isCanOpen = isCanOpen;
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isCanOpen) {
			 return super.onTouchEvent(event);
		}
		TextView tvConnect = (TextView) itemView.findViewById(R.id.tv_connect);
		tvConnect.setClickable(false);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			downX = event.getX();
			downX = event.getRawX();

			break;

		case MotionEvent.ACTION_MOVE:
			isMoving = true;
			float moveX = event.getRawX();
			float diff = moveX - downX;
			if (Math.abs(diff) >= 0) {
				if (diff <= 0) {
					if (!isMoving) {
						if (isOpen) {
							// scrollTo(screenWidth +
							// DisplayUtil.dip2px(getContext(), 60), 0);
							// scrollTo((int)lastX, 0);
							if (Math.abs(diff) >= DisplayUtil.dip2px(getContext(), hideX)) {
								currentDiff = Math.abs(diff);
								 scrollTo(((int) diff), 0);
//								mHandler.post(closeRunnable);
							}
						}
					} else {
						if (!isOpen) {
							if (Math.abs(diff) <= DisplayUtil.dip2px(getContext(), hideX)) {
								currentDiff = Math.abs(diff);
								scrollTo(-((int) diff), 0);
							}
						}
					}
				}
			}
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			// float upX = event.getX();
			// float diffUp = upX - downX;
			// if (diffUp > 0) { // 右滑
			// if (isOpen) {// 展开时
			// if (Math.abs(diffUp) >= 10) {// 当滑动的距离大于屏幕1/5
			// // scrollTo(screenWidth + DisplayUtil.dip2px(getContext(),
			// // 60), 0);
			// // scrollTo(0, 0);
			// mHandler.post(closeRunnable);
			//
			// }
			// }
			//
			// } else { // 左滑
			// if (!isOpen) {
			// if (Math.abs(diffUp) >= 10) {// 当滑动的距离大于10
			// // scrollTo(((int) screenWidth/4), 0);
			// mHandler.post(openRunnable);
			//
			// }
			// }
			// }
			if (currentDiff <= DisplayUtil.dip2px(getContext(), hideX)/4) {
					 mHandler.post(closeRunnable);
			} else {
					mHandler.post(openRunnable);
			}

			break;

		default:
			break;
		}
		tvConnect.setClickable(true);
		// scrollTo(DisplayUtil.dip2px(getContext(), 60), 0);
		return super.onTouchEvent(event);
	}

	public void close() {
		scrollTo(0, 0);
		isOpen = false;
		currentDiff = 0;
	}
	public void open() {
		scrollTo( DisplayUtil.dip2px(getContext(), hideX)/4, 0);
		isOpen = true;
		currentDiff = DisplayUtil.dip2px(getContext(), hideX)/4;
	}

}
