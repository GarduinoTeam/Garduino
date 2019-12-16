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

import beans.RuleCondition;
import beans.RuleTimeCondition;

/**
 * Servlet implementation class GetRuleTimeCondition
 */
@WebServlet("/GetRuleTimeCondition")
public class GetRuleTimeCondition extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetRuleTimeCondition() {
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
		System.out.println("\nDins del EditGetRuleTimeCondition!");
		HttpSession session;
		session=request.getSession();
		
		String ruleTimeConditionId = request.getParameter("ruleTimeConditionId");
		String url="http://localhost:8080/GarduinoApi/ruletimeconditions/get_rule_time_condition/"+ruleTimeConditionId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		RuleTimeCondition ruleTimeCondition=target.request(MediaType.APPLICATION_JSON).get(RuleTimeCondition.class);
		//res.close();
		session.setAttribute("ruleTimeCondition", ruleTimeCondition);
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/EditRuleTimeCondition");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
