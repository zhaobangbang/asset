package com.lans.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
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
 * Servlet implementation class RegisterCodeManager
 */
@WebServlet("/RegisterCodeManager.do")
public class RegisterCodeManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(RegisterCodeManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterCodeManager() {
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
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp = null;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in RegCodeManager.do "+inStr);
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
        	resp = JqueryGridDBAccess.load(dbm, "key_tbl", postMap);	
        	logger.info("respond to post in RegCodeManager.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add":{
        	JqueryGridDBAccess.insert(dbm, "key_tbl", postMap);
        	logger.info("add oper was on");
        	break;
        	
        }
        
        case "del":{
        	JqueryGridDBAccess.delete(dbm, "key_tbl ", postMap);
        	logger.info("del oper was on");
        	break;
        }
        
        case "edit":{
        	JqueryGridDBAccess.update(dbm, "key_tbl", postMap);
        	logger.info("edit oper was on");
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
