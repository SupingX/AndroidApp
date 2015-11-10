package com.mycj.beasun.business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mycj.beasun.bean.DeviceJson;
import com.mycj.beasun.service.util.FileUtil;

public class BluetoothJson {

	/**
	 * 把一个集合转换成json格式的字符串 (新)
	 * 
	 * @param beans
	 * @return
	 */

	public static String listToJson(List<DeviceJson> beans) { // 把一个集合转换成json格式的字符串
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
				obj.put("name", beans.get(i).getName()==""?"":beans.get(i).getName()); // 从集合取出数据，放入JSONObject里面
				obj.put("address", beans.get(i).getAddress());
				jsonArray.put(obj); // 把JSONObject对象装入jsonArray数组里面
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		try {
			object.put("device", jsonArray); // 再把JSONArray数据加入JSONObject对象里面(数组也是对象)
			// object.put("time", "2013-11-14");
			// //这里还可以加入数据，这样json型字符串，就既有集合，又有普通数据
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString(); // 把JSONObject转换成json格式的字符串
	}

	/**
	 * 单个
	 * 
	 * @param bean
	 * @return
	 */
	public static String objToJson(DeviceJson bean) {
		if (bean == null) {
			return null;
		}
		JSONObject object = null;
		object = new JSONObject();
		try {
			object.put("name", bean.getName().equals("")?"":bean.getName()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
			object.put("address", bean.getAddress());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * json转对象数据
	 * 
	 * @param json
	 * @return
	 */
	public static List<DeviceJson> jsonToList(String json) {
		List<DeviceJson> beans = new ArrayList<DeviceJson>();
		try {
			JSONObject object = new JSONObject(json); // 用json格式的字符串获取一个JSONObject对象 //最外层的JSONObject
			JSONArray jsonArray = object.getJSONArray("device"); // 通过key，获取JSONObject里面的一个JSONArray数组
			for (int i = 0; i < jsonArray.length(); i++) { // 遍历数据
				JSONObject obj = jsonArray.getJSONObject(i); // 从JSONArray里面获取一个JSONObject对象
				DeviceJson bean = new DeviceJson();
				bean.setName(obj.getString("name")); // 通过key，获取里面的数据
				bean.setAddress(obj.getString("address"));
				beans.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return beans;
	}

	/**
	 * json 转单个对象
	 * 
	 * @param json
	 * @return
	 */
	public static DeviceJson jsonToObj(String json) {
		DeviceJson bean = null;
		try {
			JSONObject object = new JSONObject(json);
			bean = new DeviceJson();
			bean.setName(object.getString("name")); // 通过key，获取里面的数据
			bean.setAddress(object.getString("address"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	
	
	
	
	public static List<DeviceJson>  loadJson(Context context){
		List<DeviceJson> devicesBinded = null;
		String json = FileUtil.readFileData(BeaStaticValue.JSON_DEVICE, context);
		if (json != null && !json.equals("")) {
		devicesBinded = jsonToList(json);
		}
		return devicesBinded;
	}

}
