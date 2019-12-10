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
import beans.User;

/**
 * Servlet implementation class GetRuleCondition
 */
@WebServlet("/GetRuleCondition")
public class GetRuleCondition extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetRuleCondition() {
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
		System.out.println("\nDins del EditGetRuleCondition!");
		HttpSession session;
		session=request.getSession();
		
		String ruleConditionId = request.getParameter("ruleConditionId");
		String url="http://localhost:8080/GarduinoApi/ruleconditions/get_rule_condition/"+ruleConditionId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		RuleCondition ruleCondition=target.request(MediaType.APPLICATION_JSON).get(RuleCondition.class);
		//res.close();
		System.out.println("Condition Valure"+ruleCondition.getConditionValue());
		session.setAttribute("ruleCondition", ruleCondition);
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/EditRuleCondition");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}
}
