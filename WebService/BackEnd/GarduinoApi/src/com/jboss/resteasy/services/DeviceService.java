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


public class DeviceService 
{
	private String serverHome = "/home/rochi/Escritorio/github/projects/Garduino/WebService/BackEnd";
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;
	
	
	public int createDevice(Device device)
	{
		int id = 0;
		try 
		{
			ctx = new InitialContext();
			if(ctx != null)
			{
				ds = (DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds == null)
				{
					strEstat = "Getting Error in Connection";
					System.out.println("strEstat: " + strEstat);
					return -1; //Error Connecting The user
				}
				else 
				{
					connection = ds.getConnection();
					int existDevice = 0;
					consulta = "SELECT COUNT(id) from garduino.device where name='" + device.getName() + "' and user_id='" + device.getUserId() + "'";
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					
					while(rs.next()){
						existDevice = (int)rs.getLong("count");
					}
					
					if(existDevice != 0){
						return -2;
					}
					
					consulta = "SELECT id from garduino.device order by id desc limit 1";		
					strEstat = "Connection Established";
					System.out.println("\nsql:" + consulta + "\n" + strEstat);
					
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);				
					while(rs.next()){
						id = (int)rs.getInt("id");
					}					
					id += 1;

					consulta = "insert into garduino.device (name,status,id,user_id) values ('" + device.getName() + "', '" + device.getStatus() + "'," + id+",'" + device.getUserId() + "')";
					System.out.println("\nsql:" + consulta);
					stm.executeUpdate(consulta);
					String fileName = serverHome + "/" + device.getUserId() + "/" + device.getName() + ".png";
					try{
						writeFile(device.getImage(), fileName);
					} 
					catch(IOException e){					
						e.printStackTrace();
					}
				
					connection.close();
					stm.close();				
				}				
			}			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return -1;			
		}
		
