package com.jboss.resteasy.services;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.jboss.resteasy.beans.Rule;
import com.jboss.resteasy.beans.Sensor;
import com.jboss.resteasy.beans.User;
public class RuleService {
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;
	
	public int createRule(Rule rule){
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
					int existRule=0;
					consulta="SELECT COUNT(id) from garduino.rule where name='"+rule.getName()+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existRule=(int)rs.getLong("count");
					}
					if(existRule!=0){
						return -2;
					}
					consulta="SELECT id from garduino.rule order by id desc limit 1";		
					strEstat="Connection Established";
					//connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						id=(int)rs.getInt("id");
					}
					id=id+1;
					System.out.println("\nsql:"+consulta);
					System.out.println(strEstat);

					consulta="insert into garduino.rule (id,id_device,status,name,type) values ('"+id+"', '"+rule.getIdDevice()+"',"+rule.getStatus()+",'"+rule.getName()+"','"+rule.getType()+"')";
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
	public int deleteRule(int id){
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
					int existRule=0;
					consulta="SELECT COUNT(id) from garduino.rule where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existRule=(int)rs.getLong("count");
					}
					if(existRule==0){
						return -2;
					}

					consulta="delete from garduino.rule where id='"+id+"'";
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
	public int updateRule(int id,Rule rule){
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
					int existRule=0;
					consulta="SELECT COUNT(id) from garduino.rule " +"where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existRule=(int)rs.getLong("count");
					}
					if(existRule==0){
						return -2;
					}

					consulta="update garduino.rule set status = '" +rule.getStatus()+"'" +" where id='"+id+"'";
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
	public ArrayList<Rule> getRules(int device_id){
		ArrayList<Rule> rules=new ArrayList<>();
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return rules; //Error Connecting The user
				}
				else {
					
					consulta="select * from garduino.rule where id_device='"+device_id+"'";
					
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						int id=rs.getInt("id");
						boolean status=rs.getBoolean("status");
						String name=rs.getString("name");
						int type=rs.getInt("type");
						int deviceId=device_id;
						Rule rule=new Rule();
						rule.setId(id);
						rule.setIdDevice(deviceId);
						rule.setStatus(status);
						rule.setName(name);
						rule.setType(type);
						rules.add(rule);
					}

					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return rules;
		}
		return rules;
	}
	public Rule getRule(int id){
		Rule rule=null;
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
					
					consulta="select * from garduino.rule where id ='"+id+"'"; 
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						boolean status=rs.getBoolean("status");
						String name=rs.getString("name");
						int type=rs.getInt("type");
						int deviceId=rs.getInt("id_device");
						rule=new Rule();
						rule.setId(id);
						rule.setIdDevice(deviceId);
						rule.setStatus(status);
						rule.setName(name);
						rule.setType(type);
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

		
		return rule;
	}
	

}
