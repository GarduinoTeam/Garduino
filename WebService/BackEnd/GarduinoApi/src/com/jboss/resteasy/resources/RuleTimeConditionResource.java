package com.jboss.resteasy.resources;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import com.jboss.resteasy.beans.Rule;
import com.jboss.resteasy.beans.RuleCondition;
import com.jboss.resteasy.beans.RuleTimeCondition;
import com.jboss.resteasy.services.OperationService;
import com.jboss.resteasy.services.RuleService;
import com.jboss.resteasy.services.RuleTimeConditionService;
@Path("/ruletimeconditions")
public class RuleTimeConditionResource {
	private RuleTimeConditionService myRuleTimeConditionService=new RuleTimeConditionService();
	private RuleService myRuleService= new RuleService();
	@POST
	@Path("/create_rule_time_condition")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createRuleTimeCondition(RuleTimeCondition ruletimecondition){
		OperationService myOperationService= new OperationService();
		ruletimecondition.setStatus(false);
		
		int status=myRuleTimeConditionService.createRuleTimeCondition(ruletimecondition);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			ruletimecondition.setId(status);
			Rule rule=myRuleService.getRule(ruletimecondition.getIdRule());
			try{
				JSONObject json=new JSONObject();
				json.put("operation", "create_rule");
		        json.put("device_id", String.valueOf(rule.getIdDevice()));
		        json.put("rule_type", "1");
		        json.put("rule_id", String.valueOf(rule.getId()));
		        json.put("rule_time_condition_id", String.valueOf(status));
		        json.put("start_time", ruletimecondition.getStartTime());
		        json.put("end_time", ruletimecondition.getEndTime());
		        json.put("weeks", ruletimecondition.getDaysOfWeek());
		        json.put("months", ruletimecondition.getMonthsOfTheYear());
		        json.put("specific_dates", ruletimecondition.getSpecificDates());
				myOperationService.sendRequest(json);
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			
			return Response
					.status(Status.CREATED)
					.build();
		}
	}
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/delete_rule_time_condition/{id}")
	public Response deleteRuleCondition(@PathParam("id") int id){
		RuleTimeCondition ruleTimeCondition=myRuleTimeConditionService.getRuleTimeCondition(id);
		int status=myRuleTimeConditionService.deleteRuleTimeCondition(id);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else if(status==-2){
			return Response.status(Status.NO_CONTENT).build();
		}else{
			OperationService myOperationService= new OperationService();
			Rule rule=myRuleService.getRule(ruleTimeCondition.getIdRule());
			try{
				JSONObject json=new JSONObject();
				json.put("operation", "delete_rule_time_condition");
		        json.put("device_id", String.valueOf(rule.getIdDevice()));
		        json.put("rule_type", "1");
		        json.put("rule_id", String.valueOf(rule.getId()));
		        json.put("rule_time_condition_id", String.valueOf(ruleTimeCondition.getId()));
		        
				myOperationService.sendRequest(json);
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			return Response.status(Status.OK).build();
		}
		
	}
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/update_rule_time_condition/{id}")
	public Response updateRuleTimeCondition(@PathParam("id") int id,RuleTimeCondition ruletimecondition){
		RuleTimeCondition ruleTimeCondition=myRuleTimeConditionService.getRuleTimeCondition(id);
		Rule rule=myRuleService.getRule(ruleTimeCondition.getIdRule());
		int status=myRuleTimeConditionService.updateRuleTimeCondition(id,ruletimecondition);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else if(status==-2){
			return Response.status(Status.NOT_FOUND).build();
		}else{
			OperationService myOperationService= new OperationService();
			try{
				JSONObject json=new JSONObject();
				json.put("operation", "create_rule");
		        json.put("device_id", String.valueOf(rule.getIdDevice()));
		        json.put("rule_type", "1");
		        json.put("rule_id", String.valueOf(rule.getId()));
		        json.put("rule_time_condition_id", String.valueOf(ruleTimeCondition.getId()));
		        json.put("start_time", ruletimecondition.getStartTime());
		        json.put("end_time", ruletimecondition.getEndTime());
		        json.put("weeks", ruletimecondition.getDaysOfWeek());
		        json.put("months", ruletimecondition.getMonthsOfTheYear());
		        json.put("specific_dates", ruletimecondition.getSpecificDates());
				myOperationService.sendRequest(json);
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			return Response.status(Status.OK).build();
		}
		
	}
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_rule_time_conditions")
	public Response getRuleTimeConditions(@QueryParam("rule_id") int rule_id){
		if(rule_id==0){
			return Response
					.status(Status.BAD_REQUEST)
					.build();
		}else{
			HashMap<String, List<RuleTimeCondition>> hashMapRuleTimeConditions=new HashMap<>();
			List<RuleTimeCondition> ruletimeconditions=myRuleTimeConditionService.getRuleTimeConditions(rule_id);
			hashMapRuleTimeConditions.put("ruletimeconditions", ruletimeconditions);
			return Response
					.status(Status.OK)
					.entity(hashMapRuleTimeConditions)
					.build();
		}
		
	}
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_rule_time_condition/{id}")
	public Response getRuleTimeCondition(@PathParam("id") int id){
		RuleTimeCondition ruletimecondition=myRuleTimeConditionService.getRuleTimeCondition(id);
		if(ruletimecondition==null){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else{
			return Response
					.status(Status.OK)
					.entity(ruletimecondition)
					.build();

		}
		
	}
	

}
