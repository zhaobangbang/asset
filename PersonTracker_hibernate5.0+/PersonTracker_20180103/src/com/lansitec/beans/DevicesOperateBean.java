package com.lansitec.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lansitec.common.QueryResult;
import com.lansitec.common.SaveInfoSet;
import com.lansitec.dao.DevConfigDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.beans.AcquiredemandInfo;
import com.lansitec.dao.beans.DevConfig;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.enumlist.DevOpr;
import com.lansitec.listener.ResourseMgrListener;
import com.lansitec.systemconfig.dao.ORMFactory;


class DevOprStatus {
	private Logger logger = LoggerFactory.getLogger(DevOprStatus.class);
	private String devName;
	private DevOpr preOpr;
	private DevOpr curOpr;
	Date lastMsgTime;
	private String mapId;
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
	public boolean setMapId(String mapid) {
		//Never set the map id of this device
		if(null == valid3points && null == valid2points)
		{
			mapId = mapid;
			if(getValidPoints(mapid))
			{
				logger.info("Device {} getValidPoints successful, {}", devName, mapid);
				return true;
			}
			else
			{
				logger.info("Device {} getValidPoints fail, {}", devName, mapid);
			}
		}//set the map id again
		else{
			if(!mapId.equals(mapid))//设备对应的地图发生了改变
			{
				logger.warn("Device {} update map, from {} to {}", devName, mapId, mapid);
				if(getValidPoints(mapid))
				{
					logger.info("Device {} update map getValidPoints successful, {}", devName, mapid);
					mapId = mapid;
					return true;
				}
				else
				{
					logger.info("Device {} update map getValidPoints fail, {}", devName, mapid);
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
	
	public DevOprStatus(String devname, DevOpr opr) {
		devName = devname;
		preOpr = opr;
		curOpr = opr;
		lastMsgTime = null;
	}
	public DevOprStatus(String devname, DevOpr opr,Date lastMsgTime){
		devName = devname;
		preOpr = opr;
		curOpr = opr;
		this.lastMsgTime = lastMsgTime;
	}
	
	public String getName() {
		return devName;
	}
	public void setName(String name){
		devName = name;
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

public class DevicesOperateBean{
	//private static final long TIME_OUT_M = 11; //Minutes
	private ArrayList<DevOprStatus> DevOprList;
	private long diff;
	public static DevicesOperateBean instance;
	public static LinkedList <String> offlineDevList;
	private Logger logger = LoggerFactory.getLogger(DevicesOperateBean.class);
	private static final byte search5Pool[][] = {{1,2,3},{1,2,4},{1,2,5},{1,3,4},
			                                    {1,3,5},{1,4,5},{2,3,4},{2,3,5},
			                                    {2,4,5},{3,4,5}};
	private static final byte search4Pool[][] = {{1,2,3},{1,2,4},{1,3,4},{2,3,4}};
	
	public DevicesOperateBean(){
		DevOprList = new ArrayList<DevOprStatus>();
	}
	
	public static DevicesOperateBean getInstance() {
		if (instance == null) {
			instance = new DevicesOperateBean();
		}
		
		return instance;
	}
	//使用synchronized块或者synchronized方法就可以标志一个监视区域
	public synchronized void updateOpr(String devname, DevOpr devopr){
		for (DevOprStatus curOpr: DevOprList) {
			if (curOpr.getName().equalsIgnoreCase(devname)) {
				logger.info("updateOpr:" + devopr);
				curOpr.setOpr(devopr);			 		
				return;
			}
		}
		//If fail to find the dev, add it.
		DevOprStatus newOpr = new DevOprStatus(devname, devopr);
		
		DevOprList.add(newOpr);
	}
	
	
	public synchronized void  assingToDevOprList(ArrayList<DevOprStatus> DevOprStatusList){
		DevOprList = DevOprStatusList;
	}
	
	
	public synchronized DevOpr getOpr(String devname)
	{
		for (DevOprStatus curOpr: DevOprList) {
			if (curOpr.getName().equalsIgnoreCase(devname)) {
				logger.info("getOpr:" + curOpr.getOpr());
				return curOpr.getOpr();
			}
		}
		
		return DevOpr.OFFLINE;
	}
	
	public synchronized DevOprStatus getDevOprStatus(String devname)
	{
		for (DevOprStatus curOpr: DevOprList) {
			if (curOpr.getName().equalsIgnoreCase(devname)) {
				return curOpr;
			}
		}
		
		return null;
	}
	
	public synchronized String getRegisterDev(String usrname)
	{
    	ServletContext ctx = ResourseMgrListener.getGlobalContext();
    	ObserverInfoBean userBean = (ObserverInfoBean)ctx.getAttribute("obsInfo");
    	
		String result = "[";
		for(DevOprStatus curOpr: DevOprList)
  		{
			if(userBean.devObservByMapid(curOpr.getName(), usrname)&&
					curOpr.getOpr() != DevOpr.OFFLINE)
			{
	  			result += "'" + curOpr.getName() + "',";
			}
  		}
		if(result.equals("[")){
			result += "]";
		}else{
			result = result.substring(0,result.length()-1) + "]";
		}
  		logger.info("--getRegisterDev: " + result);
  		return result;
	}
	
	public synchronized boolean locateDev(String devname)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equalsIgnoreCase(devname) && (curDev.getOpr() == DevOpr.LOCATE)) {
				return true;
			}
		}
		
		return false;
	}
	public synchronized boolean unlocateDev(String devname)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equalsIgnoreCase(devname) && (curDev.getOpr() == DevOpr.UNLOCATE)) {
				return true;
			}
		}
		
		return false;
	}
	
