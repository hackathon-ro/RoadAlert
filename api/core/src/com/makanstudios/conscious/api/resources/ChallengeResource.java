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
import com.makanstudios.conscious.api.model.Challenge;

public class ChallengeResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	String sId;

	public ChallengeResource(UriInfo uriInfo, Request request, String sId) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.sId = sId;
	}

	// For the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Challenge getChallengeBrowser() {
		return getChallenge();
	}

	// Application integration
	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Challenge getChallenge() {
		try {
			long id = Long.parseLong(sId);
			Challenge challenge = MainDao.instance.getChallenge(id);
			return challenge;
		} catch (NumberFormatException nfe) {
		}

		return null;
	}

	@DELETE
	public Response deleteChallenge() {
		try {
			long id = Long.parseLong(sId);
			MainDao.instance.deleteChallenge(id);
		} catch (NumberFormatException nfe) {
		}

		Response res = Response.noContent().build();
		return res;
	}
}
