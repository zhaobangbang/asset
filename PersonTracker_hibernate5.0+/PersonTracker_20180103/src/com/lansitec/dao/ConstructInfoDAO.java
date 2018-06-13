package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.ConstructInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class ConstructInfoDAO {
	private static ConstructInfo constructInfo = null;
	private static List<ConstructInfo> constructList = null;
	private static CriteriaBuilder builder = null;
	private static Root<ConstructInfo> root = null;
	public static void create(ConstructInfo item){
		HibernateUtils.add(item);
	}
	public static void delete(ConstructInfo item){
		HibernateUtils.delete(item);
	}
	public static void update(ConstructInfo item){
		HibernateUtils.add(item);
	}
	
	public static ConstructInfo get(int id){
		constructInfo = (ConstructInfo) HibernateUtils.get(ConstructInfo.class, id);
		return constructInfo;
	}
	
	public static List<ConstructInfo> getAllConstrut() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<ConstructInfo> criteriaQuery = rspCriteriaQuery(sess);
				constructList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<ConstructInfo>(constructList);
	}
	
	public static List<ConstructInfo> getConstructInfosByHQLParament(int page,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(page != 1){
					constructList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					constructList = sess.createQuery(hql).getResultList();
				}
			}
		});
		return new LinkedList<ConstructInfo>(constructList);
	}
	
	public static ConstructInfo getConstructInfoBySN(String constructSN) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<ConstructInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("sn"), constructSN));
				constructList = sess.createQuery(criteriaQuery).getResultList();
				if(constructList.size() == 0){
					constructInfo = null;
				}else{
					constructInfo = constructList.get(0);
				}
			}
		});
		return constructInfo;
	}
	
	public static ConstructInfo getConstructInfoByNM(String constructNM) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<ConstructInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("name"), constructNM));
				constructList = sess.createQuery(criteriaQuery).getResultList();
				if(constructList.size() == 0){
					constructInfo = null;
				}else{
					constructInfo = constructList.get(0);
				}
			}
		});
		return constructInfo;
	}
	
	public static List<ConstructInfo> getConstructInfoByField(String constructField) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<ConstructInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("field"), constructField));
				constructList= sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<ConstructInfo>(constructList);
	}
	
	public static CriteriaQuery<ConstructInfo> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<ConstructInfo> criteriaQuery = builder.createQuery(ConstructInfo.class);
		root = criteriaQuery.from(ConstructInfo.class);
		criteriaQuery.select(root);
		return criteriaQuery;
	}
}
