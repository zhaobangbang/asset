package com.lans.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.common.JqueryGridDBAccess;
import com.lans.common.JqueryGridParser;

/**
 * Servlet implementation class AreaManager
 * 二级区域管理
 */
@WebServlet("/AreaManager.do")
public class AreaManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(AreaManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AreaManager() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("receive do get in AreaManager.do");
		String cityName = request.getParameter("name");
		
		if(null == cityName)
			cityName = "呼和浩特";


        ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
       	
		String sql = "select * from area_tbl where city='" + cityName + "'";
		ResultSet rs;
		ArrayList<String> AreaList = new ArrayList<String>();
		
		try{
	       	rs = dbm.executeQuery(sql);
	       	rs.beforeFirst();
	       	while(rs.next()) {
	       			AreaList.add(rs.getString("area"));
	       		}
	       	rs.close();
	    } catch(SQLException ex) {
	    	logger.error("executeQuery1:" + ex.getMessage());
	    }
		//this is the special response for JQGrid select column
		//the 2nd level select seems can't include the Select because it will be .append(str);
		String resp = "";
		for (String area:AreaList) {
			resp += "<option value=\""+area+"\">"+area+"</option>";
		}

        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//System.out.println("respond do get in AreaManager.do "+resp);
		response.getWriter().write(resp);
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
    
		String cityName = request.getParameter("name");
		if(null == cityName)
			cityName = "呼和浩特";
		
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //防止汉字乱码
        logger.info("receive do post in AreaManager.do "+inStr+" user "+cityName);
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
        	postMap.put("whereField", "city");
        	postMap.put("whereString", cityName);
        	String sql = "select * from area_tbl where city =\"" + cityName + "\"";
        	ResultSet rs = dbm.executeQuery(sql);
        	if(null != rs)
        	{
        		try{
        			while(rs.next())
        			{//从设备表和网关表中获得各区域下的数量.
        				String areaName = rs.getString("area");
        				sql = "select count(*) as devnum from dev_list_tbl where city=\"" + cityName + "\" and area=\"" + areaName + "\"";
        				ResultSet ars = dbm.executeQuery(sql);
        				int devNumber = 0;
        				if(ars.next())
        					devNumber = ars.getInt("devnum");
        				sql = "select count(*) as gwnum from gateway_tbl where city=\"" + cityName + "\" and area=\"" + areaName + "\"";
        				ars = dbm.executeQuery(sql);
        				int gwNumber = 0;
        				if(ars.next())
        					gwNumber = ars.getInt("gwnum");
        				sql ="update area_tbl set motenumber=" + devNumber + ", gwnumber=" + gwNumber + " where city=\"" + cityName + "\" and area=\"" + areaName + "\"";
        				dbm.executeUpdate(sql);
        				ars.close();
        			}
        			rs.close();
        		}catch(SQLException ex) {
        			logger.error("executeQuery:"+ ex.getMessage());
        			return;
        		}
        	}
        	resp = JqueryGridDBAccess.load(dbm, "area_tbl", postMap);
        	logger.info("respond to post in AreaManager.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add":{
        	postMap.put("city", cityName);
        	if(JqueryGridDBAccess.insert(dbm, "area_tbl", postMap) > 0)
        	{
        		logger.info("add: " + postMap.get("area"));   		
        	}
        	logger.info("city:" + cityName);
        	break;
        	
        }
        
        case "del":{
        	postMap.put("whereField", "city");
        	postMap.put("whereString", cityName);        	
  		
        	if(JqueryGridDBAccess.delete(dbm, "area_tbl", postMap) > 0)
        	{
        		logger.info("del oper was on");
        	}
        	break;
        }
        
        case "edit":{   		
        	postMap.put("whereField", "city");
        	postMap.put("whereString", cityName);

        	if(JqueryGridDBAccess.update(dbm, "area_tbl", postMap) > 0)
        	{
        		logger.info("edit oper was on");      		
        	}
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
