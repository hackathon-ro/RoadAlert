package com.makanstudios.conscious.api.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.makanstudios.conscious.api.dao.MainDao;
import com.makanstudios.conscious.api.model.User;

public class UserResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	String sId;

	public UserResource(UriInfo uriInfo, Request request, String sId) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.sId = sId;
	}

	// For the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public User getUserBrowser() {
		return getUser();
	}

	// Application integration
	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUser() {
		try {
			long id = Long.parseLong(sId);
			User user = MainDao.instance.getUser(id);
			return user;
		} catch (NumberFormatException nfe) {
		}
		return null;
	}

	@DELETE
	public Response deleteUser() {
		try {
			long id = Long.parseLong(sId);
			MainDao.instance.deleteUser(id);
		} catch (NumberFormatException nfe) {
		}

		Response res = Response.noContent().build();
		return res;
	}
}
