package com.jboss.resteasy.main;

import java.io.File;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("")
public class GarduinoApiApplication extends Application {
	public GarduinoApiApplication(){
		 File file = new File("C:\\Users\\joanpau\\Desktop\\Servidor Data");
	        if (!file.exists()) {
	            if (file.mkdir()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }
		
	}
	
}
