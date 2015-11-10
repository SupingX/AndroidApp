package com.mycj.beasun.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileUtil {
	private static final String TAG = "FileUtil";

	public static void deleteBitmapData(String fileName) {
		File file = new File("/data/data/com.mycj.beasun/files/" + fileName);
		try {
			if (file.exists()) {
				if (file.delete()) {
//					//Log.w(TAG, "file delete success!");
				} else {
//					//Log.w(TAG, "file delete fail!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap readBitmapData(String fileName, Context context) {
		FileInputStream input = null;
		Bitmap bitmap = null;
		try {
			File file = new File("/data/data/com.mycj.beasun/files/" + fileName);
			FileInputStream inStream = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
					input = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	public static void writeBitmapData(String fileName, byte[] data, Context context) {
		try {
			FileOutputStream fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fout.write(data);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 一、私有文件夹下的文件存取（/data/data/包名/files）
	 * 
	 * @param fileName
	 * @param json
	 */
	public static void writeFileData(String fileName, String json, Context context) {
		//Log.e("FileUtil", "message :" + json);
		try {
			FileOutputStream fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			byte[] bytes = json.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * //读文件在./data/data/包名/files/下面
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFileData(String fileName, Context context) {
		StringBuffer res = new StringBuffer();
		FileInputStream input = null;
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			input = context.openFileInput(fileName);
			inputReader = new InputStreamReader(input);
			bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				res.append(line);
			}
			//
			// int length = input.available();
			// int len;
			// byte[] buffer = new byte[length];
			// while (( len=input.read(buffer))!=-1) {
			// res += EncodingUtils.getString(buffer, "UTF-8");
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
					input = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputReader != null) {
				try {
					inputReader.close();
					inputReader = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufReader != null) {
				try {
					bufReader.close();
					bufReader = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res.toString();
	}
}
