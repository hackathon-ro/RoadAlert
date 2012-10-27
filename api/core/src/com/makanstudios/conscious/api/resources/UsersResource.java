package com.makanstudios.conscious.api.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.makanstudios.conscious.api.dao.MainDao;
import com.makanstudios.conscious.api.model.Id;
import com.makanstudios.conscious.api.model.User;

@Path("/users")
public class UsersResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<User> getUsersBrowser() {
		return MainDao.instance.getUsers();
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUsers(@QueryParam("email")
	String email) {
		if (email != null)
			return MainDao.instance.getUsers(email);
		else
			return MainDao.instance.getUsers();
	}

	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Id addUser(User user) {
		long id = MainDao.instance.addReplaceUser(user);
		return new Id(id);
	}

	@DELETE
	public Response deleteAllUsers() {
		MainDao.instance.deleteAllUsers();

		Response res = Response.noContent().build();
		return res;
	}

	@Path("{user}")
	public UserResource getUser(@PathParam("user")
	String id) {
		return new UserResource(uriInfo, request, id);
	}
}
