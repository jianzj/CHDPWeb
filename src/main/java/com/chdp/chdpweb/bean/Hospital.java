package com.chdp.chdpweb.bean;

public class Hospital {
	
	private int id;
	private String name;
	private String description;
	private int finishedPrsNum;
	
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
}
