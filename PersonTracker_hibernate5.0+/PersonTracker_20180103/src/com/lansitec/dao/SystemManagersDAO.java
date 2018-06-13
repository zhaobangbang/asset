package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class SystemManagersDAO {
	private static SystemManagers managers = null;
	private static List<SystemManagers> systemManagersList = null;
	private static CriteriaBuilder builder = null;
	private static Root<SystemManagers> root = null;
	public static void create(SystemManagers item){
		HibernateUtils.add(item);
	}
	public static void delete(SystemManagers item){
		HibernateUtils.delete(item);
	}
	public static void update(SystemManagers item){
		HibernateUtils.update(item);
	}
	
	public static SystemManagers get(int id){
		managers = (SystemManagers) HibernateUtils.get(SystemManagers.class, id);
		return managers;
	}
	
	public static List<SystemManagers> getAllSystemManagInfo() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<SystemManagers> criteriaQuery = rspCriteriaQuery(sess);
				systemManagersList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<SystemManagers>(systemManagersList);
	}
	
	public static List<SystemManagers> getSystemManagInfoByHQL(String hql,int page,int paramentNum,String paramentNameOne,String paramentValueOne,String paramentNameTwo,String paramentValueTwo,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(paramentNum == 0){
					if(page != 1){
						systemManagersList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
						systemManagersList = sess.createQuery(hql).getResultList();
					}
					
				}else if(paramentNum == 1){
					if(page != 1){
						systemManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
						systemManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).getResultList();
					}
					
				}else if(paramentNum == 2){
					if(page != 1){
						systemManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).setParameter(paramentNameTwo, paramentValueTwo).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
						systemManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).setParameter(paramentNameTwo, paramentValueTwo).getResultList();
					}
				}
			}
		});
		return new LinkedList<SystemManagers>(systemManagersList);
	}
	
	public static SystemManagers getMangersInfoByUK(String username,String userkey) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<SystemManagers> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("username"), username),builder.equal(root.get("userkey"), userkey));
				systemManagersList = sess.createQuery(criteriaQuery).getResultList();
				if(systemManagersList.size() == 0){
					managers = null;
				}else{
					//managers = sess.createQuery(criteriaQuery).getSingleResult();
					managers = systemManagersList.get(0);
				}
			}
		});
		return managers;
	}
	
	public static SystemManagers getMangersInfoByUsername(String username) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<SystemManagers> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("username"), username));
				systemManagersList = sess.createQuery(criteriaQuery).getResultList();
				if(systemManagersList.size() == 0){
					managers = null;
				}else{
					//managers = sess.createQuery(criteriaQuery).getSingleResult();
					managers = systemManagersList.get(0);
				}
			}
		});
		return managers;
	}
	public static CriteriaQuery<SystemManagers> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<SystemManagers> criteriaQuery = builder.createQuery(SystemManagers.class);
		root = criteriaQuery.from(SystemManagers.class);
		criteriaQuery.select(root);
		return criteriaQuery;
	}
}
