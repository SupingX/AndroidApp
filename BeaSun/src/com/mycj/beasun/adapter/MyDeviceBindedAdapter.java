package com.mycj.beasun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.R;
import com.mycj.beasun.bean.DeviceBindedInfo;
import com.mycj.beasun.bean.DeviceJson;
import com.mycj.beasun.view.SideView;

import android.content.Context;
import android.graphics.Color;
import android.nfc.tech.IsoDep;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDeviceBindedAdapter extends BaseAdapter {
	private List<DeviceBindedInfo> mItemDataList = new ArrayList<DeviceBindedInfo>();
	private Context mContext;

	public MyDeviceBindedAdapter(List<DeviceBindedInfo> mItemDataList, Context context) {
		this.mItemDataList = mItemDataList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mItemDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void closeItem(ViewHolder vh,int position,View convertView){
		((SideView)(convertView)).close();
	}
	public void openItem(ViewHolder vh,int position,View convertView){
		((SideView)(convertView)).close();
	}
	
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
//		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = new SideView(mContext);
			if (position==mItemDataList.size()-1) {
				((SideView)convertView).setIsCanOpen(false);
			}else{
				((SideView)convertView).setIsCanOpen(true);
			}
			
			
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
			viewHolder.tvBlueState = (TextView) convertView.findViewById(R.id.tv_connect);
			viewHolder.imgDianliang = (ImageView) convertView.findViewById(R.id.img_dianliang);
			viewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.img_edit_name);
			viewHolder.tvShutdown = (TextView) convertView.findViewById(R.id.tv_shut_down);
			viewHolder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
			viewHolder.llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
			viewHolder.tvAdd = (TextView) convertView.findViewById(R.id.tv_add);
			setListener(viewHolder, position,convertView);
			
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}

		// 刷新值

		if (position == (mItemDataList.size() - 1)) {
			// 添加更多
			viewHolder.llItem.setVisibility(View.GONE);
			viewHolder.tvAdd.setVisibility(View.VISIBLE);
		} else {
			DeviceBindedInfo deviceInfo = mItemDataList.get(position);
			viewHolder.llItem.setVisibility(View.VISIBLE);
			viewHolder.tvAdd.setVisibility(View.GONE);
			
			viewHolder.tvName.setText(deviceInfo.getName());
			viewHolder.tvAddress.setText(deviceInfo.getAddress());
			if (!viewHolder.isOpen) {
				closeItem(viewHolder, position, convertView);
			}else{
				openItem(viewHolder, position, convertView);
			}
			setBlueState(viewHolder, deviceInfo);

		}
		viewHolder.position = position;

		
		return convertView;
	}

	/**
	 * 获取电量
	 * 
	 * @param dianliang
	 * @return
	 */
	private int getImg(int dianliang) {
		if (dianliang == 0) {
			return R.drawable.ic_battery0;
		} else if (dianliang == 1) {
			return R.drawable.ic_battery1;
		} else if (dianliang == 2) {
			return R.drawable.ic_battery2;
		} else if (dianliang == 3) {
			return R.drawable.ic_battery3;
		} else if (dianliang == 4) {
			return R.drawable.ic_battery4;
		} else if (dianliang == 5) {
			return R.drawable.ic_battery4;
		} else {
			return R.drawable.ic_battery0;
		}
	}

	/**
	 * 设置蓝牙状态
	 * 
	 * @param state
	 * @param holder
	 * @param deviceInfo
	 */
	public void setBlueState(ViewHolder holder, DeviceBindedInfo deviceInfo) {
		if (deviceInfo.getBlueState() == -1) {// 未连接时，隐藏电量，显示连接
			holder.tvBlueState.setText(mContext.getString(R.string.connect));
			holder.tvBlueState.setVisibility(View.VISIBLE);
			holder.imgDianliang.setVisibility(View.GONE);

		} else { // 连接好时，隐藏连接，显示电量信息
			holder.imgDianliang.setVisibility(View.VISIBLE);
			holder.tvBlueState.setVisibility(View.GONE);
			holder.imgDianliang.setImageResource(getImg(deviceInfo.getDianliang()));
		}
	}

	private void setListener(final ViewHolder viewHolder, final int position,final View convertView) {
		// 删除
		viewHolder.tvDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnItemDeleteListener != null) {
					if (position != mItemDataList.size() - 1) {
						closeItem(viewHolder,position,convertView);
						mOnItemDeleteListener.onDelete(viewHolder, position);
					}
				}
			}
		});
		// 连接
		viewHolder.tvBlueState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnItemConnectListener != null) {
					if (position != mItemDataList.size() - 1) {
						mOnItemConnectListener.onConnect(viewHolder, position);
					}
				}
			}
		});
		// 关机
		viewHolder.tvShutdown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ((SideView) itemView).close();
			
				if (mOnItemShutDownListener != null) {
					if (position != mItemDataList.size() - 1) {
						closeItem(viewHolder, position, convertView);
						mOnItemShutDownListener.onShutDown(viewHolder, position, mItemDataList.get(position).getAddress());
					}
				}
			}
		});
		// 编辑
		viewHolder.tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnItemEditNameListener != null) {
					if (position != mItemDataList.size() - 1) {
						mOnItemEditNameListener.onEditName(viewHolder, position);
					}
				}
			}
		});
		// 添加
		viewHolder.tvAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnAddListener != null) {
//					if (position == mItemDataList.size() - 1) {
						mOnAddListener.OnAdd();
//					}
				}
			}
		});

	}
	
	public void setList(List<DeviceBindedInfo> deviceJsons){
		this.mItemDataList = deviceJsons;
	}

	// /**
	// * 点击删除回调接口
	// */
	public interface OnItemDeleteListener {
		public void onDelete(ViewHolder ViewHolder, int position);
	}

	private OnItemDeleteListener mOnItemDeleteListener;

	public void setOnItemDeleteListener(OnItemDeleteListener l) {
		this.mOnItemDeleteListener = l;
	}

	//
	// /**
	// * 连接回调接口
	// */
	public interface OnItemConnectListener {
		public void onConnect(ViewHolder viewHolder, int position);
	}

	private OnItemConnectListener mOnItemConnectListener;

	public void setOnItemConnectListener(OnItemConnectListener l) {
		this.mOnItemConnectListener = l;
	}

	//
	// /**
	// * 关机回调接口
	// */
	public interface OnItemShutDownListener {
		public void onShutDown(ViewHolder viewHolder, int position, String address);
	}

	private OnItemShutDownListener mOnItemShutDownListener;

	public void setOnItemShutDownListener(OnItemShutDownListener l) {
		this.mOnItemShutDownListener = l;
	}

	//
	// /**
	// * 编辑名称
	// */
	public interface OnItemEditNameListener {
		public void onEditName(ViewHolder viewHolder, int position);
	}

	private OnItemEditNameListener mOnItemEditNameListener;

	public void setOnItemEditNameListener(OnItemEditNameListener l) {
		this.mOnItemEditNameListener = l;
	}
	
	public interface OnAddListener {
		public void OnAdd();
	}

	private OnAddListener mOnAddListener;

	public void setOnAddListener(OnAddListener l) {
		this.mOnAddListener = l;
	}

	public class ViewHolder {
		int position; // 位置
		TextView tvName; // 蓝牙名称
		TextView tvAddress;// 蓝牙地址
		TextView tvShutdown;// 关机
		TextView tvBlueState;// 连接
		TextView tvDelete;// 删除
		ImageView imgDianliang;// 电量
		ImageView imgEdit;// 编辑蓝牙名称
		LinearLayout llItem;
		TextView tvAdd;
		boolean isOpen;
		
	}
}

