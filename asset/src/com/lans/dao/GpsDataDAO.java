package com.lans.dao;


import java.util.LinkedList;
import java.util.List;


import org.hibernate.Session;
import com.lans.dao.beans.GpsData;
import com.lans.infrastructure.dao.HibTransRunnable;
import com.lans.infrastructure.dao.HibernateUtils;


public class GpsDataDAO {
	static List<GpsData> queryRet = new LinkedList<GpsData>();
	static GpsData gpsData ;
	public static void create(GpsData item){
		HibernateUtils.add(item);
	}
	public static List<GpsData> getGpsDataByDevid(String devid) throws Exception {
		HibernateUtils.query(new HibTransRunnable() {
			@SuppressWarnings("unchecked")
			public void run(Session sess) {
				String sql="select * from gps_tbl where  deveui='"+devid+"' ORDER BY time desc limit 0,1";
				queryRet= sess.createSQLQuery(sql).addEntity(GpsData.class).list();
				//因为在获取List的时候没有给List集合具体的类型
				//所以写成：(List<GpsData>)query
				//queryRet = (List<GpsData>)query.list();
			}
		});

		return new LinkedList<GpsData>(queryRet);
	}
}
