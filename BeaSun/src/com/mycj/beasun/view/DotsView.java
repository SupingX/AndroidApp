package com.mycj.beasun.view;

import com.mycj.beasun.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class DotsView extends View {

	private Paint mDotPaint;
	private Paint mTextPaint;
	private float perWidth;
	private int[] colors = new int[] { 
			R.color.dot_1, R.color.dot_2, R.color.dot_3
			, R.color.dot_4, R.color.dot_5, R.color.dot_6
			, R.color.dot_7, R.color.dot_8, R.color.dot_9, R.color.dot_10,R.color.dot_11
	};

	public DotsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public DotsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DotsView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mDotPaint = new Paint();
		mDotPaint.setAntiAlias(true); // 消除锯齿
		mDotPaint.setStyle(Paint.Style.FILL); // 绘制空心圆
		mDotPaint.setStrokeWidth(2); // 设置进度条宽度
		mDotPaint.setStrokeJoin(Paint.Join.ROUND);
		mDotPaint.setStrokeCap(Paint.Cap.ROUND); // 设置圆角
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true); // 消除锯齿
		mTextPaint.setStyle(Paint.Style.FILL); // 绘制空心圆
		mTextPaint.setStrokeWidth(1); // 设置进度条宽度
		mTextPaint.setTextSize(15);
		mTextPaint.setColor(Color.WHITE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		perWidth = widthSize / (colors.length + 1);
		currentRadus = perWidth / 2;
		currentRadusNext = perWidth / 4;
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawDots(canvas);
	}

	// 默认第一个
	private int currentDot = 0;
	// 动画的半径(缩小)
	private float currentRadus;
	// 动画的半径(增大)
	private float currentRadusNext;
	private boolean isIncrease;
	//动画是否执行完毕
	private boolean isAnimatorOver = true;
	
	private void drawDots(Canvas canvas) {
		for (int i = 0; i < colors.length; i++) {
			mDotPaint.setColor(getResources().getColor(colors[i])); // 设置进度条颜色
			if (currentDot == i) {
				// canvas.drawCircle(perWidth * (i + 1), getHeight() / 2,
				// perWidth / 2, mDotPaint);
				canvas.drawCircle(perWidth * (i + 1), getHeight() / 2, currentRadus, mDotPaint);
				String text = String.valueOf(i+1);
				Rect rectText = new Rect();
				mTextPaint.getTextBounds(text, 0, text.length(), rectText);
				canvas.drawText(text, perWidth * (i + 1)-rectText.width()/2, getHeight() / 2+rectText.height()/2, mTextPaint);
			}

			else {
				canvas.drawCircle(perWidth * (i + 1), getHeight() / 2, perWidth / 4, mDotPaint);
				if (isIncrease) {
					if (i == currentDot + 1) {
						canvas.drawCircle(perWidth * (i + 1), getHeight() / 2, currentRadusNext, mDotPaint);
					}
				}
				if (!isIncrease) {
					if (i == currentDot - 1) {
						canvas.drawCircle(perWidth * (i + 1), getHeight() / 2, currentRadusNext, mDotPaint);
					}
				}
			}
		}
	}

	/**
	 * 增加
	 */
	public void increase() {
		if (currentDot == colors.length-1) {
			return;
		}
		if (isAnimatorOver) {
			isAnimatorOver = false;
		
			isIncrease = true;
			ValueAnimator animatorReduce = ValueAnimator.ofObject(new RadusEvaluator(), perWidth / 2, perWidth / 4);
			animatorReduce.setDuration(200);
			animatorReduce.setInterpolator(new AccelerateInterpolator());
			animatorReduce.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float radus = (float) animation.getAnimatedValue();
					currentRadus = radus;
					currentRadusNext = perWidth * 3 / 4 - radus;
					invalidate();
				}
			});
			animatorReduce.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					isAnimatorOver = true;
					currentDot++;
					currentRadus = perWidth / 2;//当前点的半径
					currentRadusNext = perWidth / 4;//下一个点的半径
					if (mOnDotChangeListener!=null) {
						mOnDotChangeListener.onDotChange(currentDot);
					}
				}
			});
			animatorReduce.start();
			// currentDots++;
	
		}
	}
	
	public int getLevel(){
		return currentDot+1;
	}
	/**
	 * 减少
	 */
	public void reduce() {
		if (currentDot == 0) {
			return;
		}
		if (isAnimatorOver) {
			isAnimatorOver = false;
			isIncrease = false;
			ValueAnimator animatorReduce = ValueAnimator.ofObject(new RadusEvaluator(), perWidth / 2, perWidth / 4);
			animatorReduce.setDuration(200);
			animatorReduce.setInterpolator(new AccelerateInterpolator());
			animatorReduce.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float radus = (float) animation.getAnimatedValue();
					currentRadus = radus;//当前点的半径
					currentRadusNext = perWidth * 3 / 4 - radus;//下一个点的半径
					invalidate();
					
				}
			});
			animatorReduce.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					isAnimatorOver = true;
					currentDot--;
					currentRadus = perWidth / 2;
					currentRadusNext = perWidth / 4;
					if (mOnDotChangeListener!=null) {
						mOnDotChangeListener.onDotChange(currentDot);
					}
				}
			});
			animatorReduce.start();
		
		}
	}

	/**
	 * 半径插值器
	 * 
	 * @author Administrator
	 *
	 */
	class RadusEvaluator implements TypeEvaluator<Float> {

		@Override
		public Float evaluate(float fraction, Float startValue, Float endValue) {
			// 第一个参数fraction ：系数
			// startValue
			// endValue
			// 当前动画的值 = （结束值-开始值）* fraction系数 + 开始值
			return (endValue - startValue) * fraction + startValue;
		}

	}
	
	/**
	 * 获取当前的点
	 * @return
	 */
	public int currentDot(){
		return this.currentDot;
	}
	
	public void setCurrentDot(int currentDot){
		if (currentDot<=0) {
			currentDot=0;
		}
		if (currentDot>=10) {
			currentDot=10;
		}
		this.currentDot = currentDot;
		invalidate();
	}
	
	public interface OnDotChangeListener{
		public void onDotChange(int currentDot);
	}
	private OnDotChangeListener mOnDotChangeListener;
	public void setOnDotChangeListener (OnDotChangeListener l){
		this.mOnDotChangeListener = l;
	}
}
