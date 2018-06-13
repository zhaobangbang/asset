package com.lansitec.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.DevConfig;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class DevConfigDAO {
   private static DevConfig devcfg = null;
   private static List<DevConfig> devcfgList = null;
   private static CriteriaBuilder builder = null;
   private static Root<DevConfig> root = null;
   public static void create(DevConfig item){
	   HibernateUtils.add(item);
   }
   
   public static void update(DevConfig item){
	   HibernateUtils.update(item);
   }
   
   public static void update(DevConfig item,Session session){
	   HibernateUtils.update(item,session);
   }
   
   public static DevConfig getDevConfigByDevice(String device) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) {
				CriteriaQuery<DevConfig> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("deveui"), device));
				devcfgList = sess.createQuery(criteriaQuery).getResultList();
				if(devcfgList.size() == 0){
					devcfg = null;
				}else{
					devcfg = devcfgList.get(0);
				}
			}
		});
		return devcfg;
	}
   public static CriteriaQuery<DevConfig> rspCriteriaQuery(Session sess){
	   builder = sess.getCriteriaBuilder();
	   CriteriaQuery<DevConfig> criteriaQuery = builder.createQuery(DevConfig.class);
	   root = criteriaQuery.from(DevConfig.class);
	   criteriaQuery.select(root);
	   return criteriaQuery;
   }
} 
