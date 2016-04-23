package com.chdp.chdpweb.bean;

public class User {
	private int id;
	private String usercode;
	private String name;
	private String password;
	private int authority;
	private String last_outer_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	public String getLast_outer_id() {
		return last_outer_id;
	}

	public void setLast_outer_id(String last_outer_id) {
		this.last_outer_id = last_outer_id;
	}

}
