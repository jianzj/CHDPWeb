package com.chdp.chdpweb.bean;

public class Process {

	private int id;
	private int process_type;
	private String begin;
	private String finish;
	private int user_id;
	private int prescription_id;
	private int error_type;
	private String error_msg;
	private int previous_process_id;
	private int machine_id;
		
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getProcess_type(){
		return process_type;
	}
	
	public void setProcess_type(int process_type){
		this.process_type = process_type;
	}
	
	public String getBegin(){
		return begin;
	}
	
	public void setBegin(String begin){
		this.begin = begin;
	}
	
	public String getFinish(){
		return finish;
	}
	
	public void setFinish(String finish){
		this.finish = finish;
	}
	
	public int getUser_id(){
		return user_id;
	}
	
	public void setUser_id(int user_id){
		this.user_id = user_id;
	}
	
	public int getPrescription_id(){
		return prescription_id;
	}
	
	public void setPrescription_id(int prescription_id){
		this.prescription_id = prescription_id;
	}
	
	public int getError_type(){
		return error_type;
	}
	
	public void setError_type(int error_type){
		this.error_type = error_type;
	}
	
	public String getError_msg(){
		return error_msg;
	}
	
	public void setError_msg(String error_msg){
		this.error_msg = error_msg;
	}
	
	public int getPrevious_process_id(){
		return previous_process_id;
	}
	
	public void setPrevious_process_id(int previous_process_id){
		this.previous_process_id = previous_process_id;
	}
	
	public int getMachine_id(){
		return machine_id;
	}
	
	public void setMachine_id(int machine_id){
		this.machine_id = machine_id;
	}

}
