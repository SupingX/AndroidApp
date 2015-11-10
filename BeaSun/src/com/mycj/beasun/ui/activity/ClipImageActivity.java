package com.mycj.beasun.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import com.mycj.beasun.R;
import com.mycj.beasun.view.clip.ClipImageLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ClipImageActivity extends Activity implements OnClickListener {

	private ClipImageLayout clipImageLayout;
	private TextView tvBack;
	private TextView tvSave;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip_image);
		initVies();
		byte[] clip = getIntent().getByteArrayExtra("clip");
		String name = getIntent().getStringExtra("name");
		//Log.i("", "__________clip :" + clip);
		//Log.i("", "__________name :" + name);
		if (clip != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(clip, 0, clip.length);
			clipImageLayout.setBitmap(bitmap);
		}
		
	}

	private void initVies() {
		clipImageLayout = (ClipImageLayout) findViewById(R.id.clip_img);
		tvBack = (TextView) findViewById(R.id.tv_back);
		tvSave = (TextView) findViewById(R.id.tv_save);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvBack.setOnClickListener(this);
		tvSave.setOnClickListener(this);
		tvTitle.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_save:
			sendResultToCustomActivity();
			break;
		case R.id.tv_title:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			intent.putExtra("return_data", true);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	private void sendResultToCustomActivity() {
		Bitmap clip = clipImageLayout.clip();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		clip.compress(Bitmap.CompressFormat.JPEG, 100, out);
		byte[] data = out.toByteArray();
		Intent intent = new Intent(this, CustomMassageModelActivity.class);
		intent.putExtra("clip", data);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode==1) {
			if (resultCode == RESULT_OK) {
				Bitmap bitmap = decodeUriAsBitmap(data.getData());
				//Log.e("", "__________________bitmap :" + bitmap);
				clipImageLayout.setBitmap(bitmap);
//				byte [] datas = data.getByteArrayExtra("clip");
//				if (datas!=null) {
//					Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
//					clipImageLayout.setBitmap(bitmap);
//				}
			}
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private Bitmap decodeUriAsBitmap(Uri uri){

	    Bitmap bitmap = null;

	    try {

	    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

	        } catch (FileNotFoundException e) {

	    e.printStackTrace();

	        return null;

	        }

	    return bitmap;

	    }
	
}
