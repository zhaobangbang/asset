package com.lans.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.dao.beans.Beacons;
import com.lans.infrastructure.dao.HibTransRunnable;
import com.lans.infrastructure.dao.HibernateUtils;

public class BeaconsDAO {

	static Logger logger = LoggerFactory.getLogger(BeaconsDAO.class);
	static List<Beacons> queryRet = new LinkedList<Beacons>();
	static int countRet = 0;

	public static void create(Beacons item) throws Exception {
		HibernateUtils.add(item);
	}

	public static void create(Beacons item, Session session) throws Exception {
		HibernateUtils.add(item, session);
	}

	public static void update(Beacons item) throws Exception {
		HibernateUtils.update(item);
	}

	public static void update(Beacons item, Session session) throws Exception {
		HibernateUtils.update(item, session);
	}

	public static void delete(Beacons item) throws Exception {
		HibernateUtils.delete(item);
	}

	public static void delete(Beacons item, Session session) throws Exception {
		HibernateUtils.delete(item, session);
	}

	public static void delete(List<Beacons> items) throws Exception {
		for (Beacons item : items) {
			HibernateUtils.delete(item);
		}
	}

	public static void delete(List<Beacons> items, Session session) throws Exception {
		for (Beacons item : items) {
			HibernateUtils.delete(item, session);
		}
	}

	public static Beacons get(int id) {
		return (Beacons) HibernateUtils.get(Beacons.class, id);
	}

	public static List<Beacons> getAllBeacons() throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				Criteria criteria = sess.createCriteria(Beacons.class);
				queryRet = criteria.list();
			}
		});

		return new LinkedList<Beacons>(queryRet);
	}
	
	public static List<Beacons> getBeaconByMajorMinor(int major, int minor) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				Criteria criteria = sess.createCriteria(Beacons.class).add(Restrictions.eq("major", major))
										.add(Restrictions.eq("minor", minor));
				queryRet = criteria.list();
			}
		});

		return new LinkedList<Beacons>(queryRet);
	}
	
	public static List<Beacons> getBeaconByUsrname(String usrname) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				Criteria criteria = sess.createCriteria(Beacons.class).add(Restrictions.eq("owner", usrname));
				queryRet = criteria.list();
			}
		});

		return new LinkedList<Beacons>(queryRet);
	}
}
