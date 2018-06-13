package com.lansitec.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import com.lansitec.dao.beans.LogRecord;
import com.lansitec.infrastructure.dao.HibTransRunnable;
import com.lansitec.infrastructure.dao.HibernateUtils;

public class LogRecordDAO {
	private static LogRecord logRecord = null;
	private static List<LogRecord> logRecordList = null;
	private static CriteriaBuilder builder = null;
	private static Root<LogRecord> root = null;
    public static void create(LogRecord item){
    	HibernateUtils.add(item);
    }
    public static void create(LogRecord item,Session session){
    	HibernateUtils.add(item,session);
    }
    public static LogRecord get(int id){
    	logRecord = (LogRecord) HibernateUtils.get(LogRecord.class, id);
    	return logRecord;
    }
    public static void delete(LogRecord item){
    	HibernateUtils.delete(item);
    }
    public static List<LogRecord> getAllLogRecord() throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@Override
			public void run(Session sess) throws Exception {
				CriteriaQuery<LogRecord> criteriaQuery = rspCriteriaQuery(sess);
				logRecordList = sess.createQuery(criteriaQuery).getResultList();
			}
		});
    	return new LinkedList<LogRecord>(logRecordList);
    }
    
    public static List<LogRecord> getLogRecordsByHQLParament(int page,String hql,int start,int max) throws Exception{
    	HibernateUtils.query(new HibTransRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run(Session sess) throws Exception {
				if(page != 1){
					logRecordList = sess.createQuery(hql).setFirstResult(start).setMaxResults(max).getResultList();
				}else{
					logRecordList = sess.createQuery(hql).getResultList();
				}
			}
		});
    	return new LinkedList<LogRecord>(logRecordList);
    }
    public static CriteriaQuery<LogRecord> rspCriteriaQuery(Session sess){
		builder = sess.getCriteriaBuilder();
		CriteriaQuery<LogRecord> criteriaQuery = builder.createQuery(LogRecord.class);
		root = criteriaQuery.from(LogRecord.class);
		//root.fetch(arg0) 关联表用
		criteriaQuery.select(root);
	    return criteriaQuery;
	}
}
