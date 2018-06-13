package com.lansitec.infrastructure.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lansitec.systemconfig.dao.ORMFactory;

public class HibernateUtils {

	public static void add(Object obj) {
		Session session = null;
		Transaction tx = null;
		try {
			session = ORMFactory.getCurrSession();
			tx = session.beginTransaction();
			session.save(obj);
			//session.persist(obj);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw e;
		}
	}

	public static void update(Object obj) {
		Session session = null;
		Transaction tx = null;
		try {
			session = ORMFactory.getCurrSession();
			tx = session.beginTransaction();
			session.update(obj);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw e;
		}
	}
	
	public static void delete(Object obj) {
		Session session = null;
		Transaction tx = null;
		try {
			session = ORMFactory.getCurrSession();
			tx = session.beginTransaction();
			session.delete(obj);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw e;
		}
	}

	public static void add(Object obj, Session session) {
		session.save(obj);
	}

	public static void update(Object obj, Session session) {
		session.update(obj);
	}

	public static void delete(Object obj, Session session) {
		session.delete(obj);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object get(Class clazz, int id) {
		Session session = null;
		Transaction tx = null;
		Object ret = null;
		try {
			session = ORMFactory.getCurrSession();
			tx = session.beginTransaction();
			ret = session.get(clazz, new Integer(id));
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return ret;
	}

	public static void execute(HibTransRunnable r) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = ORMFactory.getCurrSession();
			tx = session.beginTransaction();
			r.run(session);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw e;
		}
	}

	public static void query(HibTransRunnable r) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = ORMFactory.getCurrSession();
			tx = session.beginTransaction();
			r.run(session);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
