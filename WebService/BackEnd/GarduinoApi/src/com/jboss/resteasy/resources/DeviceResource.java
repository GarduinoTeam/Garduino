package com.jboss.resteasy.resources;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.jboss.resteasy.beans.Device;
import com.jboss.resteasy.services.DeviceService;
@Path("/devices")
public class DeviceResource {
	private DeviceService myDeviceService=new DeviceService();
	@POST
	@Path("/create_device")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	public Response createDevice(@MultipartForm Device form){
		String fileName="C:\\Users\\joanpau\\Desktop\\Servidor Data\\"+form.getName()+".png";
		try {
			writeFile(form.getImage(), fileName);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		System.out.println("name: "+form.getName());
		System.out.println("status: "+form.getStatus());
		System.out.println("image: "+form.getImage());
		System.out.println("user_id: "+form.getUserId());
		System.out.println("Done");
		int status=myDeviceService.createDevice(form);
		return Response.status(Status.CREATED).build();
		    
	}
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}
