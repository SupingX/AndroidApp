package com.mycj.beasun.service.util;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected int layoutId;
	public CommonAdapter(Context context, List<T> mDatas,int layoutId) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = mDatas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,this.layoutId,position);
		convert(holder, mDatas.get(position));
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T item);
	
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public static class ViewHolder {
		private final SparseArray<View> mViews;
		private View mConvertView;
		
		private  ViewHolder(Context context,ViewGroup parent,int layoutId,int position){
			this.mViews = new SparseArray<>();
			mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
			mConvertView.setTag(this);
		}
		
		public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
			if (convertView == null) {
				return new ViewHolder(context, parent, layoutId, position);
			}else {
				return (ViewHolder) convertView.getTag();
			}
		}
		
		public <T extends View> T getView(int viewId){
			View view = mViews.get(viewId);
			if (view==null) {
				view = mConvertView.findViewById(viewId);
				mViews.put(viewId, view);
			}
			return (T) view;
		}
		
		public View getConvertView(){
			return mConvertView;
		}
		
	}

}
