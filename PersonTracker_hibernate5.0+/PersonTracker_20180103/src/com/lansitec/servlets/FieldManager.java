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
import com.lansitec.dao.FieldInfoDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.OperateSN;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.FieldInfoRspBean;

@Controller
@RequestMapping("/FieldManager")
public class FieldManager {
	 private Logger logger = LoggerFactory.getLogger(FieldManager.class);
	 
	 @RequestMapping(value="doGet",method=RequestMethod.GET)
	 @ResponseBody
	 protected String doGet(HttpServletRequest request,HttpServletResponse response) throws Exception{
		    logger.info("receive do get in FieldManager.doGet");
		    String fieldNM = request.getParameter("name");
		    //edit mapsn need to response fielNM
		    String fieldSN = request.getParameter("sn");
		    //response field'name by projectManager'username
		    String username = request.getParameter("username");
		    //response fieldname by compansn
		    String companysn = request.getParameter("ComName");
		    String assetsn = request.getParameter("a_sn");
		    String fieldsn = null;
		    String fieldnm = null;
		    String result = "";
		    List<FieldInfo> fieldList = null;
		    if(((null == fieldNM) && (null == fieldSN) && (null == username)) || (null != companysn) || (null != assetsn)){
		    	
				fieldList = FieldInfoDAO.getAllField();
				if((null == fieldList) || (fieldList.size() == 0)){
					logger.info("Fail to query fieldList {}",fieldList);
					return "['fail']";
				}
				else{
					for(FieldInfo field :fieldList){
						if((null == assetsn) && (null == companysn)){
						    fieldnm = field.getName();
						    result+="<option value=\""+fieldnm+"\">"+fieldnm+"</option>";
						}else if((null != assetsn) && (null == companysn)){
							String eightBytesBefore = assetsn.substring(0, 8);
							if(eightBytesBefore.equals(field.getSn())){
								result = field.getName();
								break;
							}
						}else if((null != companysn) && (null == assetsn)){
							String fourBytesBefore = field.getSn().substring(0,4);
							if(fourBytesBefore.equals(companysn)){
								result+="<option value=\""+field.getName()+"\">"+field.getName()+"</option>";
							}
						}
					}
				}
		    }
		    else if((null != fieldNM) && (null == fieldSN) && (null == username)){
		    	FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoByNM(fieldNM);
		    	if(null == fieldInfo){
		    		logger.info("Fail to query fieldInfo {}",fieldInfo);
		    		return "['fail']";
		    	}else{
		    		fieldsn = fieldInfo.getSn();
		    		result = fieldsn;
		    	}
		    }else if((null == fieldNM) && (null != fieldSN) && (null == username)){
		    	FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(fieldSN.substring(0, 8));
		    	if(null == fieldInfo){
		    		logger.info("Fail to query fieldInfo {}",fieldInfo);
		    		return "['fail']";
		    	}else{
		    		fieldNM = fieldInfo.getName();
		    		result = fieldNM;
		    	}
		    }else if((null == fieldNM) && (null == fieldSN) && (null != username)){
		    	ProjectManagers projectManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
		    	if(null == projectManagers){
					logger.info("Fail to query projectManagers {} by username {}",projectManagers,username);
					return "['fail']";
				}else{
					fieldList = FieldInfoDAO.getAllField();
					if((null == fieldList) || (fieldList.size() == 0)){
						logger.info("Fail to query fieldList {}",fieldList);
						return "['fail']";
					}
					else{
						boolean judgeValue = false;
						for(FieldInfo field :fieldList){
							if(field.getSn().equals(projectManagers.getField())){
								fieldnm = field.getName();
								result+="<option value=\""+fieldnm+"\">"+fieldnm+"</option>";
								judgeValue = true;
								break;
							}
							
						}
						if(!judgeValue){
							logger.info("Field's sn doesn't eq to projectManagers's field {}",projectManagers.getField());
							return "['fail']";
						}
					}
				}
		    }
		    return result;
	 }
	 
	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	 protected FieldInfoRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
	        StringBuffer sb = new StringBuffer("");  
	        String temp;  
	        while ((temp = br.readLine()) != null) {  
	            sb.append(temp);  
	        }  
	        br.close();
	        
	        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
	        logger.info("receive do post in FieldManager.doPost "+inStr);
	       	
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
			FieldInfoRspBean fieldInfoRspBean = null;
	        switch (oper){
	        case "load": {
	        	List<FieldInfo> fieldInfosList = null;
	        	int total = 1;
	        	int records = 0;
	        	int page = Integer.parseInt(postMap.get("page"));
	        	int row = Integer.parseInt(postMap.get("rows"));
	        	String hql = "from FieldInfo fd ";
	        	fieldInfosList = GetObjectListByParament.getFieldInfosByParament(page, row, hql);
	        	if(!fieldInfosList.isEmpty()){
	        		records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
	        	}else{
	        		fieldInfosList = GetObjectListByParament.getFieldInfosByParament(page-1, row, hql);
		        	if(!fieldInfosList.isEmpty()){
		        		records = ParamentSave.records;//all record number
		        		total = ParamentSave.respTotal(ParamentSave.records, row);
		        	}
		        	page = ParamentSave.resPage(page);
	        	}
	        	fieldInfoRspBean = new FieldInfoRspBean(page, total, records, fieldInfosList);
	        	logger.info("fieldInfoRspBean {}",fieldInfoRspBean);
			    break;
	        }
	        
	        case "add": {
	        	String newsn = QueryDiffDataTblInfo.responseSN("field_info_tbl", "company", postMap.get("company"));
	        	FieldInfo fieldInfo = new FieldInfo(postMap.get("name"), newsn, postMap.get("city"));
	        	FieldInfoDAO.create(fieldInfo);
	        	break;
	        }
	        
	        case "del":{
	        	//delete associate data of field'sn
	        	OperateSN.deletediffTblAssociateSN("field_info_tbl", postMap.get("id"));
	        	FieldInfo fieldInfo = FieldInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        	FieldInfoDAO.delete(fieldInfo);
	        	break;
	        }
	        case "edit":{
	        	String oldsn = OperateSN.getOldInfo("field_info_tbl", postMap.get("id"),"company",postMap.get("company"));
	        	String newsn = null;
	        	if(null != oldsn){
	        		if(!oldsn.equals(OperateSN.newestData)){
		        		OperateSN.updatediffTblJointSN("field_info_tbl", oldsn);
		        		newsn = OperateSN.changeData;
		        		//postMap.put("sn", OperateSN.changeData);
		        	}else{
		        		newsn = OperateSN.newestData;
		        		//postMap.put("sn", OperateSN.newestData);
		        	}
	        		FieldInfo fieldInfo = FieldInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        		fieldInfo.setName(postMap.get("name"));
	        		fieldInfo.setSn(newsn);
	        		fieldInfo.setCity(postMap.get("city"));
		        	FieldInfoDAO.update(fieldInfo);
	        	}else{
	        		logger.error("Fail to get oldsn {} and update to data of field_info_tbl!",oldsn);
	        	}
	        	
	        	break;
	        }
	        
	        case "query":{
	        	break;
	         }
	        
	        default:{
	         }
	       }//switch
	        return fieldInfoRspBean;
	 }
}
