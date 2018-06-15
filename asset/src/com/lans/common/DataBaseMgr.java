package com.lans.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.RowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.systemconfig.dao.ORMFactory;
import com.sun.rowset.CachedRowSetImpl;

public class DataBaseMgr {
	private String sqlUser;
	private String sqlKey;
	private String sqlEncode;
	private Connection connection;
	public static DataBaseMgr instance;
	private Logger logger = LoggerFactory.getLogger(DataBaseMgr.class);
	
	public static DataBaseMgr getInstance() {
		if (instance == null) {
			instance = new DataBaseMgr();
		}
		
		return instance;
	}

	public DataBaseMgr() {
		// TODO Auto-generated constructor stub
		sqlUser = "lansiadmin";
	    sqlKey = "lansi2016";
	    sqlEncode = "utf8";

	    String url;
	    if(ORMFactory.localDebug)
	    {
	    	url="jdbc:mysql://localhost:3306/asset" + "?user=root&password=lansi2016&useUnicode=true&characterEncoding="+sqlEncode;
	    }
	    else
	    {
	    	if(ORMFactory.officialEnv)
	    	    url="jdbc:mysql://rm-m5eb9dwj48o96ca37.mysql.rds.aliyuncs.com:3306/asset" + "?user=" + sqlUser + "&password=" + sqlKey + "&useUnicode=true&characterEncoding="+sqlEncode;
	    	else
	    	    url="jdbc:mysql://rm-m5eb9dwj48o96ca37.mysql.rds.aliyuncs.com:3306/assettest" + "?user=" + sqlUser + "&password=" + sqlKey + "&useUnicode=true&characterEncoding="+sqlEncode;
	    }
	    try {
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	     } catch(Exception e) {
	    	 logger.error("Fail to find Database Driver.");
	     }
	    
	    try {
	    	connection=DriverManager.getConnection(url);
	      } catch(SQLException e) {
	    	 logger.error("connectDB:" + e.getMessage());
	      }  
	}
	
	public void closeDateBase() {
		try {
			connection.close();
		} catch(SQLException e) {
			logger.error("closeDB:" + e.getMessage());
	    }
	}
	
	public synchronized int executeUpdate(String sql) {
		int ret = 0;
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			ret = statement.executeUpdate(sql);
			statement.close();
		} catch(SQLException ex) {
			logger.error("executeUpdate:" + ex.getMessage());
		}
	    
		return ret;
	}

	public synchronized RowSet executeQuery(String sql) {
		ResultSet rs = null;
		CachedRowSetImpl rowset = null;
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			rowset = new CachedRowSetImpl();
			rowset.populate(rs);
			rs.close();
			statement.close();
		} catch(SQLException ex) {
			logger.error("executeQuery:"+ ex.getMessage());
			return null;
		}
		
		return rowset;
	}
	
	public synchronized int executePrepareStatement(String sqlStatement, Map<String,String> DataMap) {
		PreparedStatement ps = null;
		int ret = 0;
		//System.out.println("Arraylist:"+args);
		try{
			ps = connection.prepareStatement(sqlStatement);
			/*int i = 1;
			for (String v : args) {
				ps.setString(i, v);
				i++;
			}*/
			int i=1;
            for (Map.Entry<String, String> entry : DataMap.entrySet()) {
                 ps.setString(i,entry.getValue());
                 i++;
			}
			ret = ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			logger.error("sql:" + ps.toString());
			logger.error("executeQuery:"+ ex.getMessage());
		}
		
		return ret;
	}
}
