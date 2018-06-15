package com.lans.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class DevicesManager
 */
@WebServlet("/PositionManager.do")
public class PositionManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(PositionManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PositionManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usrname = request.getParameter("usrname");
		String result = null;
		
		ServletContext ctx = request.getServletContext();
		DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		String sql = "select * from beacons_tbl where owner = \'" + usrname + "\'";
		ResultSet rs = null;
		result = "[";
		try{
			rs = db.executeQuery(sql);
			while(rs.next())
			{	
				result += "'" + rs.getString("minor") + "',";
			}
			result += "]";
			rs.close();
  			}catch(SQLException ex){
  				logger.error("executeQuery:" + ex.getMessage());
  				result=null;
  			}
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(result);
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
        
        String usrName = request.getParameter("name");
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //∑¿÷π∫∫◊÷¬“¬Î
        logger.info("receive do post in PositionManager.do "+inStr+" user "+usrName);
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
		String prio = null;
		if(reqSession != null)
		{
			String usrname = (String) reqSession.getAttribute("usrname");
			prio = (String)reqSession.getAttribute("prio");
			
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
        	if(prio != null && prio.equals("≥¨º∂π‹¿Ì‘±"))
        	{}
        	else
        	{
        		postMap.put("whereField", "owner");
        		postMap.put("whereString", usrName);
        	}
        	resp = JqueryGridDBAccess.load(dbm, "beacons_tbl", postMap);
        	logger.info("respond to post in PositionManager.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add":{
        	postMap.put("owner", usrName);
        	JqueryGridDBAccess.insert(dbm, "beacons_tbl", postMap);
        	break;        	
        }
        
        case "del":{
        	JqueryGridDBAccess.delete(dbm, "beacons_tbl", postMap);
        	break;
        }
        
        case "edit":{
        	JqueryGridDBAccess.update(dbm, "beacons_tbl", postMap);      	
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
