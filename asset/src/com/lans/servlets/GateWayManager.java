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

import com.lans.common.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class GateWayManager
 */
@WebServlet("/GateWayManager.do")
public class GateWayManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(GateWayManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GateWayManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in GateWayManager.do "+inStr);
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
        	resp = JqueryGridDBAccess.load(dbm, "gateway_tbl", postMap);	
        	logger.info("respond to post in GateWayManager.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add":{
        	JqueryGridDBAccess.insert(dbm, "gateway_tbl", postMap);
        	break;
        	
        }
        
        case "del":{
        	JqueryGridDBAccess.delete(dbm, "gateway_tbl", postMap);
        	break;
        }
        
        case "edit":{
        	JqueryGridDBAccess.update(dbm, "gateway_tbl", postMap);
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
