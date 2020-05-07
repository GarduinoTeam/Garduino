package com.jboss.resteasy.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.jboss.resteasy.beans.User;
import com.jboss.resteasy.services.UserService;

@Path("/users")

public class UserResource 
{
	private UserService myUserService=new UserService();

	
	@GET
	@Path("/status")
	@Produces("application/json")
	@Consumes("application/json")
    public Response sayHello(){     
        return Response.ok("Users ok",MediaType.APPLICATION_JSON).build();   
    }
	
	
	@POST
	@Path("/create_user")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createUser(User user)
	{
		System.out.println(user.getEmail());
		System.out.println(user.getPassword());
		System.out.println(user.getUsername());
		System.out.println(user.getPhone());
		int status = myUserService.createUser(user);
		if(status == -1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else if(status == -2){
			return Response.status(Status.FORBIDDEN).build();
		}
		else
		{
			user.setId(status);
			return Response
					.status(Status.CREATED)
					.entity(user)
					.build();
		}
	}

	
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/delete_user/{id}")
	public Response deleteUser(@PathParam("id") int id)
	{
		int status = myUserService.deleteUser(id);
		if(status == -1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else if(status == -2){
			return Response.status(Status.NO_CONTENT).build();
		}
		else{
			return Response.status(Status.OK).build();
		}
	}

	
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/update_user/{id}")
	public Response updateUser(@PathParam("id") int id, User user)
	{
		int status = myUserService.updateUser(id,user);
		if(status == -1){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else if(status == -2){
			return Response.status(Status.NOT_FOUND).build();
		}
		else{
			return Response.status(Status.OK).build();
		}
	}

	
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/get_users")
	public Response getUsers()
	{
		List<User> users = myUserService.getUsers();
		return Response
				.status(Status.OK)
				.entity(users)
				.build();
	}
	
	
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("get_user/{id}")
	public Response getUser(@PathParam("id") int id)
	{
		User user = myUserService.getUser(id);
		if(user == null){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else
		{
			return Response
					.status(Status.OK)
					.entity(user)
					.build();
		}	
	}
}