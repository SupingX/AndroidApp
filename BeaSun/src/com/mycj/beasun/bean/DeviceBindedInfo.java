package com.mycj.beasun.bean;

public class DeviceBindedInfo {
	private String name ;
	private String address;
	private int dianliang;
	private int blueState=-1;//0是在线；-1是离线
	private boolean isOpen =false;
	public DeviceBindedInfo() {
		super();
	}
	
	public DeviceBindedInfo(String name, String address, int dianliang, int blueState) {
		super();
		this.name = name;
		this.address = address;
		this.dianliang = dianliang;
		this.blueState = blueState;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getDianliang() {
		return dianliang;
	}
	public void setDianliang(int dianliang) {
		this.dianliang = dianliang;
	}
	public int getBlueState() {
		return blueState;
	}
	public void setBlueState(int blueState) {
		this.blueState = blueState;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
