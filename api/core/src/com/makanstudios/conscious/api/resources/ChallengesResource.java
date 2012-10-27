package com.makanstudios.conscious.api.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.makanstudios.conscious.api.dao.MainDao;
import com.makanstudios.conscious.api.model.Challenge;
import com.makanstudios.conscious.api.model.Id;

@Path("/challenges")
public class ChallengesResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Challenge> getChallengesBrowser() {
		return getChallenges();
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Challenge> getChallenges() {
		return MainDao.instance.getChallenges();
	}

	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Id addChallenge(Challenge challenge) {
		long id = MainDao.instance.addReplaceChallenge(challenge);
		return new Id(id);
	}

	@DELETE
	public Response deleteAllChallenges() {
		MainDao.instance.deleteAllChallenges();

		Response res = Response.noContent().build();
		return res;
	}

	@Path("{challenge}")
	public ChallengeResource getChallenge(@PathParam("challenge")
	String id) {
		return new ChallengeResource(uriInfo, request, id);
	}
}
