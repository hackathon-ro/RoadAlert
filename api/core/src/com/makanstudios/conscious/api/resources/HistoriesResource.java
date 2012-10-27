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
import com.makanstudios.conscious.api.model.History;
import com.makanstudios.conscious.api.model.Id;

@Path("/histories")
public class HistoriesResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<History> getHistoriesBrowser() {
		return getHistories();
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<History> getHistories() {
		return MainDao.instance.getHistories();
	}

	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Id addHistory(History history) {
		long id = MainDao.instance.addReplaceHistory(history);
		return new Id(id);
	}

	@DELETE
	public Response deleteAllHistories() {
		MainDao.instance.deleteAllHistories();

		Response res = Response.noContent().build();
		return res;
	}

	@Path("{history}")
	public HistoryResource getHistory(@PathParam("history")
	String id) {
		return new HistoryResource(uriInfo, request, id);
	}
}
