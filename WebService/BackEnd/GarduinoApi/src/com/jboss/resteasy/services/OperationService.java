package com.jboss.resteasy.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class OperationService {
	//private static String hostname = "192.168.43.70";
	private static String hostname = "192.168.43.148";
    private static int port = 12346;
	public int sendRequest(JSONObject json){
		try (Socket socket = new Socket(hostname, port)) 
        {
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
        
        catch (UnknownHostException ex) 
        {
            System.out.println("Server not found: " + ex.getMessage());
            return -1;
        } 
        catch (IOException ex) 
        {
            System.out.println("I/O error: " + ex.getMessage());
            return -1;
        }
		catch (InterruptedException ex) 
        {
            System.out.println("I/O error: " + ex.getMessage());
            return -1;
        }
		
	
		return 0;
	}

}
