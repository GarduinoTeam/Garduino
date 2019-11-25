package Servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import beans.User;
import jdk.nashorn.api.scripting.JSObject;

/**
 * Servlet implementation class CreatUsers
 */
@WebServlet("/CreateUsers")
public class CreateUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUsers() {
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
		
		String username = request.getParameter("userName");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String adminCheck = request.getParameter("adminCheck");
		int admin=0;
		if(adminCheck=="on"){
			admin=1;
		}else{
			
			admin=0;
		}
		String url="http://localhost:8080/GarduinoApi/users/create_user";
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		User user=new User();
		user.setUsername(username);
		user.setAdmin(admin);
		user.setEmail(email);
		user.setPassword(password);
		user.setPhone(phone);
		Response res=target.request().post(Entity.json(user));
		System.out.println(res.getStatus());
		res.close();
		
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/UsersList");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}
}
