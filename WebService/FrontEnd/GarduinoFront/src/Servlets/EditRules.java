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

import beans.Rule;

/**
 * Servlet implementation class EditRules
 */
@WebServlet("/EditRules")
public class EditRules extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditRules() {
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
  		System.out.println("\nDins del EditRules!");
  		
  		String ruleId = request.getParameter("ruleId");
  		String ruleName = request.getParameter("ruleName");
  		String activeCheck = request.getParameter("activeCheck");
  		String deviceId = request.getParameter("deviceId");
  		String type = request.getParameter("type");
  		boolean active=false;
		if(activeCheck!=null){
			active=true;
		}
		System.out.println("Status of the Rule:"+active);
  		
		String url="http://localhost:8080/GarduinoApi/rules/update_rule/"+ruleId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		
		Rule rule=new Rule();
		rule.setId(Integer.parseInt(ruleId));
		rule.setIdDevice( Integer.parseInt(deviceId));
		rule.setName(ruleName);
		rule.setStatus(active);
		rule.setType(Integer.parseInt(type));
		
		Response res=target.request(MediaType.APPLICATION_JSON).put(Entity.json(rule));
		System.out.println(rule.getName());
  		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/ListRules");
			rd.forward(request, response);
		}
  		catch ( Exception e ) {e.printStackTrace();}
  	}
}
