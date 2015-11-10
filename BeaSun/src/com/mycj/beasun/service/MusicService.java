package com.mycj.beasun.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.bean.Music;
import com.mycj.beasun.business.MusicLoader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

public class MusicService extends Service implements OnPreparedListener, OnErrorListener, OnAudioFocusChangeListener, OnCompletionListener {
	private final static String TAG = "MusicService";
	public final static String MUSIC_COMPLETION = "com.example.action.completion";
	public static final String MUSIC_PLAYING = "music_playing";
	public static final String MUSIC_STOP = "music_stop";
	public static final String MUSIC_PAUSE = "music_pause";
	public final static String MUSIC_EXTRA_PLAY_POSITION = "music_extra_playing_position";
	private int playingPosition = -1;
	private final MyBinder myBinder = new MyBinder();

	private MediaPlayer mediaPlayer;

	/**
	 * 提示音
	 */
	private boolean isRemind;
	public void setIsRemind(boolean isRemind){
		this.isRemind = isRemind;
	}
	public boolean isRemind(){
		return isRemind;
	}
	
	private AudioManager am;

	private List<Music> musicList;
	/** 保存当前音乐播放点 */
	private int currentPosition;

	public int getCurrentPostion() {
		return this.currentPosition;
	}

	/**
	 * 定义查找音乐信息数组，1.标题 2.音乐时间 3.艺术家 4.音乐id 5.显示名字 6.数据
	 */
	private String[] musicInfo = new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID

	};
	private int[] ids;// 保存音乐ID临时数组
	private String[] artists;
	private String[] titles;
	private Cursor cursor;
	private int currentPostion = 0;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "音乐服务    onBind()");
		return myBinder;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "音乐服务    onCreate()");
		// am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// int result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
		// AudioManager.AUDIOFOCUS_GAIN);
		// if(result!=AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
		// // could not get audio focus.
		// }
		musicList = initMusic();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "音乐服务    onStartCommand()");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "音乐服务        onDestroy()");
		if (mediaPlayer!=null) {
			stop();
			release();
		}
		super.onDestroy();
	}

	/**
	 * 播放资源音乐
	 */
	public void play(int resource) {
		release();
		initMediaPlayer(resource);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);
		try {
			mediaPlayer.prepare();
			blePlaying();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 播放资源音乐
	 */
	public void play(int resource,boolean isLoop) {
		release();
		initMediaPlayer(resource);
		mediaPlayer.setLooping(isLoop);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);
		try {
			mediaPlayer.prepare();
			blePlaying();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 播放本地音乐 ， 通过地址
	 * 
	 * @param filename
	 */
	public void play(String filename) {
		release();
		initMediaPlayerFromSdCard(filename);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);

		try {
			mediaPlayer.prepare();
			blePlaying();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 播放当前播放音乐- -
	 * 
	 * @param pso
	 */
	public void playByCurrentPlayingPosition() {
		if (musicList != null && musicList.size() > 0) {
			play(MusicLoader.getMusicUriById(musicList.get(playingPosition).getId()));
			blePlaying();
		}

	}

	/**
	 * 根据列表编号播放音乐
	 * 
	 * @param pos
	 */
	public void playByCurrentPlayingPosition(int pos) {
		if (musicList != null && musicList.size() > 0) {
			if (pos == -1) {
				pos = 0;
			}
			play(MusicLoader.getMusicUriById(musicList.get(pos).getId()));
			blePlaying();
		}

	}
	

	// /**
	// * 播放本地音乐 通过uri
	// * @param uri
	// */
	// public void play(Uri uri) {
	// release();
	// initMediaPlayerFromContentResolver(uri);
	// mediaPlayer.setOnPreparedListener(this);
	// try {
	// mediaPlayer.prepare();
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 播放本地音乐 通过uri
	 * 
	 * @param uri
	 */
	public void play(Uri uri) {
		release();
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(this, uri);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		try {
			mediaPlayer.prepare();
			blePlaying();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重新播放
	 */
	public void replay() {
		if (mediaPlayer != null) {
			currentPosition = 0;
			mediaPlayer.seekTo(0);
			play();
		}
	}

	/**
	 * 网络下载播放？
	 */
	public void playAsyn() {
		// mediaPlayer.prepareAsync();
	}

	/**
	 * 
	 */
	public void setWakeMode() {
		if (mediaPlayer != null) {
			mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		}
	}

	// public void setWifiWake() {
	// WifiLock wifiLock = ((WifiManager)
	// getSystemService(Context.WIFI_SERVICE)).createWifiLock(
	// WifiManager.WIFI_MODE_FULL, "mylock");
	//
	// wifiLock.acquire();

	// wifiLock.release();
	// }

	/**
	 * 停止播放
	 */
	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			bleStop();
		}
	}

	/**
	 * 释放
	 */
	public void release() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
			bleStop();
		}
	}

	/**
	 * 获取当前进度并保存
	 */
	public void setCurrentPosition() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			currentPosition = mediaPlayer.getCurrentPosition();
		}
	}

	/**
	 * 设置当前进度
	 * 
	 * @param pos
	 */
	public void setCurrentPosition(int pos) {
		this.currentPosition = pos;
	}

	/**
	 * 开始播放
	 */
	public void play() {
		if (mediaPlayer != null) {
			mediaPlayer.start();
			blePlaying();
		}
	}

	/**
	 * 暂停播放
	 */
	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			setCurrentPosition();
			// this.currentPosition = mediaPlayer.getDuration();
			Log.e("", "currentPostion : " + currentPostion);
			mediaPlayer.pause();

			blePause();
		}
	}

	public static IntentFilter getIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MUSIC_PLAYING);
		intentFilter.addAction(MUSIC_STOP);
		intentFilter.addAction(MUSIC_PAUSE);
		intentFilter.addAction(MUSIC_COMPLETION);
		return intentFilter;
	}

	protected void blePlaying() {
		Intent intent = new Intent(MUSIC_PLAYING);
		sendBroadcast(intent);
	}

	protected void blePause() {
		Intent intent = new Intent(MUSIC_STOP);
		sendBroadcast(intent);
	}

	protected void bleStop() {
		Intent intent = new Intent(MUSIC_PAUSE);
		sendBroadcast(intent);
	}

	protected void blePlayCompletetion() {
		Intent intent = new Intent(MUSIC_COMPLETION);
		intent.putExtra(MUSIC_EXTRA_PLAY_POSITION, playingPosition);
		sendBroadcast(intent);
	}

	public boolean isPlaying() {
		if (mediaPlayer != null) {
			return mediaPlayer.isPlaying();
		}
		return false;
	}

	/**
	 * 继续上次
	 */
	public void seekto() {
		// Log.e("MusicService", "mediaPlayer ：" + mediaPlayer );
		// Log.e("MusicService", "继续播放");
		// mediaPlayer.seekTo(currentPostion);
		// //
		// play(MusicLoader.getMusicUriById(musicList.get(playingPosition).getId()));
		// // stop();
		// play();
		// currentPosition = 0;
		if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(currentPosition);
			play();
			currentPosition = 0;
		}

	}

