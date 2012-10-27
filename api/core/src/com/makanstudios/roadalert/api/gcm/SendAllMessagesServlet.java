/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.makanstudios.roadalert.api.gcm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.makanstudios.roadalert.api.utils.CustomConstants;

/**
 * Servlet that adds a new message to all registered devices.
 * <p>
 * This servlet is used just by the browser (i.e., not device).
 */
@SuppressWarnings("serial")
public class SendAllMessagesServlet extends BaseServlet {

	/**
	 * Processes the request to add a new message.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		StringBuilder status = new StringBuilder();

		if (isValidInput(req, resp, status)) {
			Message message = GcmUtils.buildMessage(req);
			GcmUtils.sendMessageToAll(message);
			status
					.append("Asynchronously sending multicast messages to registered devices");
		}

		req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, status.toString());
		getServletContext().getRequestDispatcher("/gcm/dashboard").forward(req,
				resp);
	}

	public boolean isValidInput(HttpServletRequest req,
			HttpServletResponse resp, StringBuilder status) {
		final int type = Integer.parseInt(req
				.getParameter(CustomConstants.GCM_MESSAGE_TYPE));
		return type == CustomConstants.GCM_MESSAGE_TYPE_SYNC;
	}
}
