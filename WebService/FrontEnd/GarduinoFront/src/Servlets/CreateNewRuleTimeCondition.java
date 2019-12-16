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
import javax.ws.rs.core.Response;

import beans.RuleCondition;

/**
 * Servlet implementation class CreateNewRuleTimeCondition
 */
@WebServlet("/CreateNewRuleTimeCondition")
public class CreateNewRuleTimeCondition extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateNewRuleTimeCondition() {
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
		System.out.println("\nDins del CreateNewRuleTimeConditions!");
		HttpSession session;
		session=request.getSession();
		//String url="http://localhost:8080/GarduinoApi/ruleconditions/create_rule_condition";
		//Client client= ClientBuilder.newClient();
		//WebTarget target=client.target(url);
		String ruleId=(String) session.getAttribute("ruleId");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String mo="0";String tu="0";String wed="0";String th="0";
		String fr="0";String sa="0";String su="0";
		String days="";
		String jan="0";String feb="0";String mar="0";String apr="0";
		String may="0";String jun="0";String jul="0";String aug="0";
		String sep="0";String oct="0";String nov="0";String dec="0";
		String months="";
		String dateString=request.getParameter("date");
		System.out.println("Start Time:"+startTime);
		System.out.println("End Time:"+endTime);
		//days
		if(request.getParameter("Mo")!=null){
			mo="1";
		}
		if(request.getParameter("Tu")!=null){
			tu="1";
		}
		if(request.getParameter("Wed")!=null){
			wed="1";
		}
		if(request.getParameter("Th")!=null){
			th="1";
		}
		if(request.getParameter("Fr")!=null){
			fr="1";
		}
		if(request.getParameter("Sa")!=null){
			sa="1";
		}
		if(request.getParameter("Su")!=null){
			su="1";
		}
		
		days+=days+mo+tu+wed+th+fr+sa+su;
		//months of the year
		if(request.getParameter("Jan")!=null){
			jan="1";
		}
		if(request.getParameter("Feb")!=null){
			feb="1";
		}
		if(request.getParameter("Mar")!=null){
			mar="1";
		}
		if(request.getParameter("Apr")!=null){
			apr="1";
		}
		if(request.getParameter("May")!=null){
			may="1";
		}
		if(request.getParameter("Jun")!=null){
			jun="1";
		}
		if(request.getParameter("Jul")!=null){
			jul="1";
		}
		if(request.getParameter("Aug")!=null){
			aug="1";
		}
		if(request.getParameter("Sep")!=null){
			sep="1";
		}
		if(request.getParameter("Oct")!=null){
			oct="1";
		}
		if(request.getParameter("Nov")!=null){
			nov="1";
		}
		if(request.getParameter("Dec")!=null){
			dec="1";
		}
		months+=jan+feb+mar+apr+may+jun+jul+aug+sep+oct+nov+dec;
		System.out.println("Days:"+days);
		System.out.println("Months:"+months);
		System.out.println("Date:"+dateString);
		//String conditionId=(String) session.getAttribute("conditionId");
		//String conditionValue= request.getParameter("conditionValue");
		boolean status=false;
		String statusCheck=request.getParameter("statusCheck");
		if(statusCheck!=null){
			status=true;
		}
		System.out.println("Status Check: "+status);
		//Response res=target.request().post(Entity.json(ruleCondition));
		//res.close();
		System.out.println("Dins de CreateNewRuleTimeConditions ruleId:"+ruleId);
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
