package com.makanstudios.roadalert.api.gcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.makanstudios.roadalert.api.dao.MainDao;
import com.makanstudios.roadalert.api.model.Gcm;
import com.makanstudios.roadalert.api.utils.CustomConstants;

public class GcmUtils {

	private static final int MULTICAST_SIZE = 1000;

	private static Sender sender = new Sender(CustomConstants.APP_KEY);

	private static final Executor threadPool = Executors.newFixedThreadPool(5);

	private static final Logger logger = Logger.getLogger(GcmUtils.class
			.getName());

	public static void sendMessageToAll(Message message) {
		List<Gcm> devices = MainDao.instance.getGcmDevices();
		int total = devices.size();
		List<String> partialDevices = new ArrayList<String>(total);
		int counter = 0;

		for (Gcm device : devices) {
			counter++;
			partialDevices.add(device.regId);
			int partialSize = partialDevices.size();
			if (partialSize == MULTICAST_SIZE || counter == total) {
				asyncSend(partialDevices, message);
				partialDevices.clear();
			}
		}
	}

	private static void asyncSend(List<String> partialDevices,
			final Message message) {
		final List<String> devices = new ArrayList<String>(partialDevices);

		threadPool.execute(new Runnable() {

			public void run() {
				MulticastResult multicastResult;
				try {
					multicastResult = sender.send(message, devices, 5);
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error posting messages", e);
					return;
				}
				List<Result> results = multicastResult.getResults();
				// analyze the results
				for (int i = 0; i < devices.size(); i++) {
					String regId = devices.get(i);
					Result result = results.get(i);
					String messageId = result.getMessageId();
					if (messageId != null) {
						logger.fine("Succesfully sent message to device: "
								+ regId + "; messageId = " + messageId);
						String canonicalRegId = result
								.getCanonicalRegistrationId();
						if (canonicalRegId != null) {
							// same device has more than on registration id:
							// update it
							logger.info("canonicalRegId " + canonicalRegId);
							MainDao.instance.updateGcmDevice(regId,
									canonicalRegId);
						}
					} else {
						String error = result.getErrorCodeName();
						if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
							// application has been removed from device -
							// unregister it
							logger.info("Unregistered device: " + regId);
							MainDao.instance.unregisterGcmDevice(regId);
						} else {
							logger.severe("Error sending message to " + regId
									+ ": " + error);
						}
					}
				}
			}
		});
	}
}
