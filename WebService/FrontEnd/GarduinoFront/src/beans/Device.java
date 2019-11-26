package beans;

import java.io.File;

import javax.ws.rs.FormParam;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class Device {
private int id;
	
	private String name;
	private String status;
	private File image;
	private String imageURL;
	private int userId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public File getImage() {
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public void setImage(File image) {
		this.image = image;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
