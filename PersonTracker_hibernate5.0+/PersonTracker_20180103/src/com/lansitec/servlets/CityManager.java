package com.lansitec.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.common.InitUploadContext;
import com.lansitec.common.JqueryGridParser;
import com.lansitec.dao.CityInfoDAO;
import com.lansitec.dao.FieldInfoDAO;
import com.lansitec.dao.beans.CityInfo;
import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.springmvc.beans.CityRspBean;

/**
 * Servlet implementation class CityManager
 * CityManager主要用于城市的管理.
 */
@Controller
@RequestMapping("/CityManager")
public class CityManager {
    private Logger logger = LoggerFactory.getLogger(CityManager.class);
    
    @RequestMapping(value="doGet",method=RequestMethod.GET)
    @ResponseBody
	protected String doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("receive do get in CityManager.doGet");
		List<CityInfo>  cityList = null;
		String resp = "";
		String fieldsn = request.getParameter("FieldName");
        try {
        	if((null == fieldsn) || (fieldsn.equals(""))){
        		cityList = CityInfoDAO.getAllCityInfo();
    			if(null == cityList || cityList.size() == 0){
    	        	logger.info("city_info_tbl does't have any information!");
    	        	return null;
    	        }
    			//this is the special response for JQGrid select column
    			
    			for (CityInfo city:cityList) {
    				resp += "<option value=\""+city.getCity()+"\">"+city.getCity()+"</option>";
    			}
        	}else{
        		FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(fieldsn);
        		if(null == fieldInfo){
        			return resp;
        		}
        		resp += "<option value=\""+fieldInfo.getCity()+"\">"+fieldInfo.getCity()+"</option>";
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        return resp;
	}

    @RequestMapping(value="doPost",method=RequestMethod.POST)
    @ResponseBody
	protected CityRspBean doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	/*CityManager cityManager = new CityManager();
		Filters.Object = cityManager;*/
    	
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),Charset.forName("UTF-8")));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //防止汉字乱码
        logger.info("receive do post in CityManager.doPost "+inStr);
        
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
		CityRspBean cityRspBean = null;
		try {
			switch (oper){
	        case "load": {
	        	List<CityInfo> cityInfosList = null;
	        	int total = 1;
	        	int records = 0;
	        	int page = Integer.parseInt(postMap.get("page"));
	        	int row = Integer.parseInt(postMap.get("rows"));
	        	String hql = "from CityInfo ct";
	        	cityInfosList = GetObjectListByParament.getCityInfoListByParament(page, row, hql);
				if(!cityInfosList.isEmpty()){
					records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
				}else{
					cityInfosList = GetObjectListByParament.getCityInfoListByParament(page-1, row, hql);
					if(!cityInfosList.isEmpty()){
						records = ParamentSave.records;//all record number
		        		total = ParamentSave.respTotal(ParamentSave.records, row);
					}
					page = ParamentSave.resPage(page);
				}
				cityRspBean = new CityRspBean(page, total, records, cityInfosList);
				logger.info("cityRspBean {}",cityRspBean);
			    break;
	        }
	        
	        case "add": {
	        	CityInfo cityInfo = CityInfoDAO.getCityInfo(postMap.get("city"));
	        	if(null == cityInfo){
	        		//cityInfo = new CityInfo(postMap.get("city"));
	        		cityInfo = (CityInfo) InitUploadContext.helper.getBean("cityInfo");
	        		logger.info("cityInfo {} ",cityInfo);
	        		cityInfo.setCity(postMap.get("city"));
	            	CityInfoDAO.create(cityInfo);
	        	}else{
	        		logger.info("the city {} has already!",postMap.get("city"));
	        		return null;
	        	}
	        	break;
	        }
	        
	        case "del":{
	        	CityInfo cityInfo = CityInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        	CityInfoDAO.delete(cityInfo);
	        	break;
	        }
	        
	        case "edit":{
	        	CityInfo cityInfo = CityInfoDAO.get(Integer.parseInt(postMap.get("id")));
	        	cityInfo.setCity(postMap.get("city"));
	        	CityInfoDAO.update(cityInfo);
	        	break;
	        }
	        
	        case "query":{
	        	break;
	         }
	        
	        default:{
	         }
	       }//switch
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
      return cityRspBean;
	}
    
    //the second time read questInput
    public void getRequestAndResponse(HttpServletRequest request, HttpServletResponse response){
    	try {
			doPost(request,response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
