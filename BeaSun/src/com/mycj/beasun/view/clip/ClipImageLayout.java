package com.mycj.beasun.view.clip;

import com.mycj.beasun.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * @author zhy
 *
 */
public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	
	private Bitmap bitmap;
	
	public void setBitmap (Bitmap bitmap){
		this.bitmap = bitmap;
		if (bitmap!=null) {
			mZoomImageView.setImageBitmap(bitmap);
		}else{
			mZoomImageView.setImageResource(R.drawable.ic_beasun_cell0);
		}
		
		mZoomImageView.invalidate();
	}
	
	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		/**
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
//		mZoomImageView.setImageDrawable(getResources().getDrawable(
//				R.drawable.ic_beasun_cell10));
		if (bitmap!=null) {
			mZoomImageView.setImageBitmap(bitmap);
		}else{
			mZoomImageView.setImageResource(R.drawable.ic_beasun_cell0);
		}
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		
		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

}
