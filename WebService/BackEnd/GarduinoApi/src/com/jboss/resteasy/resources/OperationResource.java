package com.jboss.resteasy.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.json.JSONObject;
import org.json.JSONException;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.io.*;
import com.jboss.resteasy.beans.Operation;
import com.jboss.resteasy.services.OperationService;
//import com.jboss.resteasy.services.OperationService;

@Path("/operations")
public class OperationResource {
	private OperationService operationService=new OperationService();
	//private static String hostname = "192.168.43.70";
    //private static int port = 12346;
	//private OperationService myOperationService=new OperationService();
	/*@POST
	@Path("/manual_irrigation")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createManualIrrigation(Operation operation){
		operation.setName("irrigate");
		try (Socket socket = new Socket(hostname, port)) 
        {
            JSONObject json = new JSONObject();

            if(operation.getName() != ""){
                json.put("operation", operation.getName());
            }
            else{
            	return Response
        				.status(Status.INTERNAL_SERVER_ERROR)
        				.build();
            }

            if(String.valueOf(operation.getDevice_id()) != ""){
                json.put("device_id", String.valueOf(operation.getDevice_id()));
            }
            else{
            	return Response
        				.status(Status.INTERNAL_SERVER_ERROR)
        				.build();
            }
            
            if(operation.getIrrigationTime() != 0){
                json.put("irrigation_time", operation.getIrrigationTime());
            }
        
            String jsonData = json.toString();

            // Send JSON data
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            // First send JSON data length
            System.out.println(jsonData.length());
            writer.println(String.valueOf(jsonData.length())); 
            Thread.sleep(200);
            // Send the JSON
            System.out.println(jsonData);
            writer.println(String.valueOf(jsonData)); 
        }
        catch (JSONException ex) 
        {
            System.out.println("Invalid JSON error: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        }
        catch (UnknownHostException ex) 
        {
            System.out.println("Server not found: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        } 
        catch (IOException ex) 
        {
            System.out.println("I/O error: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        }
		catch (InterruptedException ex) 
        {
            System.out.println("I/O error: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        }
		return Response
				.status(Status.CREATED)
				.build();
	}*/
	@POST
	@Path("/irrigate")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createManualIrrigation(Operation operation){
		try{
			JSONObject json = new JSONObject();
			
            json.put("operation", "irrigate");
            
			if(String.valueOf(operation.getDevice_id()) != ""){
                json.put("device_id", String.valueOf(operation.getDevice_id()));
            }
            else{
            	return Response
        				.status(Status.INTERNAL_SERVER_ERROR)
        				.build();
            }
			if(operation.getIrrigationTime() != 0){
                json.put("irrigation_time", operation.getIrrigationTime());
            }
			else{
            	return Response
        				.status(Status.INTERNAL_SERVER_ERROR)
        				.build();
            }
			operationService.sendRequest(json);
			
			
		}
		catch (JSONException ex) 
        {
            System.out.println("Invalid JSON error: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        }
		return Response
				.status(Status.CREATED)
				.build();
	}
	@POST
	@Path("/stop_irrigate")
	@Produces("application/json")
	@Consumes("application/json")
	public Response stopIrrigation(Operation operation){
		try{
			JSONObject json = new JSONObject();
			 json.put("operation", "stop_irrigate");
			if(String.valueOf(operation.getDevice_id()) != ""){
                json.put("device_id", String.valueOf(operation.getDevice_id()));
            }
            else{
            	return Response
        				.status(Status.INTERNAL_SERVER_ERROR)
        				.build();
            }
			operationService.sendRequest(json);
			
			
		}
		catch (JSONException ex) 
        {
            System.out.println("Invalid JSON error: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        }
		return Response
				.status(Status.CREATED)
				.build();
	}
	
}

