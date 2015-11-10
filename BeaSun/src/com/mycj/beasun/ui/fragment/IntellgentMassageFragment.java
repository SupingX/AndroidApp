package com.mycj.beasun.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.R;
import com.mycj.beasun.adapter.ImpIntellgentModelRecyclerViewAdapter;
import com.mycj.beasun.adapter.ImpIntellgentModelRecyclerViewAdapter.OnItemClickListener;
import com.mycj.beasun.adapter.ImpIntellgentModelRecyclerViewAdapter.OnItemLongClickListener;
import com.mycj.beasun.bean.MassageInfo;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.MassageJson;
import com.mycj.beasun.service.util.FileUtil;
import com.mycj.beasun.service.util.SharedPreferenceUtil;
import com.mycj.beasun.ui.activity.CustomMassageModelActivity;
import com.mycj.beasun.ui.activity.MassageControlActivity;
import com.mycj.beasun.view.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class IntellgentMassageFragment extends Fragment {
	public static final String ARGUMENT = "argument";
	private List<MassageInfo> massageInfos = new ArrayList<MassageInfo>();
	private RecyclerView intellgentRecylerView;
	private ImpIntellgentModelRecyclerViewAdapter adapter;
	private AlertDialog deleteDialog;
	private final int MAX_MODEL = 20;

	/**
	 * 传入需要的参数，设置给arguments
	 * 
	 * @param argument
	 * @return
	 */
	public static IntellgentMassageFragment newInstance(String argument) {
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);
		IntellgentMassageFragment otherFragment = new IntellgentMassageFragment();
		// setArguments方法必须在fragment创建以后，添加给Activity前完成
		otherFragment.setArguments(bundle);
		return otherFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_intelligent_massage, container, false);
		intellgentRecylerView = (RecyclerView) view.findViewById(R.id.recycler_intelligent_massage);
		intellgentRecylerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
		// intellgentRecylerView.addItemDecoration(new
		// DividerGridItemDecoration(getActivity()));
		adapter = new ImpIntellgentModelRecyclerViewAdapter();
		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position, View view) {
				//Log.i("", " fragment position : " + position);
				MassageInfo info = massageInfos.get(position);
				// 一、自定义按钮
				if (position == massageInfos.size() - 1) {
					if (massageInfos.size()>MAX_MODEL) {
						Toast.makeText(getActivity(), getString(R.string.intelligent_not_add_more), Toast.LENGTH_SHORT).show();
						return ;
					}
					startActivity(new Intent(getActivity(), CustomMassageModelActivity.class));
				// 二、经典模式
				} 
//				else if (position == 0) {
//					startActivity(new Intent(getActivity(), ClassicMassageControlActivity.class));
//				}
				// 三、 自定义模式（混合）
				else if (position > 10&&position<=massageInfos.size()-1) {
					Intent intent = new Intent(getActivity(), MassageControlActivity.class);
					Bundle b = new Bundle();
					//Log.e("", info.toString());
					b.putParcelable("massage", info);
					intent.putExtras(b);
					startActivity(intent);
				//四、 系统常规模式 
				}else {
					int model_1 = 0;
					int model_2 = 0;
					long music = -1l;
					//Log.e("", "info.getIndex() :" + info.getIndex());
					//获取模式和音乐（因为单一模式和混合模式存贮的音乐方式不一样）
					switch (info.getIndex()) {
					case 0:
						//经典模式
						model_1 = 0b10;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_0), -1l);
						break;
					case 1:
						//小憩as 推拿
						model_1 = 0b10;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_1), -1l);
						break;
					case 2:
						//宅印象as 刮痧
						model_1 = 0b1000;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_2), -1l);
						break;
					case 3:
						//空山竹语as 瘦身
						model_1 = 0b0;
						model_2 = 0b01;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_3), -1l);
						break;
					case 4:
						//办公室as 轻抚
						model_1 = 0b0;
						model_2 = 0b10;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_4), -1l);
						break;
					case 5:
						//旅行as 针灸
						model_1 = 0b10000;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_5), -1l);
						break;
					case 6:
						//听潮 as 锤击
						model_1 = 0b100;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_6), -1l);
						break;
					case 7:
						//悦风聆as 指压
						model_1 = 0b100000;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_7), -1l);
						break;
					case 8:
						//静心as 颈椎
						model_1 = 0b0;
						model_2 = 0b100;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_8), -1l);
						break;
					case 9:
						//在路上as 火罐
						model_1 = 0b1000000;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_9), -1l);
						break;
					case 10:
						//静夜as 揉捏
						model_1 = 0b1;
						model_2 = 0b0;
						music = (long) SharedPreferenceUtil.get(getActivity(), (BeaStaticValue.MUSIC_INTELGENT_10), -1l);
						break;

					default:
						break;
					}
					
					Intent intent = new Intent(getActivity(), MassageControlActivity.class);
					Bundle b = new Bundle();
					info.setModel_1(model_1);
					info.setModel_2(model_2);
					info.setMusic(music);
					//Log.e("", info.toString());
					b.putParcelable("massage", info);
					intent.putExtras(b);
					startActivity(intent);
					
				}
			}
		});
		adapter.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public void onItemLongClick(int position, View view) {
				if (position > 10 && position != massageInfos.size() - 1) {
					showDialogForDelete(position);
				}
			}

		});
		intellgentRecylerView.setAdapter(adapter);

		loadDatas();

		adapter.append(massageInfos);
		return view;
	}
	
	
	private void showDialogForDelete(final int position) {
		deleteDialog = new AlertDialog(getActivity()).builder()
				.setMsg(getString(R.string.intelligent_positive_to_delete))
				.setPositiveButton(getString(R.string.positive), new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
				deleteCustomMassageModel(position);
			}
		}).setNegativeButton(getString(R.string.canel), new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		deleteDialog.show();
	}

	/**
	 * 删除自定义按摩模式
	 * 
	 * @param position
	 */
	private void deleteCustomMassageModel(int position) {
		// 更新json
		List<MassageInfo> massageInfosFromJson = MassageJson.loadJson(getActivity());
		if (massageInfosFromJson==null || massageInfosFromJson.size()==0) {
			return ;
		}
		massageInfos.remove(position);
		adapter.remove(position);
		FileUtil.deleteBitmapData(massageInfosFromJson.get(position-11).getImgCustom());
		massageInfosFromJson.remove(position - 11);// 例如第12项 pos=11 减去11就等于0 即
													// json第一个
		// 重新保存
		String json = MassageJson.listToJson(massageInfosFromJson);
		FileUtil.writeFileData(BeaStaticValue.JSON_MASSAGE, json, getActivity());
		
		
	}

	/**
	 * 加载模式数据
	 */
	private void loadDatas() {
		massageInfos.clear();
		// 1.加载默认的模式
		MassageInfo item0 = new MassageInfo(R.drawable.ic_beasun_cell0, getString(R.string.intelligent_model_jingdian),0);
		MassageInfo item1 = new MassageInfo(R.drawable.ic_beasun_cell1, getString(R.string.intelligent_model_xiaoqi),1);
		MassageInfo item2 = new MassageInfo(R.drawable.ic_beasun_cell2, getString(R.string.intelligent_model_zhaiyinxiang),2);
		MassageInfo item3 = new MassageInfo(R.drawable.ic_beasun_cell3, getString(R.string.intelligent_model_kongshanzhuyu),3);
		MassageInfo item4 = new MassageInfo(R.drawable.ic_beasun_cell4, getString(R.string.intelligent_model_bangongshi),4);
		MassageInfo item5 = new MassageInfo(R.drawable.ic_beasun_cell5, getString(R.string.intelligent_model_lvxing),5);
		MassageInfo item6 = new MassageInfo(R.drawable.ic_beasun_cell6, getString(R.string.intelligent_model_tingchao),6);
		MassageInfo item7 = new MassageInfo(R.drawable.ic_beasun_cell7, getString(R.string.intelligent_model_yuefengling),7);
		MassageInfo item8 = new MassageInfo(R.drawable.ic_beasun_cell8, getString(R.string.intelligent_model_jingxin),8);
		MassageInfo item9 = new MassageInfo(R.drawable.ic_beasun_cell9, getString(R.string.intelligent_model_zailushang),9);
		MassageInfo item10 = new MassageInfo(R.drawable.ic_beasun_cell10, getString(R.string.intelligent_model_jingye),10);
		MassageInfo itemEnd = new MassageInfo(R.drawable.ic_beasun_cell11, "",0xFF);
		massageInfos.add(item0);
		massageInfos.add(item1);
		massageInfos.add(item2);
		massageInfos.add(item3);
		massageInfos.add(item4);
		massageInfos.add(item5);
		massageInfos.add(item6);
		massageInfos.add(item7);
		massageInfos.add(item8);
		massageInfos.add(item9);
		massageInfos.add(item10);

		// 2.加载自定义模式
		List<MassageInfo> massageInfosFromJson = MassageJson.loadJson(getActivity());
		if (massageInfosFromJson != null && massageInfosFromJson.size() > 0) {

			for (int i = 0; i < massageInfosFromJson.size(); i++) {
				MassageInfo massageInfo = massageInfosFromJson.get(i);
				massageInfo.setIndex(11+i);
				massageInfo.setIsPix(1);
				massageInfos.add(massageInfo);
			}
		}

		// 3.加载添加更多
		massageInfos.add(itemEnd);

	}

	@Override
	public void onResume() {
		super.onResume();
		loadDatas();
		adapter.replace(massageInfos);
	}

	@Override
	public void onDestroy() {
		massageInfos.clear();
		if (deleteDialog != null) {
			deleteDialog.dismiss();
		}
		super.onDestroy();
	}
	
	
}
