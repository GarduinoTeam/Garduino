package com.jboss.resteasy.resources;
import java.util.List;

//import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.jboss.resteasy.beans.Condition;
import com.jboss.resteasy.services.ConditionService;

@Path("/conditions")
public class ConditionResource {
	private ConditionService conditionService=new ConditionService();
	@POST
	@Path("/create_condition")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createCondition(Condition condition){
		int status=conditionService.createCondition(condition);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			condition.setId(status);
			return Response
					.status(Status.CREATED)
					.entity(condition)
					.build();
		}
	}
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/delete_condition/{id}")
	public Response deleteCondition(@PathParam("id") int id){
		int status=conditionService.deleteCondition(id);
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
	@Path("/update_condition/{id}")
	public Response updateCondition(@PathParam("id") int id,Condition condition){
		int status=conditionService.updateCondition(id,condition);
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
	@Path("/get_conditions")
	public Response getConditions(){
		List<Condition> conditions=conditionService.getConditions(); 
		return Response
				.status(Status.OK)
				.entity(conditions)
				.build();
	}
	
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("get_condition/{id}")
	public Response getCondition(@PathParam("id") int id){
		Condition condition=conditionService.getCondition(id);
		if(condition==null){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else{
			return Response
					.status(Status.OK)
					.entity(condition)
					.build();

		}
		
	}
	

}
