package com.makanstudios.roadalert.api.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.makanstudios.roadalert.api.dao.MainDao;
import com.makanstudios.roadalert.api.model.Alert;

public class AlertResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	String sId;

	public AlertResource(UriInfo uriInfo, Request request, String sId) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.sId = sId;
	}

	// For the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Alert getAlertBrowser() {
		return getAlert();
	}

	// Application integration
	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Alert getAlert() {
		try {
			long id = Long.parseLong(sId);
			Alert alert = MainDao.instance.getAlert(id);
			return alert;
		} catch (NumberFormatException nfe) {
		}

		return null;
	}

	@DELETE
	public Response deleteAlert() {
		try {
			long id = Long.parseLong(sId);
			MainDao.instance.deleteAlert(id);
		} catch (NumberFormatException nfe) {
		}

		Response res = Response.noContent().build();
		return res;
	}
}
