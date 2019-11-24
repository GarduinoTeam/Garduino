package com.jboss.resteasy.services;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.jboss.resteasy.beans.Device;
import com.jboss.resteasy.beans.Sensor;

public class SensorService {
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;
	
	public int createSensor(Sensor sensor){
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
				
					consulta="SELECT id from garduino.sensor order by id desc limit 1";		
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						id=(int)rs.getInt("id");
					}
					id=id+1;
					System.out.println("\nsql:"+consulta);
					System.out.println(strEstat);

					consulta="insert into garduino.sensor (id,typeid,deviceid,value,date) values ('"+id+"', '"+sensor.getSensorType()+"',"+sensor.getDeviceId()+",'"+sensor.getValue()+"','"+sensor.getDate()+"')";
					stm.executeUpdate(consulta);
					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return id;
	}
	public ArrayList<Sensor> getSensors(int device_id){
		ArrayList<Sensor> sensors=new ArrayList<>();
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return sensors; //Error Connecting The user
				}
				else {
					
					consulta="select * from garduino.sensor where deviceid='"+device_id+"'";
					
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						int id=rs.getInt("id");
						int sensorType=rs.getInt("typeid");
						int deviceId=device_id;
						int value=rs.getInt("value");
						Date date=rs.getTimestamp("date");
						Sensor sensor=new Sensor();
						sensor.setId(deviceId);
						sensor.setSensorType(sensorType);
						sensor.setDeviceId(deviceId);
						sensor.setValue(value);
						sensor.setDate(date);
						sensors.add(sensor);
					}

					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return sensors;
		}
		return sensors;
	}
	public int updateSensor(int id, Sensor sensor){
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
					int existSensor=0;
					consulta="SELECT COUNT(id) from garduino.sensor " +"where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existSensor=(int)rs.getLong("count");
					}
					if(existSensor==0){
						return -2;
					}

					consulta="update garduino.sensor set value = '" +sensor.getValue()+"', date='"+sensor.getDate()+"' "+"where id='"+id+"'";
					System.out.println("\n update:"+consulta);
					stm.executeUpdate(consulta);
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

}
