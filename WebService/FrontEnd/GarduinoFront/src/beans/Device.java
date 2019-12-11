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
	private String Temperature;
	private String Humidity;
	private String Soil;
	private String imageAndroidURL;
	public String getImageAndroidURL() {
		return imageAndroidURL;
	}
	public void setImageAndroidURL(String imageAndroidURL) {
		this.imageAndroidURL = imageAndroidURL;
	}
	public String getTemperature() {
		return Temperature;
	}
	public void setTemperature(String temperature) {
		Temperature = temperature;
	}
	public String getHumidity() {
		return Humidity;
	}
	public void setHumidity(String humidity) {
		Humidity = humidity;
	}
	public String getSoil() {
		return Soil;
	}
	public void setSoil(String soil) {
		Soil = soil;
	}
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
