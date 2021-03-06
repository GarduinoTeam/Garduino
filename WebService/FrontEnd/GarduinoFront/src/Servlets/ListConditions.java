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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import beans.Device;
import beans.Rule;
import beans.RuleCondition;
import beans.RuleTimeCondition;

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
		HttpSession session;
		session=request.getSession();
		if(ruleId==null){
			ruleId=(String)session.getAttribute("ruleId");
		}
		session.setAttribute("ruleId", ruleId);
		System.out.println("RuleId:"+ruleId);

		String url="http://localhost:8080/GarduinoApi/ruleconditions/get_rule_conditions?rule_id="+ruleId;
		
		
		
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		
		RuleCondition[] ruleConditionList = null;
		HashMap<String,List<RuleCondition>> ruleConditionsHashMap=new HashMap<>();
		ruleConditionsHashMap=target.request().get(new GenericType<HashMap<String,List<RuleCondition>>>(){});
		List<RuleCondition>ruleConditions=ruleConditionsHashMap.get("ruleconditions");
		ruleConditionList = new RuleCondition[ruleConditions.size()];
		for(int i=0;i<ruleConditions.size();i++){
			RuleCondition Condition=ruleConditions.get(i);
			System.out.println("Rule Condition id:"+Condition.getId());
			System.out.println("Rule Condition ruleid:"+Condition.getIdRule());
			System.out.println("Rule Condition idCondition:"+Condition.getIdCondition());
			System.out.println("Rule Condition Condition Value:"+Condition.getConditionValue());
			System.out.println("Rule Condition Status:"+Condition.getStatus());
			ruleConditionList[i]=Condition;		
		}
		
		
		//RuleTimeCondition [] ruleTimeConditionList=null;
		url="http://localhost:8080/GarduinoApi/ruletimeconditions/get_rule_time_conditions?rule_id="+ruleId;
		client= ClientBuilder.newClient();
		target=client.target(url);
		HashMap<String,List<RuleTimeCondition>> ruleTimeConditionHashMap=new HashMap<>();
		ruleTimeConditionHashMap=target.request().get(new GenericType<HashMap<String,List<RuleTimeCondition>>>(){});
		List<RuleTimeCondition>ruleTimeConditionList=ruleTimeConditionHashMap.get("ruletimeconditions");
		RuleTimeCondition[] ruleTimeConditions=new RuleTimeCondition[ruleTimeConditionList.size()];
		for(int i=0;i<ruleTimeConditionList.size();i++){
			RuleTimeCondition ruleTimeCondition=ruleTimeConditionList.get(i);
			ruleTimeConditions[i]=ruleTimeCondition;
		}
		session.setAttribute("ruleTimeConditions", ruleTimeConditions);
		session.setAttribute("ruleConditions", ruleConditionList);
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/Conditions");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
