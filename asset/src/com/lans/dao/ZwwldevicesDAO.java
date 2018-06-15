package com.lans.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.dao.beans.Zwwldevices;
import com.lans.infrastructure.dao.HibTransRunnable;
import com.lans.infrastructure.dao.HibernateUtils;

public class ZwwldevicesDAO {
	static Logger logger = LoggerFactory.getLogger(ZwwldevicesDAO.class);
	static List<Zwwldevices> queryRet = new LinkedList<Zwwldevices>();

	public static void create(Zwwldevices item) throws Exception {
		HibernateUtils.add(item);
	}

	public static void create(Zwwldevices item, Session session) throws Exception {
		HibernateUtils.add(item, session);
	}

	public static void update(Zwwldevices item) throws Exception {
		HibernateUtils.update(item);
	}

	public static void update(Zwwldevices item, Session session) throws Exception {
		HibernateUtils.update(item, session);
	}

	public static void delete(Zwwldevices item) throws Exception {
		HibernateUtils.delete(item);
	}

	public static void delete(Zwwldevices item, Session session) throws Exception {
		HibernateUtils.delete(item, session);
	}

	public static void delete(List<Zwwldevices> items) throws Exception {
		for (Zwwldevices item : items) {
			HibernateUtils.delete(item);
		}
	}

	public static void delete(List<Zwwldevices> items, Session session) throws Exception {
		for (Zwwldevices item : items) {
			HibernateUtils.delete(item, session);
		}
	}

	public static List<Zwwldevices> getAllZwwldevices() throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				Criteria criteria = sess.createCriteria(Zwwldevices.class);
				queryRet = criteria.list();
			}
		});

		return new LinkedList<Zwwldevices>(queryRet);
	}
	
	public static List<Zwwldevices> getZwwldeviceByDevid(String devid) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				Criteria criteria = sess.createCriteria(Zwwldevices.class)
										.add(Restrictions.eq("devid", devid));
				queryRet = criteria.list();
			}
		});

		return new LinkedList<Zwwldevices>(queryRet);
	}
	
	public static List<Zwwldevices> getZwwldeviceByGateway(String gateway) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				Criteria criteria = sess.createCriteria(Zwwldevices.class)
										.add(Restrictions.eq("gateway", gateway));
				queryRet = criteria.list();
			}
		});

		return new LinkedList<Zwwldevices>(queryRet);
	}
}
