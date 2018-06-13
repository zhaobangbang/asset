package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.AssetInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class AssetInfoDAO {
	private static List<AssetInfo> assetInfoList = null;
	private static AssetInfo assetInfo = null;
	private static CriteriaBuilder builder = null;
	private static Root<AssetInfo> root = null;
	public static AssetInfo get(int id){
	     assetInfo = (AssetInfo) HibernateUtils.get(AssetInfo.class, id);
	     return assetInfo;
	}
	public static void create(AssetInfo item){
		HibernateUtils.add(item);
	}
	public static void delete(AssetInfo item){
		HibernateUtils.delete(item);
	}
	
	public static void update (AssetInfo item){
		HibernateUtils.update(item);
	}
	
	public static List<AssetInfo> getAllAssetInfo() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<AssetInfo> criteriaQuery = rspCriteriaQuery(sess);
				assetInfoList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<AssetInfo>(assetInfoList);
	}
	public static List<AssetInfo> getAssetInfoByHQLParament(int page,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(page != 1){
					assetInfoList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					assetInfoList = sess.createQuery(hql).getResultList();
				}
			}
		});
		return assetInfoList;
	}
	
	public static AssetInfo getAssetInfoBySN(String assetSN) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<AssetInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("sn"), builder.literal(assetSN)));
				assetInfoList = sess.createQuery(criteriaQuery).getResultList();
				if(assetInfoList.size() == 0){
					assetInfo = null;
				}else{
					assetInfo= assetInfoList.get(0);
				}
				
			}
		});
		return assetInfo;
	}
	public static CriteriaQuery<AssetInfo> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<AssetInfo> criteriaQuery = builder.createQuery(AssetInfo.class);
		root = criteriaQuery.from(AssetInfo.class);
		//root.fetch(arg0) 关联表用
		criteriaQuery.select(root);
	    return criteriaQuery;
	}
}
