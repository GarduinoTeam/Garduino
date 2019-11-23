package com.jboss.resteasy.services;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.jboss.resteasy.beans.Device;
import com.jboss.resteasy.beans.User;
public class DeviceService {
	private String serverHome="C:\\Users\\joanpau\\Desktop\\Servidor Web";
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;
	public int createDevice(Device device){
		int id=0;
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else {
					connection=ds.getConnection();
					int existDevice=0;
					consulta="SELECT COUNT(id) from garduino.device where name='"+device.getName()+"' and user_id='"+device.getUserId()+"'" ;
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existDevice=(int)rs.getLong("count");
					}
					if(existDevice!=0){
						return -2;
					}
					consulta="SELECT id from garduino.device order by id desc limit 1";		
					strEstat="Connection Established";
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					
					while(rs.next()){
						id=(int)rs.getInt("id");
					}
					id=id+1;
					System.out.println("\nsql:"+consulta);
					System.out.println(strEstat);

					consulta="insert into garduino.device (name,status,id,user_id) values ('"+device.getName()+"', '"+device.getStatus()+"',"+id+",'"+device.getUserId()+"')";
					System.out.println("\nsql:"+consulta);
					stm.executeUpdate(consulta);
					String fileName=serverHome+"\\"+device.getUserId()+"\\"+device.getName()+".png";
					try {
						writeFile(device.getImage(), fileName);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
			
		}
		return id;
	}
	public int deleteDevice(int id){
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else {
					int existDevice=0;
					String deviceName="";
					int user_id=0;
					consulta="SELECT * from garduino.device where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existDevice=1;
						deviceName=rs.getString("name");
						user_id=rs.getInt("user_id");
					}
					if(existDevice==0){
						return -2;
					}

					consulta="delete from garduino.device where id='"+id+"'";
					stm.executeUpdate(consulta);
					
					String newPath=serverHome+"\\"+user_id+"\\"+deviceName+".png";
					File file = new File(newPath);
			        if (file.exists()) {
			        	System.out.println(newPath);
			           file.delete();
			           System.out.println("File deleted");
			        }
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	public int updateDevice(int id,Device device){
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else {
					int existDevice=0;
					consulta="SELECT COUNT(id) from garduino.device " +"where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existDevice=(int)rs.getLong("count");
					}
					if(existDevice==0){
						return -2;
					}

					consulta="update garduino.device set status = '" +device.getStatus()+"'"+"where id='"+id+"'";
					System.out.println("\n update:"+consulta);
					stm.executeUpdate(consulta);

					String fileName=serverHome+"\\"+device.getUserId()+"\\"+device.getName()+".png";
					try {
						writeFile(device.getImage(), fileName);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
		
	}
	public ArrayList<Device> getDevices(int user_id){
		ArrayList<Device> devices=new ArrayList<>();
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return devices; //Error Connecting The user
				}
				else {
					if(user_id==0){
						consulta="select * from garduino.device";
					}else if(user_id>0){
						consulta="select * from garduino.device where user_id='"+user_id+"'";
					}
					 
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						String name=rs.getString("name");
						int id=rs.getInt("id");
						user_id=rs.getInt("user_id");
						String status=rs.getString("status");
						String fileName=serverHome+"\\"+user_id+"\\"+name+".png";
						Device device=new Device();
						device.setId(id);
						device.setName(name);
						device.setStatus(status);
						device.setImage(new byte[0]);
						device.setImageURL("http://localhost:8080/GarduinoApi/devices/images/"+user_id+"/"+name);
						device.setUserId(user_id);
						devices.add(device);
					}

					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return devices;
		}
		return devices;
		
	}
	public Device getDevice(int id){
		Device device=null;
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return null; //Error Connecting The user
				}
				else {
					
					consulta="select * from garduino.device where id ='"+id+"'"; 
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						//System.out.println("Inside getUsers loop");
						String name=rs.getString("name");
						//int id=rs.getInt("id");
						int user_id=rs.getInt("user_id");
						String status=rs.getString("status");
						String fileName=serverHome+"\\"+user_id+"\\"+name+".png";
						device=new Device();
						device.setId(id);
						device.setName(name);
						device.setStatus(status);
						device.setImage(new byte[0]);
						device.setImageURL("http://localhost:8080/GarduinoApi/devices/images/"+user_id+"/"+name);
						device.setUserId(user_id);
					}

					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		
		return device;
	}
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
	/*private byte[] readFile(String filename) throws IOException {

		File file = new File(filename);
		byte [] image=null;
		if (file.exists()) {
			image = Files.readAllBytes(file.toPath());
			
			
		}
		return image;

		

	}*/

	
	
	

}
