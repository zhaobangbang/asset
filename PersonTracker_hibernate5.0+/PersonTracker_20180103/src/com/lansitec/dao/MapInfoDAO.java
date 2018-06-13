package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class MapInfoDAO {
    private static List<MapInfo> mapList = null;
    private static MapInfo mapInfo = null;
    private static CriteriaBuilder builder = null;
    private static Root<MapInfo> root = null;
    public static void create(MapInfo item){
    	HibernateUtils.add(item);
    }
    public static void delete(MapInfo item){
    	HibernateUtils.delete(item);
    }
    
    public static void update(MapInfo item){
    	HibernateUtils.update(item);
    }
    
    public static MapInfo get(int id){
    	mapInfo = (MapInfo) HibernateUtils.get(MapInfo.class, id);
    	return mapInfo;
    }
    
    public static List<MapInfo> getAllMapInfo() throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<MapInfo> criteriaQuery = rspCriteriaQuery(sess);
				mapList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
    	return new LinkedList<MapInfo>(mapList);
    }
    
    public static List<MapInfo> getMapInfosByHQLParament(int page,String hql,int start,int max) throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(page != 1){
					mapList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					mapList = sess.createQuery(hql).getResultList();
				}
			}
		});
    	return new LinkedList<MapInfo>(mapList);
    }
    
    public static MapInfo getMapInfoByNM(String name) throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<MapInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("name"), name));
				mapList = sess.createQuery(criteriaQuery).getResultList();
				if(mapList.size() == 0){
					mapInfo = null;
				}else{
					mapInfo = mapList.get(0);
				}
			}
		});
    	return mapInfo;
    }
    
    public static MapInfo getMapInfoByMapid(String mapidsn) throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<MapInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("sn"), mapidsn));
				mapList = sess.createQuery(criteriaQuery).getResultList();
				if(mapList.size() == 0){
					mapInfo = null;
				}else{
					mapInfo = mapList.get(0);
				}
			}
		});
    	return mapInfo;
    }
    
    public static CriteriaQuery<MapInfo> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<MapInfo> criteriaQuery = builder.createQuery(MapInfo.class);
		root = criteriaQuery.from(MapInfo.class);
		criteriaQuery.select(root);
	    return criteriaQuery;
	}
}
