package com.lansitec.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class PositionRecordDAO {
	private static List<PositionRecord> positionRecList = null;
	private static PositionRecord positionRecord = null;
	private static CriteriaBuilder builder = null;
    private static Root<PositionRecord> root = null;
	public static PositionRecord get(int id){
		positionRecord  = (PositionRecord) HibernateUtils.get(PositionRecord.class, id);
		return positionRecord;
	}
	public static void create(PositionRecord item){
		HibernateUtils.add(item);
	}
	public static void delete(PositionRecord item){
		HibernateUtils.delete(item);
	}
	public static void update(PositionRecord item){
		HibernateUtils.update(item);
	}
	
	public static List<PositionRecord> getAllPositionRecord() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<PositionRecord> criteriaQuery = rspCriteriaQuery(sess);
				positionRecList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<PositionRecord>(positionRecList);
	}
	
	public static PositionRecord getPositionDataByDev(String deveui) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				String hql = "from PositionRecord where deveui= :deveui ORDER BY time desc";
				positionRecList = sess.createQuery(hql).setParameter("deveui", deveui).setFirstResult(0).setMaxResults(1).getResultList();
				if(positionRecList.size() == 0){
					positionRecord = null;
				}else{
					positionRecord = positionRecList.get(0);
				}
			}
		});
		return positionRecord;
	}
	                                                                  
	public static List<PositionRecord> getPositionDataByHQLParament(int page,int paramentNum,String paramentName,String paramentValue,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(paramentNum == 0){
					if(page != 1){
						positionRecList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
						positionRecList =  sess.createQuery(hql).getResultList();
					}
				}else if(paramentNum == 1 ){
					if(page != 1){
						positionRecList = sess.createQuery(hql).setParameter(paramentName, paramentValue).setFirstResult(start).setMaxResults(max).getResultList();
					}else{
						positionRecList =  sess.createQuery(hql).setParameter(paramentName, paramentValue).getResultList();
					}
				}
			}
		});
		return new LinkedList<PositionRecord>(positionRecList);
	}
	
	public static List<PositionRecord> getPositionBySQLdeveui(String deveui) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				String hql = "from PositionRecord where deveui= :deveui ORDER BY time desc";
				positionRecList = sess.createQuery(hql).setParameter("deveui", deveui).getResultList();
			}
		});
		return new LinkedList<PositionRecord>(positionRecList);
	}
	
	public static List<PositionRecord> getPositionBydeveuiAndTime(String deveui,String dateTimeOne,String dateTimeTwo) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				/*String hql = "from PositionRecord pr where pr.deveui = :deveui and time BETWEEN '"+dateTimeOne+"' and '"+dateTimeTwo+"' ORDER BY pr.time desc";
				positionRecList = sess.createQuery(hql).setParameter("deveui", deveui).getResultList();*/
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dateOne = sdf.parse(dateTimeOne);
				Date dateTwo = sdf.parse(dateTimeTwo);
				CriteriaQuery<PositionRecord> criteriaQuery = rspCriteriaQuery(sess);
				//time is java.util.date,so the dateOne and dateTwo also are java.util.date
				criteriaQuery.where(builder.equal(root.get("deveui"), builder.literal(deveui)),builder.between(root.get("time"), dateOne, dateTwo));
				criteriaQuery.orderBy(builder.desc(root.get("time")));
				positionRecList = sess.createQuery(criteriaQuery).getResultList();
				
			}
		});
		return new LinkedList<PositionRecord>(positionRecList);
	}
	
	//test
	public static List<PositionRecord> test(String deveui,String deveui1,String deveui2) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				/*String hql = "from PositionRecord pr where pr.deveui = :deveui and time BETWEEN '"+dateTimeOne+"' and '"+dateTimeTwo+"' ORDER BY pr.time desc";
				positionRecList = sess.createQuery(hql).setParameter("deveui", deveui).getResultList();*/
				
				/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dateOne = sdf.parse(dateTimeOne);
				Date dateTwo = sdf.parse(dateTimeTwo);
				CriteriaQuery<PositionRecord> criteriaQuery = rspCriteriaQuery(sess);
				//time is java.util.date,so the dateOne and dateTwo also are java.util.date
				criteriaQuery.where(builder.equal(root.get("deveui"), builder.literal(deveui)),builder.between(root.get("time"), dateOne, dateTwo));
				criteriaQuery.orderBy(builder.desc(root.get("time")));
				positionRecList = sess.createQuery(criteriaQuery).getResultList();*/
				String hql = "from PositionRecord pr where pr.deveui = '"+deveui+"' limit 0,2";
				positionRecList = sess.createQuery(hql).getResultList();
			}
		});
		return new LinkedList<PositionRecord>(positionRecList);
	}
	
   public static CriteriaQuery<PositionRecord> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<PositionRecord> criteriaQuery = builder.createQuery(PositionRecord.class);
		root = criteriaQuery.from(PositionRecord.class);
		criteriaQuery.select(root);
	    return criteriaQuery;
 }
}
