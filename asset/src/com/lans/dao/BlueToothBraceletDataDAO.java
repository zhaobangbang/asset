package com.lans.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.lans.dao.beans.BlueToothBraceletData;
import com.lans.infrastructure.dao.HibTransRunnable;
import com.lans.infrastructure.dao.HibernateUtils;

public class BlueToothBraceletDataDAO {
    static BlueToothBraceletData blueToothBraceletData;
	static List<BlueToothBraceletData> blueTBDataList = new LinkedList<BlueToothBraceletData>();
	public static void create(BlueToothBraceletData item){
		HibernateUtils.add(item);
	}
	
	public static void update(BlueToothBraceletData item){
		HibernateUtils.update(item);
	}
	
	public static BlueToothBraceletData getBlueToothBraceletDataByDev(String deveui) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				Criteria criteria = sess.createCriteria(BlueToothBraceletData.class).add(Restrictions.eq("deveui", deveui));
				blueToothBraceletData = (BlueToothBraceletData) criteria.uniqueResult();
				
			}
		});
		return blueToothBraceletData;
	}

}
