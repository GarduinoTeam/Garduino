package Servlets;

import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import beans.Condition;
import beans.Device;

/**
 * Servlet implementation class CreateConditions
 */
@WebServlet("/CreateConditions")
public class CreateConditions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateConditions() {
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
		System.out.println("\nDins del CreateConditions!");
		HttpSession session;
		session=request.getSession();
		
		String url="http://localhost:8080/GarduinoApi/conditions/get_conditions";
		Client client= ClientBuilder.newClient();
		WebTarget target=client.target(url);
		String ruleId=(String) session.getAttribute("ruleId");
		System.out.println("Dins de CreateConditions ruleId:"+ruleId);
	    Condition [] conditions=null;
	    HashMap<String,List<Condition>> conditionsHashMap=new HashMap<>();
	    conditionsHashMap=target.request().get(new GenericType<HashMap<String,List<Condition>>>(){});
	    List<Condition>conditionsList=conditionsHashMap.get("conditions");
	    conditions=new Condition[conditionsList.size()];
	    for(int i =0; i<conditionsList.size();i++){
	    	conditions[i]=conditionsList.get(i);
	    }
	    session.setAttribute("conditions", conditions);
	    
	    
		try {
			ServletContext context = getServletContext();
			RequestDispatcher rd = context.getRequestDispatcher("/NewCondition");
			rd.forward(request, response);
		}
		catch ( Exception e ) {e.printStackTrace();}
	}


}
