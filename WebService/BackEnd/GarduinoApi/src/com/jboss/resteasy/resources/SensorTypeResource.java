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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.jboss.resteasy.beans.SensorType;
import com.jboss.resteasy.services.SensorTypeService;


@Path("/sensortypes")
public class SensorTypeResource 
{
	private SensorTypeService sensorTypeService = new SensorTypeService();
	
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/create_sensortype")
	public Response createSensorType(SensorType sensorType)
	{
		int status = sensorTypeService.createSensorType(sensorType);
		if(status == -1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else if(status == -2){
			return Response.status(Status.FORBIDDEN).build();
		}
		else
		{
			sensorType.setId(status);
			return Response
					.status(Status.CREATED)
					.entity(sensorType)
					.build();
		}
	}
	
	
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/delete_sensortype/{id}")
	public Response deleteSensorType(@PathParam("id") int id)
	{
		int status = sensorTypeService.deleteSensorType(id);
		if(status == -1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else if(status == -2){
			return Response.status(Status.NO_CONTENT).build();
		}
		else{
			return Response.status(Status.OK).build();
		}
	}

	
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/update_sensortype/{id}")
	public Response updateSensorType(@PathParam("id") int id, SensorType sensortype)
	{
		int status = sensorTypeService.updateSensorType(id, sensortype);
		if(status == -1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else if(status == -2){
			return Response.status(Status.NOT_FOUND).build();
		}
		else{
			return Response.status(Status.OK).build();
		}
	}
	
	
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_sensortypes")
	public Response getSensorTypes()
	{
		List<SensorType> sensorTypes = sensorTypeService.getSensorTypes(); 
		return Response
				.status(Status.OK)
				.entity(sensorTypes)
				.build();
	}

	
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("get_sensortype/{id}")
	public Response getSensorType(@PathParam("id") int id)
	{
		SensorType sensorType = sensorTypeService.getSensorType(id);
		if(sensorType == null){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else
		{
			return Response
					.status(Status.OK)
					.entity(sensorType)
					.build();
		}
	}
}