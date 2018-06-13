package com.lansitec.beans;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DevicePosition {
	private float xGPS = 0;
	private float yGPS = 0;
	private Date rcvTime = null;
	
	DevicePosition(float x, float y, Date time) {
		this.xGPS = x;
		this.yGPS = y;
		this.rcvTime = time;
	}

	public float getxGPS() {
		return xGPS;
	}

	public void setxGPS(float xGPS) {
		this.xGPS = xGPS;
	}

	public float getyGPS() {
		return yGPS;
	}

	public void setyGPS(float yGPS) {
		this.yGPS = yGPS;
	}

	public Date getRcvTime() {
		return rcvTime;
	}

	public void setRcvTime(Date rcvTime) {
		this.rcvTime = rcvTime;
	}
	
}


class timeComparator implements Comparator<DevicePosition> {
	public int compare(DevicePosition obj1, DevicePosition obj2) {		
		return obj2.getRcvTime().compareTo(obj1.getRcvTime());
	}
}

class DevicePositionList {
	private static final int POSITION_NUM = 1; //Only save the latest
	private LinkedList<DevicePosition> posList = null;
	
	DevicePositionList() {
		posList = new LinkedList<DevicePosition>();
	}
	
	boolean newDevicePosition(float x, float y, Date time) {
		DevicePosition rec = new DevicePosition(x, y, time);
		if (POSITION_NUM == posList.size()) {
			posList.poll();
		}
		return posList.offer(rec);
	}
	
	public void showDeviceList()
	{
		for(DevicePosition ds : posList)
		{
			GpsNodeStatusBean.logger.info(" -xGPS:" + ds.getxGPS() + " yGPS:"+ds.getyGPS() + " Date:"+ds.getRcvTime().toString());
		}
	}
	
	public LinkedList<DevicePosition> getPosArray() {
		Collections.sort(posList, new timeComparator());
		return posList;
	}
	
	public DevicePosition getFirstPos(){
		return posList.getFirst();
	}
}

public class GpsNodeStatusBean {
	HashMap<String, DevicePositionList> m = null;
	static Logger logger = LoggerFactory.getLogger(GpsNodeStatusBean.class);
	public static GpsNodeStatusBean instance;
	
    public GpsNodeStatusBean() {
    	m = new HashMap<String, DevicePositionList>();
    	
    	//addDevicePosition("01:02:03", 13.1, 27.2, new Date());
    	//addDevicePosition("01:02:04", 24.2, 15, new Date());
    }
    
    public synchronized void addDevicePosition(String mac, float x, float y, Date time) {
    	if (!m.containsKey(mac)) {
    		DevicePositionList newInfo = new DevicePositionList();
    		if(!newInfo.newDevicePosition(x, y, time))
    		{
    			logger.error("Fail to add device position!");
    			return;
    		}
    		m.put(mac, newInfo);
    		return;
    	}
    	
    	DevicePositionList oldInfo = m.get(mac);
    	oldInfo.newDevicePosition(x, y, time);
    }
	
	public synchronized float getFirstPosX(String mac){
		DevicePositionList oldInfo = m.get(mac);
		if(oldInfo != null)
		{	
			DevicePosition dPosition =  oldInfo.getFirstPos();
			if(dPosition != null)
				return dPosition.getxGPS();
		}
		return 0;
	}
	
	public synchronized float getFirstPosY(String mac){
		DevicePositionList oldInfo = m.get(mac);
		if(oldInfo != null)
		{	
			DevicePosition dPosition =  oldInfo.getFirstPos();
			if(dPosition != null)
				return dPosition.getyGPS();
		}
		return 0;
	}
	public static GpsNodeStatusBean getInstance() {
		if (instance == null) {
			instance = new GpsNodeStatusBean();
		}
		
		return instance;
	}
    public synchronized void showDeviceList()
    {
		for (Entry<String, DevicePositionList> entry : m.entrySet()) {
			String ringMac = entry.getKey();
			DevicePositionList ringInfo = entry.getValue();
			
			logger.info("DevEUI: {} ", ringMac);
    		((DevicePositionList)ringInfo).showDeviceList();
		}
    }
}
