package com.jboss.resteasy.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.jboss.resteasy.beans.User;
public class UserService {
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;
	public int createUser(User user){
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
					int existUser=0;
					consulta="SELECT COUNT(id) from garduino.user where username='"+user.getUsername()+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existUser=(int)rs.getLong("count");
					}
					if(existUser!=0){
						return -2;
					}
					existUser=0;
					consulta="SELECT COUNT(id) from garduino.user where email='"+user.getEmail()+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existUser=(int)rs.getLong("count");
					}
					if(existUser!=0){
						return -2;
					}
					consulta="SELECT id from garduino.user order by id desc limit 1";		
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

					consulta="insert into garduino.user (username,password,id,email,phone,admin) values ('"+user.getUsername()+"', '"+user.getPassword()+"',"+id+",'"+user.getEmail()+"','"+user.getPhone()+"','"+user.getAdmin()+"')";
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
	public int deleteUser(int id){
		
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
					int existUser=0;
					consulta="SELECT COUNT(id) from garduino.user where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existUser=(int)rs.getLong("count");
					}
					if(existUser==0){
						return -2;
					}

					consulta="delete from garduino.user where id='"+id+"'";
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
	public int updateUser(int id,User user){
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
					int existUser=0;
					consulta="SELECT COUNT(id) from garduino.user " +"where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existUser=(int)rs.getLong("count");
					}
					if(existUser==0){
						return -2;
					}

					consulta="update garduino.user set password = '" +user.getPassword()+"', email = '"+user.getEmail()+"', phone =  '"+user.getPhone()+"'"+ ", admin= '"+user.getAdmin()+"'" +" where id='"+id+"'";
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
	public ArrayList<User> getUsers(){
		ArrayList<User> users=new ArrayList<>();
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return users; //Error Connecting The user
				}
				else {
					
					consulta="select * from garduino.user"; 
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						//System.out.println("Inside getUsers loop");
						String username=rs.getString("username");
						//System.out.println(username);
						String password=rs.getString("password");
						//System.out.println(password);
						int id=rs.getInt("id");
						//.out.println(id);
						String email=rs.getString("email");
						//System.out.println(email);
						String phone= rs.getString("phone");
						//System.out.println(phone);
						int admin=rs.getInt("admin");
						User user=new User();
						user.setId(id);
						user.setUsername(username);
						user.setPassword(password);
						user.setEmail(email);
						user.setPhone(phone);
						user.setAdmin(admin);
						users.add(user);
					}

					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return users;
		}
		return users;
	}
	public User getUser(int id){
		User user=null;
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
					
					consulta="select * from garduino.user where id ='"+id+"'"; 
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						//System.out.println("Inside getUsers loop");
						String username=rs.getString("username");
						//System.out.println(username);
						String password=rs.getString("password");
						//System.out.println(password);
						//.out.println(id);
						String email=rs.getString("email");
						//System.out.println(email);
						String phone= rs.getString("phone");
						//System.out.println(phone);
						int admin= rs.getInt("admin");
						user=new User();
						user.setId(id);
						user.setUsername(username);
						user.setPassword(password);
						user.setEmail(email);
						user.setPhone(phone);
						user.setAdmin(admin);
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

		
		return user;
	}
}
