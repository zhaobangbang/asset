package com.lansitec.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.common.JqueryGridParser;
import com.lansitec.dao.LogRecordDAO;
import com.lansitec.dao.beans.LogRecord;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.springmvc.beans.LogRecordRspBean;

@Controller
@RequestMapping("/UsersLoginInfo")
public class UsersLoginInfo {
	private Logger logger = LoggerFactory.getLogger(UsersLoginInfo.class);
	
	@RequestMapping("doGet")
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		logger.info("opreate in doGet");
		doPost(request,response);
	}
	
	@SuppressWarnings("null")
	@RequestMapping("doPost")
	@ResponseBody
	protected LogRecordRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while((temp = br.readLine()) != null){
			sb.append(temp);
		}
		br.close();
		
		String inStr = URLDecoder.decode(sb.toString(),"UTF-8");
		logger.info("receive do post in UsersLoginInfo.do "+inStr);
		
		Map<String,String> postMap = new HashMap<String,String>();
		String oper = JqueryGridParser.parserGridString(inStr, postMap);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		if(null == oper)
		{
			return null;
		}
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			String usrname = (String) reqSession.getAttribute("usrname");
			if(usrname != null)
			{
				if(usrname.equals("guest") && !oper.equals("load"))		
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
		LogRecordRspBean logRecordRspBean = null;
        switch (oper){
        case "load": {
        	List<LogRecord> logRecordsList = null;
        	int total = 1;
	    	int records = 0;
	    	int page = Integer.parseInt(postMap.get("page"));
	    	int row = Integer.parseInt(postMap.get("rows"));
	    	String hql = "from LogRecord lr ";
	    	logRecordsList = GetObjectListByParament.getLogRecordsListByParament(page, row, hql);
	    	if((null != logRecordsList) || (logRecordsList.size() != 0)){
	    		records = ParamentSave.records;//all record number
        		total = ParamentSave.respTotal(ParamentSave.records, row);
	    	}else{
	    		logRecordsList = GetObjectListByParament.getLogRecordsListByParament(page-1, row, hql);
		    	if((null != logRecordsList) || (logRecordsList.size() != 0)){
		    		records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
		    	}
		    	page = ParamentSave.resPage(page);
	    	}
	    	logRecordRspBean = new LogRecordRspBean(page, total, records, logRecordsList);
		    break;
        }
        
        case "add": {
        	break;
        }
        
        case "del":{
        	LogRecord logRecord = LogRecordDAO.get(Integer.parseInt(postMap.get("id")));
        	LogRecordDAO.delete(logRecord);
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
	return logRecordRspBean;
	}
}
