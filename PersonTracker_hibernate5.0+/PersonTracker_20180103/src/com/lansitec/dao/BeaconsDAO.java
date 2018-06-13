package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;

import com.lansitec.dao.beans.Beacons;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class BeaconsDAO {
	private static List<Beacons> beaconsList = null;
    private static Beacons beacons = null;
    private static CriteriaBuilder builder = null;
    private static Root<Beacons> root = null;
    public static void create (Beacons item){
    	HibernateUtils.add(item);
    }
    public static void update(Beacons item){
    	HibernateUtils.update(item);
    }
    public static void delete(Beacons item){
    	HibernateUtils.delete(item);
    }
    public static List<Beacons> getAllBeacons() throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<Beacons> criteriaQuery = rspCriteriaQuery(sess);
				beaconsList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
    	return new LinkedList<Beacons>(beaconsList);
    }
    
    public static List<Beacons> getBeaconsByMapid(String mapid) throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<Beacons> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("mapid"), builder.literal(mapid)));
				beaconsList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
    	return new LinkedList<Beacons>(beaconsList);
    }
    
    public static Beacons getBeaconsBySN(String mapsn) throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<Beacons> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("sn"), builder.literal(mapsn)));
				beaconsList = sess.createQuery(criteriaQuery).getResultList();
				if(beaconsList.size() == 0){
					beacons = null;
				}else{
					beacons = beaconsList.get(0);
				}
			}
		});
    	return beacons;
    }
   public static CriteriaQuery<Beacons> rspCriteriaQuery(Session sess){
	   builder = sess.getCriteriaBuilder();
	   CriteriaQuery<Beacons> criteriaQuery = builder.createQuery(Beacons.class);
	   root = criteriaQuery.from(Beacons.class);
	   criteriaQuery.select(root);
	   return criteriaQuery;
   }
}
