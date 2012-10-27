package com.makanstudios.roadalert.api.resources;

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

import com.google.android.gcm.server.Message;
import com.makanstudios.roadalert.api.dao.MainDao;
import com.makanstudios.roadalert.api.gcm.GcmUtils;
import com.makanstudios.roadalert.api.model.Alert;
import com.makanstudios.roadalert.api.utils.CustomConstants;

@Path("/alerts")
public class AlertsResource {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Alert> getAlertsBrowser() {
		return getAlerts();
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Alert> getAlerts() {
		return MainDao.instance.getAlerts();
	}

	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void addAlert(Alert alert) {
		MainDao.instance.addReplaceAlert(alert);

		Message.Builder builder = new Message.Builder();
		builder.addData(CustomConstants.GCM_MESSAGE_TYPE, ""
				+ CustomConstants.GCM_MESSAGE_TYPE_SYNC);
		GcmUtils.sendMessageToAll(builder.build());
	}

	@DELETE
	public Response deleteAllAlerts() {
		MainDao.instance.deleteAllAlerts();

		Response res = Response.noContent().build();
		return res;
	}

	@Path("{alert}")
	public AlertResource getAlert(@PathParam("alert")
	String id) {
		return new AlertResource(uriInfo, request, id);
	}
}
