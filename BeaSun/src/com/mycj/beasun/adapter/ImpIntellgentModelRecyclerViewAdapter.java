package com.mycj.beasun.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycj.beasun.R;
import com.mycj.beasun.bean.IntellgentModleItem;
import com.mycj.beasun.bean.MassageInfo;
import com.mycj.beasun.service.util.FileUtil;
import com.mycj.beasun.view.IntellgentModelItemView;

public class ImpIntellgentModelRecyclerViewAdapter extends RecyclerViewBaseAdapter<MassageInfo> {
	Context context;
	@Override
	public void showData(RecyclerViewBaseHolder viewHolder, int i, List<MassageInfo> mItemDataList) {
		IntellgentModeViewHolder holder = (IntellgentModeViewHolder) viewHolder;
		MassageInfo intellgentModleItem = mItemDataList.get(i);
		if (mItemDataList.size() == i + 1) { // 最后一个不写数字 即添加模式
			holder.tvText.setVisibility(View.INVISIBLE);
		} else {
			holder.tvText.setText(intellgentModleItem.getText());
		}
		if (i>10 && i<mItemDataList.size()-1) { 
			//自定义模式
			if (intellgentModleItem.getImgCustom()==null || intellgentModleItem.getImgCustom().equals("")) {
				holder.imgFront.setImageResource(R.drawable.ic_no_image);
			}else{
				holder.imgFront.setImageBitmap(FileUtil.readBitmapData("massage_"+intellgentModleItem.getText(),context));
//				holder.imgFront.setImageBitmap(FileUtil.getBitmap("massage_"+intellgentModleItem.getText()));
			}
		}else{
			//默认默认和添加
			holder.imgFront.setImageResource(intellgentModleItem.getImg());
		}

		holder.pos = i;
	}

	@Override
	public View createView(ViewGroup viewGroup,  final int i) {
		context = viewGroup.getContext();
//		final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_intellgent_model, viewGroup, false);
		IntellgentModelItemView view = new IntellgentModelItemView(viewGroup.getContext());
		return view;
	}

	@Override
	public RecyclerViewBaseHolder createViewHolder(View view) {
		return new IntellgentModeViewHolder(view);
	}

	class IntellgentModeViewHolder extends RecyclerViewBaseHolder {
		private TextView tvText;
		private ImageView imgFront;
		private int pos;

		public IntellgentModeViewHolder(View itemView) {
			super(itemView);
//			tvText = (TextView) itemView.findViewById(R.id.tv_text);
//			imgFront = (ImageView) itemView.findViewById(R.id.img_front);
			imgFront =  ((IntellgentModelItemView)itemView).getImageView();
			tvText = ((IntellgentModelItemView)itemView).getTextView();
		itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mOnItemClickListener != null) {
						mOnItemClickListener.onItemClick(pos, v);
					}
				}
			});
			itemView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					mOnItemLongClickListener.onItemLongClick(pos, v);
					return false;
				}
			});
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(int position, View view);
	}
	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener l) {
		this.mOnItemClickListener = l;
	}
	
	public interface OnItemLongClickListener{
		public void onItemLongClick(int position, View view);
	}
	private OnItemLongClickListener mOnItemLongClickListener;
	public void setOnItemLongClickListener(OnItemLongClickListener l){
		this.mOnItemLongClickListener = l;
	}
	
}
