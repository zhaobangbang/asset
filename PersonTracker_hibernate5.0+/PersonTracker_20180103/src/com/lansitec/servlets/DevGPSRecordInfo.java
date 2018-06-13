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
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.springmvc.beans.PositionRecordRspBean;

@Controller
@RequestMapping("/DevGPSRecordInfo")
public class DevGPSRecordInfo {
	private Logger logger = LoggerFactory.getLogger(DevGPSRecordInfo.class);
	
	@RequestMapping(value="doPost",method =RequestMethod.POST)
	@ResponseBody
	protected PositionRecordRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while((temp = br.readLine()) != null){
			sb.append(temp);
		}
		br.close();
		
		String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in DevGPSRecordInfo.doPost "+inStr);
        
        String deveui = request.getParameter("qdeveui");
		String datebutOne = request.getParameter("datebut1");
		String datebutTwo = request.getParameter("datebut2");
        
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
		PositionRecordRspBean positionRecordRspBean = null;
        switch (oper){
        case "load": {
        	List<PositionRecord> positionRecordsList = null;
        	int total = 1;
        	int records = 0;
        	int page = Integer.parseInt(postMap.get("page"));
        	int row = Integer.parseInt(postMap.get("rows"));
        	String hql = null;
        	String hqlStart = "from PositionRecord pr ";
        	int paramentNum = 0;
        	String paramentNameOne = null;
        	String paramentValueOne = null;
        	if((null != postMap.get("deveui")) && (null == datebutOne) && (null == datebutTwo)){
        		paramentNum = 1;
        		hql = hqlStart + "where pr.deveui = : deveui";
        		paramentNameOne = "deveui";
        		paramentValueOne = postMap.get("deveui");
        	}
        	else if((null != deveui) && (null != datebutOne) && (null != datebutTwo)){
        		paramentNum = 1;
        		hql = hqlStart + "where pr.deveui = : deveui and time BETWEEN '"+datebutOne+"' and '"+datebutTwo+"' ORDER BY pr.time desc";
        		paramentNameOne = "deveui";
        		paramentValueOne = postMap.get("deveui");
        	}else{
        		hql = hqlStart;
        	}
        	if((null == hql) || (hql.equals(""))){
        		logger.error("Fail to get the hql {}",hql);
        		return null;
        	}
        	positionRecordsList = GetObjectListByParament.getPositionRecordsListByParament(page, row,paramentNum,paramentNameOne,paramentValueOne, hql);
        	if(!positionRecordsList.isEmpty()){
        		records = ParamentSave.records;//all record number
        		total = ParamentSave.respTotal(ParamentSave.records, row);
        	}else{
        		positionRecordsList = GetObjectListByParament.getPositionRecordsListByParament(page-1, row, paramentNum,paramentNameOne,paramentValueOne, hql);
            	if(!positionRecordsList.isEmpty()){
            		records = ParamentSave.records;//all record number
            		total = ParamentSave.respTotal(ParamentSave.records, row);
            	}
            	page = ParamentSave.resPage(page);
        	}
        	positionRecordRspBean = new PositionRecordRspBean(page, total, records, positionRecordsList);
        	logger.info("positionRecordRspBean {} ",positionRecordRspBean);
		    break;
        }
        case "del":{
        	PositionRecord positionRecord = PositionRecordDAO.get(Integer.parseInt(postMap.get("id")));
        	PositionRecordDAO.delete(positionRecord);
        	break;
        }
       }
        return positionRecordRspBean;
	}
}
