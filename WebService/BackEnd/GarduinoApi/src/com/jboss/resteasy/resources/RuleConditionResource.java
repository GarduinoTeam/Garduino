package com.jboss.resteasy.resources;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.jboss.resteasy.beans.Condition;
import com.jboss.resteasy.beans.Rule;
import com.jboss.resteasy.beans.RuleCondition;
import com.jboss.resteasy.services.ConditionService;
import com.jboss.resteasy.services.RuleConditionService;


@Path("/ruleconditions")
public class RuleConditionResource {
	private RuleConditionService myRuleConditionService=new RuleConditionService();
	private ConditionService myConditionService=new ConditionService();
	@POST
	@Path("/create_rule_condition")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createRuleCondition(RuleCondition rulecondition){
		rulecondition.setStatus(false);
		int status=myRuleConditionService.createRuleCondition(rulecondition);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}else if(status==-2){
			return Response.status(Status.FORBIDDEN).build();
		
		}else{
			rulecondition.setId(status);
			return Response
					.status(Status.CREATED)
					.build();
		}
		
	}
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/delete_rule_condition/{id}")
	public Response deleteRuleCondition(@PathParam("id") int id){
		int status=myRuleConditionService.deleteRuleCondition(id);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else if(status==-2){
			return Response.status(Status.NO_CONTENT).build();
		}else{
			return Response.status(Status.OK).build();
		}
		
	}
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/update_rule_condition/{id}")
	public Response updateRuleCondition(@PathParam("id") int id,RuleCondition ruleCondition){
		int status=myRuleConditionService.updateRuleCondition(id,ruleCondition);
		if(status==-1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else if(status==-2){
			return Response.status(Status.NOT_FOUND).build();
		}else{
			return Response.status(Status.OK).build();
		}
		
	}
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_rule_conditions")
	public Response getRuleConditions(@QueryParam("rule_id") int rule_id){
		if(rule_id==0){
			return Response
					.status(Status.BAD_REQUEST)
					.build();
		}else{
			HashMap<String,List<RuleCondition>> hashMapRuleConditions=new HashMap<>();
			List<RuleCondition> ruleconditions=myRuleConditionService.getRuleConditions(rule_id);
			for(int i=0;i<ruleconditions.size();i++){
				RuleCondition ruleCondition=ruleconditions.get(i);
				Condition condition=myConditionService.getCondition(ruleCondition.getIdCondition());
				String name="";
				String measure="";
				name=condition.getName();
				measure=condition.getMeasure();
				/*if(ruleCondition.getIdCondition()==1){
					name="The temperature is higher than";
					ruleCondition.setName(name);
					ruleCondition.setMeasure("ºC");
				}else if(ruleCondition.getIdCondition()==2){
					name="The temperature is lower than";
					ruleCondition.setName(name);
					ruleCondition.setMeasure("ºC");
					
				}else if(ruleCondition.getIdCondition()==3){
					name="The humidity is higher than"+ruleCondition.getConditionValue()+" %";
					ruleCondition.setName(name);
					ruleCondition.setMeasure("%");
				}else if(ruleCondition.getIdCondition()==4){
					name="The humidity is lower than";
					ruleCondition.setName(name);
					ruleCondition.setMeasure("%");
				}
				else if(ruleCondition.getIdCondition()==5){
					name="The moistness is higher than";
					ruleCondition.setName(name);
					ruleCondition.setMeasure("%");
				}
				else if(ruleCondition.getIdCondition()==6){
					name="The moistness is lower than";
					ruleCondition.setName(name);
					ruleCondition.setMeasure("%");
				}*/
				ruleCondition.setName(name);
				ruleCondition.setMeasure(measure);
				ruleconditions.set(i, ruleCondition);
			}
			hashMapRuleConditions.put("ruleconditions", ruleconditions);
			return Response
					.status(Status.OK)
					.entity(hashMapRuleConditions)
					.build();
		}
		
	}
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_rule_condition/{id}")
	public Response getRuleCondition(@PathParam("id") int id){
		RuleCondition ruleCondition=myRuleConditionService.getRuleCondition(id);
		if(ruleCondition==null){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}else{
			Condition condition=myConditionService.getCondition(ruleCondition.getIdCondition());
			String name="";
			String measure="";
			name=condition.getName();
			measure=condition.getMeasure();
			/*if(ruleCondition.getIdCondition()==1){
				name="The temperature is higher than";
				ruleCondition.setName(name);
				ruleCondition.setMeasure("ºC");
			}else if(ruleCondition.getIdCondition()==2){
				name="The temperature is lower than";
				ruleCondition.setName(name);
				ruleCondition.setMeasure("ºC");
				
			}else if(ruleCondition.getIdCondition()==3){
				name="The humidity is higher than"+ruleCondition.getConditionValue()+" %";
				ruleCondition.setName(name);
				ruleCondition.setMeasure("%");
			}else if(ruleCondition.getIdCondition()==4){
				name="The humidity is lower than";
				ruleCondition.setName(name);
				ruleCondition.setMeasure("%");
			}
			else if(ruleCondition.getIdCondition()==5){
				name="The moistness is higher than";
				ruleCondition.setName(name);
				ruleCondition.setMeasure("%");
			}
			else if(ruleCondition.getIdCondition()==6){
				name="The moistness is lower than";
				ruleCondition.setName(name);
				ruleCondition.setMeasure("%");
			}*/
			ruleCondition.setName(name);
			ruleCondition.setMeasure(measure);
			return Response
					.status(Status.OK)
					.entity(ruleCondition)
					.build();

		}
		
	}
	

}
