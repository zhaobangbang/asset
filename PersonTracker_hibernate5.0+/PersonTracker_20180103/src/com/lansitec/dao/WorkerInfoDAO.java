package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.WorkerInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class WorkerInfoDAO {
	private static List<WorkerInfo> workerList = null;
    private static WorkerInfo workerInfo = null;
    private static CriteriaBuilder builder = null;
    private static Root<WorkerInfo> root = null;
    public static void create(WorkerInfo item){
    	HibernateUtils.add(item);
    }
    public static void delete(WorkerInfo item){
    	HibernateUtils.delete(item);
    }
	public static void update(WorkerInfo item){
		HibernateUtils.update(item);
	}
	public static WorkerInfo get(int id){
		workerInfo = (WorkerInfo) HibernateUtils.get(WorkerInfo.class, id);
		return workerInfo;
	}
	
	public static List<WorkerInfo> getAllWorkInfo() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<WorkerInfo> criteriaQuery = rspCriteriaQuery(sess);
				workerList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<WorkerInfo>(workerList);
	}
	public static List<WorkerInfo> getWorkerInfosByHQLParament(int page,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(page != 1 ){
					workerList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					workerList = sess.createQuery(hql).getResultList();
				}
			}
		});
		return new LinkedList<WorkerInfo>(workerList);
	}
	
	public static WorkerInfo getWorkInfoBySN(String worksn) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<WorkerInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("sn"), worksn));
				workerList = sess.createQuery(criteriaQuery).getResultList();
				if(workerList.size() == 0){
					workerInfo = null;
				}else{
					//workerInfo = sess.createQuery(criteriaQuery).getSingleResult();
					workerInfo = workerList.get(0);
				}
			}
		});
		return workerInfo;
	}
	public static CriteriaQuery<WorkerInfo> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<WorkerInfo> criteriaQuery = builder.createQuery(WorkerInfo.class);
		root = criteriaQuery.from(WorkerInfo.class);
		criteriaQuery.select(root);
		return criteriaQuery;
	}
	
}
