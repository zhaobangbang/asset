package com.lans.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.dao.beans.ZwwlDevsUseInfo;
import com.lans.infrastructure.dao.HibTransRunnable;
import com.lans.infrastructure.dao.HibernateUtils;

public class ZwwlDevsUseInfoDAO {
	static Logger logger = LoggerFactory.getLogger(ZwwlDevsUseInfoDAO.class);
	static List<ZwwlDevsUseInfo> queryRet = new LinkedList<ZwwlDevsUseInfo>();
	
	public static List<ZwwlDevsUseInfo> getZwwlUsernameByDevid(String deveui) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				logger.info("select devid {} from dev_usr_list",deveui);
				Criteria criteria = sess.createCriteria(ZwwlDevsUseInfo.class)
										.add(Restrictions.eq("deveui", deveui));//add方法里为查询条件
				queryRet = criteria.list();
			}
		});

		return new LinkedList<ZwwlDevsUseInfo>(queryRet);
	}

}
