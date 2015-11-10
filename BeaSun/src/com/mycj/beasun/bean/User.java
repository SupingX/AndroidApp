package com.mycj.beasun.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	//必须
	private int id;
	private String name;
	private String password;
	private String phone;
	private String email;
	//非必需
	private String call;
	private String qq;
	private String weixin;
	private String address;
	
	public User(String name, String password, String phone, String email, String call, String qq, String weixin, String address) {
		super();
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.call = call;
		this.qq = qq;
		this.weixin = weixin;
		this.address = address;
	}
	public User() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCall() {
		return call;
	}
	public void setCall(String call) {
		this.call = call;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	@Override
	public String toString() {
		return "姓名: "+this.name
				+"\n"+"密码"+this.password
				+"\n"+"手机"+this.phone
				+"\n"+"邮箱"+this.email
				+"\n"+"电话"+this.call
				+"\n"+"QQ"+this.qq
				+"\n"+"微信"+this.weixin
				+"\n"+"地址"+this.address
				;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.address);
		dest.writeString(this.call);
		dest.writeString(this.email);
		dest.writeString(this.name);
		dest.writeString(this.password);
		dest.writeString(this.phone);
		dest.writeString(this.qq);
		dest.writeString(this.weixin);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
		
		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
		
		@Override
		public User createFromParcel(Parcel source) {
			User user = new User();
			user.setId(source.readInt());
			user.setAddress(source.readString());
			user.setCall(source.readString());
			user.setEmail(source.readString());
			user.setName(source.readString());
			user.setPassword(source.readString());
			user.setPhone(source.readString());
			user.setQq(source.readString());
			user.setWeixin(source.readString());
			return user;
		}
	};
	
	
}
