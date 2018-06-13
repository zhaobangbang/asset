package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;

import com.lansitec.dao.beans.DevInfo;
import com.lansitec.enumlist.Devtype;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;
import javax.persistence.criteria.Root;
public class DevInfoDAO {
	private static List<DevInfo> devInfoList = null;
	private static DevInfo devInfo = null;
	private static CriteriaBuilder builder = null;
	private static Root<DevInfo> root = null;
	public static DevInfo get(int id){
		devInfo = (DevInfo) HibernateUtils.get(DevInfo.class, id);
		return devInfo;
	}
	
	public static void create(DevInfo item){
		HibernateUtils.add(item);
	}
	
	public static void delete(DevInfo item){
		HibernateUtils.delete(item);
	}
	
	public static void update(DevInfo item){
		HibernateUtils.update(item);
	}
	
	public static List<DevInfo> getAllDevInfo() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				/*Criteria criteria = sess.createCriteria(DevInfo.class);
				devInfoList = criteria.list();*/
				CriteriaQuery<DevInfo> criteriaQuery = rspCriteriaQuery(sess);
				devInfoList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<DevInfo>(devInfoList);
	}
	
	public static List<DevInfo> getDevInfoByDevtype(String devType) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				/*String hql = "from DevInfo dev where dev.devType= :devType";
				devInfo = (DevInfo) sess.createQuery(hql).setParameter("devType", Devtype.valueOf(devType)).uniqueResult();*/
				
				CriteriaQuery<DevInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("devtype"), Devtype.valueOf(devType)));
				devInfoList= sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<DevInfo>(devInfoList);
	}                                                   
	public static List<DevInfo> getDevInfoByHQLParament(int page,int paramentNum,String paramentName,String paramentValue,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				/*CriteriaQuery<DevInfo> criteriaQuery = rspCriteriaQuery(sess);
				devInfoList = sess.createQuery(criteriaQuery).addQueryHint(sql).getResultList();*/
				/*String hql = "from DevInfo dev where dev.devType= :devType";*/
				if(paramentNum == 0){
					if(page != 1){
						devInfoList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
					    devInfoList = sess.createQuery(hql).getResultList();
					}
					
				}else if(paramentNum == 1){
					if(page != 1){
						devInfoList = sess.createQuery(hql).setParameter(paramentName, paramentValue).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
						devInfoList = sess.createQuery(hql).setParameter(paramentName, paramentValue).getResultList();
					}
				}
			}
		});
		return new LinkedList<DevInfo>(devInfoList);
	}
	
	public static List<DevInfo> getDevInfoByField(String field) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<DevInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("field"), builder.literal(field)));
				devInfoList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<DevInfo>(devInfoList);
	}
	
	public static List<DevInfo> getDevInfoByMapid(String mapid) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<DevInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("mapid"), builder.literal(mapid)));
				devInfoList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<DevInfo>(devInfoList);
	}
	
	
	public static DevInfo getDevInfoByDeveui(String deveui) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				// can use
				/*String hql = "from DevInfo dev where dev.deveui= :deveui";
				devInfo = (DevInfo) sess.createQuery(hql).setParameter("deveui", deveui).uniqueResult();*/
				CriteriaQuery<DevInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("deveui"), builder.literal(deveui)));
				devInfoList = sess.createQuery(criteriaQuery).getResultList();
				if(devInfoList.size() == 0){
					devInfo = null;
				}else{
					devInfo= devInfoList.get(0);
				}
			}
		});
		return devInfo;
	}
	
	public static DevInfo getDevInfoByOwener(String owner) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<DevInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("owner"), builder.literal(owner)));
				devInfoList = sess.createQuery(criteriaQuery).getResultList();
				if(devInfoList.size() == 0){
					devInfo = null;
				}else{
					devInfo= devInfoList.get(0);
				}
			}
		});
		return devInfo;
	}
	
	public static CriteriaQuery<DevInfo> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<DevInfo> criteriaQuery = builder.createQuery(DevInfo.class);
		root = criteriaQuery.from(DevInfo.class);
		//root.fetch(arg0) 关联表用
		criteriaQuery.select(root);
	    return criteriaQuery;
	}
}
