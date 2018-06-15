package com.lans.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.alibaba.fastjson.JSONArray;
import com.lans.common.DataBaseMgr;
import com.lans.common.JqueryGridDBAccess;
import com.lans.common.JqueryGridParser;

import net.sf.json.JSONObject;
/**
 * Servlet implementation class MapManager
 * 地图管理.
 */
@WebServlet("/MapManager.do")
public class MapManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(MapManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapManager() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("receive do get in MapManager.do");
		String resp = "<option value=\"no\">no</option>";;
		
		String username = request.getParameter("name");
		String mapid = request.getParameter("mapid");
			ServletContext ctx = request.getServletContext();
	       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
	       	
		if(null != username && null == mapid)
        {	  	
			String sql = "select * from map_tbl where owner='" + username + "'";
			ResultSet rs = null;
			ArrayList<String> mapList = new ArrayList<String>();
			
			try{
		       	rs = dbm.executeQuery(sql);
		       	rs.beforeFirst();
		       	while(rs.next()) {
		       			mapList.add(rs.getString("sn"));
		       		}
		       	rs.close();
		    } catch(SQLException ex) {
		    	logger.error("MapManager.doGet:" + ex.getMessage());
		    }
			//this is the special response for JQGrid select column
	        resp="";
			for (String map:mapList) {
				resp += "<option value=\""+map+"\">"+map+"</option>";
			}
        }
		else if(null != username && null != mapid) //获得室内地图位置
	   	{
			if(mapid.equals("ALL"))
	   		{
				String sql = "select * from map_tbl where owner='"+username+"'";
		   		
		   		try{	   			
		   			JSONArray jArray = new JSONArray();
		   			ResultSet rs = dbm.executeQuery(sql);
		   			if(null != rs){
			   			rs.beforeFirst();
			   			while(rs.next()) {
			   				JSONObject jsonRsp = new JSONObject();
			   				jsonRsp.put("mapid", rs.getString("sn"));
			   				double value = rs.getDouble("indoorX");
			   				jsonRsp.put("indoorX", value);
			      		
			   				value = rs.getDouble("indoorY");
			   				jsonRsp.put("indoorY", value);
			   				
			   				jsonRsp.put("indoorTitle", rs.getString("indoorTitle"));
			   				jsonRsp.put("zoom", rs.getByte("zoom"));
			   				jArray.add(jsonRsp);
			   			}
			   			
			   			rs.close();
		   		    }
		   			resp = jArray.toString();
		   		} catch(SQLException ex) {
		   			logger.error("MyselfManager doGet query error:" + ex.getMessage());
		   		}
	   		}
			else {
			  //获得某一个地图的信息
				String sql = "select * from map_tbl where owner='"+username+"' and sn = '" + mapid + "'";
		   		JSONObject jsonRsp = new JSONObject();
		   		try{
			   		ResultSet rs = dbm.executeQuery(sql);
			   		if(null != rs)
		   			{
				   		rs.beforeFirst();
			   			
			   			if (rs.next()) {
			   				double value = rs.getDouble("indoorX");
			   				jsonRsp.put("indoorX", value);
			      		
			   				value = rs.getDouble("indoorY");
			   				jsonRsp.put("indoorY", value);
			   				
			   				jsonRsp.put("indoorTitle", rs.getString("indoorTitle"));
			   				jsonRsp.put("zoom", rs.getByte("zoom"));
			   			}
			   			else {
			   				jsonRsp.put("indoorX", "not found");
			   				jsonRsp.put("indoorY", "not found");
			   			}
		
			   			rs.close();
		   			}
		   			resp = jsonRsp.toString();
		   		} catch(SQLException ex) {
		   			logger.error("MyselfManager doGet query error:" + ex.getMessage());
		   		}
			}
	   	}
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		 
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //防止汉字乱码
        
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
        	resp = JqueryGridDBAccess.load(dbm, "map_tbl", postMap);
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add": {
        	String now=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        	
        	postMap.put("time", now);
        	JqueryGridDBAccess.insert(dbm, "map_tbl", postMap);
        	break;
        }
        
        case "del":{
        	JqueryGridDBAccess.delete(dbm, "map_tbl", postMap);
        	break;
        }
        
        case "edit":{
        	JqueryGridDBAccess.update(dbm, "map_tbl", postMap);
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
