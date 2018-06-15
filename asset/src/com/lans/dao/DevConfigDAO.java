package com.lans.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.lans.dao.beans.DevConfig;
import com.lans.infrastructure.dao.HibTransRunnable;
import com.lans.infrastructure.dao.HibernateUtils;

public class DevConfigDAO {
   static List<DevConfig> queryRet = new LinkedList<>();
   public static void update(DevConfig item){
	   HibernateUtils.update(item);
   }
   
   public static void update(DevConfig item,Session session){
	   HibernateUtils.update(item,session);
   }
   
   public static List<DevConfig> getDevConfigByDevice(String device) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				Criteria criteria = sess.createCriteria(DevConfig.class)
						                .add(Restrictions.eq("deveui",device));
				queryRet = criteria.list();
			}
		});

		return new LinkedList<DevConfig>(queryRet);
	}
   
} 
