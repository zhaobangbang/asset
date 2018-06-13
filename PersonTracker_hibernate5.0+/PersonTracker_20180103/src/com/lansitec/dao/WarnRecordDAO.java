package com.lansitec.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.lansitec.dao.beans.WarnRecord;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class WarnRecordDAO {
	private static WarnRecord warnRecord = null;
	private static List<WarnRecord> warnRecordsList = null;
	private static CriteriaBuilder builder = null;
	private static Root<WarnRecord> root = null;
	public static void create(WarnRecord item){
		HibernateUtils.add(item);
	}
	public static void delete(WarnRecord item){
		HibernateUtils.delete(item);
	}
    public static WarnRecord get(int id){
    	warnRecord = (WarnRecord) HibernateUtils.get(WarnRecord.class, id);
    	return warnRecord;
    }
	public static void update(WarnRecord item){
		HibernateUtils.update(item);
	}
	public static WarnRecord getWarnRecordByDeveui(String deveui) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<WarnRecord> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("deveui"), deveui));
				criteriaQuery.orderBy(builder.desc(root.get("warn_stime")));
				warnRecordsList = sess.createQuery(criteriaQuery).setFirstResult(0).setMaxResults(1).getResultList();
				if(warnRecordsList.size() == 0){
					warnRecord = null;
				}else{
					//warnRecord = sess.createQuery(criteriaQuery).setFirstResult(0).setMaxResults(1).getSingleResult();
					warnRecord = warnRecordsList.get(0);
				}
			}
		});
		return warnRecord;
	}
	
	public static List<WarnRecord> getWarnRecordsListByHQL(String hql,String limitName) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if((null == limitName) || (limitName.equals(""))){
					warnRecordsList = sess.createQuery(hql).getResultList();
				}else{
					warnRecordsList = sess.createQuery(hql).setFirstResult(0).setMaxResults(1).getResultList();
				}
			    
			}
		});
		return warnRecordsList;
	}
	
	public static List<WarnRecord> getWarnRecordsListByHQLParament(int page,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(page != 1){
					warnRecordsList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					warnRecordsList = sess.createQuery(hql).getResultList();
				}
			}
		});
		return warnRecordsList;
	}
	public static CriteriaQuery<WarnRecord> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<WarnRecord> criteriaQuery = builder.createQuery(WarnRecord.class);
		root = criteriaQuery.from(WarnRecord.class);
		criteriaQuery.select(root);
		return criteriaQuery;
	}
}
