package com.lansitec.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.common.JqueryGridParser;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.enumlist.Devtype;
import com.lansitec.enumlist.Prio;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.springmvc.beans.DevsInfoListRspBean;

@Controller
@RequestMapping("/DeviceListMgr")
public class DeviceListMgr {
   private Logger logger = LoggerFactory.getLogger(DeviceListMgr.class);
   public static List<String> fieldList  = new LinkedList<String>();
   @RequestMapping(value="doGet",method=RequestMethod.GET)
   @ResponseBody
   private String doGet(@RequestParam String usrname) throws IOException{
	   ProjectManagers projectManagers = null;
	   String field = null;
	   List<DevInfo> devInfoList = null;
	   String result = "[";
	   try {
		    projectManagers = ProjectmanagersDAO.getUsersManagersByUN(usrname);
		    if(null == projectManagers){
		    	logger.error("Fail to get the projectManagers {} data!",projectManagers);
		    	return null;
		    }else{
		    	field = projectManagers.getField();
		    	devInfoList = DevInfoDAO.getDevInfoByField(field);
		    	if((null == devInfoList) || (devInfoList.size() == 0)){
		    		logger.error("Fail to get devInfoList {} by the field {} and response any data!",devInfoList,field);
		    		return "fail";
		    	}else{
		    		for(DevInfo devInfo : devInfoList){
		    			result += "'" + devInfo.getDeveui() + "',";
		    		}
		    		result += "]";
		    	}
		    }
		    
	   } catch (Exception e) {
		e.printStackTrace();
	   }
	  return result;
   }
   
  
   @RequestMapping(value="doPost",method=RequestMethod.POST)
   @ResponseBody
   private DevsInfoListRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
           sb.append(temp);  
        }  
        br.close();
   
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //防止汉字乱码
        logger.info("receive do post in DeviceListMgr.doPost "+inStr);
       
        String usrname = request.getParameter("usrname");
	    String deveui = request.getParameter("deveui");
	    String oper = null;
        Map<String, String> postMap = new HashMap<String, String>();
       // if(((null == usrname) || (usrname.equals(""))) && ((null == deveui) || (deveui.equals("")))){
        	oper = JqueryGridParser.parserGridString(inStr, postMap);
        //}
        /*String editId = request.getParameter("id");
        if((null == editId) || (editId.equals(""))){
        	oper="edit";
        }else{
        	oper="del";
        }
        String editDeveui = request.getParameter("editDeveui");
        String editDevtype = request.getParameter("editDevtype");
        String editField = request.getParameter("editField");
        String editCon = request.getParameter("editCon");
        String editOwner = request.getParameter("editOwner");
        String editTel = request.getParameter("editTel");
        */
        
       
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		if(null == oper)
		{
			return null;
		}
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			 String username = (String) reqSession.getAttribute("usrname");
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
		DevsInfoListRspBean devsInfoListRspBean = null;
		switch (oper){
        case "load": {
        	List<DevInfo> devInfosList = null;
        	int total = 1;
        	int records = 0;
        	int page = Integer.parseInt(postMap.get("page"));
        	int row = Integer.parseInt(postMap.get("rows"));
        	String hql = null;
        	String hqlStart = "from DevInfo dev ";
        	int paramentNum = 0;
        	String paramentNameOne = null;
        	String paramentValueOne = null;
        	if((null == usrname) && (null == deveui)){
        		logger.error("Fail to get the usrname {} and the deveui {}",usrname,deveui);
        		return null;
        	}else if((null != usrname) && (null == deveui)){
        		try {
					ProjectManagers projectManagers = ProjectmanagersDAO.getUsersManagersByUN(usrname);
					if(null != projectManagers){
						/*//Prio.公司级 can search all devid
						if(projectManagers.getPrio().equals(Prio.公司级)){
							String companysn = projectManagers.getCompany();
							List<FieldInfo> fieldlist = FieldInfoDAO.getAllField();
							if((null == fieldlist) || (fieldlist.size() == 0)){
								logger.error("Fail to get fieldlist {} by getAllField,so that Faill to response information!",fieldlist);
								response.getWriter().write("['fail']");
								return;
							}else{
							    boolean judgeValue = false;
								for(FieldInfo field : fieldlist){
									String fieldsn = field.getSn();
									if(fieldsn.substring(0,4).equals(companysn)){
										fieldList.add(fieldsn);
										judgeValue = true;
									}
								}
								if(judgeValue){
									postMap.put("prio", "公司级");
								}else{
									logger.error("fieldsn four bytes before doesn't eq to companysn {}",companysn);
									response.getWriter().write("['fail']");
									break;
								}
							}
						}
						else if(projectManagers.getPrio().equals(Prio.城市级)){
							String city = projectManagers.getCity();
							List<FieldInfo> field = FieldInfoDAO.getFieldInfoByCity(city);
							if((null == field) || (field.size() == 0)){
								logger.error("Fail to get field {} by city",field);
								response.getWriter().write("['fail']");
								return;
							}else{
								boolean judgeValue = false;
								for(FieldInfo fieldInfo : field){
									// hava so many fieldInfo
									String fieldsn = fieldInfo.getSn();
									if(projectManagers.getField().equals(fieldsn)){
										fieldList.add(fieldsn);
										judgeValue = true;
									}
									
								}
								if(judgeValue){
							        postMap.put("prio", "城市级");
								}else{
									logger.error("projectManager'field is ditributed that is error! it doesn't eq to fieldsn!");
									response.getWriter().write("['fail']");
									break;
								}
							}
						}else */
						if(projectManagers.getPrio().equals(Prio.工地级)){
							String fieldsn = projectManagers.getField();
							paramentNum = 1;
							hql = hqlStart + "where dev.field = :field";
							paramentNameOne = "field";
							paramentValueOne = fieldsn;
						}else{
							logger.error("the projectManager's {} prio {} doesn't belong to loginRange !",usrname,projectManagers.getPrio());
							return null;
						}
						
					}else{
						logger.info("the logger is SystemManager!");
						paramentNum = 0;
						hql = hqlStart + "order by deveui";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}else if(null != deveui){
        		paramentNum = 1;
        		hql = hqlStart + " where dev.deveui = :deveui";
        		paramentNameOne="deveui";
        		paramentValueOne = deveui;
        	}
        	if((null == hql) || (hql.equals(""))){
        		logger.error("Fail to get the hql {}",hql);
        		return null;
        	}
        	devInfosList = GetObjectListByParament.getDevInfoListByParament(page, row, paramentNum,paramentNameOne,paramentValueOne,hql);
        	if(!devInfosList.isEmpty()){
        		records = ParamentSave.records;//all record number
        		total = ParamentSave.respTotal(ParamentSave.records, row);
        	}else{
        		devInfosList = GetObjectListByParament.getDevInfoListByParament(page-1, row, paramentNum,paramentNameOne,paramentValueOne,hql);
            	if(!devInfosList.isEmpty()){
            		records = ParamentSave.records;//all record number
            		total = ParamentSave.respTotal(ParamentSave.records, row);
            	}
            	page = ParamentSave.resPage(page);
        	}
        	devsInfoListRspBean = new DevsInfoListRspBean(page, total, records, devInfosList);
		    break;
        }
        
        case "add": {
        	if(null != usrname){
        		try {
					SystemManagers systemManagers = SystemManagersDAO.getMangersInfoByUsername(usrname);
					if(null == systemManagers){
						logger.error("Fail to get the projectManagers {} by the usrname {}",systemManagers,usrname);
						return null;
					}
					String deveuiArrayStr = postMap.get("deveui");
		    		String[] deveuiArray = deveuiArrayStr.split(",");
		    		LocalDate date = LocalDateTime.now().toLocalDate();
		    		for(String deveuis : deveuiArray) {
					    DevInfo devInfo = new DevInfo(deveuis, Devtype.valueOf("未分配"), postMap.get("field"), date,usrname, postMap.get("mapid"), postMap.get("memo"));
					    DevInfoDAO.create(devInfo);
			            //add devNumber by field'sn at the same time
			            //OperateSN.addAndDelAndUpdateDevNuberByFieldSN(postMap.get("field"),"",oper);
		    	    }	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	/*if(postMap.get("devtype").equals("未分配")){
        		logger.info("visitor is 未分配！ the owner {} is SystemManager",postMap.get("owner"));
        	}else{
        		String owner = QueryDiffDataTblInfo.getDiffSNByDevtype(postMap.get("field"),postMap.get("devtype"));
        		if(null == owner){
        			logger.info("visitor is 访客 ！ so that owner {} is null",owner);
        		}else if(owner.equals("")){
        			logger.error("Fail to get owner {} and update any data of in dev_info_tbl!",owner);
        			break;
        		}
        		postMap.put("owner", owner);
        	}*/
        	break;
        }
        case "del":{
        	//delete devNumber by field'sn at the same time
        	/*DevInfo devInfo = DevInfoDAO.get(Integer.parseInt(postMap.get("id")));
        	String fieldsn = devInfo.getField();
        	OperateSN.addAndDelAndUpdateDevNuberByFieldSN(fieldsn,"",oper);*/
        	DevInfo devInfo = DevInfoDAO.get(Integer.parseInt(postMap.get("id")));
        	DevInfoDAO.delete(devInfo);
        	break;
        }
        
        case "edit":{
        	String deveuiArrayStr = postMap.get("deveui");
    		String[] deveuiArray = deveuiArrayStr.split(",");
    		DevInfo devInfo = null;
    		try {
	    		
	    		for(String deveuis : deveuiArray) {
	    			devInfo = DevInfoDAO.get(Integer.parseInt(postMap.get("id")));
					if(null == devInfo){
						logger.error("Fail to get devInfo {} and update any data in dev_info_tbl",devInfo);
						break;
					}else{
						String owner = null;
						logger.info("edit the deveui {} devType {} owner {} field {} mapid {}", 
								              deveuis,postMap.get("devtype"),postMap.get("owner"),postMap.get("field"),postMap.get("mapid"));
						owner = postMap.get("owner");
						DevInfo devInfos = DevInfoDAO.getDevInfoByOwener(owner);
						SystemManagers sysManager = SystemManagersDAO.getMangersInfoByUsername(owner);
						if((null == devInfos) || (null != sysManager)){
							devInfo.setOwner(owner);
							devInfo.setDeveui(deveuis);
							devInfo.setDevtype(Devtype.valueOf(postMap.get("devtype")));
							devInfo.setField(postMap.get("field"));
							devInfo.setMapid(postMap.get("mapid"));
							devInfo.setMemo(postMap.get("memo"));
							DevInfoDAO.update(devInfo);
						}else{
							logger.info("the owner {} already exist in dev_info_tbl!",owner);
						}
						
						break;
					}
	    		  } 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        
        case "query":{
        	break;
         }
        
        default:{
         }
       }//switch
		return devsInfoListRspBean;
   }
   
}
