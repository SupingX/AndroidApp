package com.mycj.beasun.business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mycj.beasun.bean.DeviceJson;
import com.mycj.beasun.bean.MassageInfo;
import com.mycj.beasun.service.util.FileUtil;

public class MassageJson {

	/**
	 * 集合-->json (新)
	 * 
	 * @param beans
	 * @return
	 */

	public static String listToJson(List<MassageInfo> beans) { // 把一个集合转换成json格式的字符串
		if (beans == null) {
			return "";
		}
		if (beans.size() <= 0) {
			return "";
		}
		JSONArray jsonArray = null;
		JSONObject object = null;//最外层的JSONObject
		jsonArray = new JSONArray();
		object = new JSONObject();
		for (int i = 0; i < beans.size(); i++) { // 遍历上面初始化的集合数据，把数据加入JSONObject里面
			JSONObject obj = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
			try {
				MassageInfo massageInfo = beans.get(i);
//				obj.put("index", massageInfo.getIndex()); // 从集合取出数据，放入JSONObject里面
				obj.put("model_1",massageInfo.getModel_1());
				obj.put("model_2",massageInfo.getModel_2());
//				obj.put("power", massageInfo.getPower());
//				obj.put("time", massageInfo.getTime());
				obj.put("music", massageInfo.getMusic());
				obj.put("img", massageInfo.getImg());
				obj.put("text", massageInfo.getText());
				obj.put("music_custom", massageInfo.getImgCustom()==null?"":massageInfo.getImgCustom());
				jsonArray.put(obj); // 把JSONObject对象装入jsonArray数组里面
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		try {
			object.put("massage", jsonArray); // 再把JSONArray数据加入JSONObject对象里面(数组也是对象)
			// object.put("time", "2013-11-14");
			// //这里还可以加入数据，这样json型字符串，就既有集合，又有普通数据
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString(); // 把JSONObject转换成json格式的字符串
	}


	/**
	 * json转对象数据
	 * 
	 * @param json
	 * @return
	 */
	public static List<MassageInfo> jsonToList(String json) {
		List<MassageInfo> beans = new ArrayList<MassageInfo>();
		try {
			JSONObject object = new JSONObject(json); // 用json格式的字符串获取一个JSONObject对象 //最外层的JSONObject
			JSONArray jsonArray = object.getJSONArray("massage"); // 通过key，获取JSONObject里面的一个JSONArray数组
			for (int i = 0; i < jsonArray.length(); i++) { // 遍历数据
				JSONObject obj = jsonArray.getJSONObject(i); // 从JSONArray里面获取一个JSONObject对象
				MassageInfo bean = new MassageInfo();
//				bean.setIndex(obj.getInt("index")); // 通过key，获取里面的数据
				bean.setModel_1(obj.getInt("model_1")); // 通过key，获取里面的数据
				bean.setModel_2(obj.getInt("model_2")); // 通过key，获取里面的数据
				bean.setMusic(obj.getLong("music"));
				bean.setImg(obj.getInt("img"));
				bean.setText(obj.getString("text"));
				bean.setImgCustom(obj.getString("music_custom")==null?"":obj.getString("music_custom"));
				beans.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return beans;
	}

	
	
	
	
	
	public static	List<MassageInfo>  loadJson(Context context){
		List<MassageInfo> massageInfos = null;
		String json = FileUtil.readFileData(BeaStaticValue.JSON_MASSAGE, context);
		if (json != null && !json.equals("")) {
			massageInfos = jsonToList(json);
		}
		return massageInfos;
	}

}
