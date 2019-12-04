package com.jboss.resteasy.services;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.jboss.resteasy.beans.Rule;
import com.jboss.resteasy.beans.RuleTimeCondition;
public class RuleTimeConditionService {
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;
	public int createRuleTimeCondition(RuleTimeCondition ruletimecondition){
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
			
					consulta="SELECT id from garduino.rule_time_condition order by id desc limit 1";		
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
					
					//consulta="insert into garduino.rule_time_condition (id,id_rule,status,start_time,end_time,months_of_the_year,days_of_week,specific_dates) values ('"+id+"', '"+ruletimecondition.getIdRule()+"','"+ruletimecondition.getStatus()+"','"+ruletimecondition.getStartTime()+"','"+ruletimecondition.getEndTime()+"','"+ruletimecondition.getMonthsOfTheYear()+"','"+ruletimecondition.getDaysOfWeek()+"','"+ruletimecondition.getSpecificDates()+"')";
					//System.out.println("\nsql:"+consulta);
					consulta="insert into garduino.rule_time_condition (id,id_rule,status,start_time,end_time,months_of_the_year,days_of_week,specific_dates) values (?,?,?,?,?,?,?,?)";
					PreparedStatement pst=connection.prepareStatement(consulta);
					Array dateArray=connection.createArrayOf("date", ruletimecondition.getSpecificDates());
					pst.setInt(1, id);
					pst.setInt(2, ruletimecondition.getIdRule());
					pst.setBoolean(3,ruletimecondition.getStatus());
					pst.setDate(4, ruletimecondition.getStartTime());
					pst.setDate(5, ruletimecondition.getEndTime());
					pst.setString(6, ruletimecondition.getMonthsOfTheYear());
					pst.setString(7, ruletimecondition.getDaysOfWeek());
					pst.setArray(8, dateArray);
					pst.executeUpdate();
					//stm.executeUpdate(consulta);
					
					//System.out.println("\nfora:");
				
					connection.close();
					pst.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return id;
	}
	public int deleteRuleTimeCondition(int id){
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
					int existRuleTimeCondition=0;
					consulta="SELECT COUNT(id) from garduino.rule_time_condition where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existRuleTimeCondition=(int)rs.getLong("count");
					}
					if(existRuleTimeCondition==0){
						return -2;
					}

					consulta="delete from garduino.rule_time_condition where id='"+id+"'";
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
	public int updateRuleTimeCondition(int id,RuleTimeCondition ruletimecondition){
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
					int existRuleTimeCondition=0;
					consulta="SELECT COUNT(id) from garduino.rule_time_condition " +"where id='"+id+"'";
					strEstat="Connection Established";
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						existRuleTimeCondition=(int)rs.getLong("count");
					}
					if(existRuleTimeCondition==0){
						return -2;
					}

					//consulta="update garduino.rule set status = '" +rule.getStatus()+"'" +" where id='"+id+"'";
					consulta="update garduino.rule_time_condition set status = ?, months_of_the_year = ?, days_of_week= ?, start_time = ?, end_time = ?, specific_dates= ? where id = ?";
					Array dateArray=connection.createArrayOf("date", ruletimecondition.getSpecificDates());
					PreparedStatement pst=connection.prepareStatement(consulta);
					System.out.println(ruletimecondition.getStatus());
					System.out.println(ruletimecondition.getMonthsOfTheYear());
					pst.setBoolean(1, ruletimecondition.getStatus());
					pst.setString(2, ruletimecondition.getMonthsOfTheYear());
					pst.setString(3, ruletimecondition.getDaysOfWeek());
					pst.setDate(4, ruletimecondition.getStartTime());
					pst.setDate(5, ruletimecondition.getEndTime());
					pst.setArray(6, dateArray);
					pst.setInt(7, id);
					//stm.executeUpdate(consulta);
					int row=pst.executeUpdate();

					
					System.out.println(row);
				
					connection.close();
					pst.close();
					stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	public ArrayList<RuleTimeCondition> getRuleTimeConditions(int rule_id){
		ArrayList<RuleTimeCondition> ruletimeconditions=new ArrayList<>();
		try {
			ctx= new InitialContext();
			if(ctx!=null) {
				ds=(DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds==null){
					strEstat="Getting Error in Connection";
					System.out.println(strEstat);
					return ruletimeconditions; //Error Connecting The user
				}
				else {
					
					consulta="select * from garduino.rule_time_condition where id_rule='"+rule_id+"'";
					
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						int id=rs.getInt("id");
						int idRule=rule_id;
						boolean status=rs.getBoolean("status");
						String monthsOfTheYear=rs.getString("months_of_the_year");
						String daysOfWeek=rs.getString("days_of_week");
						Array array=rs.getArray("specific_dates");
						Date[] specificDates=(Date[])array.getArray();
						Date start_time=rs.getDate("start_time");
						Date end_time=rs.getDate("end_time");
						RuleTimeCondition ruletimecondition=new RuleTimeCondition();
						ruletimecondition.setId(id);
						ruletimecondition.setIdRule(idRule);
						ruletimecondition.setStatus(status);
						ruletimecondition.setMonthsOfTheYear(monthsOfTheYear);
						ruletimecondition.setDaysOfWeek(daysOfWeek);
						ruletimecondition.setSpecificDates(specificDates);
						ruletimecondition.setStartTime(start_time);
						ruletimecondition.setEndTime(end_time);
						
						ruletimeconditions.add(ruletimecondition);
					}

					
					//System.out.println("\nfora:");
				
					connection.close();
					stm.close();
					//stm.close();				
					}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return ruletimeconditions;
		}
		return ruletimeconditions;
	}
	public RuleTimeCondition getRuleTimeCondition(int id){
		RuleTimeCondition ruletimecondition=null;
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
					
					consulta="select * from garduino.rule_time_condition where id ='"+id+"'"; 
					System.out.println(consulta);
					connection=ds.getConnection();
					stm= connection.createStatement();
					rs=stm.executeQuery(consulta);
					while(rs.next()){
						int idRule=rs.getInt("id_rule");
						boolean status=rs.getBoolean("status");
						String monthsOfTheYear=rs.getString("months_of_the_year");
						String daysOfWeek=rs.getString("days_of_week");
						Array array=rs.getArray("specific_dates");
						Date[] specificDates=(Date[])array.getArray();
						Date start_time=rs.getDate("start_time");
						Date end_time=rs.getDate("end_time");
						ruletimecondition=new RuleTimeCondition();
						ruletimecondition.setId(id);
						ruletimecondition.setIdRule(idRule);
						ruletimecondition.setStatus(status);
						ruletimecondition.setMonthsOfTheYear(monthsOfTheYear);
						ruletimecondition.setDaysOfWeek(daysOfWeek);
						ruletimecondition.setSpecificDates(specificDates);
						ruletimecondition.setStartTime(start_time);
						ruletimecondition.setEndTime(end_time);
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

		
		return ruletimecondition;
	}

}
