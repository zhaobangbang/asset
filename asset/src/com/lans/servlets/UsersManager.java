package com.lans.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import com.lans.beans.ObserverInfoBean;
import com.lans.common.*;

import net.sf.json.JSONObject;

import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class UsersManager
 */
@WebServlet("/UsersManager.do")
public class UsersManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(UsersManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsersManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//logger.info("receive do get in UserManager.do ");
		String usrName = request.getParameter("name");
		String type = request.getParameter("type");
		
		if(null == usrName || null == type)
		{
			return;
		}
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");

   		JSONObject jsonRsp = new JSONObject();
	   	if(type.equals("city"))
	   	{
	   		String sql = "select * from users_tbl where username=\""+usrName+"\"";
  		
	   		ResultSet rs = db.executeQuery(sql);
	   		try{
	   			rs.beforeFirst();
	   			if (rs.next()) {
	   				String value = rs.getString("city");
	   				jsonRsp.put("city", value);
	      		
	   				value = rs.getString("area");
	   				jsonRsp.put("area", value);
	   			}
	   			else {
	   				jsonRsp.put("city", "not found");
	   				jsonRsp.put("area", "not found");
	   			}
	   			rs.close();
	   		} catch(SQLException ex) {
	   			logger.error("MyselfManager doGet query error:" + ex.getMessage());
	   		}
	   	}
	   	else if(type.equals("prio"))//获得用户的城市,区域
	   	{
	   		String sql = "select * from users_tbl where username=\""+usrName+"\"";
  		
	   		ResultSet rs = db.executeQuery(sql);
	   		try{
	   			rs.beforeFirst();
	   			if (rs.next()) {
	   				String value = rs.getString("city");
	   				jsonRsp.put("city", value);
	      		
	   				if (!usrName.equals("guest")) {
	   				value = rs.getString("prio");
	   				} else {
	   					value = (String)request.getSession().getAttribute("prio");
	   				}
	   				jsonRsp.put("prio", value);
	   				
	   				value = rs.getString("area");
	   				jsonRsp.put("area", value);
	   			}
	   			else {
	   				jsonRsp.put("city", "not found");
	   				jsonRsp.put("prio", "not found");
	   				jsonRsp.put("area", "not found");
	   			}
	   			rs.close();
	   		} catch(SQLException ex) {
	   			logger.error("MyselfManager doGet query error:" + ex.getMessage());
	   		}
	   	}
	   				
	   	//System.out.println("doGet respond in MySelfManager.do "+jsonRsp.toString());
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(jsonRsp.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String cityName = request.getParameter("city");
        String areaName = request.getParameter("area");
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //防止汉字乱码
        logger.info("receive do post in UserManager.do "+inStr);
        ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
        
        Map<String, String> postMap = new HashMap<String, String>();
        String oper = JqueryGridParser.parserGridString(inStr, postMap);
        String resp = "";
        
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
        
		if(null == oper)
		{
			return;
		}
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			String usrname = (String) reqSession.getAttribute("usrname");
			if(usrname != null)
			{
				if(usrname.equals("guest") && !oper.equals("load"))		
					return;
			}
			else
			{
				return;
			}
		}
		else
		{
			return;
		}
        
        switch (oper){
        case "load": {
        	if(null != cityName)
        	{
        		if(null == areaName)
        		{//获得某个城市下的所有用户,系统管理员/一级区域管理员除外
        			postMap.put("whereField", "city");
        			postMap.put("whereString", cityName);
        			postMap.put("condString", "(prio=\"二级区域管理员\" or prio=\"普通用户\")");
        		}
        		else
        		{//获得某个城市某个区域下的所有普通
        			postMap.put("whereField", "city");
        			postMap.put("whereString", cityName);
        			postMap.put("condString", "area=\""+areaName+"\" and prio=\"普通用户\"");       			
        		}
        	}
        	resp = JqueryGridDBAccess.load(dbm, "users_tbl", postMap);	
        	logger.info("respond to post in UserManager.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add":{
        	if(null != cityName)
        	{
        		if(null == areaName)
        		{//获得某个城市下的所有用户,系统管理员/一级区域管理员除外
        			postMap.put("city", cityName);
        		}
        		else
        		{//获得某个城市某个区域下的所有普通
        			postMap.put("city", cityName);
        			postMap.put("area", areaName); 			
        		}
        	}
        	String now=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        	postMap.put("time", now);
        	JqueryGridDBAccess.insert(dbm, "users_tbl", postMap);
        	break;
        	
        }
        
        case "del":{
        	JqueryGridDBAccess.delete(dbm, "users_tbl ", postMap);
        	String usrName = postMap.get("username");
        	
        	String sql = "delete * from dev_tbl where username = '" + usrName + "'";
        	dbm.executeUpdate(sql);
        	logger.info("--User " + usrName + "  deleted, dev table updated.");
        	
        	ObserverInfoBean obInfo = (ObserverInfoBean)ctx.getAttribute("obsInfo");
            obInfo.deleteObserverInfoBean(usrName);
        	break;
        }
        
        case "edit":{
        	JqueryGridDBAccess.update(dbm, "users_tbl", postMap);
        	break;
        }
        
        case "query":{
        	
        	break;
        	 
         }
        
        default:{
        	 
         }
        
        }//switch
       
	
	}
	

}
