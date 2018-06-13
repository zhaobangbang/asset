package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;

import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class FieldInfoDAO {
   private static List<FieldInfo> fieldList = null;
   private static FieldInfo fieldInfo = null;
   private static CriteriaBuilder builder = null;
   private static Root<FieldInfo> root = null;
   public static void create(FieldInfo item){
	   HibernateUtils.add(item);
   }
   public static void delete(FieldInfo item){
	   HibernateUtils.delete(item);
   }
   public static void update(FieldInfo item){
	   HibernateUtils.update(item);
   }
   
   public static FieldInfo get(int id){
	   FieldInfo field = (FieldInfo) HibernateUtils.get(FieldInfo.class, id);
	   return field;
   }
   
   public static List<FieldInfo> getAllField() throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<FieldInfo> criteriaQuery = rspCriteriaQuery(sess);
			fieldList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	   return new LinkedList<FieldInfo>(fieldList);
   }
   
   public static List<FieldInfo> getFieldInfoByHQLParament(int page,String hql,int start,int max) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void run(Session sess) throws Exception {
			if(page != 1){
				fieldList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
			}else{
				fieldList = sess.createQuery(hql).getResultList();
			}
		}
	});
	   return new LinkedList<FieldInfo>(fieldList);
   }
   
   public static FieldInfo getFieldInfoByNM(String fieldNM) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<FieldInfo> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("name"), fieldNM));
			fieldList = sess.createQuery(criteriaQuery).getResultList();
			if(fieldList.size() == 0){
				fieldInfo = null;
			}else{
				fieldInfo = fieldList.get(0);
			}
		}
	});
	   return fieldInfo;
   }
   
   public static FieldInfo getFieldInfoBySN(String fieldSN) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<FieldInfo> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("sn"), fieldSN));
			fieldList = sess.createQuery(criteriaQuery).getResultList();
			if(fieldList.size() == 0){
				fieldInfo = null;
			}else{
				fieldInfo = fieldList.get(0);
			}
		}
	});
	   return fieldInfo;
   }
   
   public static List<FieldInfo> getFieldInfoByCity(String fieldCity) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<FieldInfo> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("city"), fieldCity));
			fieldList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	   return new LinkedList<FieldInfo>(fieldList);
   }
   public static CriteriaQuery<FieldInfo> rspCriteriaQuery(Session sess){
	   builder = sess.getCriteriaBuilder();
	   CriteriaQuery<FieldInfo> criteriaQuery = builder.createQuery(FieldInfo.class);
	   root = criteriaQuery.from(FieldInfo.class);
	   criteriaQuery.select(root);
	   return criteriaQuery;
   }
}
