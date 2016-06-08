package com.chdp.chdpweb.bean;

import java.math.BigDecimal;

public class Hospital implements Comparable<Hospital> {
	
	private int id;
	private String name;
	private String description;
	
	//以下变量用于医院维度统计处方情况时使用
	private int finishedPrsNum;
	private int totalPacketNum;
	private BigDecimal totalPrice;
	private int orderNum;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public int getFinishedPrsNum(){
		return finishedPrsNum;
	}
	
	public void setFinishedPrsNum(int num){
		this.finishedPrsNum = num;
	}

	public int getTotalPacketNum(){
		return totalPacketNum;
	}
	
	public void setTotalPacketNum(int totalPacketNum){
		this.totalPacketNum = totalPacketNum;
	}
	
	public BigDecimal getTotalPrice(){
		return totalPrice;
	}
	
	public void setTotalPrice(BigDecimal price){
		this.totalPrice = price;
	}

	public int getOrderNum(){
		return orderNum;
	}
	
	public void setOrderNum(int num){
		this.orderNum = num;
	}
	
	public int compareTo(Hospital hosp1) {
		if (this.getFinishedPrsNum() > hosp1.getFinishedPrsNum()){
			return -1;
		}else if (this.getFinishedPrsNum() < hosp1.getFinishedPrsNum()){
			return 1;
		}else{
			return 0;
		}
    }
}
