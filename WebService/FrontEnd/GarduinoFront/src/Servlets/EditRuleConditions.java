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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.RuleCondition;

/**
 * Servlet implementation class EditRuleConditions
 */
@WebServlet("/EditRuleConditions")
public class EditRuleConditions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditRuleConditions() {
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
		System.out.println("\nDins del EditRuleConditions!");
		HttpSession session;
		session=request.getSession();
		
		String ruleConditionId = request.getParameter("ruleConditionId");
		String url="http://localhost:8080/GarduinoApi/ruleconditions/update_rule_condition/"+ruleConditionId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		String conditionValue= request.getParameter("conditionValue");
		boolean status=false;
		String statusCheck=request.getParameter("statusCheck");
		String ruleId=request.getParameter("ruleId");
		System.out.println("StatusCheck:"+statusCheck);
		System.out.println("ruleId:"+ruleId);
		request.setAttribute("ruleId", ruleId);
		if(statusCheck!=null){
			status=true;
		}
		RuleCondition ruleCondition=new RuleCondition();
		ruleCondition.setConditionValue(Integer.parseInt(conditionValue));
		ruleCondition.setStatus(status);
		Response res=target.request(MediaType.APPLICATION_JSON).put(Entity.json(ruleCondition));
		
		
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/ListConditions");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
