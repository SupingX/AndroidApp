package com.mycj.beasun.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.mycj.beasun.R;
import com.mycj.beasun.bean.MassageItem;

public class MassageUtil {
	
//	massages.add("瘦身按摩");
//	massages.add("轻抚按摩");
//	massages.add("颈椎按摩");
//	massages.add("揉捏按摩");
//	massages.add("推拿按摩");
//	massages.add("锤击按摩");
//	massages.add("刮痧按摩");
//	massages.add("针灸按摩");
//	massages.add("指压按摩");
//	massages.add("火罐按摩");
	/**
	 * 根据type 对应中文
	 * @param type
	 * @return
	 */
	public static String getTextFromInteger(int type,Context context) {
		String result = "";
		Resources resources = context.getResources();
		switch (type) {
		case 0x01:
//			result = "揉捏按摩";
			result = resources.getString(R.string.massage_kneading);
			break;
		case 0x02:
//			result = "推拿按摩";
			result = resources.getString(R.string.massage_manipulation);
	
			break;
		case 0x03:
//			result = "锤击按摩";
			result = resources.getString(R.string.massage_hammer);

			break;
		case 0x04:
//			result = "刮痧按摩";
			result = resources.getString(R.string.massage_scrapping);

			break;
		case 0x05:
//			result = "针灸按摩";
			result = resources.getString(R.string.massage_acupuncture);
			break;
		case 0x06:
//			result = "指压按摩";
			result = resources.getString(R.string.massage_acupressure);
			break;
		case 0x07:
//			result = "火罐按摩";
			result = resources.getString(R.string.massage_cupping);
			break;
		case 0x08:
//			result = "瘦身按摩";
			result = resources.getString(R.string.massage_slimming);
			break;
		case 0x09:
//			result = "轻抚按摩";
			result = resources.getString(R.string.massage_gently);
			break;
		case 0x0a:
//			result = "颈椎按摩";
			result = resources.getString(R.string.massage_neck);
			break;
			

		default:
			break;
		}
		return result;
	}
	
	/**
	 * 根据type 对应中文
	 * @param type
	 * @return
	 */
	public static String getTextFromIndex(int index,Context context) {
		String result = "";
		Resources resources = context.getResources();
		switch (index) {
		case 1:
//			result = "小憩";
			result = resources.getString(R.string.intelligent_model_xiaoqi);
			break;
		case 2:
//			result = "宅印象";
			result = resources.getString(R.string.intelligent_model_zhaiyinxiang);
			break;
		case 3:
//			result = "空山竹语";
			result = resources.getString(R.string.intelligent_model_kongshanzhuyu);
			break;
		case 4:
//			result = "办公室";
			result = resources.getString(R.string.intelligent_model_bangongshi);

			break;
		case 5:
//			result = "旅行";
			result = resources.getString(R.string.intelligent_model_lvxing);
			break;
		case 6:
//			result = "听潮";
			result = resources.getString(R.string.intelligent_model_tingchao);
			break;
		case 7:
//			result = "悦风聆";
			result = resources.getString(R.string.intelligent_model_yuefengling);
			break;
		case 8:
//			result = "静心";
			result = resources.getString(R.string.intelligent_model_jingxin);
			break;
		case 9:
//			result = "在路上";
			result = resources.getString(R.string.intelligent_model_zailushang);
			break;
		case 10:
//			result = "静夜";
			result = resources.getString(R.string.intelligent_model_jingye);
			break;
		default:
			break;
		}
		return result;
	}
	/**
	 * 从一个二进制数 ， 生成选择模式列表 。
	 * 例如：massageSaved = 0b 11_0000_1111 表示：1表示选中 0 表示未选中
	 * 
	 * @param massageSaved
	 * @return
	 */
	public static List<MassageItem> getSavedMassageItems(int massageSaved1,int massageSaved2){
		List<MassageItem> massages = new ArrayList<MassageItem>();
		MassageItem item1 = new MassageItem((massageSaved1 & 0b0000_0001)==0b0000_0001?1:0,0x01,"F3");
		MassageItem item2 = new MassageItem((massageSaved1 & 0b0000_0010)==0b0000_0010?1:0,0x02,"F4");
		MassageItem item3 = new MassageItem((massageSaved1 & 0b0000_0100)==0b0000_0100?1:0,0x03,"F5");
		MassageItem item4 = new MassageItem((massageSaved1 & 0b0000_1000)==0b0000_1000?1:0,0x04,"F6");
		MassageItem item5 = new MassageItem((massageSaved1 & 0b0001_0000)==0b0001_0000?1:0,0x05,"F7");
		MassageItem item6 = new MassageItem((massageSaved1 & 0b0010_0000)==0b0010_0000?1:0,0x06,"F8");
		MassageItem item7 = new MassageItem((massageSaved1 & 0b0100_0000)==0b0100_0000?1:0,0x07,"F9");
		MassageItem item8 = new MassageItem((massageSaved2 & 0b0000_0001)==0b0000_0001?1:0,0x08,"FA");
		MassageItem item9 = new MassageItem((massageSaved2 & 0b0000_0010)==0b0000_0010?1:0,0x09,"FB");
		MassageItem item10 = new MassageItem((massageSaved2 & 0b0000_0100)==0b0000_0100?1:0,0x0a,"FC");
		massages.add(item1);
		massages.add(item2);
		massages.add(item3);
		massages.add(item4);
		massages.add(item5);
		massages.add(item6);
		massages.add(item7);
		massages.add(item8);
		massages.add(item9);
		massages.add(item10);
		return massages;
	}
	
