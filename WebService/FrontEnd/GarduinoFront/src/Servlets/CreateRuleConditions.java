package Servlets;

import java.io.IOException;
import java.util.HashMap;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import beans.Condition;
import beans.RuleCondition;

/**
 * Servlet implementation class CreateRuleConditions
 */
@WebServlet("/CreateRuleConditions")
public class CreateRuleConditions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateRuleConditions() {
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
	
	
	public void doFer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("\nDins del CreateRuleConditions!");
		HttpSession session;
		session=request.getSession();
		String url="http://localhost:8080/GarduinoApi/ruleconditions/create_rule_condition";
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		String ruleId=(String) session.getAttribute("ruleId");
		String conditionId=(String) session.getAttribute("conditionId");
		String conditionValue= request.getParameter("conditionValue");
		boolean status=false;
		String statusCheck=request.getParameter("statusCheck");
		if(statusCheck!=null){
			status=true;
		}
		System.out.println("conditionId: "+conditionId);
		System.out.println("conditionValue: "+conditionValue);
		System.out.println("Status Check: "+status);
		RuleCondition ruleCondition=new RuleCondition();
		ruleCondition.setConditionValue(Integer.parseInt(conditionValue));
		ruleCondition.setIdCondition(Integer.parseInt(conditionId));
		ruleCondition.setIdRule(Integer.parseInt(ruleId));
		ruleCondition.setStatus(status);
		Response res=target.request().post(Entity.json(ruleCondition));
		res.close();
		System.out.println("Dins de CreateRuleConditions ruleId:"+ruleId);
		//System.out.println("Dins de CreateRuleConditions conditionId:"+conditionId);
		request.setAttribute("ruleId", ruleId);
		session.setAttribute("ruleId", ruleId);
	    
	    
	    
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/ListConditions");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
