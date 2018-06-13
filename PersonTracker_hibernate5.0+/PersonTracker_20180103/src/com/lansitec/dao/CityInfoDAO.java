package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.CityInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class CityInfoDAO {
  private static CityInfo city = null;
  private static List<CityInfo>  citylist= null;
  private static CriteriaBuilder builder = null;
  private static Root<CityInfo> root = null;
  public static void create(CityInfo item){
	  HibernateUtils.add(item);
  }
  public static void delete(CityInfo item){
	  HibernateUtils.delete(item);
  }
  public static void update(CityInfo  item){
	  HibernateUtils.update(item);
  }
  public static CityInfo get(int id){
	  city = (CityInfo) HibernateUtils.get(CityInfo.class, id);
	  return city;
  }
  
  public static CityInfo getCityInfo(String cityName) throws Exception{
	  HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<CityInfo> criteriaQuery = rspCriteriaQuery(sess);
			criteriaQuery.where(builder.equal(root.get("city"), builder.literal(cityName)));
			citylist = sess.createQuery(criteriaQuery).getResultList();
			if(citylist.size() == 0){
				city = null;
			}else{
				city = citylist.get(0);
			}
		}
	});
	  return city;
  }
  
  public static List<CityInfo> getAllCityInfo() throws Exception{
	  HibernateUtils.query(new HibTransRunnable() {
		
		@Override
		public void run(Session sess) throws Exception {
			CriteriaQuery<CityInfo> criteriaQuery = rspCriteriaQuery(sess);
			citylist = sess.createQuery(criteriaQuery).getResultList();
		}
	});
	 return new LinkedList<CityInfo>(citylist);
  }
  public static List<CityInfo> getCityInfoByHQL(int page,String hql,int start,int max) throws Exception{
	  HibernateUtils.query(new HibTransRunnable() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void run(Session sess) throws Exception {
			if(page != 1){
				citylist = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
			}else{
				citylist = sess.createQuery(hql).getResultList();
			}
		}
	});
	 return new LinkedList<CityInfo>(citylist);
  }
  
  public static CriteriaQuery<CityInfo> rspCriteriaQuery(Session sess){
	  builder = sess.getCriteriaBuilder();
	  CriteriaQuery<CityInfo> criteriaQuery = builder.createQuery(CityInfo.class);
	  root = criteriaQuery.from(CityInfo.class);
	  criteriaQuery.select(root);
	  return criteriaQuery;
			  
  }
}
