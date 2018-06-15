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

import com.lans.common.DataBaseMgr;
import com.lans.common.JqueryGridDBAccess;
import com.lans.common.JqueryGridParser;

/**
 * Servlet implementation class CityManager
 * CityManager和AreaManager主要用于城市和地区的管理.
 */
@WebServlet("/CityManager.do")
public class CityManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(CityManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CityManager() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("receive do get in CityManager.do");

        ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
       	
		String sql = "select * from city_tbl";
		ResultSet rs;
		ArrayList<String> cityList = new ArrayList<String>();
		
		try{
	       	rs = dbm.executeQuery(sql);
	       	rs.beforeFirst();
	       	while(rs.next()) {
	       			cityList.add(rs.getString("city"));
	       		}
	       	rs.close();
	    } catch(SQLException ex) {
	    	logger.error("executeQuery1:" + ex.getMessage());
	    }
		//this is the special response for JQGrid select column
		String resp = "";
		for (String city:cityList) {
			resp += "<option value=\""+city+"\">"+city+"</option>";
		}

        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//System.out.println("respond do get in CityManager.do "+resp);
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
        logger.info("receive do post in CityManager.do "+inStr);
        
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
        	resp = JqueryGridDBAccess.load(dbm, "city_tbl", postMap);
        	logger.info("respond to post in DevicesManager.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add": {
        	String now=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        	
        	postMap.put("time", now);
        	JqueryGridDBAccess.insert(dbm, "city_tbl", postMap);
        	break;
        }
        
        case "del":{
        	JqueryGridDBAccess.delete(dbm, "city_tbl", postMap);
        	break;
        }
        
        case "edit":{
        	JqueryGridDBAccess.update(dbm, "city_tbl", postMap);
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
