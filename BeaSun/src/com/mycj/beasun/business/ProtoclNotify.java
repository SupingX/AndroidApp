package com.mycj.beasun.business;

import android.util.Log;

import com.mycj.beasun.service.util.DataUtil;


public class ProtoclNotify {
	private static ProtoclNotify mProtoclRead;
	private ProtoclNotify(){
		super();
	}
	public static ProtoclNotify  instance(){
		if (mProtoclRead==null) {
			mProtoclRead= new ProtoclNotify();
		}
		return mProtoclRead;
	}
	
	/**
	 * 	解析
	 * 	@return 1:无负载； 2：有负载 ； 3：电量
	 */
	public int parseData(byte[] data){
		String dataStr = DataUtil.byteToHexString(data);
		String protocl = dataStr.substring(0, 2);
		if (protocl.equals("F0")) {
			return 1;
		}else if (protocl.equals("F1")) {
			return 2;
		}else if (protocl.equals("F2")) {
			return 3;
		}else{
			//Log.i("ProtoclNotify", " ProtoclNotify  数据解析异常 ：" + dataStr);
			return -1;
		}
	}
	
	/**
	 * 
	 * 	获取电量
	 *	协议: 0xF2, 0xXX
			第2个字节: 表示电量级别
			01: 1级
			02: 2级
			03: 3级
			04: 4级
	 * @return 1~4
	 * 
	 */
	public int getElectricity(byte[] data){
		if (parseData(data)==3) {
			String dataStr = DataUtil.byteToHexString(data);
			int elec = Integer.valueOf(dataStr.substring(2, 4),16);
			//Log.i("ProtoclNotify", " ProtoclNotify  之 	电量 ： " + elec);
			return elec;
		}else{
			//Log.i("ProtoclNotify", " ProtoclNotify  之 	获取电量失败 。");
			return -1;
		}
	}
	
	
	
}
