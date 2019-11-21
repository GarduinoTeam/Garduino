package com.jboss.resteasy.beans;

import javax.ws.rs.FormParam;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class Device {
	private String name;
	private String status;
	private byte[] image;
	private int userId;

	public byte[] getImage() {
		return image;
	}
	public String getName() {
		return name;
	}
	public String getStatus() {
		return status;
	}
	public int getUserId() {
		return userId;
	}

	@FormParam("image")
	@PartType("application/octet-stream")
	public void setImage(byte[] image) {
		this.image = image;
	}
	@FormParam("name")
	@PartType("application/json")
	public void setName(String name) {
		this.name = name;
	}
	@FormParam("status")
	@PartType("application/json")
	public void setStatus(String status) {
		this.status = status;
	}
	@FormParam("user_id")
	@PartType("application/json")
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
