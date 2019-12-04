package Servlets;

import java.io.IOException;
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
import beans.Rule;
import beans.RuleCondition;

/**
 * Servlet implementation class ListConditions
 */
@WebServlet("/ListConditions")
public class ListConditions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListConditions() {
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
		System.out.println("\nDins del ListCondtitons!");	
		String ruleId=request.getParameter("ruleId");

		String url="http://localhost:8080/GarduinoApi/ruleconditions/get_rule_conditions?rule_id="+ruleId;
		
		HttpSession session;
		session=request.getSession();
		session.setAttribute("ruleId", ruleId);
		
		
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		
		RuleCondition[] ruleConditionList = null;
		
		List<RuleCondition>ruleConditions=target.request().get(new GenericType<List<RuleCondition>>(){});
		ruleConditionList = new RuleCondition[ruleConditions.size()];
		for(int i=0;i<ruleConditions.size();i++){
			RuleCondition Condition=ruleConditions.get(i);
			ruleConditionList[i]=Condition;		
		}
		session.setAttribute("ruleConditions", ruleConditionList);
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/Conditions");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
