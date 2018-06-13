package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.GatewayInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class GatewayInfoDAO {
   private static List<GatewayInfo> gatewayList = null;
   private static GatewayInfo gatewayInfo = null;
   private static CriteriaBuilder builder = null;
   private static Root<GatewayInfo> root = null;
   public static GatewayInfo get(int id){
	   gatewayInfo = (GatewayInfo) HibernateUtils.get(GatewayInfo.class, id);
	   return gatewayInfo;
   }
   public static void create(GatewayInfo item){
	   HibernateUtils.add(item);
   }
   public static void delete(GatewayInfo item){
	   HibernateUtils.delete(item);
   }
   public static void update(GatewayInfo item){
	   HibernateUtils.update(item);
   }
   
   public static List<GatewayInfo> getAllGateway() throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<GatewayInfo> criteriaQuery = rspCriteriaQuery(sess);
			gatewayList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	   return new LinkedList<GatewayInfo>(gatewayList);
   }
   
   public static List<GatewayInfo> getGatewayInfoByFieldSN(String fieldsn) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<GatewayInfo> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("field"), fieldsn));
			gatewayList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	   return new LinkedList<GatewayInfo>(gatewayList);
   }
   
   public static List<GatewayInfo> getGatewayInfoByHQLParament(int page,String hql,int start,int max) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void run(Session sess) throws Exception {
			if(page != 1){
				gatewayList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
			}else{
				gatewayList = sess.createQuery(hql).getResultList();
			}
		}
	});
	   return new LinkedList<GatewayInfo>(gatewayList);
   }
   
   public static CriteriaQuery<GatewayInfo> rspCriteriaQuery(Session sess){
	   builder = sess.getCriteriaBuilder();
	   CriteriaQuery<GatewayInfo> criteriaQuery = builder.createQuery(GatewayInfo.class);
	   root = criteriaQuery.from(GatewayInfo.class);
	   criteriaQuery.select(root);
	   return criteriaQuery;
   }
}
