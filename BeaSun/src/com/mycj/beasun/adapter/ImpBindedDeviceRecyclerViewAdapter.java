package com.mycj.beasun.adapter;

import java.util.List;

import com.mycj.beasun.R;
import com.mycj.beasun.bean.DeviceBindedInfo;
import com.mycj.beasun.view.SideView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImpBindedDeviceRecyclerViewAdapter extends RecyclerViewBaseAdapter<DeviceBindedInfo> {

	@Override
	public void showData(RecyclerViewBaseHolder viewHolder, int i, List<DeviceBindedInfo> mItemDataList) {
		DeviceBindedViewHolder holder = (DeviceBindedViewHolder) viewHolder;
		if (i == (mItemDataList.size() - 1)) {
			//添加更多
			holder.llItem.setVisibility(View.GONE);
			holder.tvAdd.setVisibility(View.VISIBLE);
		} else {
			DeviceBindedInfo deviceInfo = mItemDataList.get(i);
			holder.llItem.setVisibility(View.VISIBLE);
			holder.tvAdd.setVisibility(View.GONE);
			holder.tvName.setText(deviceInfo.getName());
			holder.tvAddress.setText(deviceInfo.getAddress());
			setBlueState( holder, deviceInfo);
			
		}
		holder.position = i;
		

	}

	private Context context;
	@Override
	public View createView(ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		// View view =
		// LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device_binded,
		// viewGroup, false);
		// if (i==getItemCount()+1) {
		// return new TextView(viewGroup.getContext());
		// }else{
//		if (i == (mItemDataList.size() - 1)) {
//			View btnView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_device_btn, viewGroup, false);
//			return btnView;
//		} else {
			SideView view = new SideView(viewGroup.getContext());
			return view;
//		}

		// }
	}
	
	
	@Override
	public RecyclerViewBaseHolder createViewHolder(View view) {
		return new DeviceBindedViewHolder(view);
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
		}else {
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
	public void setBlueState( DeviceBindedViewHolder holder, DeviceBindedInfo deviceInfo) {
		if (deviceInfo.getBlueState() == -1) {// 未连接时，隐藏电量，显示连接
			holder.tvBlueState.setText(context.getString(R.string.connect));
			holder.tvBlueState.setVisibility(View.VISIBLE);
			holder.imgDianliang.setVisibility(View.GONE);
			
		} else { // 连接好时，隐藏连接，显示电量信息
			holder.imgDianliang.setVisibility(View.VISIBLE);
			holder.tvBlueState.setVisibility(View.GONE);
			holder.imgDianliang.setImageResource(getImg(deviceInfo.getDianliang()));
		}
	}

//	public class AddMoreViewHolder extends RecyclerViewBaseHolder {
//		private TextView tvAdd;
//
//		public AddMoreViewHolder(View itemView) {
//			super(itemView);
//			tvAdd = (TextView) itemView.findViewById(R.id.tv_add);
//			tvAdd.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (mOnAddMoreClickListener!=null) {
//						mOnAddMoreClickListener.onClick(v);
//					}
//				}
//			});
//		}
//	}

	public interface OnAddMoreClickListener {
		public void onClick(View v);
	}

	private OnAddMoreClickListener mOnAddMoreClickListener;

	public void setOnAddMoreClickListener(OnAddMoreClickListener l) {
		this.mOnAddMoreClickListener = l;
	}

	/**
	 * 绑定的设备item展示视图
	 * 
	 * @author Administrator
	 *
	 */
	public class DeviceBindedViewHolder extends RecyclerViewBaseHolder {
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

		public DeviceBindedViewHolder(View itemView) {
			super(itemView);
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
			tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
			tvBlueState = (TextView) itemView.findViewById(R.id.tv_connect);
			imgDianliang = (ImageView) itemView.findViewById(R.id.img_dianliang);
			imgEdit = (ImageView) itemView.findViewById(R.id.img_edit_name);
			tvShutdown = (TextView) itemView.findViewById(R.id.tv_shut_down);
			tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
			llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
			tvAdd = (TextView) itemView.findViewById(R.id.tv_add);
			setListener(itemView);
		}

		private void setListener(final View itemView) {
			// 删除
			tvDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mOnItemDeleteListener != null) {
						if (position!=mItemDataList.size()-1) {
							
						mOnItemDeleteListener.onDelete(itemView, position);
						// 删除后item关闭isOpen=false
						((SideView) itemView).close();
						}
					}
				}
			});
			// 连接
			tvBlueState.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// ObjectAnimator animation =
					// ObjectAnimator.ofFloat(tvBlueState, "alpha", 1f,0.2f,1f);
					// animation.setDuration(500);
					// animation.start();
					if (mOnItemConnectListener != null) {
						if (position!=mItemDataList.size()-1) {
						mOnItemConnectListener.onConnect(itemView, position);
						}
					}
				}
			});
			// 关机
			tvShutdown.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((SideView) itemView).close();
				
					if (mOnItemShutDownListener != null) {
						if (position!=mItemDataList.size()-1) {
							mOnItemShutDownListener.onShutDown(itemView,position,	mItemDataList.get(position).getAddress());
						}
					}
				}
			});
			// 编辑
			tvName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mOnItemEditNameListener != null) {
						if (position!=mItemDataList.size()-1) {
						mOnItemEditNameListener.onEditName(itemView,position);
						}
					}
				}
			});
			//添加
			tvAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mOnAddMoreClickListener!=null) {
						if (position==mItemDataList.size()-1) {
						mOnAddMoreClickListener.onClick(v);
						}
					}
				}
			});

		}
	}

	/**
	 * 点击删除回调接口
	 */
	public interface OnItemDeleteListener {
		public void onDelete(View v, int position);
	}

	private OnItemDeleteListener mOnItemDeleteListener;

	public void setOnItemDeleteListener(OnItemDeleteListener l) {
		this.mOnItemDeleteListener = l;
	}

	/**
	 * 连接回调接口
	 */
	public interface OnItemConnectListener {
		public void onConnect(View v, int position);
	}

	private OnItemConnectListener mOnItemConnectListener;

	public void setOnItemConnectListener(OnItemConnectListener l) {
		this.mOnItemConnectListener = l;
	}

	/**
	 * 关机回调接口
	 */
	public interface OnItemShutDownListener {
		public void onShutDown(View v,int position,String address);
	}

	private OnItemShutDownListener mOnItemShutDownListener;

	public void setOnItemShutDownListener(OnItemShutDownListener l) {
		this.mOnItemShutDownListener = l;
	}

	/**
	 * 编辑名称
	 */
	public interface OnItemEditNameListener {
		public void onEditName(View v,int position);
	}

	private OnItemEditNameListener mOnItemEditNameListener;

	public void setOnItemEditNameListener(OnItemEditNameListener l) {
		this.mOnItemEditNameListener = l;
	}

}
