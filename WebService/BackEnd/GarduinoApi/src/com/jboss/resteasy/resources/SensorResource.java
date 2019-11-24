package com.jboss.resteasy.resources;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.jboss.resteasy.beans.Sensor;
import com.jboss.resteasy.services.SensorService;
@Path("/sensors")
public class SensorResource {
	private SensorService mySensorService=new SensorService();
	@POST
	@Path("/create_sensor")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createSensor(Sensor sensor){
		String res="";
		int value=0;
		Date date=new Date();
		//String strdateFormat="aaaa-MM-dd HH: mm: ss ";
		//SimpleDateFormat dateFormat=new SimpleDateFormat(strdateFormat);
		sensor.setDate(date);
		sensor.setValue(value);
		int status=mySensorService.createSensor(sensor);
		//dateFormat.format(arg0)
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			sensor.setId(status);
			return Response
					.status(Status.CREATED)
					.entity(sensor)
					.build();
		}
		
	}
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_sensors")
	public Response getDevices(@QueryParam("device_id") int device_id){
		System.out.println(device_id);
		if(device_id==0){
			return Response
					.status(Status.BAD_REQUEST)
					.build();
		}else{
			List<Sensor> sensors=mySensorService.getSensors(device_id);
			return Response
					.status(Status.OK)
					.entity(sensors)
					.build();
		}
		
	}
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/update_sensor/{id}")
	public Response updateSensor(@PathParam("id") int id, Sensor sensor){
		Date date=new Date();
		sensor.setDate(date);
		int status=mySensorService.updateSensor(id,sensor);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else if(status==-2){
			return Response.status(Status.NOT_FOUND).build();
		}else{
			return Response.status(Status.OK).build();
		}
	}

}
