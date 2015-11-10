package com.mycj.beasun.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.mycj.beasun.BaseActivity;
import com.mycj.beasun.R;
import com.mycj.beasun.bean.MassageInfo;
import com.mycj.beasun.business.BeaStaticValue;
import com.mycj.beasun.business.MassageJson;
import com.mycj.beasun.business.MassageUtil;
import com.mycj.beasun.service.util.FileUtil;
import com.mycj.beasun.service.util.SharedPreferenceUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomMassageModelActivity extends BaseActivity implements OnClickListener {

	private TextView tvBack;
	private TextView tvSave;
	private ImageView imgFront;
	private EditText tvText;
	private RelativeLayout rlChooseModel;
	private RelativeLayout rlChooseMusic;
	private Handler mHandler = new Handler(){
		
	};
	/**
	 * 音乐的编号
	 */
	private long musicId=-1;
	/**
	 * 按摩模式1
	 */
	private int massageModel_1 = 0b0000_0000;
	/**
	 * 按摩模式2
	 */
	private int massageModel_2 = 0b0000_0000;

	private TextView tvMusicName;

	private TextView tvModelList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_massage_model);
		initViews();
	}

	@Override
	protected void onDestroy() {
		// 退出界面时，将保存的值归0
		SharedPreferenceUtil.put(this, BeaStaticValue.MASSAGE_MODEL_1, 0);
		SharedPreferenceUtil.put(this, BeaStaticValue.MASSAGE_MODEL_2, 0);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resulet, Intent intent) {
		switch (requestCode) {
		case REQUEST_MODEL:
			if (resulet == RESULT_OK) {
				Bundle b = intent.getExtras();
				//Log.i("", "来自按摩模式列表的数据 ：" + b);
				if (b != null) {
					massageModel_1 = b.getInt("model_1");
					massageModel_2 = b.getInt("model_2");
					//Log.i("", "来自按摩模式列表的数据 ：" + massageModel_1);
					//Log.i("", "来自按摩模式列表的数据 ：" + massageModel_2);
					updateMassageList();
				}
			}
			break;
		case REQUEST_MUSIC:
			if (resulet == RESULT_OK) {
				Bundle b = intent.getExtras();
				//Log.i("", "来自音乐列表的数据 ：" + b);
				if (b != null) {
					musicId = b.getLong("musicId");//
					String musicTitle = b.getString("musicTitle");
					//Log.i("", "来自音乐列表的数据 musicId	：" + musicId);
					//Log.i("", "来自音乐列表的数据 musicTitle	：" + musicTitle);
					if (musicTitle != null) {
						updateMusicName(musicTitle);
					}
				}
			}
			break;
		case REQUEST_IMG_CLIP:
			if (resulet == RESULT_OK) {
				datasClip = intent.getByteArrayExtra("clip");
				if (datasClip!=null) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(datasClip, 0, datasClip.length);
					imgFront.setImageBitmap(bitmap);
				}
			}
			break;
		case REQUEST_IMG:
			if (resulet ==RESULT_OK) {
//				datas = intent.getByteArrayExtra("clip");
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						Intent intentClip = new Intent(CustomMassageModelActivity.this,ClipImageActivity.class);
//						intentClip.putExtra("name	", "suping");
//						intentClip.putExtra("clip", datas);
//						startActivity(intentClip);//进入裁剪页面
//					}
//				}, 500);
			
				
//				if (datas!=null) {
//					Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
//					imgFront.setImageBitmap(bitmap);
//			
//				}
				
//				Uri uri = intent.getData();
//				ContentResolver resolver = getContentResolver();
//				try {
//					Bitmap map = MediaStore.Images.Media.getBitmap(resolver, uri);
//					imgFront.setImageBitmap(map);
//					
//					String[] proj = {MediaStore.Images.Media.DATA};
//                    //好像是android多媒体数据库的封装接口，具体的看Android文档
//                    Cursor cursor = managedQuery(uri, proj, null, null, null);
//                    //按我个人理解 这个是获得用户选择的图片的索引值
//                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
//                    cursor.moveToFirst();
//                    //最后根据索引值获取图片路径
//                    String path = cursor.getString(column_index);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
			
			updateImg();
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resulet, intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_save:
			doSaveModel();
			break;
		case R.id.img_custom_front:
			doChooseImg();
			break;
		case R.id.rl_choose_model:
			doChooseModel();
			break;
		case R.id.rl_choose_music:
			doChooseMusic();
			break;

		default:
			break;
		}
	}

	private void initViews() {
		tvBack = (TextView) findViewById(R.id.tv_back);
		tvSave = (TextView) findViewById(R.id.tv_save);
		tvMusicName = (TextView) findViewById(R.id.tv_music_name);
		tvModelList = (TextView) findViewById(R.id.tv_model_list);
		imgFront = (ImageView) findViewById(R.id.img_custom_front);
		tvText = (EditText) findViewById(R.id.et_custom_text);
		rlChooseModel = (RelativeLayout) findViewById(R.id.rl_choose_model);
		rlChooseMusic = (RelativeLayout) findViewById(R.id.rl_choose_music);

		tvBack.setOnClickListener(this);
		tvSave.setOnClickListener(this);
		imgFront.setOnClickListener(this);
		rlChooseModel.setOnClickListener(this);
		rlChooseMusic.setOnClickListener(this);
	}

	/**
	 * 更新图片--来自图片的数据
	 */
	private void updateImg() {
		
	}

	/**
	 * 更新模式--来自模式的数据
	 */
	private void updateMassageList() {
		String massageListName = MassageUtil.getMassageListName(massageModel_1, massageModel_2,this);
		if (!massageListName.equals("")) {
			tvModelList.setText(massageListName);
		}
	}

	/**
	 * 更新音乐--来自音乐的数据
	 * 
	 * @param name
	 */
	private void updateMusicName(String name) {
		tvMusicName.setText(name);
	}

	public final static int REQUEST_MODEL = 0xa1;
	public final static int REQUEST_MUSIC = 0xa2;
	public final static int REQUEST_IMG = 0xa3;
	public final static int REQUEST_IMG_CLIP = 0xa4;
	private byte [] datasClip;

	/**
	 * 选择音乐
	 */
	private void doChooseMusic() {
		Intent intent = new Intent(this, MusicActivity.class);
		intent.putExtra("musicId", musicId);
		startActivityForResult(intent, REQUEST_MUSIC);
	}

	/**
	 * 选择模式
	 */
	private void doChooseModel() {
		Intent intent = new Intent(this, MassageActivity.class);
		intent.putExtra("model_1", massageModel_1);
		intent.putExtra("model_2", massageModel_2);
		startActivityForResult(intent, REQUEST_MODEL);
	}

	/**
	 * 选择图片
	 */
	private void doChooseImg() {
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.addCategory(intent.CATEGORY_OPENABLE);
//		intent.setType("image/*");
//		intent.putExtra("return_data", true);
//		startActivityForResult(intent, REQUEST_IMG);
		
		Intent intent = new Intent(this,ClipImageActivity.class);
		intent.putExtra("clip", datasClip);
		startActivityForResult(intent, REQUEST_IMG_CLIP);
		
	}

	/**
	 * 保存模式
	 */
	private void doSaveModel() {
		String text = tvText.getText().toString();
		if (text == null || text.equals("")) {
//			showShortToast("请填写模式名称");
			showIosDialog(getString(R.string.custom_give_name_for_model));
			return ;
		}
		if (massageModel_1==0 && massageModel_2==0) {
//			showShortToast("至少选择一种模式");
			showIosDialog(getString(R.string.custom_choose_model_min));
			return ;
		}
		// 取出本地json，将新的添加进去
		List<MassageInfo> massageInfos = MassageJson.loadJson(this);
		if (massageInfos == null) {
			// 当为空时，new
			massageInfos = new ArrayList<>();
		}else{
			for (MassageInfo info :massageInfos) {
				if (info.getText().equals(text)) {
					showIosDialog(getString(R.string.custom_give_name_isexit));
					return;
				}
			}
		}
		
		
		
		
		// 建立自定义模式：模式、音乐、图片、文字
		MassageInfo massageInfo = new MassageInfo();
		massageInfo.setModel_1(massageModel_1);
		massageInfo.setModel_2(massageModel_2);
		massageInfo.setMusic(musicId);
//		massageInfo.setImg(R.drawable.ic_no_image);
		massageInfo.setText(text);
		
		if (datasClip!=null) {
			String url = "massage_"+text;
			FileUtil.writeBitmapData(url, datasClip,this);//保存的文件名为massage_1
//			FileUtil.writeBitmapData(url, BitmapFactory.decodeByteArray(datasClip, 0, datasClip.length));//保存的文件名为massage_1
//			String getandSaveCurrentImage = FileUtil.getandSaveCurrentImage(BitmapFactory.decodeByteArray(datasClip, 0, datasClip.length), url);
			massageInfo.setImgCustom(url);
		}
//		else{
//			massageInfo.setImgCustom(null);
//		}
		
		//Log.e("", "保存的massageInfo ："+ massageInfo);
		massageInfos.add(massageInfo);
		// 将list转为json
		String json = MassageJson.listToJson(massageInfos);
		// 保存
		FileUtil.writeFileData(BeaStaticValue.JSON_MASSAGE, json, this);
		finish();
	}
}
