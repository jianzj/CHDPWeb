package com.chdp.chdpweb.bean;

public class Machine {

	private int id;
	private String uuid;
	private int type;
	private String name;
	private int pour_machine_id;
	private String pour_machine_name;
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getPour_machine_id() {
		return pour_machine_id;
	}

	public void setPour_machine_id(int pour_machine_id) {
		this.pour_machine_id = pour_machine_id;
	}

	public String getPour_machine_name() {
		return pour_machine_name;
	}

	public void setPour_machine_name(String pour_machine_name) {
		this.pour_machine_name = pour_machine_name;
	}

}
