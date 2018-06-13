package com.lansitec.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.time.LocalDateTime;
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
import com.lansitec.dao.FieldInfoDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.beans.CompanyInfo;
import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.enumlist.Prio;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.OperateSN;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.ProjectManagerBean;
import com.lansitec.springmvc.beans.ProjectManagerRspBean;

@Controller
@RequestMapping("/ProjectManagers")
public class ProjManager {
    private Logger logger = LoggerFactory.getLogger(ProjManager.class);
    
	@RequestMapping(value="doGet",method=RequestMethod.GET)
    @ResponseBody
    protected String doGet(HttpServletRequest request,HttpServletResponse response,ProjectManagerBean projectManagerBean ) throws Exception{
    	logger.info("receive do get in ProjectManagers.doGet");
    	//sn is user_tbl' sn = companysn(4 bytes)+usersn(4 bytes)
    	String sn = projectManagerBean.getSn();
    	String company = projectManagerBean.getCompany();
    	String fieldName = projectManagerBean.getFieldName();
    	String projManagName = projectManagerBean.getName();
    	String result = "";
    	if((null == projManagName) || (projManagName.equals(""))){
    		if((null == fieldName) || (fieldName.equals(""))){
        		if(((null == sn) || (sn.equals(""))) && ((null == company) || (company.equals("")))){
        			logger.error("Fail to get the sn {} or the company {} or username {}",sn,company,fieldName);
            		return "['fail']";
                	
            	}else{
            		String companysn = sn.substring(0, 4);
                	String companyNM = null;
                	CompanyInfo companyInfo = CompanyInfoDAO.getCompanyInfoBySN(companysn);
                	if(null == companyInfo){
                		logger.info("Fail to query companyInfo {}",companyInfo);
                		return "['fail']";
                	}else{
                		companyNM = companyInfo.getName();
                		if(!companyNM.equals(company)){
                			return "['fail']";
                		}
                	}
            	}
        	}else{
        		FieldInfo field = FieldInfoDAO.getFieldInfoByNM(fieldName);
        		String fieldsn = field.getSn();
        		List<ProjectManagers> projectManagersList = ProjectmanagersDAO.getUsersManagersByField(fieldsn);
        		if((null ==  projectManagersList) || (projectManagersList.size() == 0)){
        			logger.info("Fail to get the projectManager {} by the username {}",projectManagersList,fieldsn);
        			return "['fail']";
        		}else{
        			for(ProjectManagers projectManagers : projectManagersList){
        				String projrctManagerName = projectManagers.getUsername();
        				result += "<option value=\""+projectManagers.getTel()+"\">"+projrctManagerName+"</option>";
        			}
        		}
        	}
    	}else{
    		ProjectManagers projectManager = ProjectmanagersDAO.getUsersManagersByUN(projManagName);
    		if(null == projectManager){
    			logger.error("Fail to get the projectManager {} by the projectManagName {}",projectManager,projManagName);
    			return "['fail']";
    		}else{
    			result = projectManager.getSn();
    		}
    	}
    	
    	return result;
    }
    
