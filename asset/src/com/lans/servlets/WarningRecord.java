package com.lans.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
 * Servlet implementation class WarningRecord
 */
@WebServlet("/WarningRecord.do")
public class WarningRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(WarningRecord.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WarningRecord() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		 
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp = null;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String usrname = request.getParameter("usrname");
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in WarningRecord.do "+inStr);
        
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
        	postMap.put("whereField", "usrname");
        	postMap.put("whereString", usrname);
        	resp = JqueryGridDBAccess.load(dbm, "warning_record_tbl", postMap);
        	logger.info("respond to post in WarningRecord.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add": {
        	String now=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        	
        	postMap.put("time", now);
        	JqueryGridDBAccess.insert(dbm, "warning_record_tbl", postMap);
        	break;
        }
        
        case "del":{
        	JqueryGridDBAccess.delete(dbm, "warning_record_tbl", postMap);
        	break;
        }
        
        case "edit":{
        	JqueryGridDBAccess.update(dbm, "warning_record_tbl", postMap);
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

