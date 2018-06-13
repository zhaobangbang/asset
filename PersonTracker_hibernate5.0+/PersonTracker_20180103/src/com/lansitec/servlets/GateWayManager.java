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
import com.lansitec.dao.GatewayInfoDAO;
import com.lansitec.dao.beans.GatewayInfo;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.springmvc.beans.GateWayInfoRspBean;

@Controller
@RequestMapping("/GateWayManager")
public class GateWayManager {
	private Logger logger = LoggerFactory.getLogger(GateWayManager.class);
	
	@RequestMapping(value="doGet",method=RequestMethod.GET)
	protected void doGet(HttpServletResponse response) throws Exception{
		logger.info("receive do get in GateWayManager.doGet");
	}
	
	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	protected GateWayInfoRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuffer sb = new StringBuffer("");
		String temp = null;
		while((temp = br.readLine()) != null){
			sb.append(temp);
		}
		String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in GateWayManager.doPost "+inStr);
		
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
		GateWayInfoRspBean gateWayInfoRspBean = null;
		
		try{
			switch (oper){
	        case "load": {
	        	List<GatewayInfo> gatewayInfosList = null;
	        	int total = 1;
	        	int records = 0;
	        	int page = Integer.parseInt(postMap.get("page"));
	        	int row = Integer.parseInt(postMap.get("rows"));
	        	String hql = "from GatewayInfo gw ";
	        	gatewayInfosList = GetObjectListByParament.getGatewayInfoListByParamnet(page, row, hql);
	        	if(!gatewayInfosList.isEmpty()){
	        		records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
	        	}else{
	        		//page(>1) data is deleted to null
	        		gatewayInfosList = GetObjectListByParament.getGatewayInfoListByParamnet(page-1, row, hql);
	        		if(!gatewayInfosList.isEmpty()){
		        		records = ParamentSave.records;//all record number
		        		total = ParamentSave.respTotal(ParamentSave.records, row);
		        	}
	        		page = ParamentSave.resPage(page);
	        	}
	        	gateWayInfoRspBean = new GateWayInfoRspBean(page, total, records, gatewayInfosList);
			    break;
	        }
	        
	        case "add": {
	        	GatewayInfo gatewayInfo = new GatewayInfo(postMap.get("name"), postMap.get("sn"), postMap.get("type"), postMap.get("field"));
	        	GatewayInfoDAO.create(gatewayInfo);
	        	//OperateSN.addAndDelAndUpdateGatewayNumByField(postMap.get("field"),"", oper);
	        	break;
	        }
	        
	        case "del":{
	        	/*GatewayInfo gatewayInfo = GatewayInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        	String fieldsn = gatewayInfo.getField();
	            OperateSN.addAndDelAndUpdateGatewayNumByField(fieldsn,"", oper);*/
	        	GatewayInfo gatewayInfo = GatewayInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        	GatewayInfoDAO.delete(gatewayInfo);
	        	break;
	        }
	        
	        case "edit":{
	        	/*GatewayInfo gatewayInfo = GatewayInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        	String fieldsnTbl = gatewayInfo.getField();
	        	if(fieldsnTbl.equals(postMap.get("field"))){
					logger.info("the fieldsnTbl {} eq to the fieldsnNow {} , so that update the devNum in fieldsn {}",fieldsnTbl,postMap.get("field"),fieldsnTbl);
						
	        	}else{
	        		OperateSN.addAndDelAndUpdateGatewayNumByField(fieldsnTbl,postMap.get("field"), oper);
	        	}*/
	        	GatewayInfo gatewayInfo = GatewayInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        	gatewayInfo.setName(postMap.get("name"));
	        	gatewayInfo.setSn(postMap.get("sn"));
	        	gatewayInfo.setType(postMap.get("type"));
	        	gatewayInfo.setField(postMap.get("field"));
	        	GatewayInfoDAO.update(gatewayInfo);
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
        return gateWayInfoRspBean;
	}
}
