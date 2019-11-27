package Servlets;

import java.io.IOException;

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
import javax.ws.rs.core.MediaType;

import beans.Device;
import beans.User;

/**
 * Servlet implementation class GetDevice
 */
@WebServlet("/GetDevice")
public class GetDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDevice() {
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
		System.out.println("\nDins del Get Device!");
		HttpSession session;
		session=request.getSession();
		
		String userId = request.getParameter("userId");
		String deviceId=request.getParameter("deviceId");
		session.setAttribute("userId",userId);
		session.setAttribute("deviceId", deviceId);
		System.out.println("User id:"+userId);
		System.out.println("Device id:"+deviceId);
		String url="http://localhost:8080/GarduinoApi/devices/get_device/"+deviceId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		Device device=target.request(MediaType.APPLICATION_JSON).get(Device.class);
		session.setAttribute("device", device);
		System.out.println("Get Device: "+device.getName());
		/*User user=target.request(MediaType.APPLICATION_JSON).get(User.class);
		System.out.println(user.getUsername());
		//res.close();
		session.setAttribute("user", user);*/
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/EditDevice");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
