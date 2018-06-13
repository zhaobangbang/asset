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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.common.JqueryGridParser;
import com.lansitec.dao.WarnRecordDAO;
import com.lansitec.dao.beans.WarnRecord;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.WarnRecordRspBean;

@Controller
@RequestMapping("/DevWarningRecordInfo")
public class DevWarningRecordInfo {
	private Logger logger = LoggerFactory.getLogger(DevWarningRecordInfo.class);
	
	@RequestMapping(value="doPost",method =RequestMethod.POST)
	@ResponseBody
	protected WarnRecordRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while((temp = br.readLine()) != null){
			sb.append(temp);
		}
		br.close();
		
		String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in DevWarningRecordInfo.doPost "+inStr);
        
        /*String deveui = request.getParameter("qdeveui");
		String datebutOne = request.getParameter("datebut1");
		String datebutTwo = request.getParameter("datebut2");*/
        String username = request.getParameter("usrname");
       	
        Map<String, String> postMap = new HashMap<String, String>();
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
		WarnRecordRspBean warnRecordRspBean = null;
        switch (oper){
		    case "load": {
		    	List<WarnRecord> warnRecordsList = null;
		    	int total = 1;
		    	int records = 0;
		    	int page = Integer.parseInt(postMap.get("page"));
		    	int row = Integer.parseInt(postMap.get("rows"));
		    	String hql = "";
		    	/*if((null != deveui) && (null != datebutOne) && (null != datebutTwo)){
		    		postMap.put("condString", "deveui='"+deveui+"' and time BETWEEN '"+datebutOne+"' and '"+datebutTwo+"'");
		    	}*/
		    	
		    	//judge the user if not belong to level
		    	if((null == username) || (username.equals(""))){
		    		//the user is belong to systemManager
		    		hql = ParamentSave.getJoinSQL("WarnRecord wd ", "", "", "", "", "", "");
				}else{
					//the user is belong to projectSystem
		    		String deveuis = QueryDiffDataTblInfo.getDevsByPriojectManagerName(username);
		    		String result = "";
		    		String finalResult = "";
		    		if(deveuis.equals("fail")){
		    			logger.info("Fail to get the deveuis by the projManagName {}",deveuis,username);
		    			return null;
		    		}
					String[] deveuiArray = deveuis.split(",");
		    		for(String devid : deveuiArray){
		    			result += "wd.deveui='"+devid+"' or ";
		    			
		    		}
		    		finalResult = result.substring(0, result.length() - 4);
		    		hql = "from WarnRecord wd where " + finalResult;
		    		
				}
		    	if((null == hql) || (hql.equals(""))){
	        		logger.error("Fail to get the sql {}",hql);
	        		return null;
	        	}
	    		warnRecordsList = GetObjectListByParament.getWarnRecordsListByParament(page, row, hql);
	    		if(!warnRecordsList.isEmpty()){
	        		records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
	        	}else{
	        		warnRecordsList = GetObjectListByParament.getWarnRecordsListByParament(page-1, row, hql);
		    		if(!warnRecordsList.isEmpty()){
		        		records = ParamentSave.records;//all record number
		        		total = ParamentSave.respTotal(ParamentSave.records, row);
		        	}
		    		page = ParamentSave.resPage(page);
	        	}
		    	warnRecordRspBean = new WarnRecordRspBean(page, total, records, warnRecordsList);
		    	logger.info(" WarnRecordRspBean {}",warnRecordRspBean);
			    break;
		    }
		    case "del":{
		    	WarnRecord warnRecord = WarnRecordDAO.get(Integer.parseInt(postMap.get("id")));
		        WarnRecordDAO.delete(warnRecord);
		    	break;
		    }
       }
        return warnRecordRspBean;
	}

}
