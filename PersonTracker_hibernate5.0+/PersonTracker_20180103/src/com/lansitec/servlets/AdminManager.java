package com.lansitec.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

import com.lansitec.common.*;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.springmvc.beans.SystemManagerRspBean;

import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/AdminManager")
public class AdminManager {
    private Logger logger = LoggerFactory.getLogger(AdminManager.class);
    
    @RequestMapping(value="doGet",method=RequestMethod.GET)
    @ResponseBody
	protected String doGet(HttpServletRequest request, HttpServletResponse response,@RequestParam String usrname) throws ServletException, IOException {
		logger.info("receive do get in AdminManager.doGet ");
		
		if((null == usrname) || (usrname.equals("")))
		{
			return "['fail']";
		}
		SystemManagers managers = null;
	
		try {
			managers = SystemManagersDAO.getMangersInfoByUsername(usrname);
			response.setCharacterEncoding("UTF-8");
		    response.setContentType("text/html;charset=UTF-8");
				
			if(managers == null){
				logger.info("Fail to query the manager {}",usrname);
				return "['fail']";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "['ok']";
		
	}

	@RequestMapping(value="doPost",method=RequestMethod.POST)
    @ResponseBody
	protected SystemManagerRspBean doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in AdminManager.doPost "+inStr);
        
        Map<String, String> postMap = new HashMap<String, String>();
        String oper = JqueryGridParser.parserGridString(inStr, postMap);
        
        String username = request.getParameter("username");
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
        
		if(null == oper)
		{
			return null;
		}
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			username = (String) reqSession.getAttribute("usrname");
			if(username != null)
			{
				if(username.equals("guest") && !oper.equals("load"))		
					return null;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		SystemManagerRspBean  systemManagerRspBean = null;
		
		try{
			switch (oper){
	        case "load": {
	        	List<SystemManagers> managersList = null;
	        	int total = 1;
	        	int records = 0;
	        	int page = Integer.parseInt(postMap.get("page"));
	        	int row = Integer.parseInt(postMap.get("rows"));
	        	String hql = null;
	        	String hqlStart = "from SystemManagers stm ";
	        	int paramentNum = 0;
	        	String paramentNameOne = null;
	        	String paramentValueOne = null;
	        	String paramentValueTwo = null;
	        	String paramentNameTwo = null;
	        	if((null == postMap.get("username")) && (null == postMap.get("usermail")))
	        	{
	        	    hql = hqlStart; 
	        	}else if((null != postMap.get("username")) && (null == postMap.get("usermail"))){
	        		hql = hqlStart + " where stm.username = :username";
	        		paramentNum = 1;
	        		paramentNameOne = "username";
	        		paramentValueOne = postMap.get("username");
	        		
	        	}else if((null == postMap.get("username")) && (null != postMap.get("usermail"))){
	        		hql = hqlStart + " where stm.usermail = :usermail";
	        		paramentNum = 1;
	        		paramentNameOne = "usermail";
	        		paramentValueOne = postMap.get("usermail");
	        	}else if((null != postMap.get("username")) && (null != postMap.get("usermail"))){
	        		hql = hqlStart + " where stm.username = :username and stm.usermail = :usermail";
	        		paramentNum = 2;
	        		paramentNameOne = "username";
	        		paramentValueOne = postMap.get("username");
	        		paramentNameTwo = "usermail";
	        		paramentValueTwo = postMap.get("usermail");
	        	}
	        	if((null == hql) || (hql.equals(""))){
	        		logger.error("Fail to get the hql {}",hql);
	        		return null;
	        	}
	        	managersList = GetObjectListByParament.getSystemManagerListByParament(page,row,hql,paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo);
	        	if(!managersList.isEmpty()){
        			records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
        		}else{
        			managersList = GetObjectListByParament.getSystemManagerListByParament(page-1,row,hql,paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo);
    	        	if(!managersList.isEmpty()){
            			records = ParamentSave.records;//all record number
    	        		total = ParamentSave.respTotal(ParamentSave.records, row);
            		}
    	        	page = ParamentSave.resPage(page);
        		}
	        	systemManagerRspBean = new SystemManagerRspBean(page, total, records, managersList);
        		logger.info("managersList {}",systemManagerRspBean );
			    break;
	        }
	        
	        case "add":{
	        	if(null != username)
	        	{
	        		/*if(((null == postMap.get("username")) || (postMap.get("username").equals(""))) || ((null == postMap.get("userkey")) || (postMap.get("userkey").equals(""))) 
	        				                                                                       || ((null == postMap.get("usermail")) || (postMap.get("usermail").equals(""))) ){
	        			return null;
	        		}*/
	        		SystemManagers systemManagers = new SystemManagers(postMap.get("username"), postMap.get("userkey"), postMap.get("tel"), postMap.get("usermail"), LocalDateTime.now().toLocalDate());
	            	SystemManagersDAO.create(systemManagers);
	        	}
	        	break;
	        	
	        }
	        case "del":{
	        	SystemManagers systemManagers = SystemManagersDAO.get(Integer.parseInt(postMap.get("id")));
	        	SystemManagersDAO.delete(systemManagers);
	        	break;
	        }
	        
	        case "edit":{
	        	SystemManagers systemManagers = SystemManagersDAO.get(Integer.parseInt(postMap.get("id")));
	        	LocalDate time = systemManagers.getTime();
	        	systemManagers.setUsername(postMap.get("username"));
	        	systemManagers.setUserkey(postMap.get("userkey"));
	        	systemManagers.setTel(postMap.get("tel"));
	        	systemManagers.setUsermail(postMap.get("usermail"));
	        	systemManagers.setTime(time);
	        	SystemManagersDAO.update(systemManagers);
	        	break;
	        }
	        
	        case "query":{
	        	break;
	         }
	        
	        default:{
	         }
	        
	        }//switch
		}catch (Exception e) {
			e.printStackTrace();
		}
       return systemManagerRspBean;
	
	}
	
 
}