		return id;
	}
	
	
	public int deleteDevice(int id)
	{
		try 
		{
			ctx = new InitialContext();
			if(ctx != null) 
			{
				ds = (DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds == null)
				{
					strEstat = "Getting Error in Connection";
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else 
				{
					int existDevice = 0;
					int user_id = 0;
					String deviceName = "";
					
					consulta = "SELECT * from garduino.device where id='" + id + "'";
					strEstat = "Connection Established";
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					
					while(rs.next())
					{
						existDevice = 1;
						deviceName = rs.getString("name");
						user_id = rs.getInt("user_id");
					}
					
					if(existDevice == 0){
						return -2;
					}

					consulta = "delete from garduino.device where id='" + id + "'";
					stm.executeUpdate(consulta);
					
					String newPath = serverHome + "/" + user_id + "/" + deviceName + ".png";
					File file = new File(newPath);
					
			        if(file.exists()) 
			        {
			        	file.delete();
			        	System.out.println("File " + newPath + " deleted");
			        }
			        else{
			        	System.out.println("Cannot delete " + newPath + " file");
			        }
		
					connection.close();
					stm.close();			
				}			
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	
	public int updateDevice(int id, Device device)
	{
		try 
		{
			ctx = new InitialContext();
			if(ctx != null) 
			{
				ds = (DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds == null)
				{
					strEstat = "Getting Error in Connection";
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else 
				{
					consulta = "SELECT * from garduino.device where id='" + id + "'";
					strEstat = "Connection Established";
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					
					String storedName = null;
					while(rs.next()){
						storedName = rs.getString("name");	
					}
					
					if(storedName == null){
						return -2;
					}

					consulta = 	"update garduino.device set status = '" + device.getStatus() + "'," + "name='" + device.getName() + "'" + "where id='" + id + "'";					
					System.out.println("\n update:" + consulta);					
					stm.executeUpdate(consulta);
					
					String storedFileName = serverHome + "/" + device.getUserId() + "/" + storedName + ".png";
					String fileName = serverHome + "/" + device.getUserId() + "/" + device.getName() + ".png";					
					File file = new File(storedFileName);
			        if(file.exists()) 
			        {
				        file.delete();
				        System.out.println("File " + storedFileName + " deleted");
			        }
			        
					try{
						writeFile(device.getImage(), fileName);
					} 
					catch(IOException e){	
						e.printStackTrace();
					}
				
					connection.close();
					stm.close();		
				}				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		
		return 0;		
	}
	
	
	public int updateDeviceAndroid(int id,Device device)
	{
		try 
		{
			ctx = new InitialContext();
			if(ctx != null) 
			{
				ds = (DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds == null)
				{
					strEstat = "Getting Error in Connection";
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else
				{
					consulta = "SELECT * from garduino.device where id='" + id + "'";
					strEstat = "Connection Established";
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					
					String storedName = null;
					while(rs.next()){
						storedName = rs.getString("name");						
					}
					
					if(storedName == null){
						return -2;
					}

					consulta = "update garduino.device set status = '" + device.getStatus() + "', name='" + device.getName() + "' where id='" + id + "'";
					System.out.println("\n update:" + consulta);
					stm.executeUpdate(consulta);
					
					String storedFileName = serverHome + "/" + device.getUserId() + "/" + storedName + ".png";
					String fileName = serverHome + "/" + device.getUserId() + "/" + device.getName() + ".png";
					File file = new File(storedFileName);
					File newFile = new File(fileName);
			        if(file.exists())
			        {
			        	System.out.println(storedFileName);
			        	if(file.renameTo(newFile)){
			        		System.out.println("File Name updated to " + newFile);
			           }			           
			        }
			        
					connection.close();
					stm.close();								
				}				
			}			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	
	public ArrayList<Device> getDevices(int user_id)
	{
		ArrayList<Device> devices = new ArrayList<>();
		try 
		{
			ctx = new InitialContext();
			if(ctx != null) 
			{
				ds = (DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds == null)
				{
					strEstat = "Getting Error in Connection";
					System.out.println(strEstat);
					return devices; //Error Connecting The user
				}
				else 
				{
					if(user_id == 0)
						consulta="select * from garduino.device";
					else if(user_id > 0)
						consulta = "select * from garduino.device where user_id='" + user_id + "'";
					 
					System.out.println(consulta);
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					while(rs.next())
					{
						String name = rs.getString("name");
						int id = rs.getInt("id");
						user_id = rs.getInt("user_id");
						String status = rs.getString("status");
						Device device = new Device();
						device.setId(id);
						device.setName(name);
						device.setStatus(status);
						device.setImage(new byte[0]);
						device.setImageURL("http://localhost:8080/GarduinoApi/devices/images/" + user_id + "/" + name);
						device.setImageAndroidURL("http://10.0.2.2:8080/GarduinoApi/devices/images/" + user_id + "/" + name);
						device.setUserId(user_id);
						devices.add(device);
					}			
				
					connection.close();
					stm.close();				
				}
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return devices;
		}
		
		return devices;
	}
	
	
	public Device getDevice(int id)
	{
		Device device = null;
		try 
		{
			ctx = new InitialContext();
			if(ctx != null) 
			{
				ds = (DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds == null)
				{
					strEstat = "Getting Error in Connection";
					System.out.println(strEstat);
					return null; //Error Connecting The user
				}
				else 
				{			
					consulta = "select * from garduino.device where id ='" + id + "'"; 
					System.out.println(consulta);
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					while(rs.next())
					{
						String name = rs.getString("name");
						int user_id = rs.getInt("user_id");
						String status = rs.getString("status");
						device=new Device();
						device.setId(id);
						device.setName(name);
						device.setStatus(status);
						device.setImage(new byte[0]);
						device.setImageURL("http://localhost:8080/GarduinoApi/devices/images/" + user_id + "/" + name);
						device.setImageAndroidURL("http://10.0.2.2:8080/GarduinoApi/devices/images/" + user_id + "/" + name);
						device.setUserId(user_id);
					}
	
					connection.close();
					stm.close();
				}				
			}			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return device;
	}
	
	
	private void writeFile(byte[] content, String filename) throws IOException 
	{
		File file = new File(filename);
		if(!file.exists()){
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();
	}
}