	public synchronized boolean registerDev(String devname)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equalsIgnoreCase(devname) && (curDev.getOpr() == DevOpr.REG)) {
				return true;
			}
		}
		
		return false;
	}
	
	public synchronized DevOpr getDevOpr(String devname)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equalsIgnoreCase(devname)) {
				return curDev.getOpr();
			}
		}
		return DevOpr.OFFLINE;
	}
	
	public synchronized String getDevOprStr(String devname)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equalsIgnoreCase(devname)) {
				DevOpr DevState = curDev.getOpr();
				if(DevState == DevOpr.REG)
					return "REG";
				else if(DevState == DevOpr.LOCATE)
					return "LOCATE";
				else if(DevState == DevOpr.UNLOCATE)
					return "UNLOCATE";
				else if(DevState == DevOpr.DATARCV)
					return "DATARCV";
				else
					return "OFFLINE";
			}
		}
		return "OFFLINE";
	}
	public synchronized DevOpr getDevPreOpr(String devname)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equalsIgnoreCase(devname)) {
				return curDev.getPreOpr();
			}
		}
		return DevOpr.OFFLINE;
	}
	
	public synchronized void updateLastMsgTime(String devname, Date dt) {
		logger.info("Update lastmsgtime {}, total dev number {}", devname, DevOprList.size());
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equalsIgnoreCase(devname)) {
				curDev.setLastMsgTime(dt);
				break;
			}
		}
	}
	
	public synchronized Date queryDevStatusTime(String deveui){
		String statustime = null;
		Date time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DevInfo devInfo = null;
		try{
			devInfo = DevInfoDAO.getDevInfoByDeveui(deveui);
			if(null == devInfo){
				time = new Date();
				return time;
			}
			statustime = sdf.format(devInfo.getStatustime());
		    time = sdf.parse(statustime);
		}catch (Exception e) {
			logger.error("Fail to query the deveui {} statustime ：" + deveui);
			e.printStackTrace();
		}
		return time;
	}
	
	public synchronized void scanTimeOutDev(LinkedList <String> offlineDevs) {
		Date now = new Date();
		DevConfig devconfig = null;
		for (DevOprStatus curDev: DevOprList) {
			if (null == curDev.getLastMsgTime()) {
				 curDev.setLastMsgTime(queryDevStatusTime(curDev.getName()));
				 logger.info("the device {} setLastMsgTime {}",curDev.getName(),queryDevStatusTime(curDev.getName()));
				continue;
			}
			
			diff = now.getTime()-curDev.getLastMsgTime().getTime();
			
			try {
				devconfig = DevConfigDAO.getDevConfigByDevice(curDev.getName());
				if(null == devconfig)
					continue;
				
				if(null != devconfig)
				{//30 is the heartbeat interval unit, means the least interval is 30s.
					for(AcquiredemandInfo acquInfo: SaveInfoSet.acqmandSet){
						if(acquInfo == null){
							logger.info("acquInfo {}",acquInfo);
							break;
						}
						if(curDev.getName().equalsIgnoreCase(acquInfo.getDevid())){
							//logger.info("curDev.getName {}  acquInfo.getDevid {}",curDev.getName(),acquInfo.getDevid());
							long diffTimeNum = QueryResult.getValidConfigHeartBeat(curDev.getName(),acquInfo.getMgsTimeBefore(),acquInfo);
							logger.info("diffTimeNum {}  2*acquInfo.getCfgHB {}  diff {}",diffTimeNum,
									     2*acquInfo.getCfgHB(),(diffTimeNum / (2*acquInfo.getCfgHB()))); 
							//确保差值在2*cfgHB的时间内可以上报下次心跳
							if((diffTimeNum / (2*acquInfo.getCfgHB())) > 1){
								updateLastMsgTime(curDev.getName(),new Date());
								continue;
							}
							acquInfo.setDevid(null);
							SaveInfoSet.judge(acquInfo);
						}
					}
					if(diff > 3*devconfig.getHB()*30*1000){
						if (DevOpr.OFFLINE != curDev.getOpr()) {
							logger.info("devices {} time out {} ", curDev.getName(), diff);
							curDev.setOpr(DevOpr.OFFLINE);
							offlineDevs.add(curDev.getName());
						}
					}else{
                       if(null != offlineDevs && offlineDevs.size() > 0)
						  offlineDevs.remove(curDev.getName());
						//logger.info("dev {} diff {} less than HBtime {}",curDev.getName(), diff,3*devc.getHB()*30*1000);
			        }
		        }
				
			} catch (Exception e) {
				logger.error("scanTimeOutDev try/catch error in DevicesOperateBean");
				e.printStackTrace();
			}
			
		}
		
		offlineDevList = offlineDevs;
	}
	public synchronized boolean scanTimeOutByDev(String dev) {
	    for(String device : offlineDevList){				    	
		      if(dev.equalsIgnoreCase(device)){
			          return true;  
		        }				      
		    }
	    return false;
	}
	
	public synchronized boolean setMapId(String deveui, String mapid)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equals(deveui)) {
				return curDev.setMapId(mapid);
			}
		}
		return false;
	}
	
	public synchronized String getMapId(String deveui)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equals(deveui)) {
				return curDev.getMapId();
			}
		}
		return "";
	}
	
	public synchronized String getDevicePos(String deveui)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equals(deveui)) {
				return curDev.getPosition();
			}
		}
		return "";
	}
	
	public synchronized boolean setDevicePos(String deveui, String location)
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equals(deveui)) {
				curDev.setPosition(location);
				return true;
			}
		}
		return false;
	}
	//search2point: 如果没有找到三点的合法点，继续寻找两点的合法点
	private byte[] search3Points(DevOprStatus curDev, byte[]input, boolean search2point)
	{
		PositionValid3Points[] p3Array = curDev.getValid3points();
		if(null == p3Array)
			return null;
		
    	for(PositionValid3Points p3: p3Array)
    	{
    		byte[] valid3Points = p3.getValid3points();
    		if((input[0] == valid3Points[0] && input[1] == valid3Points[1] && input[2] == valid3Points[2]) ||
    		   (input[0] == valid3Points[0] && input[1] == valid3Points[2] && input[2] == valid3Points[1]) ||
    		   (input[0] == valid3Points[1] && input[1] == valid3Points[0] && input[2] == valid3Points[2]) ||
    		   (input[0] == valid3Points[1] && input[1] == valid3Points[2] && input[2] == valid3Points[0]) ||
    		   (input[0] == valid3Points[2] && input[1] == valid3Points[0] && input[2] == valid3Points[1]) ||
    		   (input[0] == valid3Points[2] && input[1] == valid3Points[1] && input[2] == valid3Points[0]))
    		{
    			logger.info("valid 3 points found, {},{},{}", input[0],input[1],input[2]);
				return input;
    		}
    	}
    	
    	if(search2point)
    	{
	    	PositionValid2Points[] p2Array = curDev.getValid2points();
	    	if(null == p2Array)
	    		return null;
	    	
	    	for(PositionValid2Points p2: p2Array)
	    	{
	    		byte[] valid2Points = p2.getValid2points();
	    		byte[] selBytes = new byte[2];
	    		if((input[0] == valid2Points[0] && input[1] == valid2Points[1]) ||
	    				(input[0] == valid2Points[1] && input[1] == valid2Points[0]))
	    		{				
					selBytes[0] = input[0];
					selBytes[1] = input[1];
					return selBytes;
	    		}
	    		else if((input[0] == valid2Points[0] && input[2] == valid2Points[1]) ||
	    				(input[0] == valid2Points[1] && input[2] == valid2Points[0]))
	    		{
					selBytes[0] = input[0];
					selBytes[1] = input[2];
					return selBytes;
	    		}
	    		else if((input[1] == valid2Points[0] && input[2] == valid2Points[1]) ||
	    				(input[2] == valid2Points[1] && input[1] == valid2Points[0]))
	    		{
					selBytes[0] = input[1];
					selBytes[1] = input[2];
					return selBytes;
	    		}
	    	}
    	}
    	return null;
	}
	
	//从三点中寻找合法的两点
	private byte[] search2Points(DevOprStatus curDev, byte[]input)
	{		
    	PositionValid2Points[] p2Array = curDev.getValid2points();
    	if(null == p2Array)
    		return null;
    	
    	for(PositionValid2Points p2: p2Array)
    	{
    		byte[] valid2Points = p2.getValid2points();
    		byte[] selBytes = new byte[2];
    		if((input[0] == valid2Points[0] && input[1] == valid2Points[1]) ||
    				(input[0] == valid2Points[1] && input[1] == valid2Points[0]))
    		{				
				selBytes[0] = input[0];
				selBytes[1] = input[1];
				return selBytes;
    		}
    		else if((input[0] == valid2Points[0] && input[2] == valid2Points[1]) ||
    				(input[0] == valid2Points[1] && input[2] == valid2Points[0]))
    		{
				selBytes[0] = input[0];
				selBytes[1] = input[2];
				return selBytes;
    		}
    		else if((input[1] == valid2Points[0] && input[2] == valid2Points[1]) ||
    				(input[2] == valid2Points[1] && input[1] == valid2Points[0]))
    		{
				selBytes[0] = input[1];
				selBytes[1] = input[2];
				return selBytes;
    		}
    	}
    	return null;
	}
	
	public synchronized byte[] searchValidPoints(String deveui, byte input[])
	{
		for (DevOprStatus curDev: DevOprList) {
			if (curDev.getName().equals(deveui)) {
				if(input.length == 1)
				{
					return input;
				}
				else if(input.length == 2)
			    {
			    	PositionValid2Points[] p2Array = curDev.getValid2points();
			    	if(null == p2Array)
			    		return null;
			    	
			    	for(PositionValid2Points p2: p2Array)
			    	{
			    		byte[] valid2Points = p2.getValid2points();
			    		if((input[0] == valid2Points[0] && input[1] == valid2Points[1]) ||
			    				(input[0] == valid2Points[1] && input[1] == valid2Points[0]))
			    		{
			    			logger.info("deveui {}, valid 2 points found, {}, {}",deveui, input[0],input[1]);
							return input;
			    		}
			    	}
			    }	
			    else if(input.length == 3)
			    {
			    	return search3Points(curDev, input, true);
			    }
			    else if(input.length == 4)
			    {
			    	byte[] selBytes = null;
			    	for(byte[] s3p: search4Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];
			    		
			    		selBytes = search3Points(curDev, newInputs, false);
			    		if(null != selBytes)
			    			return selBytes;
			    	}

			    	for(byte[] s3p: search4Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];

                        selBytes = search2Points(curDev, newInputs);
			    		if(null != selBytes)
			    			return selBytes;
			    	}	    			    			
			    }
			    else if(input.length >= 5)
			    {
			    	for(byte[] s3p: search5Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];
			    		
			    		byte[] selBytes = search3Points(curDev, newInputs, false);
			    		if(null != selBytes)
			    			return selBytes;
			    	}
			    	for(byte[] s3p: search5Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];
			    		
			    		byte[] selBytes = search2Points(curDev, newInputs);
			    		if(null != selBytes)
			    			return selBytes;
			    	}	
			    }
			    break;
			}
		}
		return null;
	}

	
}
