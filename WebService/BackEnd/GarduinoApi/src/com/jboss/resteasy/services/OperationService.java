package com.jboss.resteasy.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class OperationService {
	private static String hostname = "192.168.43.181";
	//private static String hostname = "192.168.43.147";
    private static int port = 12346;
    private Socket socket;
    
    public OperationService()
    {
    	try
    	{
    		socket = new Socket(hostname, port);
    	}
    	catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	
    }
    
	public int sendRequest(JSONObject json)
	{
		try
        {
            String jsonData = json.toString();

            // Send JSON data
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            // First send JSON data length
            //System.out.println(jsonData.length());
            writer.println(String.valueOf(jsonData.length())); 
            Thread.sleep(200);
            // Send the JSON
            System.out.println(jsonData);
            writer.println(String.valueOf(jsonData));
            writer.flush();            
        }
        
        catch (Exception ex)
		{
        	System.out.println(ex.getMessage());
        	return -1;
		}
			
		return 0;
	}
	
	public String receiveData(){
		String response;
		try
        {           
            // Receive JSON data
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));           
            response = in.readLine();
            System.out.println(response);
        }        
        catch (Exception ex) 
        {
            System.out.println( ex.getMessage());
            return null;
        } 
		
		return response;
	}

}