	/**
	 * 第二字节协议: 0XF2, 按摩
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

	 * @param model_1
	 * @param model_2
	 * @return
	 */
	public static String getSingleHex(int model_1,int model_2){
		String hex = "F3";
		if (model_1 ==0 && model_2==0) {
			return hex;
		}
		if (model_1!=0 && model_2!=0) {
			return hex;
		}
		
		if (model_1==0) {
			if ((model_2 & 0b0000_0001)==0b0000_0001) {
				hex = "FA";
			}else if ((model_2 & 0b0000_0010)==0b0000_0010) {
				hex = "FB";
			}else if ((model_2 & 0b0000_0100)==0b0000_0100) {
				hex = "FC";
			}
		}
		
		if (model_2==0) {
			if ((model_1 & 0b0000_0001)==0b0000_0001) {
				hex = "F3";
			}else if ((model_1 & 0b0000_0010)==0b0000_0010) {
				hex = "F4";
			}else if ((model_1 & 0b0000_0100)==0b0000_0100) {
				hex = "F5";
			}else if ((model_1 & 0b0000_1000)==0b0000_1000) {
				hex = "F6";
			}
			else if ((model_1 & 0b0001_0000)==0b0001_0000) {
				hex = "F7";
			}else if ((model_1 & 0b0010_0000)==0b0010_0000) {
				hex = "F8";
			}else if ((model_1 & 0b0100_0000)==0b0100_0000) {
				hex = "F9";
			}
		}
		
		return hex;
	}
	
	public static String  getSingleHex(int index){
		String model = "F5";
		switch (index) {
		case 0xF1:
		case 10:
			model = "F1";
			break;
		case 0xF2:
		case 0x1:
			model = "F2";
			break;
		case 0xF3:
		case 0x2:
			model = "F3";
			break;
		case 0xF4:
		case 0x3:
			model = "F4";
			break;
		case 0xF5:
		case 0x4:
			model = "F5";
			break;
		case 0xF6:
		case 0x5:
			model = "F6";
			break;
		case 0xF7:
		case 0x6:
			model = "F7";
			break;
		case 0xF8:
		case 0x7:
			model = "F8";
			break;
		case 0xF9:
		case 0x8:
			model = "F9";
			break;
		case 0xFA:
		case 0x9:
			model = "FA";
			break;

		default:
			break;
		}
		return model;
	}
	
	public static String getMassageListName(int massageSaved1,int massageSaved2,Context context){
		StringBuffer sb = new StringBuffer();
		
		if((massageSaved1 & 0b0000_0001)==0b0000_0001){
			sb.append(getTextFromInteger(0x01,context)+" ");
		} 
		if ((massageSaved1 & 0b0000_0010)==0b0000_0010) {
			sb.append(getTextFromInteger(0x02,context)+" ");
		} 
		if ((massageSaved1 & 0b0000_0100)==0b0000_0100) {
			sb.append(getTextFromInteger(0x03,context)+" ");
		} 
		if ((massageSaved1 & 0b0000_1000)==0b0000_1000) {
			sb.append(getTextFromInteger(0x04,context)+" ");
		}
		if ((massageSaved1 & 0b0001_0000)==0b0001_0000) {
			sb.append(getTextFromInteger(0x05,context)+" ");
		}
		if ((massageSaved1 & 0b0010_0000)==0b0010_0000) {
			sb.append(getTextFromInteger(0x06,context)+" ");
		}
		if ((massageSaved1 & 0b0100_0000)==0b0100_0000) {
			sb.append(getTextFromInteger(0x07,context)+" ");
		}
		if ((massageSaved2 & 0b0000_0001)==0b0000_0001) {
			sb.append(getTextFromInteger(0x08,context)+" ");
		}else if ((massageSaved2 & 0b0000_0010)==0b0000_0010) {
			sb.append(getTextFromInteger(0x09,context)+" ");
		}
		if ((massageSaved2 & 0b0000_0100)==0b0000_0100) {
			sb.append(getTextFromInteger(0x0a,context));
		}
		return sb.toString();
	}
	
	
}
