package Servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import beans.Device;

/**
 * Servlet implementation class CreateDevices
 */

@WebServlet("/CreateDevices")
@MultipartConfig
public class CreateDevices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateDevices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doFer(request, response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doFer(request, response);
	}
	
	
	public void doFer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("\nDins del CreateDevices!");
		HttpSession session;
		session=request.getSession();
		
		String url="http://localhost:8080/GarduinoApi/devices/create_device";
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		
		String userId = (String)session.getAttribute("userId");
		String deviceName = request.getParameter("deviceName");
		String activeCheck = request.getParameter("activeCheck");
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		InputStream fileContent = filePart.getInputStream();
	    //File image=new File(fileName);
	    //System.out.println(fileName);
	    MultipartFormDataOutput upload = new MultipartFormDataOutput();
	    upload.addFormData("name", deviceName, MediaType.APPLICATION_JSON_TYPE);
	    upload.addFormData("user_id", userId, MediaType.APPLICATION_JSON_TYPE);
	    upload.addFormData("status", "DISCONNECTED", MediaType.APPLICATION_JSON_TYPE);
	    upload.addFormData("image", fileContent, MediaType.APPLICATION_OCTET_STREAM_TYPE);
	    Device device=new Device();
	    device.setName(deviceName);
	    device.setStatus("DISCONNECTED");
	    device.setUserId(Integer.parseInt(userId));
	    //device.setImage(image);
	    Response res=target.request().post(Entity.entity(upload, MediaType.MULTIPART_FORM_DATA));
	    
	    
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/ListDevices");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
