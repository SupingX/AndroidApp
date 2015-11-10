package com.mycj.beasun.view;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.mycj.beasun.R;
import com.mycj.beasun.service.util.DisplayUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TimeArcView extends View {

	private Paint mCompletePaint;
	private Paint paint;
	private Paint mBackgroundPaint;
	private Paint mThumbPaint;
	private Paint mTextThumbPaint;
	private Paint mTextCenterPaint;
	private int times = 20;// 总的计时
	private float mmWidth;
	private Paint mLinePaint;
	private float strokeWidth;
	private float bgStrokeWidth;
	private boolean isCanTouch = true;

	public void setIsCanTouch(boolean isCanTouch) {
		this.isCanTouch = isCanTouch;
	}

	/**
	 * 是否可移动
	 */
	private boolean isTrag;
	private Bitmap bg;
	private float centerX;
	private float centerY;
	private Timer timer;
	private Bitmap thumb;
	private boolean drawOnce = false;

	// private static final int[] COLORS = new int[] { 0xFF00479D, 0xFF1D2088,
	// 0xFF601986, 0xFF920783, 0xFFBE0081, 0xFFE4007F, 0xFFE5006A, 0xFFE5004F,
	// 0xFF22AC38, 0xFF009944, 0xFF009B6B, 0xFF009E96,
	// 0xFF00A0C1, 0xFF00A0E9, 0xFF0086D1, 0xFF0068B7, 0xFFE60033, 0xFFE60012,
	// 0xFFEB6100, 0xFFF39800, 0xFFFCC800, 0xFFFFF100, 0xFFCFDB00, 0xFF8FC31F,
	// 0xFF00479D };
	private static final int[] COLORS = new int[] { 0xFFF16D12

	, 0xFFDF262E //
			, 0xFFFF00FE //
			, 0xFF536AD3//
			, 0xFF66BEED//
			, 0xFF2EA673//
			, 0xFF6CAC19//
			, 0xFFF3BB0B//
			, 0xFFF16D12 //

	};
	/** 当前分钟 0~20 **/
	private int timePoint = 20;
	/** 当前角度 范围 [0,270] **/
	private double degree = 270.0000f;

	/**
	 * 圆环的半径
	 */
	private float radius;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			state = STATE_TIMING;
			if (mOnTimerStateListener != null) {
				mOnTimerStateListener.OnTimerState(state);
			}
			timePoint--;// 每1个分钟--
			if (timePoint <= 0) {
				stopTimer();
				degree = 0;
			}
			float perTime = 270f / times;
			degree = timePoint * perTime;

			// degree -= Math.ceil(270f / (20 * 60 ));//每秒角度减少量 = 总的角度
			// /20分钟*60*1000
			// if (degree <= 0) {
			// stopTimer();
			// degree = 0;
			// }
			invalidate();
			mHandler.postDelayed(runTimer, 60 * 1000);
		};
	};

	public TimeArcView(Context context) {
		super(context);
		init(context);
	}

	public TimeArcView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public TimeArcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 屏幕的参数
		Point screenMetrics = DisplayUtil.getScreenMetrics(getContext());
		int mWidth = screenMetrics.x * 3 / 4;
		int mHeight = mWidth;
		if (thumb != null) {
			//Log.i("", "thumb : " + thumb.getHeight() + "," + thumb.getWidth());
		}
		// 中心点
		centerX = mWidth / 2;
		centerY = mHeight / 2;
		// 圆环宽度
		bgStrokeWidth = (float) thumb.getWidth() / 2;
		strokeWidth = bgStrokeWidth - 8f;
		mBackgroundPaint.setStrokeWidth(bgStrokeWidth); // 设置进度条宽度
		mCompletePaint.setStrokeWidth(strokeWidth); // 设置进度条宽度
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 整个view的宽度 （和高度一样）
		mmWidth = getWidth();
		// 获取圆环的半径 为整个View的宽度/2-thumb的宽度/2
		radius = (mmWidth - thumb.getWidth()) / 2;
		
		drawArc(canvas);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isCanTouch) {
				float downX = event.getX();
				float downY = event.getY();
				//Log.i("", "(downX,downX) :" + "(" + downX + "," + downX + ")");
				// 当点在thumb上时
				// 1。求出当前progress对应的thumb坐标
				// float left = (float) ((radius) *
				// Math.cos(degreeToPi(progressToDeGree(progress))) + radius);
				// float top = (float) ((radius) *
				// Math.sin(degreeToPi(progressToDeGree(progress))) + radius);
				// float top = (float) ((radius) *
				// Math.abs(Math.sin(degreeToPi(degree))) + radius);
				// float left = (float) ((radius) *
				// Math.abs(Math.cos(degreeToPi(degree))) + radius);
				float top = 0;
				float left = 0;
				if (degree > 225 && degree <= 270) {// 第4象限
					float pi = (float) (Math.PI * degree / 180f - Math.PI / 4 - Math.PI);
					top = (float) ((radius) * Math.sin(pi) + radius);
					left = (float) ((radius) * Math.cos(pi) + radius);
				} else if (degree > 135 && degree <= 225) {// 第1象限
					float pi = (float) (Math.PI / 4 + Math.PI - Math.PI * degree / 180f);
					top = (float) (radius - (radius) * Math.sin(pi));
					left = (float) ((radius) * Math.cos(pi) + radius);
				} else if (degree > 45 && degree <= 135) {// 第2象限
					float pi = (float) (Math.PI * degree / 180f - Math.PI / 4);
					top = (float) (radius - (radius) * Math.sin(pi));
					left = (float) (radius - (radius) * Math.cos(pi));
				} else if (degree >= 0 && degree <= 45) {// 第3象限
					float pi = (float) (Math.PI / 4 - Math.PI * degree / 180f);
					top = (float) (radius + (radius) * Math.sin(pi));
					left = (float) (radius - (radius) * Math.cos(pi));
				}

				// 2。是否按在 thmub上
				if (!(downX < left || downX > left + thumb.getWidth() || downY < top || downY > top + thumb.getHeight())) {
					mThumbPaint.setAlpha(100);
					isTrag = true;// 可以移动
					invalidate();
				}
			}
			
			break;
		case MotionEvent.ACTION_MOVE:
			if (isCanTouch) {
				if (isTrag) {// 当可以移动时，根据移动的坐标 改变progress
					float moveX = event.getX();
					float moveY = event.getY();
					double pi = getPi(new PointF(moveX, moveY), new PointF(centerX, centerY));// 当前点对应的弧度（例如：Pi/2）
					if (moveX > centerX && moveY > centerY) {// 第四象限
						//Log.i("", "第四象限");
						degree = (double) ((pi + Math.PI / 4 + Math.PI) * 180 / Math.PI);
					} else if (moveX > centerX && moveY < centerY) {
						//Log.i("", "第一象限");
						degree = (double) ((-pi + Math.PI / 4 + Math.PI) * 180 / Math.PI);
					} else if (moveX < centerX && moveY < centerY) {
						//Log.i("", "第二象限");
						degree = (double) ((pi + Math.PI / 4) * 180 / Math.PI);
					} else if (moveX < centerX && moveY > centerY) {
						//Log.i("", "第三象限");
						degree = (double) ((Math.PI / 4 - pi) * 180 / Math.PI);
					} else {

						break;
					}
					// 当超过最大角度时 =270
					if (degree > 270) {
						degree = 270;
					}
					// 当超过最小角度时 =0
//					if (degree < 0) {
//						degree = 0;
//					}
					if (degree <= 270f/20) {
						degree = 270f/20;
					}
					invalidate();
					if (mOnTimePointChange != null) {
						timePoint = getTimePoint();
						mOnTimePointChange.onChange(timePoint);
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			
			if (isCanTouch) {
				mThumbPaint.setAlpha(255);
				isTrag = false;// 清空
				invalidate();
				timePoint = getTimePoint();
				if (mOnTimePointChange != null) {
					mOnTimePointChange.onChange(timePoint);
				}
				if (mOnTouchCancelListener!=null) {
					mOnTouchCancelListener.onCancel(timePoint);
				}
			}
			break;

		default:
			break;
		}

//		return super.onTouchEvent(event);
		return true;
	}

	/**
	 * 当前时间点
	 * 
	 * @param timePoint
	 * @return
	 */
	// private float progressToDeGree(int progress) {
	// float degree = 270 * progress / max;
	// //Log.i("", "当前progress对应的degree ：" + degree);
	// return degree;
	// }

	public int getTimePoint() {
		float perTime = 270f / times;
		int number = (int) (degree / perTime);
		return number;
	}

	public void setTimePoint(int timePoint) {
		//Log.i("", "setTimePoint() :	" + timePoint);
		float perTime = 270f / times;
		this.degree = timePoint * perTime;
		invalidate();
		if (mOnTimePointChange != null) {
			mOnTimePointChange.onChange(timePoint);
		}
	}

	/**
	 * 开始倒计时
	 * 
	 * @param degree
	 */
	public void startTimer() {
		state = STATE_START;
		isCanTouch = false;
		if (mOnTimerStateListener != null) {
			mOnTimerStateListener.OnTimerState(state);
		}
		runTimer = new Runnable() {
			public void run() {
				Message message = new Message();
				message.what = 1;
				mHandler.sendMessage(message);
			}
		};
		mHandler.postDelayed(runTimer, 60 * 1000);
		// timer = new Timer(true);
		// timer.schedule(runTimer, 1000, 1000);// 延迟1000ms后执行，1000ms执行一次

	}

	/**
	 * 结束倒计时
	 * 
	 * @param degree
	 */
	public void stopTimer() {
		isCanTouch = true;
		// if (timer != null) {
		// timer.cancel();
		// timer = null;
		//
		// state = STATE_CANCEL;
		// if (mOnTimerStateListener!=null) {
		// mOnTimerStateListener.OnTimerState(state);
		// }
		// }
		if (runTimer != null) {
			mHandler.removeCallbacks(runTimer);

			state = STATE_CANCEL;
			if (mOnTimerStateListener != null) {
				mOnTimerStateListener.OnTimerState(state);
			}
		}
	}

	private void init(Context context) {
		setClickable(true);
		setLongClickable(true);
		setFocusable(true);

		mCompletePaint = new Paint();
		mCompletePaint.setAntiAlias(true); // 消除锯齿
		mCompletePaint.setColor(getResources().getColor(R.color.color_orange_main));
		mCompletePaint.setStyle(Paint.Style.STROKE); // 绘制空心圆

		mTextThumbPaint = new Paint();
		mTextThumbPaint.setAntiAlias(true); // 消除锯齿
		mTextThumbPaint.setColor(Color.WHITE);
		mTextThumbPaint.setStrokeWidth(10);
		mTextThumbPaint.setStyle(Paint.Style.FILL); // 绘制空心圆
		mTextThumbPaint.setTextSize(60);

		mTextCenterPaint = new Paint();
		mTextCenterPaint.setAntiAlias(true); // 消除锯齿
		mTextCenterPaint.setColor(Color.WHITE);
		mTextCenterPaint.setStrokeWidth(10);
		mTextCenterPaint.setStyle(Paint.Style.FILL); // 绘制空心圆
		mTextCenterPaint.setTextSize(120);
		
		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true); // 消除锯齿
		mLinePaint.setColor(Color.parseColor("#66dddddd"));
		mLinePaint.setStrokeWidth(2);
		mLinePaint.setStyle(Paint.Style.FILL); // 绘制空心圆

		mCompletePaint.setStrokeJoin(Paint.Join.ROUND);
		mCompletePaint.setColor(Color.argb(180, 255, 255, 255));

		mCompletePaint.setStrokeCap(Paint.Cap.ROUND); // 设置圆角

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setAntiAlias(true); // 消除锯齿
		mBackgroundPaint.setStyle(Paint.Style.STROKE); // 绘制空心圆

		mBackgroundPaint.setStrokeJoin(Paint.Join.ROUND);
		mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND); // 设置圆角

		paint = new Paint();
		paint.setAntiAlias(true); // 消除锯齿

		mThumbPaint = new Paint();
		mThumbPaint.setAntiAlias(true); // 消除锯齿
		mThumbPaint.setStyle(Paint.Style.FILL); // 绘制空心圆

		thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_time_bar_button);
		// bg = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_jingdian_bar_bg);

	}

	private int measureHeight(int heightMeasureSpec) {
		int result = 0;
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else if (mode == MeasureSpec.AT_MOST) {
			result = getHeight() - getPaddingTop() - getPaddingBottom();
			result = Math.min(result, size);
		}
		return result;
	}

	private int measureWidth(int widthMeasureSpec) {
		int result = 0;
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int size = MeasureSpec.getSize(widthMeasureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else if (mode == MeasureSpec.AT_MOST) {
			result = getWidth() - getPaddingLeft() - getPaddingRight();
			result = Math.min(result, size);
		}
		return result;
	}

	private void drawArc(Canvas canvas) {
		// 	画背景
		RectF oval = new RectF(thumb.getWidth() / 2, thumb.getWidth() / 2, mmWidth - thumb.getWidth() / 2, mmWidth - thumb.getWidth() / 2);
		SweepGradient shader = new SweepGradient(centerX, centerY, COLORS, null);
		mBackgroundPaint.setShader(shader);
		canvas.drawArc(oval, -225, 270, false, mBackgroundPaint);
		// 	画进度
		canvas.drawArc(oval, -225, (float) degree, false, mCompletePaint);
		//	画蚯蚓
		drawFushe(canvas);
		// 	画游标
		if (thumb != null) {
			float top = 0;
			float left = 0;
			if (degree > 225 && degree <= 270) {
				float pi = (float) (Math.PI * degree / 180f - Math.PI / 4 - Math.PI);
				top = (float) ((radius) * Math.sin(pi) + radius);
				left = (float) ((radius) * Math.cos(pi) + radius);
			} else if (degree > 135 && degree <= 225) {
				float pi = (float) (Math.PI / 4 + Math.PI - Math.PI * degree / 180f);
				top = (float) (radius - (radius) * Math.sin(pi));
				left = (float) ((radius) * Math.cos(pi) + radius);
			} else if (degree > 45 && degree <= 135) {
				float pi = (float) (Math.PI * degree / 180f - Math.PI / 4);
				top = (float) (radius - (radius) * Math.sin(pi));
				left = (float) (radius - (radius) * Math.cos(pi));
			} else if (degree >= 0 && degree <= 45) {
				float pi = (float) (Math.PI / 4 - Math.PI * degree / 180f);
				top = (float) (radius + (radius) * Math.sin(pi));
				left = (float) (radius - (radius) * Math.cos(pi));
			}
			;
			canvas.drawBitmap(thumb, left, top, mThumbPaint);
			// 	画游标数字
			float perTime = 270 / times;
			int number = (int) (degree / perTime);
			String text = String.valueOf(number);
			Rect rectText = new Rect();
			mTextThumbPaint.getTextBounds(text, 0, text.length(), rectText);
			canvas.drawText(text, left + thumb.getWidth() / 2 - rectText.width() / 2, top + thumb.getHeight() / 2 + rectText.height() / 2, mTextThumbPaint);
			//	 画中间数字
			if (isTrag) {
				Rect rectTextCenter = new Rect();
				mTextCenterPaint.getTextBounds(text, 0, text.length(), rectTextCenter);
			    canvas.drawText(text, centerX-rectTextCenter.centerX(), centerY-rectTextCenter.centerY(), mTextCenterPaint); 
//				canvas.drawText(text, centerX - rectText.width() / 2, centerY + rectText.height() / 2, mTextCenterPaint);
			}
		}
	}
	
	private void drawFushe(Canvas canvas) {
		int per = 360/2;
		for (int i = 0; i < per; i++) {
			PointF pointFromPi1 = getPointFromPi((Math.PI*2*i)/180f,radius-bgStrokeWidth/2+4);
			PointF pointFromPi2 = getPointFromPi((Math.PI*2*i)/180f,radius+bgStrokeWidth/2-4);
			canvas.drawLine(pointFromPi1.x, pointFromPi1.y, pointFromPi2.x, pointFromPi2.y, mLinePaint);
		}
	}
	
	/**
	 * 获取离中心点radius距离 角度Pi的点
	 * @param pi
	 * @param radius
	 * @return
	 */
	private PointF getPointFromPi(double pi,float radius){
		float x = 0F;
		float y = 0F;
		if (pi>=0 &&pi<=Math.PI/2) {
			x = (float) (mmWidth/2+radius*Math.cos(pi));
			y = (float) (mmWidth/2-radius*Math.sin(pi));
		}else if(pi>Math.PI/2&&pi<=Math.PI) {
			x = (float) (mmWidth/2-radius*Math.cos(Math.PI-pi));
			y = (float) (mmWidth/2-radius*Math.sin(Math.PI-pi));
		}else if(pi>Math.PI&&pi<=Math.PI+Math.PI/4) {
			x = (float) (mmWidth/2-radius*Math.cos(pi-Math.PI));
			y = (float) (mmWidth/2+radius*Math.sin(pi-Math.PI));
		}else if (pi>2*Math.PI-Math.PI/4&&pi<2*Math.PI) {
			x = (float) (mmWidth/2+radius*Math.cos(2*Math.PI-pi));
			y = (float) (mmWidth/2+radius*Math.sin(2*Math.PI-pi));
		}
//		//Log.e("", "(x,y)  :"  + "(" + x+","+y+")");
		return new PointF(x,y);
	}

	private double getPi(PointF pointF1, PointF pointF2) {
		double _x = Math.abs(pointF1.x - pointF2.x);
		double _y = Math.abs(pointF1.y - pointF2.y);
		if (_x == 0) {
			if (pointF1.y - pointF2.y > 0) {

				return Math.PI / 4 + Math.PI;
			} else {
				return Math.PI / 4;
			}
		}
		if (_y == 0) {
			return Math.PI / 4 + Math.PI / 2;
		}
		//Log.i("", "移动后的pi ：" + Math.atan2(_y, _x));
		// Math.atan2(centerY, centerX);//ATAN2(a,b)的取值范围介于 -pi 到 pi 之间（不包括
		// -pi），
		return Math.atan2(_y, _x);
	}

	/** 回调接口 **/
	private OnTimePointChangeListener mOnTimePointChange;

	public void setOnTimePointChangeListener(OnTimePointChangeListener l) {
		this.mOnTimePointChange = l;
	}

	public interface OnTimePointChangeListener {
		public void onChange(int progress);
	}

	public interface onTouchCancelListener {
		public void onCancel(int time);
	}
	public void setonTouchCancelListener(onTouchCancelListener l){
		mOnTouchCancelListener = l;
	}
	private onTouchCancelListener mOnTouchCancelListener;
	
	public interface OnTimerStateListener {
		public void OnTimerState(int state);
	}
	
	
	
	
	private OnTimerStateListener mOnTimerStateListener;
	public static final int STATE_START = 0x01;
	public static final int STATE_TIMING = 0x02;
	public static final int STATE_CANCEL = 0x00;
	private int state = STATE_CANCEL;
	private Runnable runTimer;

	public void setOnTimerStateListener(OnTimerStateListener l) {
		this.mOnTimerStateListener = l;
	}

}
