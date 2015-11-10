package com.mycj.beasun.bean;

/**
 * 按摩模式列表item
 * @author Administrator
 *
 */
public class MassageItem {
	public MassageItem(int isChoose, int index,String hex) {
		super();
		this.isChoose = isChoose;
		this.index = index;
		this.hex = hex;
	}
	public int getIsChoose() {
		return isChoose;
	}
	public void setIsChoose(int isChoose) {
		this.isChoose = isChoose;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getHex() {
		return hex;
	}
	public void setHex(String hex) {
		this.hex = hex;
	}
	private int isChoose;
	private String name;
	private int index;
	private String hex;
}
