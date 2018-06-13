package com.lansitec.servlets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lansitec.enumlist.DevOpr;
import com.alibaba.fastjson.JSON;
import com.lansitec.beans.DevicesOperateBean;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.StatusRecordDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.dao.beans.StatusRecord;
import com.lansitec.springmvc.beans.DevStatusAndpositionData;
import com.lansitec.springmvc.beans.GetRegDevInfo;
import com.lansitec.springmvc.beans.StatusAndPositionArray;

@Controller
@RequestMapping("/GetRegisterDevices")
public class GetRegisterDevices {
	private Logger logger = LoggerFactory.getLogger(GetRegisterDevices.class);
	
	@RequestMapping(value ="doPost",method=RequestMethod.POST)
    protected void doPost(HttpServletRequest request,HttpServletResponse response,GetRegDevInfo getRegDevInfo) throws Exception{
    	String type = getRegDevInfo.getType();
    	String mapNM = getRegDevInfo.getUsrname();
    	MapInfo mapInfo = MapInfoDAO.getMapInfoByNM(mapNM);
    	String username = null;
    	String result = null;
    	if(null == mapInfo){
    		logger.error("Fail to get the mapInfo {} by the mapName {}",mapInfo,mapNM);
    		result = "fail";
    	}else{
    		username = mapInfo.getSn();
        	if(type.equals("ALL")){
        	   String devInfoList = getDevInfoList(username,type);
        	   result = devInfoList.toString();
        	}
        	else if(type.equals("REG")){
        		ServletContext ctx = request.getServletContext();
    		   	DevicesOperateBean db = (DevicesOperateBean)ctx.getAttribute("DevOper");
    		   	// username eq to mapid
    			result = db.getRegisterDev(username);
        	}
            else if(type.equals("STE")){
            	ServletContext ctx = request.getServletContext();
    		   	DevicesOperateBean db = (DevicesOperateBean)ctx.getAttribute("DevOper");
    		   	//username eq to devName
    		   	DevOpr devStatus = db.getDevOpr(username);
    		   	String status = null;

    		   	if(DevOpr.LOCATE == devStatus) 
    		   		status = "LOCATE";
    		   	else if(DevOpr.UNLOCATE == devStatus)
    		   		status = "UNLOCATE";
    		   	else if(DevOpr.REG == devStatus)
    		   		status = "REG";
    		   	else if(DevOpr.DATARCV == devStatus)
    		   		status = "DATARCV";
    		   	else if(DevOpr.OFFLINE == devStatus)
    		   		status = "OFFLINE";
    		   		
    		   	result = "['" + status + "']";
        	}
            else if(type.equals("POS")){
            	//username eq to mapid
                String devInfoList = getDevInfoList(username,type);
           	    String[] deveuiArray = devInfoList.split(",");	
    			StatusAndPositionArray statusAndPositionArray = new StatusAndPositionArray();
    			String batStatus = null;
    			String statustime = null;
    			String devType = null;
    			String rssi = null;
    			String snr = null;
    			String vib = null;
    			String mapid =null;
    			String mapName = null;
				String x = null;
				String y = null;
				 String status = null;
				 DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
    			try {
    				for(String devid : deveuiArray){
    				   StatusRecord statusRecord = StatusRecordDAO.getStatusRecordDataByDev(devid);
    				   PositionRecord positionRecord = PositionRecordDAO.getPositionDataByDev(devid);
    				   DevInfo devInfo = DevInfoDAO.getDevInfoByDeveui(devid);
    				   
    				   DevicesOperateBean db = DevicesOperateBean .getInstance();
					   DevOpr devStatus = db.getDevOpr(devid);
				   	   if(DevOpr.LOCATE == devStatus) 
				   		 status = "LOCATE";
				       else if(DevOpr.UNLOCATE == devStatus)
				   		 status = "UNLOCATE";
				   	   else if(DevOpr.REG == devStatus)
				   		 status = "REG";
				   	   else if(DevOpr.DATARCV == devStatus)
				   		status = "DATARCV";
				   	   else if(DevOpr.OFFLINE == devStatus)
				   		status = "OFFLINE";
				   	   
    				   if(null == statusRecord){
    					   logger.error("Fail to get the statusRecord {} by the devid {}",statusRecord,devid);
    					   rssi = "0";
    					   snr = "0";
    					   vib = "静止";
    				   }else {
    					   rssi = Short.toString(statusRecord.getRssi());
    					   snr = Byte.toString(statusRecord.getSnr());
    					   if(statusRecord.getVib()==0)
    			     		{
    						    vib = "静止";
    			     		}
    			     		else
    			     		{
    			     			vib = "运动强度:"+statusRecord.getVib();
    			     		}
    				   }
    				   if(null == positionRecord){
    					   logger.error("Fail to get the positionRecord {} by the devid {}",positionRecord,devid);
    					   mapName = "";
    					   float a = (float) (Math.random()*((1080) + 1 - (100))+(100));
    					   float b = (float) (Math.random()*((1175) + 1 - (100))+(100));
    					   
    					   x = Float.toString(a);
    					   y = Float.toString(b);
    				   }else {
    					   mapid = positionRecord.getMapid();
    					   mapName = getMapNameByMapsn(mapid);
    					   x = Float.toString(positionRecord.getX());
    					   y = Float.toString(positionRecord.getY());
    				   }
    				   if(null == devInfo){
    					   logger.error("Fail to get the devInfo {} by the devid {}",devInfo,devid);
    					   statustime = sdf.format(new Date());
    					   batStatus = "电池供电";
    					   devType = "无设备类型";
    				   } else{
    					   DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    					   statustime =  df.format(devInfo.getStatustime());
    					   batStatus = devInfo.getBattery();
    					   if(null == batStatus)
    			      			batStatus = "";
    			      	   else if(!batStatus.equals("充电中"))
    			      			batStatus = "电池供电";
    					   devType = devInfo.getDevtype().toString();
    				   }
    				   DevStatusAndpositionData devData = new DevStatusAndpositionData(devid, batStatus, rssi, snr, vib, mapName, x, y,statustime,devType,status);
					   statusAndPositionArray.getDevData().add(devData);
    				}
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			result = JSON.toJSONString(statusAndPositionArray);
     	 }			
    	}
    	response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(result);
    }
	
    protected String getDevInfoList(String mapid,String type){
		String result = "";
		List<DevInfo> devInfoList =null;
		try {
			
			devInfoList = DevInfoDAO.getDevInfoByMapid(mapid);
			if((null == devInfoList) || (devInfoList.size() == 0)){
				logger.error("Fail to get the devinfo {} by the mapid {}",devInfoList,mapid);
				return "fail";
			}else{
				if(type.equals("ALL")){
					result = "[";
					for(DevInfo devInfo : devInfoList){
						result += "'" + devInfo.getDeveui() + "',";
					}
					result += "]";
				}
				else if(type.equals("POS")){
					for(DevInfo devInfo : devInfoList){
						result += devInfo.getDeveui() + ",";
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
    
    public String getMapNameByMapsn(String mapsn){
    	String result = "";
    	try {
			MapInfo mapInfo = MapInfoDAO.getMapInfoByMapid(mapsn);
			if(null == mapInfo){
				logger.error("Fail to get the mapInfo {} by mapsn {}",mapInfo,mapsn);
			}else{
				result = mapInfo.getName();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
}
