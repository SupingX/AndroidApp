package com.mycj.beasun.service.util;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

/**
 * 音效播放  工具类
 * 创建时间: 2014-3-29 下午6:56:19
 *
 * @author 许仕永
 * 项目名称: Audio
 * 文件名: Audio.java
 * 编码: 
 * @Description：
 * @JKD JDK 1.6.0_21 
 * @version v1.0
 * @TODO
 */
public class SoundPlay
{
	private HashMap<String, Integer> data;
	private static SoundPlay sound;
	private Context context;
	private List<String> soundName;
	private SoundPool soundPool;
	private boolean isLoadOver = false; 
			
	/**使用单实例模式操作**/
	private SoundPlay(Context context)
	{
		init(context);
	}
	
	/**完成类数据的初始化工作**/
	private void init(Context context)
	{
		this.context = context;
		this.data = new HashMap<String, Integer>();
		this.soundName = new ArrayList<String>();
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
	}

	/**向存放所有音频文件的HashMap里添加数据，并加载**/
	public void put(String name,Integer file)  throws IndexOutOfBoundsException,FileNotFoundException
	{
		isLoadOver = false;
		data.put(name, soundPool.load(context,file, 100));
		soundName.add(name);	//将名字添加到音频文件名的List
		
		//添加加载监听
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				isLoadOver = true;
			}
		});
	}
	
	/**返回一个静态Sound实例并使用传入的data来初始化数据**/
	public static SoundPlay getInstance(Context context)
	{
		if(sound == null)
			sound = new SoundPlay(context);
		return sound;
	}

	/**
	 * 传入在HashMap里添加的音频文件名字来启动播放
	 * @param soundName  名字
	 * @return 播放成功返回true饭后返回false
	 */
	public boolean play(String soundName)  throws IndexOutOfBoundsException,FileNotFoundException
	{
		if(isLoadOver)
		{
			soundPool.play(data.get(soundName), 1f, 1f, 0, -1, 1);
			return true;
		}
		System.out.println("尚未加载完毕");
		return false;
	}
	
	/**
	 * 传入索引值来启动播放
	 * @param index 索引号
	 * @return 播放成功与否的返回标识
	 */
	public boolean play(int index) throws IndexOutOfBoundsException,FileNotFoundException
	{
		if(isLoadOver)
		{
			soundPool.play(data.get(soundName.get(index)), 1f, 1f, 0, 0, 1);
			return true;
		}
		System.out.println("尚未加载完毕");
		return false;
	}
	
	public boolean stop(String soundName) throws IndexOutOfBoundsException,FileNotFoundException
	{
		if(isLoadOver)
		{
			soundPool.stop(data.get(soundName));
			return true;
		}
		System.out.println("尚未加载完毕");
		return false;
	}
	
	/**销毁  包含本类静态实例，音频HashMap,音频名字的List**/
	public void destroy()
	{
		if(soundPool != null)
			soundPool.release();
		if(!data.isEmpty())
			data.clear();
		if(!soundName.isEmpty())
			soundName.clear();
	}

	/**清空存放音频数据的HashMap    成功返回true**/
	public boolean cleanSoundMap()
	{
		if(!data.isEmpty())
		{
			data.clear();
			return true;
		}
		return false;
	}
	
	/**情况存放音频文件名字的List对象   成功返回true**/
	public boolean cleanSoundName()
	{
		if(!soundName.isEmpty())
		{
			soundName.clear();
			return true;
		}
		return false;
	}
}


//
//不要重复地去发明轮子
//
//
//使用方法示范：
//
//package cc.icoc.javaxu.audio;
//
//import android.os.Bundle;
//import android.app.Activity;
//import android.util.Log;
//import android.view.Menu;
//
//public class MainActivity extends Activity
//{
//	private Sound sound;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		sound = Sound.getInstance(this);
//		//放入数据  音效文件名，音效文件  其中R.raw.button为你的项目下raw(需自建)文件夹中的音频文件 一般格式为OGG，可以使用其他音频转换工具进行格式转换
//		sound.put("按钮点击", R.raw.button);
//		
//		//播放  以索引值作为播放条件
//		sound.play(1);
//		
//		//播放 以文件名作为播放条件
//		sound.play("按钮点击");
//		
//		//获取存入的数据
//		sound.getSoundHashMap();
//		
//		//不再使用时调用销毁
//		sound.destroy();
//		
//	}
//
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//}
