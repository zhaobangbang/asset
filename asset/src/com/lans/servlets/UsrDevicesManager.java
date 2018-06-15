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

import com.lans.beans.ObserverInfoBean;
import com.lans.common.DataBaseMgr;
import com.lans.common.JqueryGridDBAccess;
import com.lans.common.JqueryGridParser;

import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class DevicesManager
 * 普通用户设置终端信息，如报警中心点，半径，别名等。
 */
@WebServlet("/UsrDevicesManager.do")
public class UsrDevicesManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(UsrDevicesManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsrDevicesManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usrname = request.getParameter("usrname");
		String type = request.getParameter("type");
		String result = null;
		
		ServletContext ctx = request.getServletContext();
		DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		String sql = "select * from dev_list_tbl where owner = \'" + usrname + "\'";
		ResultSet rs = null;
		result = "[";
		try{
			rs = db.executeQuery(sql);
			while(rs.next())
			{
				if(null != type && type.equals("one")) //只获得第一个数据
				{
					result += "'" + rs.getString("deveui") + "'";
					break;
				}
				else
				{	
					result += "'" + rs.getString("deveui") + "',";
				}
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
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //防止汉字乱码
        logger.info("receive do post in UsrDevicesManager.do "+inStr+" user "+usrName);
        ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
       	ObserverInfoBean obInfo = (ObserverInfoBean)ctx.getAttribute("obsInfo");
       	
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
        	postMap.put("whereField", "username");
        	postMap.put("whereString", usrName);
        	resp = JqueryGridDBAccess.load(dbm, "dev_usr_tbl", postMap);
        	logger.info("respond to post in DevicesManager.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add":{
        	postMap.put("username", usrName);
        	if(JqueryGridDBAccess.insert(dbm, "dev_usr_tbl", postMap) > 0)
        	{
        		Map<String, String>updateValue = new HashMap<String, String>();
        		
        		updateValue.put("newvalue", postMap.get("deveui"));
        		logger.info("add: " + postMap.get("deveui"));
        		
        		obInfo.updateObserverInfoBean(usrName, "add", updateValue);
        	}
        	break;
        	
        }
        
        case "del":{
        	postMap.put("whereField", "username");
        	postMap.put("whereString", usrName);
        	
    		String sql = "select * from dev_usr_tbl where id = '" + postMap.get("id") + "';";
    		String newValue = null;
    		ResultSet result = dbm.executeQuery(sql);
    		try {
				if(result.next())
					newValue = result.getString("deveui");
	    		result.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  		
        	if(JqueryGridDBAccess.delete(dbm, "dev_usr_tbl", postMap) > 0)
        	{
        		Map<String, String>updateValue = new HashMap<String, String>();
        		
        		updateValue.put("newvalue", newValue);
        		logger.info("del oper was on");
            	obInfo.updateObserverInfoBean(usrName, "del", updateValue);
        	}
        	break;
        }
        
        case "edit":{
        	String sql = "select * from dev_usr_tbl where id = '" + postMap.get("id") + "';";
    		String oldValue = "";
    		String newValue = "";
    		ResultSet result = dbm.executeQuery(sql);
    		try {
				if(result.next())
					oldValue = result.getString("deveui");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
        	postMap.put("whereField", "username");
        	postMap.put("whereString", usrName);
    		newValue = postMap.get("deveui");
        	if(JqueryGridDBAccess.update(dbm, "dev_usr_tbl", postMap) > 0)
        	{
        		Map<String, String>updateValue = new HashMap<String, String>();

        		updateValue.put("oldvalue", oldValue);
        		updateValue.put("newvalue", newValue);
        		
        		if(null != newValue && newValue != oldValue)
        		{
        			logger.info("edit oper was on");
        			obInfo.updateObserverInfoBean(usrName, "edit", updateValue);
        		}
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
