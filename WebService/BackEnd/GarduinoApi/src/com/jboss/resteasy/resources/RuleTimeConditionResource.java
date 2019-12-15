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

import com.jboss.resteasy.beans.Rule;
import com.jboss.resteasy.beans.RuleCondition;
import com.jboss.resteasy.beans.RuleTimeCondition;
import com.jboss.resteasy.services.RuleTimeConditionService;
@Path("/ruletimeconditions")
public class RuleTimeConditionResource {
	private RuleTimeConditionService myRuleTimeConditionService=new RuleTimeConditionService();
	@POST
	@Path("/create_rule_time_condition")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createRuleTimeCondition(RuleTimeCondition ruletimecondition){
		ruletimecondition.setStatus(false);
		
		int status=myRuleTimeConditionService.createRuleTimeCondition(ruletimecondition);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			ruletimecondition.setId(status);
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
		int status=myRuleTimeConditionService.deleteRuleTimeCondition(id);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else if(status==-2){
			return Response.status(Status.NO_CONTENT).build();
		}else{
			return Response.status(Status.OK).build();
		}
		
	}
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/update_rule_time_condition/{id}")
	public Response updateRuleTimeCondition(@PathParam("id") int id,RuleTimeCondition ruletimecondition){
		int status=myRuleTimeConditionService.updateRuleTimeCondition(id,ruletimecondition);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else if(status==-2){
			return Response.status(Status.NOT_FOUND).build();
		}else{
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
