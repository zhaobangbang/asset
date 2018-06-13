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
import com.lansitec.dao.CompanyInfoDAO;
import com.lansitec.dao.beans.CompanyInfo;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.springmvc.beans.CompanyManagerRspBean;

@Controller
@RequestMapping("/CompanyManager")
public class CompanyManager {
    private Logger logger = LoggerFactory.getLogger(CompanyManager.class);
    
	@RequestMapping(value="doGet",method=RequestMethod.GET)
    @ResponseBody
    protected String doGet(HttpServletRequest request,HttpServletResponse reponse) throws Exception{
    	logger.info("receive do get in CompanyManager.doGet");
    	String companyNM = request.getParameter("name");
    	//edit fieldsn need to response companysn
    	String responsecompsn = request.getParameter("sn");
    	String companySN = null;
    	String companynm = null;
    	String result = "";
    	if((null == companyNM) && (null == responsecompsn)){
    		List<CompanyInfo> companyList = null;
        	companyList = CompanyInfoDAO.getAllCompanyInfo();
        	
        	if((null == companyList) || (companyList.size() == 0)){
        	    logger.info("Fail to query companyList {}",companyList);
        	    return "['fail']";
        	}else{
        		for(CompanyInfo company : companyList){
        			companynm = company.getName();
        			result+= "<option value=\""+companynm+"\">"+companynm+"</option>";
        		}
        	}
    	}else if((null != companyNM) && (null == responsecompsn)){
    		CompanyInfo companyInfo = CompanyInfoDAO.getCompanyInfoByNM(companyNM);
    		if(null == companyInfo){
    			logger.info("Fail to query companyInfo {}",companyInfo);
    			return "'[fail]'";
    		}else{
    			companySN = companyInfo.getSn();
    			result = companySN;
    		}
    	}else if((null == companyNM) && (null != responsecompsn)){
    		CompanyInfo companyInfo = CompanyInfoDAO.getCompanyInfoBySN(responsecompsn.substring(0, 4));
    		if(null == companyInfo){
    			logger.info("Fail to query companyInfo {}",companyInfo);
    			return "'[fail]'";
    		}else{
    			companyNM = companyInfo.getName();
    			result = companyNM;
    		}
    	}
    	return result;
    }
    
    @RequestMapping(value="doPost",method=RequestMethod.POST)
    @ResponseBody
    protected CompanyManagerRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    	StringBuffer sb = new StringBuffer("");
    	String temp;
    	while((temp = br.readLine()) != null){
    		  sb.append(temp);
    	}
    	br.close();
    	String inStr = URLDecoder.decode(sb.toString(),"UTF-8");
    	logger.info("receive do post in CompanyManager.doPost",inStr);
    	
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
 		CompanyManagerRspBean companyManagerRspBean = null;
 		try{
 			switch (oper){
 	         case "load": {
 	        	List<CompanyInfo> companyInfosList = null;
	        	int total = 1;
	        	int records = 0;
	        	int page = Integer.parseInt(postMap.get("page"));
	        	int row = Integer.parseInt(postMap.get("rows"));
	        	String hql = "from CompanyInfo com";
 	        	companyInfosList = GetObjectListByParament.getCompanyInfoListByParament(page, row, hql);
 	        	if(!companyInfosList.isEmpty()){
 	        		records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
 	        	}else{
 	        		companyInfosList = GetObjectListByParament.getCompanyInfoListByParament(page-1, row, hql);
 	 	        	if(!companyInfosList.isEmpty()){
 	 	        		records = ParamentSave.records;//all record number
 		        		total = ParamentSave.respTotal(ParamentSave.records, row);
 	 	        	}
 	 	        	page = ParamentSave.resPage(page);
 	        	}
 	        	companyManagerRspBean  = new CompanyManagerRspBean(page, total, records, companyInfosList);
 	 		    break;
 	         }
 	         
 	         case "add": {
 	        	String newsn =Integer.toString((int) ((Math.random()*9+1)*1000));
 	        	CompanyInfo companyInfo = new CompanyInfo(postMap.get("name"), newsn, postMap.get("captain"), postMap.get("tel"), postMap.get("memo"));
 	        	CompanyInfoDAO.create(companyInfo);
 	         	break;
 	         }
 	         
 	         case "del":{
 	        	CompanyInfo companyInfo = CompanyInfoDAO.get(Integer.parseInt(postMap.get("id")));
 	        	CompanyInfoDAO.delete(companyInfo);
 	         	break;
 	         }
 	         
 	         case "edit":{
 	        	/*logger.info("postMap {}",postMap);
 	        	String oldsn = OperateSN.getOldSN("company_info_tbl", postMap);
 	        	if(oldsn != null){
 	        		OperateSN.updatediffTblJointSN("company_info_tbl", oldsn);
 	        	}*/
 	        	 //don't get sn
 	        	 /*String oldsn = OperateSN.getOldInfo("company_info_tbl", postMap);
 	        	 if(null != oldsn){
 	        		 postMap.put("sn", oldsn);
 	        		 JqueryGridDBAccess.update(dbm, "company_info_tbl", postMap);
 	        	 }else{
 	        		 logger.error("Fail to get oldsn {} and update to data of company_info_tbl!",oldsn);
 	        	 }*/
 	        	CompanyInfo companyInfo = CompanyInfoDAO.get(Integer.parseInt(postMap.get("id")));
 	        	companyInfo.setName(postMap.get("name"));
 	        	companyInfo.setCaptain(postMap.get("captain"));
 	        	companyInfo.setTel(postMap.get("tel"));
 	        	companyInfo.setMemo(postMap.get("memo"));
 	        	CompanyInfoDAO.update(companyInfo);
 	         	break;
 	         }
 	         case "query":{
 	         	break;
 	          }
 	         
 	         default:{
 	          }
 	        }//switch
 		}catch (Exception e) {
			// TODO: handle exception
 			e.printStackTrace();
		}
        return companyManagerRspBean;
 	}
    
}
