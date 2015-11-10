package com.mycj.beasun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.R;
import com.mycj.beasun.adapter.DeviceScanAdapter.OnSelectedItemListener;
import com.mycj.beasun.bean.MassageItem;
import com.mycj.beasun.business.MassageUtil;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MassageAdapter extends RecyclerView.Adapter<MassageAdapter.MassageViewHolder>{
	private List<MassageItem> massages = new ArrayList<MassageItem>();
	private Context mContext;

	public MassageAdapter(List<MassageItem> massages, Context context) {
		this.massages = massages;
		this.mContext = context;
	}

	@Override
	public int getItemCount() {
		return massages.size();
	}

	@Override
	public void onBindViewHolder(MassageViewHolder holder, int pos) {
		MassageItem massage = massages.get(pos);
		holder.tvName.setText(MassageUtil.getTextFromInteger(massage.getIndex(),mContext));
		holder.tvChoose.setVisibility(massage.getIsChoose()==0?View.GONE:View.VISIBLE);
		holder.pos = pos;
	}



	@Override
	public MassageViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_massage, parent,false);
		return new MassageViewHolder(view);
	}
	
	
	public interface OnSelectedItemListener {
		public void onSelect(int position,MassageItem massageItem);
	}

	private OnSelectedItemListener mOnSelectedItemListener;

	public void setOnSelectedItemListener(OnSelectedItemListener l) {
		this.mOnSelectedItemListener = l;
	}
	public class MassageViewHolder extends ViewHolder{
		private TextView tvName;
		private TextView tvChoose;
		private int pos;
		
		public MassageViewHolder(View itemView) {
			super(itemView);
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
			tvChoose = (TextView) itemView.findViewById(R.id.tv_choose_ok);
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MassageItem massageItem = massages.get(pos);
					int isChoose = massageItem.getIsChoose();
					if (isChoose==1) {
						tvChoose.setVisibility(View.GONE);
						massageItem.setIsChoose(0);
					}else if (isChoose==0) {
						tvChoose.setVisibility(View.VISIBLE);
						massageItem.setIsChoose(1);
					}
					if (mOnSelectedItemListener != null) {
						mOnSelectedItemListener.onSelect(pos,massageItem);
					}
				}
			});
		}
		
	}

}
