package com.jboss.resteasy.beans;

public class Rule 
{
	private int id;
	private int idDevice;
	private boolean status;
	private String name;
	private int type;

	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getIdDevice(){
		return idDevice;
	}
	
	public void setIdDevice(int idDevice){
		this.idDevice = idDevice;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getType(){
		return type;
	}
	
	public void setType(int type){
		this.type = type;
	}
}