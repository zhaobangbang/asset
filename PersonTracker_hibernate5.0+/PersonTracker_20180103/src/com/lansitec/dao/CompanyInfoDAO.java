package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.CompanyInfo;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class CompanyInfoDAO {
	private static List<CompanyInfo> companyList = null;
	private static CompanyInfo companyInfo = null;
	private static CriteriaBuilder builder = null;
	private static Root<CompanyInfo> root = null;
	public static CompanyInfo get(int id){
		CompanyInfo company = (CompanyInfo) HibernateUtils.get(CompanyInfo.class, id);
		return company;
	}
	public static void create(CompanyInfo item){
		HibernateUtils.add(item);
	}
	public static void delete(CompanyInfo item){
		HibernateUtils.delete(item);
	}
	public static void update(CompanyInfo item){
		HibernateUtils.update(item);
	}
	public static  List<CompanyInfo> getAllCompanyInfo() throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<CompanyInfo> criteriaQuery = rspCriteriaQuery(sess);
				companyList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
		return new LinkedList<CompanyInfo>(companyList);
	}
	
	public static  List<CompanyInfo> getCompanyInfoByHQLParament(int page,String hql,int start,int max) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				
				if(page != 1){
					companyList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					companyList = sess.createQuery(hql).getResultList();
				}
			}
		});
		return new LinkedList<CompanyInfo>(companyList);
	}
	
	public static CompanyInfo getCompanyInfoBySN(String companysn) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<CompanyInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("sn"), builder.literal(companysn)));
				companyList = sess.createQuery(criteriaQuery).getResultList();
				if(companyList.size() == 0){
					companyInfo = null;
				}else{
					companyInfo =companyList.get(0);
				}
			}
		});
		return companyInfo;
	}
	
	public static CompanyInfo getCompanyInfoByNM(String companyNM) throws Exception{
		HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<CompanyInfo> criteriaQuery = rspCriteriaQuery(sess);
				criteriaQuery.where(builder.equal(root.get("name"), companyNM));
				companyList = sess.createQuery(criteriaQuery).getResultList();
				if(companyList.size() == 0){
					companyInfo = null;
				}else{
					companyInfo = companyList.get(0);
				}
			}
		});
		return companyInfo;
	}
	
	public static CriteriaQuery<CompanyInfo> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<CompanyInfo> criteriaQuery = builder.createQuery(CompanyInfo.class);
		root = criteriaQuery.from(CompanyInfo.class);
		criteriaQuery.select(root);
		return criteriaQuery;
	}
}
