package com.mycj.beasun.business;

import android.util.Log;

import com.mycj.beasun.service.util.DataUtil;

public class ProtoclWrite {
	private static ProtoclWrite mProtoclWrite;
	private ProtoclWrite(){
		super();
	}
	public static ProtoclWrite  instance(){
		if (mProtoclWrite==null) {
			mProtoclWrite= new ProtoclWrite();
		}
		return mProtoclWrite;
	}
	/**
	 * 	智能理疗 协议: 0xF1, 0xXX, 0xXX 
	 * 	1: 0xF1 
	 * 	2: 0xXX 火罐7 指压6 针灸5 刮痧4 锤击3 推拿2 揉捏1 按摩0（删除）
	 * 	3: 0xXX 颈椎2轻抚1 瘦身0
	 * 
	 * @return 
	 * @param model1 : 火罐7 指压6 针灸5 刮痧4 锤击3 推拿2 揉捏1 按摩0（删除） [例如 ： 0111_1110]
	 * @param model2 : 颈椎2轻抚1 瘦身0[例如：0000_0111]
	 */
	
	public byte[] protoclWriteForIntelligent(int model1,int model2){
		StringBuffer sb = new StringBuffer();
		sb.append("F1");
		sb.append(DataUtil.toHexString(model1));
		sb.append(DataUtil.toHexString(model2));
		//Log.i("ProtoclWrite", "ProtoclWrite 之 智能理疗 ：" +sb.toString());
		return DataUtil.hexStringToByte(sb.toString());
	}
	
	/**
	 * 	单一按摩模式
	 * 	协议: 0xF2, 0xXX
		1: 	0xF2
		2：	0xXX
		第二字节协议: 0XF2, 按摩(删除)
		第二字节协议: 0xF3, 揉捏
		第二字节协议: 0xF4, 推拿
		第二字节协议: 0xF5, 锤击
		第二字节协议: 0XF6, 刮痧
		第二字节协议: 0xF7, 针灸
		第二字节协议: 0xF8, 指压
		第二字节协议: 0xF9, 火罐
		第二字节协议: 0xFA, 瘦身
		第二字节协议: 0xFB, 轻抚
		第二字节协议: 0xFC, 颈椎
		@param model F2-FC
	 */
//	public byte[] protoclWriteForSingle(int model){
//		StringBuffer sb = new StringBuffer();
//		sb.append("F2");
//		sb.append(DataUtil.toHexString(model));
//		//Log.i("ProtoclWrite", "ProtoclWrite 之 	单一按摩 ：" +sb.toString());
//		return DataUtil.hexStringToByte(sb.toString());
//	}
	public byte[] protoclWriteForSingle(String model){
		StringBuffer sb = new StringBuffer();
		sb.append("F2");
		sb.append(model);
		//Log.i("ProtoclWrite", "ProtoclWrite 之 	单一按摩 ：" +sb.toString());
		return DataUtil.hexStringToByte(sb.toString());
	}
	public byte[] protoclWriteForSingle(int model_1,int model_2){
		String singleHex = MassageUtil.getSingleHex(model_1, model_2);
		StringBuffer sb = new StringBuffer();
		sb.append("F2");
		sb.append(singleHex);
		//Log.i("ProtoclWrite", "ProtoclWrite 之 	单一按摩 ：" +sb.toString());
		return DataUtil.hexStringToByte(sb.toString());
	}
	
	
	/**
	 * 	强度调节
	 * 	协议: 0xFB, 0xXX第二个字节: 表示具体强度
	 * 	@param level 1~11
	 */
	public byte[] protoclWriteForLevel(int level){
		
		StringBuffer sb = new StringBuffer();
		sb.append("FB");
		sb.append(DataUtil.toHexString(level));
		//Log.i("ProtoclWrite", "ProtoclWrite 之 	强度调节 ：" +sb.toString());
		return DataUtil.hexStringToByte(sb.toString());
	}
	
	/**
	 * 	开始/暂停 /停止
	 * 	协议: 0xFC, 0xXX
		第2个字节: 表示具体操作
		0x01: 表示开始
		0x02: 表示暂停
		0x03: 表示停止
		@param order 0x01|0x02|0x03
	 * 
	 */
	public byte[] protoclWriteForControl(int order){
		StringBuffer sb = new StringBuffer();
		sb.append("FC");
		sb.append(DataUtil.toHexString(order));
		//Log.i("ProtoclWrite", "ProtoclWrite 之	开始/暂停/停止 ：" +sb.toString());
		return DataUtil.hexStringToByte(sb.toString());
	}
	
	/**
	 * 关机
	 * 协议：0xFD 表示关闭按摩器
	 * @return
	 */
	public byte[] protoclWriteForShutDown(){
		StringBuffer sb = new StringBuffer();
		sb.append("FD");
		//Log.i("ProtoclWrite", "ProtoclWrite 之 	关机 ：" +sb.toString());
		return DataUtil.hexStringToByte(sb.toString());
	}
}
