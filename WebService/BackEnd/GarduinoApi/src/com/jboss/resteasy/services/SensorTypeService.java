package com.jboss.resteasy.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.jboss.resteasy.beans.SensorType;
public class SensorTypeService {
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;
	public int createSensorType(SensorType sensorType){
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
					int existSensorType=0;
					consulta="SELECT COUNT(id) from garduino.sensor_type where name='"+sensorType.getName()+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existSensorType=(int)rs.getLong("count");
					}
					if(existSensorType!=0){
						return -2;
					}
					consulta="SELECT id from garduino.sensor_type order by id desc limit 1";		
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

					consulta="insert into garduino.sensor_type (id,name) values ('"+id+"', '"+sensorType.getName()+"')";
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

}
