package com.mycj.beasun.adapter;


import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class RecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<RecyclerViewBaseHolder> {

	protected ArrayList<T> mItemDataList = new ArrayList<T>();
	
	public ArrayList<T> getList() {
		return mItemDataList;
	}
	/**
	 * 动态增加一条数据
	 * 
	 * @param itemDataType
	 *            数据实体类对象
	 */
	public void append(T itemDataType) {
		if (itemDataType != null) {
			mItemDataList.add(itemDataType);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 动态增加一组数据集合
	 * 
	 * @param itemDataTypes
	 *            数据实体类集合
	 */
	public void append(List<T> itemDataTypes) {
		if (itemDataTypes.size() > 0) {
			for (T itemDataType : itemDataTypes) {
				mItemDataList.add(itemDataType);
			}
			notifyDataSetChanged();
		}
	}

	public void appendToList(List<T> list) {
		if (list == null) {
			return;
		}
		if (list.size() == 0) {
			return;
		}
		mItemDataList.addAll(list);
		notifyDataSetChanged();
	}

	/**
	 * 替换全部数据
	 * 
	 * @param itemDataTypes
	 *            数据实体类集合
	 */
	public void replace(List<T> itemDataTypes) {
		mItemDataList.clear();
		if (itemDataTypes.size() > 0) {
			mItemDataList.addAll(itemDataTypes);
			notifyDataSetChanged();
		}
	}

	/**
	 * 移除一条数据集合
	 * 
	 * @param position
	 */
	public void remove(int position) {
		mItemDataList.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * 移除所有数据
	 */
	public void removeAll() {
		mItemDataList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return mItemDataList.size();
	}

	@Override
	public void onBindViewHolder(RecyclerViewBaseHolder viewHolder, int i) {
		showData(viewHolder, i, mItemDataList);
	}
	
	@Override
	public RecyclerViewBaseHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = createView(viewGroup, i);
		RecyclerViewBaseHolder holder = createViewHolder(view);
		return holder;
	}

	/**
	 * 显示数据抽象函数
	 * 
	 * @param viewHolder
	 *            基类ViewHolder,需要向下转型为对应的ViewHolder（example:
	 *            MainRecyclerViewHolder
	 *            mainRecyclerViewHolder=(MainRecyclerViewHolder) viewHolder;）
	 * @param i
	 *            位置
	 * @param mItemDataList
	 *            数据集合
	 */
	public abstract void showData(RecyclerViewBaseHolder viewHolder, int i, List<T> mItemDataList);

	/**
	 * 加载item的view,直接返回加载的view即可
	 * 
	 * @param viewGroup
	 *            如果需要Context,可以viewGroup.getContext()获取
	 * @param i
	 * @return item 的 view
	 */
	public abstract View createView(ViewGroup viewGroup, int i);

	/**
	 * 加载一个ViewHolder,为RecyclerViewHolderBase子类,直接返回子类的对象即可
	 * 
	 * @param view
	 *            item 的view
	 * @return RecyclerViewHolderBase 基类ViewHolder
	 */
	public abstract RecyclerViewBaseHolder createViewHolder(View view);

}
