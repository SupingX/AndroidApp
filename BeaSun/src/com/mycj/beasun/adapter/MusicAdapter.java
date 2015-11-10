package com.mycj.beasun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.R;
import com.mycj.beasun.adapter.DeviceScanAdapter.OnSelectedItemListener;
import com.mycj.beasun.bean.MassageItem;
import com.mycj.beasun.bean.Music;
import com.mycj.beasun.business.MassageUtil;
import com.mycj.beasun.business.MusicLoader;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{
	private List<Music> musics = new ArrayList<Music>();
	private Context mContext;
	
	public MusicAdapter(List<Music> musics, Context context) {
		this.musics = musics;
		this.mContext = context;
	}

	@Override
	public int getItemCount() {
		return musics.size();
	}
	
	
	private int selected =-1;
	public void setSelected(int position){
		this.selected = position;
		notifyDataSetChanged();
	}
	
	@Override
	public void onBindViewHolder(MusicViewHolder holder, int pos) {
		holder.pos  = pos;
		Music music = musics.get(pos);
		holder.tvName.setText(music.getTitle());
		holder.tvSinger.setText(music.getArtist());
		Bitmap art = MusicLoader.getArt(mContext, music.getId(), music.getAlbumId(), true);
		if (art==null) {
			holder.imgPic.setImageResource(R.drawable.ic_acquiesce_img);
		}else{
			holder.imgPic.setImageBitmap(art);
		}
		
		if( pos == selected){
            // 按压背景
			holder.setbackground(Color.parseColor("#22ffffff"));
	      }else{
            // 正常背景
				holder.setbackground(Color.parseColor("#ffffff"));
	      }
	}


	@Override
	public MusicViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_music, parent,false);
		return new MusicViewHolder(view);
	}
	
	
	public interface OnItemClickListener {
		public void onItemClick(int position,View v);
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		this.mOnItemClickListener = l;
	}
	public class MusicViewHolder extends ViewHolder{
		private TextView tvName;
		private TextView tvSinger;
		private ImageView imgPic;
		private int pos;
		private View view;
		
		public MusicViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			itemView.setBackgroundColor(Color.WHITE);
			tvName = (TextView) itemView.findViewById(R.id.tv_music_name);
			tvSinger = (TextView) itemView.findViewById(R.id.tv_music_singer);
			imgPic = (ImageView) itemView.findViewById(R.id.img_music);
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mOnItemClickListener != null) {
						mOnItemClickListener.onItemClick(pos,v);
					}
				}
			});
		}
		
		public void setbackground(int color){
			view.setBackgroundColor(color);
		}
		
	}

}
