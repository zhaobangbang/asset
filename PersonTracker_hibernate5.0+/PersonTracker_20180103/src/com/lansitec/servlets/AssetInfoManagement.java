package com.lansitec.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import com.lansitec.dao.AssetInfoDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.beans.AssetInfo;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.enumlist.AssetStatus;
import com.lansitec.enumlist.AssetType;
import com.lansitec.enumlist.Devtype;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.OperateSN;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.AssetInfoRspBean;

@Controller
@RequestMapping("/AssetInfoManagement ")
public class AssetInfoManagement {
	private Logger logger = LoggerFactory.getLogger(AssetInfoManagement.class);
	
	@RequestMapping(value="doGet",method=RequestMethod.GET)
	@ResponseBody
	protected String doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String assetId = request.getParameter("id");
		String fieldSN = request.getParameter("fieldSn");
		//page get all asset info by type
		
		String result = "";
		try {
			if((null == assetId) || (assetId.equals(""))){
				List<AssetInfo> assetInfoList = null;
				assetInfoList = AssetInfoDAO.getAllAssetInfo();
				if(assetInfoList.isEmpty()){
					logger.error("Fail to get the assetInfoList {}",assetInfoList);
					return result;
				}
				for(AssetInfo assetInfo : assetInfoList){
					//assetsn eight bytes before is fieldsn
					String assetEightBytesBefore = assetInfo.getSn().substring(0, 8);
					if(assetEightBytesBefore.equals(fieldSN)){
						result += "<option value=\""+assetInfo.getName()+"\">"+assetInfo.getName()+"</option>";
					}
				}
			}else{
				AssetInfo assetInfo = AssetInfoDAO.get(Integer.parseInt(assetId));
				if(null == assetInfo){
					logger.error("Fail to get the assetInfo {} ",assetInfo);
					return result;
				}
				result += "<option value=\""+assetInfo.getId()+"\">"+assetInfo.getName()+"</option>";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	protected AssetInfoRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in CityManager.doPost "+inStr);
        
        String username = request.getParameter("usrname");
        String assetName = request.getParameter("name");
       	
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
		AssetInfoRspBean assetInfoRspBean = null;
		switch (oper) {
		case "load":{
			List<AssetInfo> assetInfosList = null;
			int total = 1;
        	int records = 0;
        	int page = Integer.parseInt(postMap.get("page"));
        	int row = Integer.parseInt(postMap.get("rows"));
        	String hql = null;
        	String result = "";
			ProjectManagers projectManagers = null;
			if((null == username) && (null == assetName)){
				logger.error("Fail to get the username {},so that Can't response any data!",username);
				response.getWriter().write("['fail']");
				break;
			}else if((null != username) && (null == assetName)){
				try {
					String field = null;
					List<AssetInfo> assetInfoList = null;
					projectManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
					if(null == projectManagers){
						logger.error("Fail to get the projectManagers {} by the username {}",projectManagers,username);
						return null;
					}else{
						field = projectManagers.getField();
						assetInfoList = AssetInfoDAO.getAllAssetInfo();
						if((null == assetInfoList) || (assetInfoList.size() == 0)){
							logger.info("Fail to get assetInfoList {} data!",assetInfoList);
							return null;
						}else{
							boolean existSN = false;
							for(AssetInfo assetInfo : assetInfoList){
								if(assetInfo.getSn().substring(0, 8).equals(field)){
									DeviceListMgr.fieldList.add(assetInfo.getSn());
									existSN = true;
								}
							}
							if(!existSN){
					        	logger.info("Fail to get asset'sn eight bytes before eq to projectManager'field {}",field);
					        	response.getWriter().write("['fail']");
								break;
							}
						}
					}
					hql = ParamentSave.getJoinSQL("AssetInfo af", "", "", "", "", "", "");
					if((null == hql) || (hql.equals(""))){
		        		logger.error("Fail to get the sql {}",hql);
		        		return null;
		        	}
					if(!DeviceListMgr.fieldList.isEmpty()){
						for(String assetsn :DeviceListMgr.fieldList){
							 result += "af.sn= '"+assetsn+"' or ";
						}
						hql = hql+" where " + result.substring(0,result.length()-4);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if((null != username) && (null != assetName)){
				hql = ParamentSave.getJoinSQL("AssetInfo af", "af.name", assetName, "", "", "", "");
			}
			assetInfosList = GetObjectListByParament.getAssetInfoByParament(page, row, hql);
			if(!assetInfosList.isEmpty()){
				records = ParamentSave.records;//all record number
        		total = ParamentSave.respTotal(ParamentSave.records, row);
			}else{
				assetInfosList = GetObjectListByParament.getAssetInfoByParament(page-1, row, hql);
				if(!assetInfosList.isEmpty()){
					records = ParamentSave.records;//all record number
	        		total = ParamentSave.respTotal(ParamentSave.records, row);
				}
				page = ParamentSave.resPage(page);
			}
			assetInfoRspBean = new AssetInfoRspBean(page, total, records, assetInfosList);
			logger.info("assetInfoRspBean {}",assetInfoRspBean);
			break;
		}
		case "add":{	
			AssetInfoRspBean rsp = new AssetInfoRspBean();
			String newsn = QueryDiffDataTblInfo.responseSN("asset_info_tbl", "field", postMap.get("f_sn"));
			try {
				//purchase time
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dates = sdf.parse(postMap.get("purchase"));
				Instant instant = dates.toInstant();
				ZoneId zoneId = ZoneId.systemDefault();
				LocalDate time= instant.atZone(zoneId).toLocalDate();
				AssetInfo assetInfo = new AssetInfo(postMap.get("assetname"), newsn, AssetType.valueOf(postMap.get("a_type")), time, AssetStatus.valueOf(postMap.get("a_status")), postMap.get("memo"));
				AssetInfoDAO.create(assetInfo);
				rsp.setStatus("ok");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rsp.setStatus("internal error!");
				return rsp;
			}
			return rsp;
		}
		case "del":{
			AssetInfoRspBean rsp = new AssetInfoRspBean();
			try {
				//delete associate data of asset'sn
				OperateSN.deletediffTblAssociateSN("asset_info_tbl", postMap.get("id"));
				AssetInfo  assetInfo = AssetInfoDAO.get(Integer.parseInt(postMap.get("id")));
				String deveui = assetInfo.getDeveui();
				String assetname = assetInfo.getName();
				AssetInfoDAO.delete(assetInfo);
				if((null != deveui) && (deveui.equals(""))){
				DevInfo devInfo = DevInfoDAO.getDevInfoByDeveui(deveui);
				if(null != devInfo){
					devInfo.setOwner("");
					DevInfoDAO.update(devInfo);
				}else{
					logger.info("the assetUser {} doesn't distructed the deveui!",assetname);
				}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rsp.setStatus("internal error!");
				return rsp;
			}
			rsp.setStatus("ok");
        	return rsp;
		}
	    case "edit":{
	    	AssetInfoRspBean rsp = new AssetInfoRspBean();
	    	String editDeveui = null;
	    	DevInfo devInfo = null;
	    	String oldsn = OperateSN.getOldInfo("asset_info_tbl", postMap.get("a_id"),"field",postMap.get("f_sn"));
	    	try {
		    	if((null != postMap.get("deveui")) && (!postMap.get("deveui").equals(""))){
					devInfo = DevInfoDAO.getDevInfoByDeveui("004a77021103"+postMap.get("deveui"));
					if(null != devInfo){
						if(((null == devInfo.getOwner()) || (devInfo.getOwner().equals(""))) || (devInfo.getOwner().equals(oldsn))){
							editDeveui = devInfo.getDeveui();
						}else{
							SystemManagers systemManagers = SystemManagersDAO.getMangersInfoByUsername(devInfo.getOwner());
        					if((null ==  systemManagers)){
        						logger.info("the deveui {} already has been distributed others !","004a77021103"+postMap.get("deveui"));
            					rsp.setStatus("the deveui already has been distributed others !");
            					return rsp;
        					}
        					editDeveui = devInfo.getDeveui();
						}
						
					}else{
						logger.error("Fail to get the devInfo {} by the deveui {}",devInfo,"004a77021103"+postMap.get("deveui"));
        	        	rsp.setStatus("the deveui doesn't exist in dataBase !");
        	        	return rsp;
					}
					
		    	}
	    	String newsn = null;
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dates = sdf.parse(postMap.get("purchase"));
			Instant instant = dates.toInstant();
			ZoneId zoneId = ZoneId.systemDefault();
			LocalDate time= instant.atZone(zoneId).toLocalDate();
			if(null != oldsn){
	    		if(!oldsn.equals(OperateSN.newestData)){
	    			OperateSN.updatediffTblJointSN("asset_info_tbl", oldsn);
	    			newsn = OperateSN.changeData;
	        		//postMap.put("sn", OperateSN.changeData);
	        	}else{
	        		newsn = OperateSN.newestData;
	        		//postMap.put("sn", OperateSN.newestData);
	        	}
	    		AssetInfo assetInfo = AssetInfoDAO.get(Integer.parseInt(postMap.get("a_id")));
	    		assetInfo.setName(postMap.get("assetname"));
	    		assetInfo.setSn(newsn);
	    		assetInfo.setPurchase(time);
	    		assetInfo.setType(AssetType.valueOf(postMap.get("a_type")));
	    		assetInfo.setStatus(AssetStatus.valueOf(postMap.get("a_status")));
	    		assetInfo.setMemo(postMap.get("memo"));
	    		assetInfo.setDeveui(editDeveui);
	        	AssetInfoDAO.update(assetInfo);
	        	if((null != editDeveui) && (!devInfo.getOwner().equals(newsn))){
	        		devInfo.setOwner(newsn);
	        		devInfo.setDevtype(Devtype.×Ê²ú);
		        	DevInfoDAO.update(devInfo);
	        	}
	        	rsp.setStatus("ok");
		    	}else{
		    		logger.error("Fail to get oldsn and update any date in asset_info_tbl!");
		    	}
			} catch (Exception e) {
				e.printStackTrace();
				rsp.setStatus("internal error!");
				return rsp;
			}
	    	return rsp;
	   }
	   case "query":{
         	break;
       }
	   default:{
		    break;
	   }
	  }//switch
	return assetInfoRspBean;

	}
}
