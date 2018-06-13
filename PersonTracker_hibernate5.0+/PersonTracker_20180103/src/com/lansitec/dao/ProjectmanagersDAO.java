package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class ProjectmanagersDAO {
   private static List<ProjectManagers> proManagersList = null;
   private static ProjectManagers proManagers = null;
   private static CriteriaBuilder builder = null;
   private static Root<ProjectManagers> root = null;
   public static void create(ProjectManagers item){
	   HibernateUtils.add(item);
   }
   public static void delete(ProjectManagers item){
	   HibernateUtils.delete(item);
   }
   public static void update(ProjectManagers item){
	   HibernateUtils.update(item);
   }
   
   public static ProjectManagers get(int id){
	   proManagers = (ProjectManagers) HibernateUtils.get(ProjectManagers.class, id);
	   return proManagers;
   }
   
   public static List<ProjectManagers> getAllUsersManagers() throws Exception{
	  HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<ProjectManagers> criteriaQuery = rspCriteriaQuery(sess);
			proManagersList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	  return new LinkedList<ProjectManagers>(proManagersList);
   }
                                                                  
   public static List<ProjectManagers> getUsersManagersByHQLParament(int page,int paramentNum,String paramentNameOne,String paramentValueOne,String paramentNameTwo,String paramentValueTwo,String hql,int start,int max) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void run(Session sess) throws Exception {
		
			if(paramentNum == 0){
				if(page != 1){
					proManagersList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					proManagersList = sess.createQuery(hql).getResultList();
				}
			}else if(paramentNum == 1){
				if(page != 1){
					proManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					proManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).getResultList();
				}
			}else if(paramentNum == 2){
				if(page != 1){
					proManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).setParameter(paramentNameTwo, paramentValueTwo).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					proManagersList = sess.createQuery(hql).setParameter(paramentNameOne, paramentValueOne).setParameter(paramentNameTwo, paramentValueTwo).getResultList();
				}
			}
		}
	});
	   return proManagersList;
   }
   
   public static ProjectManagers getUsersManagersByUN(String username) throws Exception{
		  HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<ProjectManagers> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("username"), username));
				proManagersList = sess.createQuery(criteriaQuery).getResultList();
				if(proManagersList.size() == 0){
					proManagers = null;
				}else{
					proManagers = proManagersList.get(0);
				}
			}
		});
		  return proManagers;
	   }
   
   public static ProjectManagers getUsersManagersBySN(String sn) throws Exception{
		  HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<ProjectManagers> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("sn"), sn));
				proManagersList = sess.createQuery(criteriaQuery).getResultList();
				if(proManagersList.size() == 0){
					proManagers = null;
				}else{
					proManagers = proManagersList.get(0);
				}
			}
		});
		  return proManagers;
	   }
   
   public static ProjectManagers getUsersManagersByUK(String username,String userkey) throws Exception{
		  HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<ProjectManagers> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("username"), username),builder.equal(root.get("userkey"), userkey));
				proManagersList = sess.createQuery(criteriaQuery).getResultList();
				if(proManagersList.size() == 0){
					proManagers = null;
				}else{
					proManagers = proManagersList.get(0);
				}
			}
		});
		  return proManagers;
	   }
   
   public static List<ProjectManagers> getUsersManagersByCompany(String company) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<ProjectManagers> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("company"), company));
			proManagersList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	   return new LinkedList<ProjectManagers>(proManagersList);
   }
   
   public static List<ProjectManagers> getUsersManagersByCity(String projManagecity) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<ProjectManagers> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("city"), projManagecity));
			proManagersList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	   return new LinkedList<ProjectManagers>(proManagersList);
   }
   
   public static List<ProjectManagers> getUsersManagersByField(String projManageField) throws Exception{
	   HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<ProjectManagers> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("field"), projManageField));
			proManagersList = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	   return new LinkedList<ProjectManagers>(proManagersList);
   }
   
   public static CriteriaQuery<ProjectManagers> rspCriteriaQuery(Session sess){
	   builder = sess.getCriteriaBuilder();
	   CriteriaQuery<ProjectManagers> criteriaQuery = builder.createQuery(ProjectManagers.class);
	   root = criteriaQuery.from(ProjectManagers.class);
	   criteriaQuery.select(root);
	   return criteriaQuery;
	   
	   
   }
}
