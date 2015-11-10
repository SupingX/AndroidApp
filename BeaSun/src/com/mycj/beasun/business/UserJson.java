package com.mycj.beasun.business;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mycj.beasun.bean.User;
import com.mycj.beasun.service.util.FileUtil;

public class UserJson {
	/**
	 * 网络请求json转单个对象
	 * @param json
	 * @return
	 */
	public static User resultToObj(String json) {
		User bean = null;
		try {
			JSONObject objectResult = new JSONObject(json);
			JSONObject object = objectResult.getJSONObject("UserInfo");
			bean = new User();
			bean.setId(object.getInt("UserInfoId"));
			bean.setName(object.getString("UserName")); // 通过key，获取里面的数据
			bean.setEmail(object.getString("Mail"));
			bean.setPhone(object.getString("Phone"));
			bean.setCall(object.getString("TelePhone"));
			bean.setQq(object.getString("QQ"));
			bean.setWeixin(object.getString("WeChat"));
			bean.setAddress(object.getString("Address"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 网络请求json转单个对象
	 * @param json
	 * @return
	 */
	public static int isLoginSuccess(String json) {
		try {
			JSONObject objectResult = new JSONObject(json);
			int state = objectResult.getInt("State");
			return state;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public static int getRegisterState(String json) {
		try {
			JSONObject objectResult = new JSONObject(json);
			int state = objectResult.getInt("State");
			return state;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * json 转单个对象
	 * @param json
	 * @return
	 */
	public static User jsonToObj(String json) {
		User bean = null;
		try {
			JSONObject object = new JSONObject(json);
			bean = new User();
			bean.setId(object.getInt("UserInfoId"));
			bean.setName(object.getString("UserName")); // 通过key，获取里面的数据
			bean.setEmail(object.getString("Mail"));
			bean.setPhone(object.getString("Phone"));
			bean.setCall(object.getString("TelePhone"));
			bean.setQq(object.getString("QQ"));
			bean.setWeixin(object.getString("WeChat"));
			bean.setAddress(object.getString("Address"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	/**
	 * 单个
	 * 
	 * @param bean
	 * @return
	 */
	public static String objToJson(User bean) {
		if (bean == null) {
			return null;
		}
		JSONObject object = null;
		object = new JSONObject();
		try {
			//必须
			object.put("UserName", bean.getName().equals("")?"":bean.getName()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
			object.put("Phone", bean.getPhone().equals("")?"":bean.getPhone()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
			object.put("Mail", bean.getEmail().equals("")?"":bean.getEmail()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
			object.put("UserInfoId",bean.getId());
			//非必需
			object.put("TelePhone", bean.getCall().equals("")?"":bean.getCall()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
			object.put("QQ", bean.getQq().equals("")?"":bean.getQq()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
			object.put("WeChat", bean.getWeixin().equals("")?"":bean.getWeixin()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
			object.put("Address", bean.getAddress().equals("")?"":bean.getAddress()); // 把数据加入JSONObject对象即可，"userid"相当于map里面的key,1即为value的值。
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}
	

}
