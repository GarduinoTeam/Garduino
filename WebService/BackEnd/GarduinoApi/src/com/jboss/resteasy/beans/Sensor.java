package com.jboss.resteasy.beans;

import java.util.Date;

public class Sensor 
{
	private int id;
	private int sensorType;
	private int deviceId;
	private int value;
	private Date date;


	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getSensorType(){
		return sensorType;
	}
	
	public void setSensorType(int sensorType){
		this.sensorType = sensorType;
	}
	
	public int getDeviceId(){
		return deviceId;
	}
	
	public void setDeviceId(int deviceId){
		this.deviceId = deviceId;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public Date getDate(){
		return date;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
}
