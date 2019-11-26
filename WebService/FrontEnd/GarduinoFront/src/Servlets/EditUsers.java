package Servlets;

import java.io.IOException;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;

/**
 * Servlet implementation class EditUsers
 */
@WebServlet("/EditUsers")
public class EditUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUsers() {
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
		System.out.println("\nDins del EditUsers!");
		
		String username = request.getParameter("userName");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String adminCheck = request.getParameter("adminCheck");
		String userId = request.getParameter("userId");
		System.out.println("User id:" + userId);
		int admin=0;
		if(adminCheck=="on"){
			admin=1;
		}else{
			
			admin=0;
		}
		String url="http://localhost:8080/GarduinoApi/users/update_user/"+userId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		
		User user=new User();
		user.setUsername(username);
		user.setAdmin(admin);
		user.setEmail(email);
		user.setPassword(password);
		user.setPhone(phone);
		
		Response res=target.request(MediaType.APPLICATION_JSON).put(Entity.json(user));
		System.out.println(user.getUsername());
		//res.close();
		
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/ListUsers");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
