package com.mycj.beasun.bean;

public class DeviceJson {
	private String name ;
	private String address;
	private String rssi;
	
	public DeviceJson(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}
	public DeviceJson(String name, String address,String rssi) {
		super();
		this.name = name;
		this.address = address;
		this.setRssi(rssi);
	}
	public DeviceJson() {
		super();
		// TODO Auto-generated constructor stub
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
	
	@Override
	public String toString() {
		return "["+ name + "," +address+"]";
	}
	public String getRssi() {
		return rssi;
	}
	public void setRssi(String rssi) {
		this.rssi = rssi;
	}
	
}
