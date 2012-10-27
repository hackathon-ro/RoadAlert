package com.makanstudios.roadalert.api.resources;

import java.util.ArrayList;
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

import com.makanstudios.roadalert.api.dao.MainDao;
import com.makanstudios.roadalert.api.gcm.GcmUtils;
import com.makanstudios.roadalert.api.model.Id;
import com.makanstudios.roadalert.api.model.Report;

@Path("/reports")
public class ReportsResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Report> getReportsBrowser() {
		return MainDao.instance.getReports();
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Report> getReports(@QueryParam("userid")
	String userId) {
		if (userId != null) {
			long id = 0L;
			try {
				id = Long.parseLong(userId);
			} catch (NumberFormatException nfe) {
				return new ArrayList<Report>();
			}

			return MainDao.instance.getReports(id);
		} else
			return MainDao.instance.getReports();
	}

	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Id addReport(Report report) {
		long id = MainDao.instance.addReplaceReport(report);
		GcmUtils.scheduleReportsSync();
		return new Id(id);
	}

	@DELETE
	public Response deleteAllReports() {
		MainDao.instance.deleteAllReports();

		Response res = Response.noContent().build();
		return res;
	}

	@Path("{report}")
	public ReportResource getReport(@PathParam("report")
	String id) {
		return new ReportResource(uriInfo, request, id);
	}
}
