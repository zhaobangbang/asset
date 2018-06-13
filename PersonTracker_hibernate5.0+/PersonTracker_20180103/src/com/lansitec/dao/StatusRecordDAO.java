package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.lansitec.dao.beans.StatusRecord;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class StatusRecordDAO {
	private static StatusRecord statusRecord = null;
	private static List<StatusRecord> statusRecordsList = null;
	private static CriteriaBuilder builder = null;
	private static Root<StatusRecord> root = null;
	public static void create(StatusRecord item){
		HibernateUtils.add(item);
	}
	public static void delete(StatusRecord item){
		HibernateUtils.delete(item);
	}
	public static void update(StatusRecord item){
	    HibernateUtils.update(item);
	}
	public static StatusRecord get(int id){
		statusRecord = (StatusRecord) HibernateUtils.get(StatusRecord.class, id);
		return statusRecord;
	}
	
	public static List<StatusRecord> getAllStatusRecord() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<StatusRecord> criteriaQuery = rspCriteriaQuery(sess);
				statusRecordsList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<StatusRecord>(statusRecordsList);
	}
	                                                            
	public static List<StatusRecord> getStatusRecordByDiffParament(int page,int paramenetNum,String paramentName,String paramentValue,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(paramenetNum == 0){
					if(page != 1){
						statusRecordsList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
						statusRecordsList = sess.createQuery(hql).getResultList();
					}
				}else if(paramenetNum == 1){
					if(page != 1){
						statusRecordsList = sess.createQuery(hql).setParameter(paramentName, paramentValue).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
					    statusRecordsList = sess.createQuery(hql).setParameter(paramentName, paramentValue).getResultList();
					}
				}
			}
		});
		return new LinkedList<StatusRecord>(statusRecordsList);
	}
	
    public static StatusRecord getStatusRecordDataByDev(String deveui) throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				String hql = "from StatusRecord str where str.deveui= :deveui ORDER BY time desc";
				statusRecordsList = sess.createQuery(hql).setParameter("deveui", deveui).setFirstResult(0).setMaxResults(1).getResultList();
				if(statusRecordsList.size() == 0){
					statusRecord = null;
				}else{
					//statusRecord = (StatusRecord) sess.createQuery(hql).setFirstResult(0).setMaxResults(1).getSingleResult();
					statusRecord = statusRecordsList.get(0);
				}
			}
		});
    	return statusRecord;
    }
    
    public static CriteriaQuery<StatusRecord> rspCriteriaQuery(Session sess){
    	builder = sess.getCriteriaBuilder();
    	CriteriaQuery<StatusRecord> criteriaQuery = builder.createQuery(StatusRecord.class);
    	root = criteriaQuery.from(StatusRecord.class);
    	criteriaQuery.select(root);
    	return criteriaQuery;
    	
    }
}
