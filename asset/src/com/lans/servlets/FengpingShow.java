package com.lans.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.RowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.common.JqueryGridDBAccess;
import com.lans.common.JqueryGridParser;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class FengpingShow
 */
@WebServlet("/FengpingShow.do")
public class FengpingShow extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(FengpingShow.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FengpingShow() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usrname = request.getParameter("name");
		if(null == usrname)
			usrname = null;
		
		if(null != usrname)
		{
	        ServletContext ctx = request.getServletContext();
	       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
	       	
			JSONObject jsonObj = new JSONObject();
			String sql = "select * from fengping_tbl where user=\"" + usrname + "\"";
			RowSet rs = dbm.executeQuery(sql);
			try {
				rs.last();
				int devnumber = rs.getRow();
				jsonObj.put("devnumber", devnumber);					

		        response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(jsonObj.toString());
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in FengpingShow.do "+inStr);
       
        String oprtype = request.getParameter("type");
        String usrname = request.getParameter("name");
        String devname = request.getParameter("deveui");
        String devid = request.getParameter("id");
        ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
       	
        Map<String, String> postMap = new HashMap<String, String>();
        String oper = JqueryGridParser.parserGridString(inStr, postMap);
        String resp = "";
        
        if(null != oprtype)
        	oper = oprtype;
        
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		if(null == oper)
		{
			return;
		}
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			String usrname1 = (String) reqSession.getAttribute("usrname");
			if(usrname1 != null)
			{
				if(usrname1.equals("guest") && !oper.equals("load"))		
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
        	if(null != usrname)
        	{
        		postMap.put("whereField", "user");
        		postMap.put("whereString", usrname);
        	}
        	resp = JqueryGridDBAccess.load(dbm, "fengping_tbl", postMap);
        	logger.info("respond to post in FengpingShow.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add": {
        	if(null != usrname && null != devname)
        	{
        		postMap.put("user", usrname);
        		postMap.put("deveui", devname);
        	}
        	JqueryGridDBAccess.insert(dbm, "fengping_tbl", postMap);
        	break;
        }
        
        case "del":{
        	if(null != devid)
        	{
        		postMap.put("id", devid);
        	}
        	JqueryGridDBAccess.delete(dbm, "fengping_tbl", postMap);
        	break;
        }
        
        case "edit":{
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
