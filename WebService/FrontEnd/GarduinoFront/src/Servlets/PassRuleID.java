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

import beans.User;

/**
 * Servlet implementation class PassRuleID
 */
@WebServlet("/PassRuleID")
public class PassRuleID extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
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
		System.out.println("\nDins del PassRuleID!");
		
		HttpSession session;
		session=request.getSession(true);
		
		String deviceId = request.getParameter("deviceId");
		session.setAttribute("deviceId", deviceId);
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/NewRule");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
