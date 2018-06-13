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
import com.lansitec.dao.ConstructInfoDAO;
import com.lansitec.dao.beans.ConstructInfo;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.ConstructInfoRspBean;

@Controller
@RequestMapping("/ConstructionManager")
public class ConstructionManager {
	private Logger logger = LoggerFactory.getLogger(CompanyManager.class);
	
	@RequestMapping(value="doGet",method=RequestMethod.GET)
	@ResponseBody
	protected String doGet(HttpServletRequest request) throws Exception{
		String constructName = request.getParameter("c_name");
		String username = request.getParameter("username");
		String fieldsn = request.getParameter("fieldSn");
		String workersn = request.getParameter("w_sn");
		String result = "";
		String constructSN= null;
		String constructNM= null;
		List<ConstructInfo> constructList = null;
		if(null != workersn){
			constructList = ConstructInfoDAO.getAllConstrut();
			if(!constructList.isEmpty()){
				for(ConstructInfo constructInfo : constructList){
					String c_sn = constructInfo.getSn();
					String workerFourBytesBefore = workersn.substring(0,4);
					if(c_sn.equals(workerFourBytesBefore)){
						//a worker mapping a construct
						result=constructInfo.getName();
						break;
					}
				}
			}
		}
		else{
			if((null == fieldsn) || (fieldsn.equals(""))){
				if((null == constructName) && (null == username)){
					constructList = ConstructInfoDAO.getAllConstrut();
					if(null == constructList || constructList.size() == 0){
						logger.info("Fail to query constructList {}",constructList);
						return "['fail']";
					}else{
						for(ConstructInfo construt : constructList){
							constructNM = construt.getName();
							result+="<option value=\""+constructNM+"\">"+constructNM+"</option>";
						}
					}
				}else if((null != constructName) && (null == username)){
					ConstructInfo constructInfo = ConstructInfoDAO.getConstructInfoByNM(constructName);
					if(constructInfo == null){
						logger.info("Fail to query constructInfo {}",constructInfo);
						return "['fail']";
					}
					else{
						constructSN = constructInfo.getSn();
						result = constructSN;
					}
					
				}else if((null == constructName) && (null != username)){
					String oper = null;
					result = QueryDiffDataTblInfo.getConstructInfoByNM(username, oper);
				}
			}else{
				constructList = ConstructInfoDAO.getConstructInfoByField(fieldsn);
				if(constructList.isEmpty()){
					logger.error("Fail to get the constructList {} by the fieldsn {}",constructList,fieldsn);
					return "['fail']";
				}else{
					for(ConstructInfo constructInfo : constructList){
						constructNM = constructInfo.getName();
						result+="<option value=\""+constructNM+"\">"+constructNM+"</option>";
					}
					
				}
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	protected ConstructInfoRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in ConstructionManager.doPost "+inStr);
        
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
		ConstructInfoRspBean constructInfoRspBean = null;
        switch (oper){
        case "load": {
        	List<ConstructInfo> constructInfosList = null;
        	int total = 1;
	    	int records = 0;
	    	int page = Integer.parseInt(postMap.get("page"));
	    	int row = Integer.parseInt(postMap.get("rows"));
        	String hql = null;
        	String result = QueryDiffDataTblInfo.getConstructInfoByNM(username, oper);
        	if(result.equals("['fail']")){
        		logger.error("Fail to get ConstructInfo By usrname {}",username);
        		response.getWriter().write(result);
        		break;
        	}else{
        		hql = ParamentSave.getJoinSQL("ConstructInfo cf", "", "", "", "", "", "");
        		if((null == hql) || (hql.equals(""))){
	        		logger.error("Fail to get the hql {}",hql);
	        		return null;
	        	}
        		for(String fieldsn : DeviceListMgr.fieldList){
        			result += "cf.sn= '"+fieldsn+"' or ";
        		}
        		hql = hql +" where " + result.substring(0,result.length()-4);
        		
        		constructInfosList = GetObjectListByParament.getConstructInfosByParament(page, row, hql);
        		if(!constructInfosList.isEmpty()){
        			records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
        		}else{
        			constructInfosList = GetObjectListByParament.getConstructInfosByParament(page-1, row, hql);
            		if(!constructInfosList.isEmpty()){
            			records = ParamentSave.records;//all record number
    	        		total = ParamentSave.respTotal(ParamentSave.records, row);
            		}
            		page = ParamentSave.resPage(page);
        		}
        		constructInfoRspBean = new ConstructInfoRspBean(page, total, records, constructInfosList);
    		    break;
        	}
        }
        
        case "add": {
        	String newsn =Integer.toString((int) ((Math.random()*9+1)*1000));
        	ConstructInfo constructInfo = new ConstructInfo(postMap.get("name"), newsn, postMap.get("captain"), postMap.get("tel"), postMap.get("field"), postMap.get("memo"));
        	ConstructInfoDAO.create(constructInfo);
        	break;
        }
        
        case "del":{
        	ConstructInfo constructInfo = ConstructInfoDAO.get(Integer.parseInt(postMap.get("id")));
        	ConstructInfoDAO.delete(constructInfo);
        	break;
        }
        
        case "edit":{
        	//construct'sn can update then uses it
        	/*logger.info("postMap {}",postMap);
        	String oldsn = OperateSN.getOldSN("construct_info_tbl", postMap);
        	if((oldsn != null) && (!oldsn.equals(OperateSN.newestSN))){
        		OperateSN.updatediffTblJointSN("construct_info_tbl", oldsn);
        	}
        	postMap.put("sn", OperateSN.newestSN);*/
        	//don't get sn 
        	/*String oldsn = OperateSN.getOldInfo("construct_info_tbl", postMap);
        	if(null != oldsn ){
        		postMap.put("sn", oldsn);
            	JqueryGridDBAccess.update(dbm, "construct_info_tbl", postMap);
        	}else{
        		logger.error("Fail to get oldsn {} and update to data of construct_info_tbl!",oldsn);
        	}*/
        	ConstructInfo constructInfo = ConstructInfoDAO.get(Integer.parseInt(postMap.get("id")));
        	constructInfo.setName(postMap.get("name"));
        	constructInfo.setCaptain(postMap.get("captain"));
        	constructInfo.setTel(postMap.get("tel"));
        	constructInfo.setField(postMap.get("field"));
        	constructInfo.setMemo(postMap.get("memo"));
        	ConstructInfoDAO.update(constructInfo);
        	break;
        }
        
        case "query":{
        	break;
         }
        
        default:{
         }
       }//switch
       return constructInfoRspBean;
	}
}
