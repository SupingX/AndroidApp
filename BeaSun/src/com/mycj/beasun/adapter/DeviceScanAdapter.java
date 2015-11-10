package com.mycj.beasun.adapter;

import java.util.List;

import com.mycj.beasun.R;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 搜索蓝牙列表 adapter
 * 
 * @author Administrator
 *
 */
public class DeviceScanAdapter extends RecyclerView.Adapter<DeviceScanAdapter.DeviceScanViewHolder> {
	private List<BluetoothDevice> devices;
	private Context mContext;

	public DeviceScanAdapter(List<BluetoothDevice> devices, Context context) {
		this.devices = devices;
		this.mContext = context;
	}

	@Override
	public int getItemCount() {
		return devices.size();
	}

	@Override
	public void onBindViewHolder(DeviceScanViewHolder holder, int position) {
		BluetoothDevice device = devices.get(position);
		holder.tvName.setText(device.getName());
		holder.tvAddress.setText(device.getAddress());
		holder.imgChoose.setImageResource(R.drawable.ic_device_unselected);
		holder.position = position;
	}

	@Override
	public DeviceScanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		DeviceScanViewHolder holder = new DeviceScanViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_device_scan, parent, false));
		return holder;
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	public interface OnSelectedItemListener {
		public void onSelect(int position,boolean isSelected);
	}

	private OnSelectedItemListener mOnSelectedItemListener;

	public void setOnSelectedItemListener(OnSelectedItemListener l) {
		this.mOnSelectedItemListener = l;
	}

	/**
	 * 查找到的设备item展示视图
	 * 
	 * @author Administrator
	 *
	 */
	public class DeviceScanViewHolder extends ViewHolder {
		int position;
		boolean isSelected;
		TextView tvName;
		TextView tvAddress;
		ImageView imgChoose;
		RelativeLayout rlChoose;
		

		public DeviceScanViewHolder(View itemView) {
			super(itemView);
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
			tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
			imgChoose = (ImageView) itemView.findViewById(R.id.img_choose);
			rlChoose = (RelativeLayout) itemView.findViewById(R.id.rl_info);
			rlChoose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isSelected) {
						//	当选中时，设置不选中
						imgChoose.setImageResource(R.drawable.ic_device_unselected);
					}else{
						//	设置选中
						imgChoose.setImageResource(R.drawable.ic_device_selected);
					}
					isSelected = !isSelected;
					if (mOnSelectedItemListener != null) {
						mOnSelectedItemListener.onSelect(position,isSelected);
					}
				}
			});
		}

	}
}
