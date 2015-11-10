package com.mycj.beasun.bean;

/**
 * 智能模式item类
 * @author Administrator
 *
 */
public class IntellgentModleItem {
	private int img;//图片
	private String text;//标题
	public IntellgentModleItem(){
		
	}
	
	public IntellgentModleItem(int img, String text) {
		super();
		this.img = img;
		this.text = text;
	}

	public int getImg() {
		return img;
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
	
}
