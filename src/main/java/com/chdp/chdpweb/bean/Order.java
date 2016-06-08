package com.chdp.chdpweb.bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Order implements Comparable<Order> {

    private int id;
    private String uuid;
    private int hospital_id;
    private String create_time;
    private int create_user_id;
    private String outbound_time;
    private int outbound_user_id;
    private int status;

    private String hospital_name;
    
    private int prs_num;
    private String create_user_name;
    private String outbound_user_name;
    private BigDecimal price_total;
    private int packet_num;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getCreate_time() {
		try{
			if (create_time != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				create_time = sdf.format(sdf.parse(create_time));
			}
			return create_time;
		}catch (Exception e){
			return create_time;
		}
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getOutbound_time() {
		try{
			if (outbound_time != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				outbound_time = sdf.format(sdf.parse(outbound_time));
			}
			return outbound_time;
		}catch (Exception e){
			return outbound_time;
		}
    }

    public void setOutbound_time(String outbound_time) {
        this.outbound_time = outbound_time;
    }

    public int getOutbound_user_id() {
        return outbound_user_id;
    }

    public void setOutbound_user_id(int outbound_user_id) {
        this.outbound_user_id = outbound_user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public int getPrs_num(){
    	return prs_num;
    }
    
    public void setPrs_num(int num){
    	this.prs_num = num;
    }
    
    public String getCreate_user_name(){
    	return create_user_name;
    }
    
    public void setCreate_user_name(String usr_name){
    	this.create_user_name = usr_name;
    }
    
    public String getOutbound_user_name(){
    	return outbound_user_name;
    }
    
    public void setOutbound_user_name(String user_name){
    	this.outbound_user_name = user_name;
    }
  
    public BigDecimal getPrice_total(){
    	return price_total;
    }
    
    public void setPrice_total(BigDecimal priceTotal){
    	this.price_total = priceTotal;
    }
    
    public int getPacket_num(){
    	return packet_num;
    }
    
    public void setPacket_num(int num){
    	this.packet_num = num;
    }
    
    public int compareTo(Order another){
    	if (this.getPrs_num() > another.getPrs_num()){
    		return -1;
    	}else if (this.getPrs_num() < another.getPrs_num()){
    		return 1;
    	}else{
    		return 0;
    	}
    }
}
