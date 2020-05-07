package com.jboss.resteasy.beans;

public class Condition 
{
	private int id;
	private String name;
	private String measure;
	
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
	
	public String getMeasure(){
		return measure;
	}
	
	public void setMeasure(String measure){
		this.measure = measure;
	}
}
