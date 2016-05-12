package com.chdp.chdpweb.bean;

public class User implements Comparable<User>{
	private int id;
	private String usercode;
	private String name;
	private String password;
	private int authority;
	@SuppressWarnings("unused")
	private String authority_str;
	
	private String position;
	private int done_prs_num;
	private int prs_five_packet_num;
	private int prs_seven_packet_num;
	private int prs_ten_packet_num;
	private int prs_fourteen_packet_num;
	private int prs_other_packet_num;
	private int error_num;
	
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

	public String getAuthority_str() {
		String str = "";
		if ((authority & 1024) > 0) {
			str += "管理员 ";
		}
		if ((authority & 512) > 0) {
			str += "接方 ";
		}
		if ((authority & 256) > 0) {
			str += "审方 ";
		}
		if ((authority & 128) > 0) {
			str += "调配 ";
		}
		if ((authority & 64) > 0) {
			str += "调配审核 ";
		}
		if ((authority & 32) > 0) {
			str += "浸泡 ";
		}
		if ((authority & 16) > 0) {
			str += "煎煮 ";
		}
		if ((authority & 8) > 0) {
			str += "灌装 ";
		}
		if ((authority & 4) > 0) {
			str += "清场 ";
		}
		if ((authority & 2) > 0) {
			str += "包装 ";
		}
		if ((authority & 1) > 0) {
			str += "运输 ";
		}
		return str;
	}

	public int getDone_prs_num(){
		return done_prs_num;
	}
	
	public void setDone_prs_num(int num){
		this.done_prs_num = num;
	}
	
	public int getError_num(){
		return error_num;
	}
	
	public void setError_num(int num){
		this.error_num = num;
	}
	
	public String getPosition(){
		return position;
	}
	
	public void setPosition(String position){
		this.position = position;
	}
	
	public int compareTo(User another){
		if(this.getDone_prs_num() > another.getDone_prs_num()){
			return -1;
		}else if (this.getDone_prs_num() < another.getDone_prs_num()){
			return 1;
		}else{
			if (this.getError_num() > another.getError_num()){
				return -1;
			}else if (this.getError_num() < another.getError_num()){
				return 1;
			}
			return 0;
		}
	}
	
	public int getPrs_five_packet_num(){
		return prs_five_packet_num;
	}
	
	public void setPrs_five_packet_num(int num){
		this.prs_five_packet_num = num;
	}

	public int getPrs_seven_packet_num(){
		return prs_seven_packet_num;
	}
	
	public void setPrs_seven_packet_num(int num){
		this.prs_seven_packet_num = num;
	}

	public int getPrs_ten_packet_num(){
		return prs_ten_packet_num;
	}
	
	public void setPrs_ten_packet_num(int num){
		this.prs_ten_packet_num = num;
	}
	
	public int getPrs_fourteen_packet_num(){
		return prs_fourteen_packet_num;
	}
	
	public void setPrs_fourteen_packet_num(int num){
		this.prs_fourteen_packet_num = num;
	}
	
	public int getPrs_other_packet_num(){
		return prs_other_packet_num;
	}
	
	public void setPrs_other_packet_num(int num){
		this.prs_other_packet_num = num;
	}

}
