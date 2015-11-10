package com.mycj.beasun.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 按摩信息
 * 
 * @author Administrator index 经典 0 小憩 1 宅印象 2 空山竹语 3 办公室 4 旅行 5 听潮 6 悦风聆 7 静心 8
 *         在路上 9 静夜 10
 * 
 *         添加 FF
 * 
 * 
 *        揉捏 0xF1 推拿 0xF2 刮痧 0xF3 瘦身 0xF4 轻抚 0xF5
 *        针灸 0xF6 锤击 0xF7 指压 0xF8 颈椎 0xF9 火罐 0xFa
 * 
 * 
 * 
 *
 */
public class MassageInfo implements Parcelable {
	/**
	 * 序号 存贮对应的模式 0x01:揉捏 0x02:推拿 0x03:刮痧 0x04:瘦身 0x05:轻抚 0x06:针灸 0x07:锤击
	 * 0x08:指压 0x09:颈椎 0x0a:火罐
	 * 
	 * 小憩 as 推拿 宅印象 as 刮痧 空山竹语 as 瘦身 办公室 as 轻抚 旅行 as 针灸 听潮 as 锤击 悦风聆 as 指压 静心 as
	 * 颈椎 在路上 as 火罐 静夜 as 按摩
	 */
	private int index;
	/** 模式：可以是一种也可以是多种，由二进制数字存贮。位置对应关系为： **/
	private int model_1; // 0101_0001//
	private int model_2; // 0101_0001//
	/** 力度 **/
	private int power=1;
	/** 倒计时间 **/
	private int time=20;
	/** 音乐地址 **/
	private long music=-1L;
	/** 是否为混合模式 1:是 ;0：不是 **/
	private int isPix;
	private int img;// 图片
	private String imgCustom;

	public String getImgCustom() {
		return imgCustom;
	}

	public void setImgCustom(String imgCustom) {
		this.imgCustom = imgCustom;
	}

	private String text;// 标题
	/**
	 * 是否是混合模式 1：是 ；0：否
	 * @return
	 */
	public int getIsPix() {
		return isPix;
	}

	/**
	 * 设置是否是混合模式 1：是 ；0：否
	 * 
	 * @param isPix
	 */
	public void setIsPix(int isPix) {
		this.isPix = isPix;
	}

	public int getImg() {
		return img;
	}

	public MassageInfo(int img, String text, int index) {
		super();
		this.img = img;
		this.text = text;
		this.index = index;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MassageInfo() {
		super();
	}

	public int getModel_1() {
		return model_1;
	}

	public void setModel_1(int model_1) {
		this.model_1 = model_1;
	}

	public int getModel_2() {
		return model_2;
	}

	public void setModel_2(int model_2) {
		this.model_2 = model_2;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public long getMusic() {
		return music;
	}

	public void setMusic(long music) {
		this.music = music;
	}

	@Override
	public String toString() {
		return "序号" + index + ",模式：" + model_1 + "," + model_2 + ",力度" + power + "，时间" + time + "，音乐地址 " + music
				
				+"图片 ："+this.imgCustom;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MassageInfo) {
			MassageInfo info = (MassageInfo) o;
			return info.getIndex() == this.index;// 通过index区别是否是同一个模式
		}
		return super.equals(o);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.index);
		dest.writeInt(this.model_1);
		dest.writeInt(this.model_2);
		dest.writeInt(this.power);
		dest.writeInt(this.time);
		dest.writeLong(this.music);
		dest.writeInt(this.isPix);
		dest.writeInt(this.img);
		dest.writeString(this.text);
		dest.writeString(this.imgCustom);
		//写的顺序和读的顺序要一致
	}

	public static final Parcelable.Creator<MassageInfo> CREATOR = new Creator<MassageInfo>() {

		@Override
		public MassageInfo[] newArray(int size) {
			return new MassageInfo[size];
		}

		@Override
		public MassageInfo createFromParcel(Parcel source) {
			//写的顺序和读的顺序要一致
			MassageInfo info = new MassageInfo();  
			info.setIndex(source.readInt());
			info.setModel_1(source.readInt());
			info.setModel_2(source.readInt());
			info.setPower(source.readInt());
			info.setTime(source.readInt());
			info.setMusic(source.readLong());
			info.setIsPix(source.readInt());
			info.setImg(source.readInt());
			info.setText(source.readString());
			info.setImgCustom(source.readString());
			return info;
		}
	};

}
