package com.jboss.resteasy.resources;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.jboss.resteasy.beans.Device;
import com.jboss.resteasy.beans.User;
import com.jboss.resteasy.services.DeviceService;
@Path("/devices")
public class DeviceResource {
	private DeviceService myDeviceService=new DeviceService();
	@POST
	@Path("/create_device")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	public Response createDevice(@MultipartForm Device form){
		
		System.out.println("name: "+form.getName());
		System.out.println("status: "+form.getStatus());
		System.out.println("image: "+form.getImage());
		System.out.println("user_id: "+form.getUserId());
		System.out.println("Done");
		int status=myDeviceService.createDevice(form);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			return Response.status(Status.CREATED).build();
		}
		
		    
	}
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/delete_device/{id}")
	public Response deleteDevice(@PathParam("id") int id){
		int status=myDeviceService.deleteDevice(id);
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
	@Consumes("multipart/form-data")
	@Path("/update_device/{id}")
	public Response updateDevice(@PathParam("id") int id,@MultipartForm Device device){
		int status=myDeviceService.updateDevice(id,device);
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
	@Path("/get_devices")
	public Response getDevices(@QueryParam("user_id") int user_id){
		System.out.println(user_id);
		List<Device> devices=myDeviceService.getDevices(user_id);
		return Response
				.status(Status.OK)
				.entity(devices)
				.build();
	}
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_device/{id}")
	public Response getDevice(@PathParam("id") int id){
		Device device=myDeviceService.getDevice(id);
		if(device==null){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else{
			return Response
					.status(Status.OK)
					.entity(device)
					.build();

		}
		
	}
	
	@GET
	@Produces("image/png")
	@Consumes("application/json")
	@Path("/images/{userId}/{deviceName}")
	public Response getImage(@PathParam("userId") int userId,@PathParam("deviceName") String deviceName){
		String serverHome="C:\\Users\\joanpau\\Desktop\\Servidor Web";
		String filename=serverHome+"\\"+userId+"\\"+deviceName+".png";
		File file = new File(filename);
		return Response.ok((Object)file).build();
	}
}
