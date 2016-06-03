package com.hjy.model;

import java.io.Serializable;

public class UserInfo implements Serializable{
	private int uid;
	private String uname;
	private String uclass;
	private String mobile;
	private String schoolId;
	private String password;
	
	public UserInfo(){
		
	}

	public UserInfo(String schoolId,String uname, String uclass) {
		super();
		this.uname = uname;
		this.uclass = uclass;
		this.schoolId = schoolId;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUclass() {
		return uclass;
	}

	public void setUclass(String uclass) {
		this.uclass = uclass;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString(){
		
		return getMobile()+getPassword()+getSchoolId()+getUclass()+getUname();
	}

}
