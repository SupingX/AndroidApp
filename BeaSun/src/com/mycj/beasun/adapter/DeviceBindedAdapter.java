package com.mycj.beasun.adapter;

import java.util.List;

import com.mycj.beasun.R;
import com.mycj.beasun.bean.DeviceJson;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.hardware.Camera.Size;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 绑定蓝牙列表 adapter
 * 
 * @author Administrator
 *
 */
public class DeviceBindedAdapter extends RecyclerView.Adapter<DeviceBindedAdapter.DeviceBindedViewHolder> {
	private List<DeviceJson> devices;
	private Context mContext;

	public DeviceBindedAdapter(List<DeviceJson> devices, Context context) {
		this.devices = devices;
		this.mContext = context;
	}

	@Override
	public int getItemCount() {
		return devices.size();
	}

	@Override
	public void onBindViewHolder(DeviceBindedViewHolder holder, int position) {
		DeviceJson device = devices.get(position);
		holder.tvName.setText(device.getName());
		holder.tvAddress.setText(device.getAddress());
		holder.imgEdit.setImageResource(R.drawable.ic_device_unselected);
		holder.tvConnect.setText(mContext.getString(R.string.connect));
		holder.position = position;
	}

	@Override
	public DeviceBindedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		DeviceBindedViewHolder holder = new DeviceBindedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_device_binded, parent, false));
		if (viewType==getItemCount()) {
		}
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
	
	


	/**
	 * 绑定的设备item展示视图
	 * 
	 * @author Administrator
	 *
	 */
	public class DeviceBindedViewHolder extends ViewHolder {
		int position;
		boolean isSelected;
		TextView tvName;
		TextView tvAddress;
		TextView tvConnect;
		ImageView imgEdit;
		public DeviceBindedViewHolder(View itemView) {
			super(itemView);
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
			tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
			tvConnect = (TextView) itemView.findViewById(R.id.tv_connect);
			imgEdit = (ImageView) itemView.findViewById(R.id.img_edit_name);
		}
	}
}