	@RequestMapping(value="doPost",method=RequestMethod.POST)
    @ResponseBody
    protected ProjectManagerRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in ProjectManagers.doPost "+inStr);
       	
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
		ProjectManagerRspBean projectManagerRspBean = null;
        switch (oper){
        case "load": {
        	List<ProjectManagers> projectManagersList = null;
        	int total = 1;
        	int records = 0;
        	int page = Integer.parseInt(postMap.get("page"));
        	int row = Integer.parseInt(postMap.get("rows"));
        	String hql = null;
        	String hqlStart = "from ProjectManagers pm ";
        	int paramentNum = 0;
        	String paramentNameOne = null;
        	String paramentValueOne = null;
        	String paramentValueTwo = null;
        	String paramentNameTwo = null;
        	if((null == postMap.get("username")) && (null == postMap.get("usermail")))
        	{
        		paramentNum = 0;
        		hql = hqlStart;
        	}else if((null != postMap.get("username")) && (null == postMap.get("usermail"))){
        		paramentNum = 1;
        		hql = hqlStart + " where pm.username = :username";
        		paramentNameOne = "username";
        		paramentValueOne = postMap.get("username");
        	}else if((null == postMap.get("username")) && (null != postMap.get("usermail"))){
        		paramentNum = 1;
        		hql = hqlStart + " where pm.usermail = :usermail";
        		paramentNameOne = "usermail";
        		paramentValueOne = postMap.get("usermail");
        	}else if((null != postMap.get("username")) && (null != postMap.get("usermail"))){
        		paramentNum = 2;
        		hql = hqlStart + " where pm.username = :username and pm.usermail = :usermail";
        		paramentNameOne = "username";
        		paramentValueOne = postMap.get("username");
        		paramentNameTwo = "usermail";
        		paramentValueTwo = postMap.get("usermail");
        	}
        	if((null == hql) || (hql.equals(""))){
        		logger.error("Fail to get the hql {}",hql);
        		return null;
        	}
        	projectManagersList = GetObjectListByParament.getProjectManagersByParament(page, row, paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo,hql);
        	if(!projectManagersList.isEmpty()){
        		records = ParamentSave.records;//all record number
        		total = ParamentSave.respTotal(ParamentSave.records, row);
        	}else{
        		projectManagersList = GetObjectListByParament.getProjectManagersByParament(page-1, row, paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo,hql);
            	if(!projectManagersList.isEmpty()){
            		records = ParamentSave.records;//all record number
            		total = ParamentSave.respTotal(ParamentSave.records, row);
            	}
            	page = ParamentSave.resPage(page);
        	}
        	projectManagerRspBean = new ProjectManagerRspBean(page, total, records, projectManagersList);
		    break;
        }
        
        case "add": {
        	//judge SystemManager if not exist the username
        	try {
				SystemManagers systemManagers = SystemManagersDAO.getMangersInfoByUsername(postMap.get("username"));
				if((null != systemManagers) && (!systemManagers.equals(""))){
					logger.info("the username {} has existed in the SystemManagers! so that Not allow to add the usename to ProjectManager!",postMap.get("username"));
					return null;
				}
				boolean result = QueryDiffDataTblInfo.judgeCityField(postMap.get("city"),postMap.get("field"));
	        	if(result){
	        		String newsn = QueryDiffDataTblInfo.responseSN("users_tbl", "company", postMap.get("company"));
	            	ProjectManagers projectManagers = new ProjectManagers(postMap.get("username"), newsn, postMap.get("userkey"), postMap.get("tel"), postMap.get("usermail"), LocalDateTime.now().toLocalDate(),
	            			                                              postMap.get("company"), postMap.get("city"), postMap.get("field"), Prio.valueOf(postMap.get("prio")));
	            	ProjectmanagersDAO.create(projectManagers);
	            	break;
	        	}else{
	        		logger.error("Choose the city {}  that hasn't the filed {} ! so Needn't response any data!",postMap.get("city"),postMap.get("field"));
	        		return null;
	        	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
        }
        
        case "del":{
        	//delete associated data of prijectManager'sn
        	OperateSN.deletediffTblAssociateSN("users_tbl", postMap.get("id"));
        	ProjectManagers projectManagers = ProjectmanagersDAO.get(Integer.parseInt(postMap.get("id")));
        	ProjectmanagersDAO.delete(projectManagers);
        	break;
        }
        
        case "edit":{
        	boolean result = QueryDiffDataTblInfo.judgeCityField(postMap.get("city"),postMap.get("field"));
        	String newsn = null;
        	if(result){
        		String oldsn = OperateSN.getOldInfo("users_tbl",postMap.get("id"),"company",postMap.get("company"));
            	if(null != oldsn){
            		if(oldsn != OperateSN.newestData){
                		OperateSN.updatediffTblJointSN("users_tbl", oldsn);
                		newsn = OperateSN.changeData;
                		//postMap.put("sn", OperateSN.changeData);
                	}else{
                		postMap.put("sn", OperateSN.newestData);
                		newsn = OperateSN.newestData;
                	}
            		ProjectManagers projectManagers = ProjectmanagersDAO.get(Integer.parseInt(postMap.get("id")));
            		projectManagers.setUsername(postMap.get("username"));
                    projectManagers.setSn(newsn);
                    projectManagers.setUserkey(postMap.get("userkey"));
                    projectManagers.setTel(postMap.get("tel"));
                    projectManagers.setUsermail(postMap.get("usermail"));
                    projectManagers.setCompany(postMap.get("company"));
                    projectManagers.setCity(postMap.get("city"));
                    
                    projectManagers.setField(postMap.get("field"));
                    projectManagers.setPrio(Prio.valueOf(postMap.get("prio")));
            		ProjectmanagersDAO.update(projectManagers);
            	}else{
            		logger.error("Fail to get oldsn {} and update to data of users_tbl!",oldsn);
            		return null;
            	}
        	}else{
        		logger.error("Choose the city {} that has the filed {} ! so Needn't response any data!",postMap.get("city"),postMap.get("field"));
        		return null;
        	}
        	break;
        }
        
        case "query":{
        	break;
         }
        
        default:{
         }
       }//switch
        return projectManagerRspBean;
    }
}