//	/**
//	 * 发送通知
//	 */
//	public void notification() {
//		String song = "xianjian";
//		PendingIntent pend = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MusicActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
//		Notification notifi = new Notification();
//		notifi.tickerText = "";
//		notifi.icon = R.drawable.ic_more_music;
//		notifi.flags |= Notification.FLAG_ONGOING_EVENT;
//		notifi.setLatestEventInfo(getApplicationContext(), "仙剑奇侠传", song, pend);
//		startForeground(1, notifi);
//
//	}

	/**
	 * 设置音乐集合
	 * 
	 * @param list
	 */
	public void setList(List<Music> list) {
		this.musicList = list;
	}

	/**
	 * 上一曲
	 */
	public void playUp() {
		if (musicList != null && musicList.size() > 0) {
			playingPosition--;
			if (playingPosition < 0) {
				playingPosition = musicList.size() - 1;
			}
			play(MusicLoader.getMusicUriById(musicList.get(playingPosition).getId()));
		}
	};

	/**
	 * 下一曲
	 */
	public void playDown() {
		if (musicList != null && musicList.size() > 0) {
			playingPosition++;
			if (playingPosition == musicList.size()) {
				playingPosition = 0;
			}
			play(MusicLoader.getMusicUriById(musicList.get(playingPosition).getId()));
		}
	};

	/**
	 * 当前播放歌曲
	 * 
	 * @param pos
	 */
	public void setPlayingPosition(int pos) {
		this.playingPosition = pos;
	}

	public int getPlayingPosition() {
		return playingPosition;
	}
	
	public void playResource(Context context,int resId){
		release();
		try {
			mediaPlayer = MediaPlayer.create(this, resId);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		try {
			mediaPlayer.prepare();
			blePlaying();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	private void initMediaPlayerFromContentResolver(Uri uri) {
		ContentResolver contentResolver = getContentResolver();
		// Uri uri =
		// android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		if (cursor == null) {
			// query failed, handle error
		} else if (!cursor.moveToFirst()) {
			// no media on the device
		} else {
			int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			do {
				long thisId = cursor.getLong(idColumn);
				String thisTitle = cursor.getString(titleColumn);
				// ...process entry...
				Long id = thisId;
				Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setOnCompletionListener(this);
				try {
					mediaPlayer.setDataSource(getApplicationContext(), contentUri);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (cursor.moveToNext());

		}
	}

	/**
	 * 
	 * @param filename
	 */
	private void initMediaPlayerFromSdCard(String filename) {
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(file.getAbsolutePath());
			mediaPlayer.setOnCompletionListener(this);
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 通过ResourceID 初始化MediaPlayer
	 * 
	 * @param resource
	 */
	private void initMediaPlayer(int resource) {
		mediaPlayer = MediaPlayer.create(getApplicationContext(), resource);
	}
	/**
	 * 播放模式
	 */
	private int mode=1;
	public void setPlayMode(int mode){
		this.mode =mode;
	}
	public static final int MODE_SHUXU=1;
	public static final int MODE_SUIJI=2;
	public static final int MODE_XUNHUAN=3;
	
	
	/** for OnCompletionListener **/
	@Override
	public void onCompletion(MediaPlayer mp) {
		// Log.e("", "______播放完毕，继续下一首_______(pos+1) :" + playingPosition+1);
		if (mode==MODE_SHUXU) {
			playDown();
		}else if (mode==MODE_XUNHUAN) {
			play();
		}
		blePlayCompletetion();
		
	}

	/** for OnPreparedListener **/
	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.i(TAG, "音乐服务        onPrepared()");
		mediaPlayer.start();
	}

	/** for OnErrorListener **/
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.i(TAG, "音乐服务        onError()");
		return false;
	}

	/** for OnAudioFocusChangeListener **/
	@Override
	public void onAudioFocusChange(int focusChange) {
		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			// resume playback
			if (mediaPlayer == null) {
				// initMediaPlayer(R.raw.xj);
			} else if (!mediaPlayer.isPlaying()) {
				mediaPlayer.start();
			}
			mediaPlayer.setVolume(1.0f, 1.0f);

			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			// Lost focus for a short time, but we have to stop
			// playback. We don't release the media player because playback
			// is likely to resume
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
			// Lost focus for an unbounded amount of time: stop playback and
			// release media player
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			// Lost focus for a short time, but it's ok to keep playing
			// at an attenuated level
			// if (mediaPlayer.isPlaying()) {
			// mediaPlayer.setVolume(0.1f, 0.1f);
			// }

			break;

		default:
			break;
		}
	}

	public class MyBinder extends Binder {
		public MusicService getMusicService() {
			return MusicService.this;
		}
	}

	public List<Music> initMusic() {
		MusicLoader.instance(getContentResolver());
		cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicInfo, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Music> listMusic;
		if (cursor != null) {
			cursor.moveToFirst();
			int size = cursor.getCount();
			ids = new int[size];
			artists = new String[size];
			titles = new String[size];
			for (int i = 0; i < size; i++) {
				ids[i] = cursor.getInt(3);
				artists[i] = cursor.getString(2);
				titles[i] = cursor.getString(0);
				cursor.moveToNext();
			}
			listMusic = MusicLoader.getMusicList();
			Log.d("TAG", "所有的音乐list : " + listMusic);
			return listMusic;
		}
		return null;
	}

	public List<Music> getMusicList() {
		return this.musicList;
	}
}
