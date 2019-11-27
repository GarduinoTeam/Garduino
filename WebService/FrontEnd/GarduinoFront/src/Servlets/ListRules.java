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

/**
 * Servlet implementation class ListRules
 */
@WebServlet("/ListRules")
public class ListRules extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListRules() {
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
		System.out.println("\nDins del ListRules!");	
		String userId=request.getParameter("userId");
		String deviceId=request.getParameter("deviceId");
		System.out.println("List ListRules!");
		System.out.println(deviceId);
		System.out.println(userId);
		String url="http://localhost:8080/GarduinoApi/rules/get_rules?device_id="+deviceId;
		
		HttpSession session;
		session=request.getSession();
		session.setAttribute("userId", userId);
		
		
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		
		Rule[] rulesList = null;
		
		List<Rule>rules=target.request().get(new GenericType<List<Rule>>(){});
		rulesList = new Rule[rules.size()];
		for(int i=0;i<rules.size();i++){
			Rule rule=rules.get(i);
			rulesList[i]=rule;		
		}
		session.setAttribute("rules", rulesList);
		session.setAttribute("deviceId", deviceId);
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/Rules");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}

}
