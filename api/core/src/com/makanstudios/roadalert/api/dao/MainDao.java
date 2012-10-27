package com.makanstudios.roadalert.api.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.makanstudios.roadalert.api.model.Alert;
import com.makanstudios.roadalert.api.model.Gcm;

public enum MainDao {

	instance;

	private EntityManager em;

	private static final String PERSISTENCE_UNIT = "roadalert";

	private MainDao() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory(PERSISTENCE_UNIT);
		em = factory.createEntityManager();
	}

	/* Alert */

	@SuppressWarnings("unchecked")
	public List<Alert> getAlerts() {
		Query query = em.createQuery("SELECT c FROM Alert c");
		List<Alert> alerts = (List<Alert>) query.getResultList();
		return alerts;
	}

	public long addReplaceAlert(Alert alert) {
		em.getTransaction().begin();
		Alert oldAlert = em.find(Alert.class, alert.id);
		if (oldAlert == null)
			em.persist(alert);
		else
			em.merge(alert);
		em.getTransaction().commit();

		return alert.id;
	}

	public void deleteAllAlerts() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Alert c").executeUpdate();
		em.getTransaction().commit();
	}

	public Alert getAlert(long id) {
		return em.find(Alert.class, id);
	}

	public void deleteAlert(long id) {
		em.getTransaction().begin();
		Alert alert = em.find(Alert.class, id);
		if (alert != null)
			em.remove(alert);
		em.getTransaction().commit();
	}

	/* GCM */

	@SuppressWarnings("unchecked")
	public List<Gcm> getGcmDevices() {
		Query query = em.createQuery("SELECT g FROM Gcm g");
		List<Gcm> devices = (List<Gcm>) query.getResultList();
		return devices;
	}

	public void registerGcmDevice(Gcm device) {
		em.getTransaction().begin();
		em.persist(device);
		em.getTransaction().commit();
	}

	public void unregisterGcmDevice(String regId) {
		em.clear();
		Query query = em
				.createQuery("DELETE FROM Gcm g WHERE g.regId = :regId");
		query.setParameter("regId", regId);

		em.getTransaction().begin();
		query.executeUpdate();
		em.getTransaction().commit();
	}

	/**
	 * Updates the registration id of a device.
	 */
	public void updateGcmDevice(String oldId, String newId) {
		em.clear();
		Query query = em
				.createQuery("UPDATE Gcm g SET g.regId = :newId WHERE g.regId = :oldId");
		query.setParameter("newId", newId);
		query.setParameter("oldId", oldId);

		em.getTransaction().begin();
		query.executeUpdate();
		em.getTransaction().commit();
	}
}
