package Servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import beans.Device;
import beans.User;

/**
 * Servlet implementation class ListDevices
 */
@WebServlet("/ListDevices")
public class ListDevices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListDevices() {
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
	
	
	public void doFer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("\nDins del ListDevices!");		
		String userId=request.getParameter("userId");
		System.out.println("List Devices:");
		System.out.println(userId);
		String url="http://localhost:8080/GarduinoApi/devices/get_devices?user_id="+userId;
		HttpSession session;
		session=request.getSession();
		if(userId==null){
			userId=(String)session.getAttribute("userId");
		}
		session.setAttribute("userId", userId);
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		
		Device[] devicesList = null;
		HashMap<String,List<Device>> devicesHashMap=new HashMap<>();
		devicesHashMap=target.request().get(new GenericType<HashMap<String,List<Device>>>(){});
		List<Device>devices=devicesHashMap.get("devices");
		//List<Device>devices=target.request().get(new GenericType<List<Device>>(){});
		devicesList = new Device[devices.size()];
		for(int i=0;i<devices.size();i++){
			Device device=devices.get(i);
			devicesList[i]=device;
			System.out.println(device.getName());
			System.out.println(device.getImageURL());
			
		}
		session.setAttribute("devices", devicesList);
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/UserDevices");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
