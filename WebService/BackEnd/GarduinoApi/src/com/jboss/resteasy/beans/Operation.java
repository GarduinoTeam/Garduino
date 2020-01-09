package com.jboss.resteasy.beans;

public class Operation {
	private String name;
	private int device_id;
	private int irrigationTime;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public int getIrrigationTime() {
		return irrigationTime;
	}
	public void setIrrigationTime(int irrigationTime) {
		this.irrigationTime = irrigationTime;
	}

}
