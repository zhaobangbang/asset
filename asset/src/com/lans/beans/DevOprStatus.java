package com.lans.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lans.systemconfig.dao.ORMFactory;

public class DevOprStatus {
	private Logger logger = LoggerFactory.getLogger(DevOprStatus.class);
	private DevOpr preOpr; //终端前一状态
	private DevOpr curOpr; //终端当前状态
	private DevWorkType devWorkType;
	Date lastMsgTime = null;      //最近一次收到数据的时间
	private String mapId; //终端所在地图
    private String position = "未知"; //终端所处位置
 
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	private PositionValid3Points[] valid3points = null;
	private PositionValid2Points[] valid2points = null;
	
	public String getMapId() {
		return mapId;
	}
/*
 * return boolean 
 */
	public boolean setMapId(String deveui, String mapid) {
		//Never set the map id of this device
		if(null == valid3points && null == valid2points)
		{
			mapId = mapid;
			if(getValidPoints(mapid))
			{
				logger.info("Device {} getValidPoints successful, {}", deveui, mapid);
				return true;
			}
			else
			{
				logger.info("Device {} getValidPoints fail, {}", deveui, mapid);
			}
		}//set the map id again
		else{
			if(!mapId.equals(mapid))//设备对应的地图发生了改变
			{
				logger.warn("Device {} update map, from {} to {}", deveui, mapId, mapid);
				if(getValidPoints(mapid))
				{
					logger.info("Device {} update map getValidPoints successful, {}", deveui, mapid);
					mapId = mapid;
					return true;
				}
				else
				{
					logger.info("Device {} update map getValidPoints fail, {}", deveui, mapid);
				}
			}
		}
		return false;
	}
	
	public boolean getValidPoints(String mapid)
	{
		if(null != mapid && !mapid.equals(" "))
		{
			String filepath="";
			if(ORMFactory.localDebug)
	    	{
	    		filepath = "c:/map/";
	    	}
	    	else if(ORMFactory.officialEnv)
    		{
	    		filepath = "/usr/java/tomcat/apache-tomcat-8.0.33/webapps/map/";
	    	}
	    	else
	    	{
	    		filepath = "/usr/java/apache-tomcat-8.0.46/webapps/map/";
	    	}
			filepath += mapid + ".vp";
			
			File file = new File(filepath);
	    	if(!file.exists()||file.isDirectory()) {
	    		logger.info("file {} not exist or directory", filepath);
	            return false;
	    	}
	    	
	        BufferedReader reader = null;  
	        String jsonStr = "";
	        
	        try {
	            reader = new BufferedReader(new FileReader(file));  
	            String tempString = null;

	            while ((tempString = reader.readLine()) != null) {
	            	jsonStr += tempString;
	            }  
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        } finally {
	            if (reader != null) {  
	                try {
	                    reader.close();  
	                } catch (IOException e1) {
	                	e1.printStackTrace(); 
	                }
	            }
	        }
	        try{
		        PositionValidPoints positions= JSON.parseObject(jsonStr, PositionValidPoints.class);
		        setValid2points(positions.getValid2Points());
		        setValid3points(positions.getValid3Points());
	        }catch (Exception e) {
				logger.error("getValidPoints - {}", e.getMessage());
				return false;
			}
	        return true;
		}
		return false;
	}
	
	public DevOprStatus(){		
	}
	
	public DevOprStatus(DevOpr opr) {
		preOpr = opr;
		curOpr = opr;
		lastMsgTime = null;
		devWorkType = null;
	}
	
	public DevOpr getOpr() {
		return curOpr;
	}
	
	public DevOpr getPreOpr(){
		return preOpr;
	}
	public void setOpr(DevOpr opr) {
		preOpr = curOpr;
		curOpr = opr;
	}
	
	public void setLastMsgTime(Date dt) {
		lastMsgTime = dt;
	}
	
	public Date getLastMsgTime() {
		return lastMsgTime;
	}
	
	public DevWorkType getDevWorkType() {
		return devWorkType;
	}
	public void setDevWorkType(DevWorkType devWorkType) {
		this.devWorkType = devWorkType;
	}
	public PositionValid3Points[] getValid3points() {
		return valid3points;
	}

	public void setValid3points(PositionValid3Points[] valid3points) {
		this.valid3points = valid3points;
	}

	public PositionValid2Points[] getValid2points() {
		return valid2points;
	}

	public void setValid2points(PositionValid2Points[] valid2points) {
		this.valid2points = valid2points;
	}	
	
  
}
