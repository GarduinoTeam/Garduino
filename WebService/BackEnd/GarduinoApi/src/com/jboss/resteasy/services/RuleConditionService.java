package com.jboss.resteasy.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.jboss.resteasy.beans.RuleCondition;


public class RuleConditionService 
{
	private static Statement stm;
	private static Connection connection;
	private static String consulta;
	private static DataSource ds;
	private static InitialContext ctx;
	private static String strEstat;
	private static ResultSet rs;

	
	public int createRuleCondition(RuleCondition rulecondition)
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
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else 
				{
					consulta = "SELECT id from garduino.rule_condition order by id desc limit 1";		
					strEstat = "Connection Established";
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					while(rs.next()){
						id = (int)rs.getInt("id");
					}
					id += 1;
					System.out.println("\nsql:"+consulta);
					System.out.println(strEstat);

					consulta = "insert into garduino.rule_condition (id,id_rule,id_condition,condition_value,status) values ('" + id + "', '" + rulecondition.getIdRule() + "'," + rulecondition.getIdCondition() + ",'" + rulecondition.getConditionValue()+"','"+rulecondition.getStatus()+"')";
					stm.executeUpdate(consulta);
					
					connection.close();
					stm.close();			
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return id;
	}
	
	
	public int deleteRuleCondition(int id)
	{
		try 
		{
			ctx = new InitialContext();
			if(ctx != null) {
				ds = (DataSource) ctx.lookup("java:jboss/PostgresXA");
				if(ds == null)
				{
					strEstat = "Getting Error in Connection";
					System.out.println(strEstat);
					return -1; //Error Connecting The user
				}
				else 
				{
					int existRuleCondition = 0;
					consulta = "SELECT COUNT(id) from garduino.rule_condition where id='" + id + "'";
					strEstat = "Connection Established";
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					while(rs.next()){
						existRuleCondition = (int)rs.getLong("count");
					}
					if(existRuleCondition == 0){
						return -2;
					}

					consulta = "delete from garduino.rule_condition where id='" + id + "'";
					stm.executeUpdate(consulta);
				
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
	
	
	public int updateRuleCondition(int id,RuleCondition rulecondition)
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
					int existRuleCondition = 0;
					consulta = "SELECT COUNT(id) from garduino.rule_condition where id='" + id + "'";
					strEstat = "Connection Established";
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					while(rs.next()){
						existRuleCondition = (int)rs.getLong("count");
					}
					if(existRuleCondition == 0){
						return -2;
					}

					consulta = "update garduino.rule_condition set status = '" +rulecondition.getStatus()+"', condition_value= '"+rulecondition.getConditionValue()+"'" +" where id='"+id+"'";
					System.out.println("\n update:"+consulta);
					stm.executeUpdate(consulta);

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
	
	
	public ArrayList<RuleCondition> getRuleConditions(int rule_id)
	{
		ArrayList<RuleCondition> ruleconditions = new ArrayList<>();
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
					return ruleconditions; //Error Connecting The user
				}
				else 
				{	
					consulta = "select * from garduino.rule_condition where id_rule='" + rule_id + "'";
					
					System.out.println(consulta);
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					while(rs.next())
					{
						int id = rs.getInt("id");
						int id_rule = rs.getInt("id_rule");
						int id_condition = rs.getInt("id_condition");
						int conditionValue = rs.getInt("condition_value");
						boolean status = rs.getBoolean("status");
						RuleCondition rulecondition = new RuleCondition();
						rulecondition.setId(id);
						rulecondition.setIdRule(id_rule);
						rulecondition.setIdCondition(id_condition);
						rulecondition.setConditionValue(conditionValue);
						rulecondition.setStatus(status);
						ruleconditions.add(rulecondition);
					}
					
					connection.close();
					stm.close();
				}
			}			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return ruleconditions;
		}
		
		return ruleconditions;
	}

	
	public RuleCondition getRuleCondition(int id)
	{
		RuleCondition rulecondition = null;
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
					consulta = "select * from garduino.rule_condition where id ='" + id + "'"; 
					System.out.println(consulta);
					connection = ds.getConnection();
					stm = connection.createStatement();
					rs = stm.executeQuery(consulta);
					while(rs.next())
					{
						int id_rule = rs.getInt("id_rule");
						int id_condition = rs.getInt("id_condition");
						int conditionValue = rs.getInt("condition_value");
						boolean status = rs.getBoolean("status");
						rulecondition = new RuleCondition();
						rulecondition.setId(id);
						rulecondition.setIdRule(id_rule);
						rulecondition.setIdCondition(id_condition);
						rulecondition.setConditionValue(conditionValue);
						rulecondition.setStatus(status);
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

		return rulecondition;
	}
}
