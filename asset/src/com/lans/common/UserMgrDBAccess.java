package com.lans.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMgrDBAccess {
	static Logger logger = LoggerFactory.getLogger(UserMgrDBAccess.class);
	public static String userExist(HttpServletRequest request, String username){
		if(null == username)
	   	   	return "";
	   	   	
		String bExists = "";
		ServletContext ctx = request.getServletContext();
		DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		String sql = "select * from users_tbl where username=\'" + username + "\'";
		ResultSet rs = null;
		try{
	       	rs = db.executeQuery(sql);
	        if(rs != null && rs.next())
	        {
	        	bExists = rs.getString("prio"); //The user already exists, return the priority.
	        	if(bExists.equals("") || bExists.equals("0"))
	        		bExists = "∆’Õ®”√ªß";
	        }
	        else
	    	   bExists = "";
	        rs.close();
	    	  }catch(SQLException ex){
	    	  	logger.error("executeQuery:" + ex.getMessage());
	    	  }
	    	  
	    	  return bExists;
	   }
	public static boolean userValid(HttpServletRequest request, String username, String tel, String email, String uid){
		if(null == username)
	   	   	return false;
	   	   	
		boolean bExists = false;
		ServletContext ctx = request.getServletContext();
		DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		String sql = "select * from users_tbl where username=\'" + username + "\'";
		ResultSet rs = null;
		try{
	       	rs = db.executeQuery(sql);
	        if(rs.next())
	        {
	        	String _tel = rs.getString("usertel");
	        	String _email = rs.getString("usermail");
	        	String _uid = rs.getString("uid");
	        	
	        	if(tel.equals(_tel) && email.equals(_email) && uid.equals(_uid))
	        		bExists = true;
	        	else
	        		bExists = false;
	        		
	        }
	        else
	    	   bExists = false;
	    	  rs.close();
	    	  }catch(SQLException ex){
	    		  logger.error("executeQuery:" + ex.getMessage());
	    	  }
	    	  
	    	  return bExists;
	   }
}
