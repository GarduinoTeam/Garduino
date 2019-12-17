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
import javax.ws.rs.core.Response;

/**
 * Servlet implementation class DeleteRuleTimeCondition
 */
@WebServlet("/DeleteRuleTimeCondition")
public class DeleteRuleTimeCondition extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteRuleTimeCondition() {
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
		System.out.println("\nDins del DeleteRuleTimeCondition!");		
		String ruleTimeConditionId=request.getParameter("ruleTimeConditionId");
		String ruleId=request.getParameter("ruleId");
		System.out.println("\n ruleTimeConditionId:"+ruleTimeConditionId);
		String url="http://localhost:8080/GarduinoApi/ruletimeconditions/delete_rule_time_condition/"+ruleTimeConditionId;
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		Response res=target.request().delete();
		HttpSession session;
		session=request.getSession();
		session.setAttribute("ruleId", ruleId);
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/ListConditions");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
