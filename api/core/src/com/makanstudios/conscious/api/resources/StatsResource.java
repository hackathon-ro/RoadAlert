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
import com.makanstudios.conscious.api.gcm.GcmUtils;
import com.makanstudios.conscious.api.model.Count;
import com.makanstudios.conscious.api.model.Id;
import com.makanstudios.conscious.api.model.Stat;

@Path("/stats")
public class StatsResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Stat> getStatsBrowser() {
		return getStats();
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Stat> getStats() {
		return MainDao.instance.getStats();
	}

	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Id addStat(Stat stat) {
		long id = MainDao.instance.addReplaceStat(stat);
		GcmUtils.scheduleStatsSync();
		return new Id(id);
	}

	@DELETE
	public Response deleteAllStats() {
		MainDao.instance.deleteAllStats();

		Response res = Response.noContent().build();
		return res;
	}

	@Path("{stat}")
	public StatResource getStat(@PathParam("stat")
	String id) {
		return new StatResource(uriInfo, request, id);
	}

	@GET
	@Path("count_accepted")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Count getCountAccepted(@QueryParam("historyId")
	String historyId) {
		if (historyId != null) {
			long id = 0L;
			try {
				id = Long.parseLong(historyId);
			} catch (NumberFormatException nfe) {
				return new Count(0);
			}

			long count = MainDao.instance.getStatsCountAccepted(id);
			return new Count(count);
		} else
			return new Count(0);
	}
}
