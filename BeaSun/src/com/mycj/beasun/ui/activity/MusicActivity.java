package com.mycj.beasun.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.adapter.MusicAdapter;
import com.mycj.beasun.adapter.MusicAdapter.OnItemClickListener;
import com.mycj.beasun.bean.Music;
import com.mycj.beasun.business.MusicLoader;
import com.mycj.beasun.service.MusicService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 音乐列表
 * @author Administrator
 *
 */
public class MusicActivity extends BaseActivity {
	private MusicService musicService;
	private List<Music> musicList;
	private RecyclerView massageRecyclerView;
	
	int fromId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		load();
		initViews();
	}
	private void load(){
		Bundle b = getIntent().getExtras();
		if (b!=null) {
			musicId = b.getLong("musicId");
		}
	}
	private void initViews() {
		massageRecyclerView = (RecyclerView) findViewById(R.id.recycler_music);
		massageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		TextView tvBack = (TextView) findViewById(R.id.tv_back);
		TextView tvSave = (TextView) findViewById(R.id.tv_save);
		tvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					musicService.stop();
					if (musicList.size()>0) {
						Music music = musicList.get(selectPosition);
						sendResultToCustomActivity(music);
					}else{
						finish();
					}
				
			}
		});
		
	}
	
	private int selectPosition;
	private MusicAdapter adapter;
	private long musicId=-1L;
	@Override
	protected void onStart() {
		super.onStart();
		musicService = getMusicService();
		musicList = musicService.getMusicList();
		if (musicList==null || musicList.size()<=0) {
			return ;
		}
		adapter = new MusicAdapter(musicList, this);
		if (musicId!=-1) {
			for (int i = 0; i < musicList.size(); i++) {
				if (musicId ==musicList.get(i).getId()) {
					adapter.setSelected(i);
				}
			}
		}
		adapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(int position,View v) {
				selectPosition = position;
				adapter.setSelected(position);
				Music music = musicList.get(selectPosition);
				//Log.i("", "music.getId()	:" +music.getId());
				musicService.play(MusicLoader.getMusicUriById(music.getId()));
			}
		});
		massageRecyclerView.setAdapter(adapter);
	}
	
	/**
	 * 
	 * 选择的音乐地址URI/名称返回
	 * @param music
	 */
	private void sendResultToCustomActivity(Music music){
		Intent intent = new Intent();
		intent.putExtra("musicId", music.getId());
		intent.putExtra("musicTitle", music.getTitle());
		setResult(RESULT_OK,intent);
		finish();
	}
}
