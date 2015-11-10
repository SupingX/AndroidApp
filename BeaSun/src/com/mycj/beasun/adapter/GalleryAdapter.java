package com.mycj.beasun.adapter;

import java.util.List;

import com.mycj.beasun.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	 private int[] mDatas;
	 private String[] mTexts;
	
	public void setDatas(int[] mDatas){
		this.mDatas =mDatas;
		notifyDataSetChanged();
	}

	public GalleryAdapter(Context context, int[] mDatas) {
		mInflater = LayoutInflater.from(context);
//		this.selected = selected;
//		this.unSelected = unSelected;
		 this.mDatas = mDatas;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		ImageView mImg;
		TextView mTxt;
		int pos ;
		public ViewHolder(View view) {
			super(view);
			mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
			mTxt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
			mImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					notifyDataSetChanged();
					if (mOnItemClickListener != null) {
						mOnItemClickListener.onItemClick(v, pos);
					
					}
				}
			});
		}
		
	}

	@Override
	public int getItemCount() {
		return mDatas.length;
	}

	/**
	 * 创建ViewHolder
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
		View view = mInflater.inflate(R.layout.item_gallery, viewGroup, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}
	
	
	/**
	 * 设置值
	 */
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder,  int i) {
			viewHolder.pos = i;
			viewHolder.mImg.setImageResource(mDatas[i]);
			viewHolder.mTxt.setText(mTexts[i]);
			
	}

	public interface OnItemClickListener {
		public void onItemClick(View v, int position);
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		this.mOnItemClickListener = l;
	}
}