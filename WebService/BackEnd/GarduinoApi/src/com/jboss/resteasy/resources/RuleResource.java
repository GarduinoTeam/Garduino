package com.jboss.resteasy.resources;
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
import com.jboss.resteasy.beans.Sensor;
import com.jboss.resteasy.beans.User;
import com.jboss.resteasy.services.RuleService;

@Path("/rules")
public class RuleResource {
	private RuleService myRuleService=new RuleService();
	@POST
	@Path("/create_rule")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createRule(Rule rule){
		rule.setStatus(false);
		int status=myRuleService.createRule(rule);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			rule.setId(status);
			return Response
					.status(Status.CREATED)
					.build();
		}
		
	}
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/delete_rule/{id}")
	public Response deleteRule(@PathParam("id") int id){
		int status=myRuleService.deleteRule(id);
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
	@Path("/update_rule/{id}")
	public Response updateRule(@PathParam("id") int id,Rule rule){
		int status=myRuleService.updateRule(id,rule);
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
	@Path("/get_rules")
	public Response getRules(@QueryParam("device_id") int device_id){
		if(device_id==0){
			return Response
					.status(Status.BAD_REQUEST)
					.build();
		}else{
			List<Rule> rules=myRuleService.getRules(device_id);
			return Response
					.status(Status.OK)
					.entity(rules)
					.build();
		}
		
	}
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_rule/{id}")
	public Response getRule(@PathParam("id") int id){
		Rule rule=myRuleService.getRule(id);
		if(rule==null){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else{
			return Response
					.status(Status.OK)
					.entity(rule)
					.build();

		}
		
	}

	
}
