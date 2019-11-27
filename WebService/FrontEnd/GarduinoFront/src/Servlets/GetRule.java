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

import beans.Rule;
import beans.User;

/**
 * Servlet implementation class GetRule
 */
@WebServlet("/GetRule")
public class GetRule extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetRule() {
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
  		System.out.println("\nDins del GetRule!");
  		HttpSession session;
  		session=request.getSession(true);
  		
  		String ruleId = request.getParameter("ruleId");
  		String deviceId = request.getParameter("deviceId");
  		
  		String url="http://localhost:8080/GarduinoApi/rules/get_rule/"+ruleId;
  		Client client= ClientBuilder.newClient();
  		WebTarget target=client.target(url);
  		Rule rule=target.request(MediaType.APPLICATION_JSON).get(Rule.class);

  		session.setAttribute("rule", rule);
  		session.setAttribute("deviceId", deviceId);
  		
  		try {
  			ServletContext context = getServletContext();
  			RequestDispatcher rd = context.getRequestDispatcher("/EditRule");
  			rd.forward(request, response);
  		}
  		catch ( Exception e ) {e.printStackTrace();}
  	}

}
