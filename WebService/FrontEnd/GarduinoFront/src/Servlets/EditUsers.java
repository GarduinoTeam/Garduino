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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

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
		
		String userId = request.getParameter("userId");
		String url="http://localhost:8080/GarduinoApi/users/get_user/"+userId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		User user=target.request(MediaType.APPLICATION_JSON).get(User.class);
		System.out.println(user.getUsername());
		//res.close();
		
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/EditUser");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
