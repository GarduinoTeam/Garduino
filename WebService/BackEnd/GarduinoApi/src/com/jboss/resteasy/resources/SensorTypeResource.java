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

import com.jboss.resteasy.beans.SensorType;
import com.jboss.resteasy.services.SensorTypeService;
@Path("/sensortypes")
public class SensorTypeResource {
	private SensorTypeService sensorTypeService=new SensorTypeService();
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response createSensorType(SensorType sensorType){
		int status= sensorTypeService.createSensorType(sensorType);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			sensorType.setId(status);
			return Response
					.status(Status.CREATED)
					.entity(sensorType)
					.build();
		}
		
	}
	

}
