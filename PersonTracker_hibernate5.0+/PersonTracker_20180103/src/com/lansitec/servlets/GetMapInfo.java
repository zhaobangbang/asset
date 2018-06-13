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
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.OperateSN;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.MapInfoRspBean;

@Controller
@RequestMapping("/GetMapInfo")
public class GetMapInfo {
    private Logger logger = LoggerFactory.getLogger(GetMapInfo.class);
    
	@RequestMapping(value="doGet",method=RequestMethod.GET)
	@ResponseBody
	protected String doGet(HttpServletRequest request) throws IOException{
		logger.info("receive do get in GetMapInfo.doGet");
		//sn is mapsn,fieldSN eq first eight bytes of mapsn
		String mapName = request.getParameter("name");
		//response mapName by projectManager'username
	    String username = request.getParameter("username");
	    String fieldName = request.getParameter("fieldName");
		String result = "";
		List<MapInfo> mapInfoList = null;
	    MapInfo mapInfo = null;
		try {
			if((null != mapName) && (null == username) && (null == fieldName)){
				mapInfo = MapInfoDAO.getMapInfoByNM(mapName);
				if(null == mapInfo ){
					logger.info("Fail to get data about the mapName {}",mapName);
					return null;
				}else{
					result = mapInfo.getSn();
				}
			
			}else if((null == mapName) && (null == username) && (null == fieldName)){
				mapInfoList = MapInfoDAO.getAllMapInfo();
				if((null == mapInfoList) || (mapInfoList.size() == 0)){
					logger.error("Fail to get mapInfoList {}",mapInfoList);
					result = "fail";
					return result;
				}else{
					for(MapInfo mapInfos : mapInfoList){
						String mapname = mapInfos.getName();
						result+="<option value=\""+mapname+"\">"+mapname+"</option>";
					}
				}
		   }else if((null == mapName) && (null != username) && (null == fieldName)){
			   ProjectManagers projectManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
			   if(null == projectManagers){
					logger.info("Fail to query projectManagers {} by username {}",projectManagers,username);
					return "fail";
				}else{
					result = getMapNameByField(projectManagers.getField());
				}
		   }else if((null == mapName) && (null == username) && (null != fieldName)){
			   FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoByNM(fieldName);
			   if(null == fieldInfo){
				   logger.error("Fail to get the fieldInfo {} by the fieldName {}",fieldInfo,fieldName);
				   return "fail";
			   }else{
				   String fieldsn = fieldInfo.getSn();
				   result = getMapNameByField(fieldsn);
			   }
		   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	protected MapInfoRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in GetMapInfo.doPost "+inStr);
       	
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
		MapInfoRspBean mapInfoRspBean = null;
        switch (oper){
        case "load": {
        	List<MapInfo> mapInfosList = null;
        	int total = 1;
        	int records = 0;
        	int page = Integer.parseInt(postMap.get("page"));
        	int row = Integer.parseInt(postMap.get("rows"));
        	String hql = "from MapInfo map";
        	mapInfosList = GetObjectListByParament.getMapInfosByParament(page, row, hql);
        	if(!mapInfosList.isEmpty()){
        		records = ParamentSave.records;//all record number
        		total = ParamentSave.respTotal(ParamentSave.records, row);
        	}else{
        		mapInfosList = GetObjectListByParament.getMapInfosByParament(page-1, row, hql);
            	if(!mapInfosList.isEmpty()){
            		records = ParamentSave.records;//all record number
            		total = ParamentSave.respTotal(ParamentSave.records, row);
            	}
            	page = ParamentSave.resPage(page);
        	}
        	mapInfoRspBean = new MapInfoRspBean(page, total, records, mapInfosList);
		    break;
        }
        
        case "add": {
        	String newsn = QueryDiffDataTblInfo.responseSN("map_info_tbl", "field", postMap.get("field"));
        	MapInfo mapInfo = new MapInfo(postMap.get("name"), newsn, postMap.get("building"), postMap.get("ground"), Integer.parseInt(postMap.get("width")), Integer.parseInt(postMap.get("length")), postMap.get("memo"));
        	MapInfoDAO.create(mapInfo);
        	break;
        }
        
        case "del":{
        	MapInfo mapInfo = MapInfoDAO.get(Integer.parseInt(postMap.get("id")));
        	MapInfoDAO.delete(mapInfo);
        	break;
        }
        
        case "edit":{
        	String oldsn = OperateSN.getOldInfo("map_info_tbl",postMap.get("id"),"field", postMap.get("field"));
        	String newsn = null;
        	if(null != oldsn){
        		if(!oldsn.equals(OperateSN.newestData)){
        			newsn = OperateSN.changeData;
            		//postMap.put("sn", OperateSN.changeData);
            	}else{
            		newsn = OperateSN.newestData;
            		//postMap.put("sn", OperateSN.newestData);
            	}
        		MapInfo mapInfo = MapInfoDAO.get(Integer.parseInt(postMap.get("id")));
        		mapInfo.setName(postMap.get("name"));
        		mapInfo.setSn(newsn);
        		mapInfo.setBuilding(postMap.get("building"));
        		mapInfo.setGround(postMap.get("ground"));
        		mapInfo.setWidth(Integer.parseInt(postMap.get("width")));
        		mapInfo.setLength(Integer.parseInt(postMap.get("length")));
        		mapInfo.setMemo(postMap.get("memo"));
        		MapInfoDAO.update(mapInfo);
        	}else{
        		logger.error("Fail to get oldsn {} and update to data of map_info_tbl!",oldsn);
        	}
        	
        	break;
        }
        
        case "query":{
        	break;
         }
        
        default:{
         }
       }//switch
        return mapInfoRspBean;
	}
	
	protected String getMapNameByField(String field){
		String result = "";
		List<MapInfo> mapInfoList;
		try {
			mapInfoList = MapInfoDAO.getAllMapInfo();
			if((null == mapInfoList) || (mapInfoList.size() == 0)){
				logger.error("Fail to get mapInfoList {}",mapInfoList);
				result = "fail";
				return result;
			}else{
				boolean judgeValue = false;
				for(MapInfo mapInfos : mapInfoList){
					if(mapInfos.getSn().substring(0, 8).equals(field)){
						String mapname = mapInfos.getName();
						result+="<option value=\""+mapname+"\">"+mapname+"</option>";
						judgeValue = true;
					}
				}
				if(!judgeValue){
					logger.info("MapInfo's sn eight bytes before doesn't eq to projectManagers's field {}",field);
					return "fail";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return result;
	}
}
