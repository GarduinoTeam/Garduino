package com.jboss.resteasy.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.json.JSONObject;
import org.json.JSONException;
import com.jboss.resteasy.beans.Device;
import com.jboss.resteasy.beans.Operation;
import com.jboss.resteasy.services.DeviceService;
import com.jboss.resteasy.services.OperationService;


@Path("/operations")
public class OperationResource 
{
	private OperationService operationService = new OperationService();
	private DeviceService myDeviceService = new DeviceService();

	
	@POST
	@Path("/irrigate")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createManualIrrigation(Operation operation)
	{
		try
		{
			JSONObject json = new JSONObject();
            json.put("operation", "irrigate");

            if(String.valueOf(operation.getDevice_id()) != ""){
                json.put("device_id", String.valueOf(operation.getDevice_id()));
            }
            else
            {
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
	public Response stopIrrigation(Operation operation)
	{
		try
		{
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
	
	
	@GET
	@Path("/sensor")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getSensorData(Operation operation)
	{
		JSONObject jsonResponse;
		int deviceId = operation.getDevice_id();
		try
		{
			JSONObject json = new JSONObject();
			json.put("operation", "sensor");
			if(String.valueOf(deviceId) != ""){
                json.put("device_id", String.valueOf(operation.getDevice_id()));
            }
            else{
            	return Response
        				.status(Status.INTERNAL_SERVER_ERROR)
        				.build();
            }
			operationService.sendRequest(json);
			String response=operationService.receiveData();
			
			/*Code to send the device data with the real sensor Data*/
			//System.out.println(response);
			jsonResponse = new JSONObject(response);
			System.out.println(jsonResponse.getString("temperature"));
			System.out.println(jsonResponse.getString("humidity"));
			System.out.println(jsonResponse.getString("moisture"));
			System.out.println(jsonResponse.names());
			
			Device device = myDeviceService.getDevice(deviceId);
			device.setTemperature("Temperature: " + jsonResponse.getString("temperature")+" ºC");
			device.setHumidity("Humidity: " +jsonResponse.getString("humidity")+" %");
			device.setSoil("Soil / moisture: " +jsonResponse.getString("moisture")+" %");
			
			operationService = new OperationService();
			json.put("operation", "webcam");
			operationService.sendRequest(json);
			response = operationService.receiveData();
			jsonResponse = new JSONObject(response);
			device.setImageAndroidURL(jsonResponse.getString("image_base64"));
			device.setImageURL(jsonResponse.getString("image_base64"));
			return Response
					.status(Status.CREATED)
					.entity(device)
					.build();
		}
		catch (Exception ex) 
        {
            System.out.println("Invalid JSON error: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        }
	}
	
	
	@GET
	@Path("/webcam")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getWebCam(Operation operation)
	{
		int deviceId = operation.getDevice_id();
		try
		{
			JSONObject json = new JSONObject();
			json.put("operation", "webcam");
			if(String.valueOf(deviceId) != ""){
                json.put("device_id", String.valueOf(operation.getDevice_id()));
            }
            else{
            	return Response
        				.status(Status.INTERNAL_SERVER_ERROR)
        				.build();
            }
			operationService.sendRequest(json);
			String response = operationService.receiveData();
			
			/*Code to send the device data with the real sensor Data*/
			System.out.println(response);
			
			return Response
					.status(Status.CREATED)
					.build();
		}
		catch (Exception ex) 
        {
            System.out.println("Invalid JSON error: " + ex.getMessage());
            return Response
    				.status(Status.INTERNAL_SERVER_ERROR)
    				.build();
        }
	}
}