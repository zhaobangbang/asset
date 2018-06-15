package com.lans.common;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserMgrDevAccess {
	static Logger logger = LoggerFactory.getLogger(UserMgrDevAccess.class);
	public static List<String> getUserDev(HttpServletRequest request,String username){
		List<String> userDevlist = new ArrayList<String>();
		ServletContext ctx = request.getServletContext();
		DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		String sql = "select deveui from dev_usr_tbl where username='"+username+"'";
		try{
		    ResultSet rs = db.executeQuery(sql);
		    while(rs.next()){
		    userDevlist.add(rs.getString("deveui"));
		    }
		}catch (Exception e) {
			logger.error("UserMgrDevAccess getUserDev 获取不到用户下的deveui:" + e.getMessage());
		}
		return userDevlist;
	}
	
	
}
