package com.makanstudios.roadalert.api.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.makanstudios.roadalert.api.model.Challenge;
import com.makanstudios.roadalert.api.model.Gcm;
import com.makanstudios.roadalert.api.model.History;
import com.makanstudios.roadalert.api.model.Report;
import com.makanstudios.roadalert.api.model.Stat;
import com.makanstudios.roadalert.api.model.User;

public enum MainDao {

	instance;

	private EntityManager em;

	private static final String PERSISTENCE_UNIT = "roadalert";

	private MainDao() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory(PERSISTENCE_UNIT);
		em = factory.createEntityManager();
	}

	/* Challenge */

	@SuppressWarnings("unchecked")
	public List<Challenge> getChallenges() {
		Query query = em.createQuery("SELECT c FROM Challenge c");
		List<Challenge> challenges = (List<Challenge>) query.getResultList();
		return challenges;
	}

	public long addReplaceChallenge(Challenge challenge) {
		em.getTransaction().begin();
		Challenge oldChallenge = em.find(Challenge.class, challenge.id);
		if (oldChallenge == null)
			em.persist(challenge);
		else
			em.merge(challenge);
		em.getTransaction().commit();

		return challenge.id;
	}

	public void deleteAllChallenges() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Challenge c").executeUpdate();
		em.getTransaction().commit();
	}

	public Challenge getChallenge(long id) {
		return em.find(Challenge.class, id);
	}

	public void deleteChallenge(long id) {
		em.getTransaction().begin();
		Challenge challenge = em.find(Challenge.class, id);
		if (challenge != null)
			em.remove(challenge);
		em.getTransaction().commit();
	}

	/* User */

	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		Query query = em.createQuery("SELECT u FROM User u");
		List<User> users = (List<User>) query.getResultList();
		return users;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers(String email) {
		em.clear();
		Query query = em
				.createQuery("SELECT u FROM User u WHERE u.email = :email");
		query.setParameter("email", email);
		List<User> users = (List<User>) query.getResultList();
		return users;
	}

	public long addReplaceUser(User user) {
		em.getTransaction().begin();
		User oldUser = em.find(User.class, user.id);
		if (oldUser == null)
			em.persist(user);
		else
			em.merge(user);
		em.getTransaction().commit();

		return user.id;
	}

	public void deleteAllUsers() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM User u").executeUpdate();
		em.getTransaction().commit();
	}

	public User getUser(long id) {
		return em.find(User.class, id);
	}

	public void deleteUser(long id) {
		em.getTransaction().begin();
		User user = em.find(User.class, id);
		if (user != null)
			em.remove(user);
		em.getTransaction().commit();
	}

	/* History */

	@SuppressWarnings("unchecked")
	public List<History> getHistories() {
		Query query = em.createQuery("SELECT h FROM History h");
		List<History> histories = (List<History>) query.getResultList();
		return histories;
	}

	public long addReplaceHistory(History history) {
		em.getTransaction().begin();
		History oldHistory = em.find(History.class, history.id);
		if (oldHistory == null)
			em.persist(history);
		else
			em.merge(history);
		em.getTransaction().commit();

		em.getTransaction().begin();
		History newHistory = em.find(History.class, history.id);
		if (newHistory != null)
			em.refresh(newHistory);
		em.getTransaction().commit();

		return history.id;
	}

	public void deleteAllHistories() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM History h").executeUpdate();
		em.getTransaction().commit();
	}

	public History getHistory(long id) {
		return em.find(History.class, id);
	}

	public void deleteHistory(long id) {
		em.getTransaction().begin();
		History history = em.find(History.class, id);
		if (history != null)
			em.remove(history);
		em.getTransaction().commit();
	}

	/* Report */

	@SuppressWarnings("unchecked")
	public List<Report> getReports() {
		Query query = em.createQuery("SELECT r FROM Report r");
		List<Report> reports = (List<Report>) query.getResultList();
		return reports;
	}

	@SuppressWarnings("unchecked")
	public List<Report> getReports(long userId) {
		em.clear();
		Query query = em
				.createQuery("SELECT r FROM Report r WHERE r.user.id = :userid");
		query.setParameter("userid", userId);
		List<Report> reports = (List<Report>) query.getResultList();
		return reports;
	}

	public long addReplaceReport(Report report) {
		em.getTransaction().begin();
		Report oldReport = em.find(Report.class, report.id);
		if (oldReport == null)
			em.persist(report);
		else
			em.merge(report);
		em.getTransaction().commit();

		em.getTransaction().begin();
		Report newReport = em.find(Report.class, report.id);
		if (newReport != null)
			em.refresh(newReport);
		em.getTransaction().commit();

		return report.id;
	}

	public void deleteAllReports() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Report r").executeUpdate();
		em.getTransaction().commit();
	}

	public Report getReport(long id) {
		return em.find(Report.class, id);
	}

	public void deleteReport(long id) {
		em.getTransaction().begin();
		Report report = em.find(Report.class, id);
		if (report != null)
			em.remove(report);
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

	/* Stat */

	@SuppressWarnings("unchecked")
	public List<Stat> getStats() {
		em.clear();
		Query query = em.createQuery("SELECT s FROM Stat s");
		List<Stat> stats = (List<Stat>) query.getResultList();
		return stats;
	}

	public long addReplaceStat(Stat stat) {
		em.getTransaction().begin();
		Stat oldStat = em.find(Stat.class, stat.id);
		if (oldStat == null)
			em.persist(stat);
		else
			em.merge(stat);
		em.getTransaction().commit();

		em.getTransaction().begin();
		Stat newStat = em.find(Stat.class, stat.id);
		if (newStat != null)
			em.refresh(newStat);
		em.getTransaction().commit();

		return stat.id;
	}

	public long getStatsCountAccepted(long historyId) {
		em.clear();
		Query query = em
				.createQuery("SELECT s FROM Stat s WHERE s.history.id = :historyId");
		query.setParameter("historyId", historyId);
		return query.getResultList().size();
	}

	public void deleteAllStats() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Stat s").executeUpdate();
		em.getTransaction().commit();
	}

	public Stat getStat(long id) {
		return em.find(Stat.class, id);
	}

	public void deleteStat(long id) {
		em.getTransaction().begin();
		Stat stat = em.find(Stat.class, id);
		if (stat != null)
			em.remove(stat);
		em.getTransaction().commit();
	}
}
