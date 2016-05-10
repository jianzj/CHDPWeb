package com.chdp.chdpweb.bean;

public class Node {
	
	private int nodeId;
	private int nodeType;
	private String nodeTypeName;
	private String resolvedBy;
	//0: 尚未开始；1.正在进行；2.已经完成
	private int status;
	//false:说明Process处于非浸泡、煎煮、清场阶段; true:说明Process正在处于浸泡，煎煮或清场阶段
	private boolean specialDisplay;
	private String startTime;
	private String endTime;
	private int errorStatus;
	private String errorMsg;
	private String machineName;
	
	private String orderStatus;
	
	private String dealTime;
	private String decoctTime;
	private String heatTime;
	
	public int getNodeId(){
		return nodeId;
	}
	
	public void setNodeId(int nodeId){
		this.nodeId = nodeId;
	}
	
	public int getNodeType(){
		return nodeType;
	}
	
	public void setNodeType(int nodeType){
		this.nodeType = nodeType;
	}

	public String getNodeTypeName(){
		return nodeTypeName;
	}
	
	public void setNodeTypeName(String name){
		this.nodeTypeName = name;
	}
	
	public String getResolvedBy(){
		return resolvedBy;
	}
	
	public void setResolvedBy(String resolvedBy){
		this.resolvedBy = resolvedBy;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public boolean getSpecialDisplay(){
		return specialDisplay;
	}
	
	public void setSpecialDisplay(boolean display){
		this.specialDisplay = display;
	}
	
	public String getStartTime(){
		return startTime;
	}
	
	public void setStartTime(String startTime){
		this.startTime = startTime;
	}
	
	public String getEndTime(){
		return endTime;
	}
	
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public int getErrorStatus(){
		return errorStatus;
	}
	
	public void setErrorStatus(int status){
		this.errorStatus = status;
	}

	
	public String getErrorMsg(){
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	public String getDealTime(){
		return dealTime;
	}
	
	public void setDealTime(String dealTime){
		this.dealTime = dealTime;
	}
	
	public String getDecoctTime(){
		return decoctTime;
	}
	
	public void setDecoctTime(String decoctTime){
		this.decoctTime = decoctTime;
	}
	
	public String getHeatTime(){
		return heatTime;
	}
	
	public void setHeatTime(String heatTime){
		this.heatTime = heatTime;
	}

	public String getMachineName(){
		return machineName;
	}
	
	public void setMachineName(String name){
		this.machineName = name;
	}
	
	public String getOrderStatus(){
		return orderStatus;
	}
	
	public void setOrderStatus(String orderStatus){
		this.orderStatus = orderStatus;
	}
}
