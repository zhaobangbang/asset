package com.lansitec.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.MapInfo;

class DevObserverInfo {
	private String userName;
	private String devEUI;
	
	public DevObserverInfo(String name, String dev) {
		userName = name;
		devEUI = dev;
	}
	public DevObserverInfo(String dev) {
		devEUI = dev;
	}
	public String getName() {
		return userName;
	}
	
	public String getDevEUI() {
		return devEUI;
	}

	@Override
	public String toString() {
		return "DevObserverInfo [userName=" + userName + ", devEUI=" + devEUI + "]";
	}
}

public class ObserverInfoBean {
	
	private Set<DevObserverInfo> obserInfos;
	private Logger logger = LoggerFactory.getLogger(ObserverInfoBean.class);
	public static ObserverInfoBean instance = null;
	
	public static ObserverInfoBean getInstance() {
		return instance;
	}
	
	public ObserverInfoBean() {
		// TODO Auto-generated constructor stub
		obserInfos = new HashSet<DevObserverInfo>();

		try {
			List<DevInfo> devInfosList = DevInfoDAO.getAllDevInfo();
			if(!devInfosList.isEmpty()){
				for(DevInfo devInfo : devInfosList){
					//test 
					//MapInfo mapInfo = MapInfoDAO.getMapInfoByMapid(devInfo.getMapid());
							
					DevObserverInfo newObserInfo = new DevObserverInfo(devInfo.getDeveui());
					obserInfos.add(newObserInfo);
				}
			}
				
		} catch (Exception ex) {
			logger.error("executeQuery:"+ ex.getMessage());
		}
		
		instance = this;
	}

	public void updateObserverInfoBean(String usrname, String type, Map<String, String> devMap){
		if(type == null || usrname == null)
		{
			logger.error("null parameter: updateObserverInfoBean");
			return;
		}
		if(type.equals("add"))
		{
			String devname = devMap.get("newvalue");
			for (DevObserverInfo curUser: obserInfos) {
				if(curUser.getName().equals(usrname) && curUser.getDevEUI().equalsIgnoreCase(devname))
				{
					logger.info("--Observer " + usrname + " add dev " + devname + ", but dev exist, return");
					return;
				}
			}
			DevObserverInfo newObserInfo = new DevObserverInfo(usrname,devname);
			obserInfos.add(newObserInfo);
			logger.info("--Observer " + usrname + " add dev " + devname);
		}
		else if(type.equals("del"))
		{
			String devname = devMap.get("newvalue");
			Set<DevObserverInfo> rmSet = new HashSet<DevObserverInfo>();
			for (DevObserverInfo curUser: obserInfos) {
				if(curUser.getName().equals(usrname) && curUser.getDevEUI().equalsIgnoreCase(devname))
				{
					rmSet.add(curUser);
				}
			}
			for(DevObserverInfo delUser: rmSet){
				obserInfos.remove(delUser);
				logger.info("--Observer " + usrname + " remove dev " + devname);
			}
		}
		else if(type.equals("edit"))
		{
			String devname = devMap.get("oldvalue");
			DevObserverInfo rmUserDev = null;
			for (DevObserverInfo curUser: obserInfos) {
				if(curUser.getName().equals(usrname) && curUser.getDevEUI().equalsIgnoreCase(devname))
				{
					rmUserDev = curUser;
					break;
				}
			}
			if(null != rmUserDev)
			{
				obserInfos.remove(rmUserDev);
				logger.info("--Observer " + usrname + " update remove dev " + devname);
			}
			String newdevname = devMap.get("newvalue");
			for (DevObserverInfo curUser: obserInfos) {
				if(curUser.getName().equals(usrname) && curUser.getDevEUI().equalsIgnoreCase(newdevname))
				{
					logger.info("--Observer " + usrname + " update add dev " + newdevname + " which already exist, return.");
					return;
				}
			}
			DevObserverInfo newObserInfo = new DevObserverInfo(usrname,newdevname);
			obserInfos.add(newObserInfo);
			logger.info("--Observer " + usrname + " update add dev " + newdevname);
		}
	}
	
	public void deleteObserverInfoBean(String usrname)
	{		
		Set<DevObserverInfo> rmList = new HashSet<DevObserverInfo>();
		for (DevObserverInfo curUser: obserInfos) {
			//System.out.println("loop user "+curUser.getName()+"devEUI "+curUser.getDevEUI());
			if (curUser.getName().equals(usrname)) {
				rmList.add(curUser);
			}
		}
		for(DevObserverInfo rmUser: rmList)
		{
			logger.info("--Remove Observer " + rmUser.getName() + " delete dev " + rmUser.getDevEUI());
			obserInfos.remove(rmUser);
		}
	}
	
	public String getFirstObserver(String dev) {
		String str = null;
		
		for (DevObserverInfo curUser: obserInfos) {
			//System.out.println("loop user "+curUser.getName()+"devEUI "+curUser.getDevEUI());
			if (curUser.getDevEUI().equalsIgnoreCase(dev)) {
				str = curUser.getName();
			}
		}
		
		return str;
	}
	
	public ArrayList<String> getObservers(String dev) {
		ArrayList<String> strArray = new ArrayList<String>();
		
		for (DevObserverInfo curUser: obserInfos) {
			//System.out.println("loop user "+curUser.getName()+"devEUI "+curUser.getDevEUI());
			if (curUser.getDevEUI().equalsIgnoreCase(dev)) {
				strArray.add(curUser.getName());
			}
		}
		
		return strArray;
	}
	public boolean devValid(String dev){
		for(DevObserverInfo curUser: obserInfos){
			if(curUser.getDevEUI().equalsIgnoreCase(dev))
				return true;
		}
		return false;
	}
	
	/* 设备是否属于此人*/
	public boolean devObservBySb(String dev, String name)
	{
		for(DevObserverInfo curUser: obserInfos) {
			if(curUser.getDevEUI().equalsIgnoreCase(dev) && curUser.getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	/* 设备是否属于此map*/
	public boolean devObservByMapid(String dev, String mapid)
	{
		List<DevInfo> devInfoList = null;
		try {
			devInfoList = DevInfoDAO.getDevInfoByMapid(mapid);
			if((null == devInfoList) || (devInfoList.size() == 0)){
				logger.error("Fail to get the devInfoList {} by mapid {}",devInfoList,mapid);
				return false;
			}
			for(DevInfo devInfo: devInfoList) {
				if(devInfo.getDeveui().equalsIgnoreCase(dev))
				{
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
