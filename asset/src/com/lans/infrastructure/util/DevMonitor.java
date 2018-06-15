package com.lans.infrastructure.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlSender;
import com.lans.beans.DevicesOperateBean;
import com.lans.beans.OnlineUserBean;
import com.lans.dao.ZwwldevicesDAO;
import com.lans.dao.beans.Zwwldevices;

import net.sf.json.JSONObject;

/*
 * 监控设备是否超时，超过一定时间未收到数据则讲此设备设为离线。
 */
public class DevMonitor extends TimerTask {
	private Logger logger = LoggerFactory.getLogger(DevMonitor.class);
	public static LinkedList <String> offlineDevs = new LinkedList<String>();
    private ServletContext context = null;  
    private  String DEVICE_STATUES = "1";
	public DevMonitor() {
		super();
	}
	
    public DevMonitor(ServletContext context) {  
        this.context = context;  
    }  
    
    public void notifyObserver(String dev, JSONObject jsonMsg) throws IOException{
		OnlineUserBean onlineBean = (OnlineUserBean)context.getAttribute("OnlineUser");
		ArrayList<Session> sessArray = onlineBean.getDevSession(dev);
		for(Session sess : sessArray)
		{
			if ((null != sess) && (sess.isOpen())){
				sess.getBasicRemote().sendText(jsonMsg.toString()); 
				logger.info("send to dev {} with jsonMsg.toString() {}",dev,jsonMsg.toString());
				//LansUtil.LogDebug("send to "+dev+" with "+jsonMsg.toString());
			}
		}
    }
    
	public void run() {
		
		List<Zwwldevices> zwwldev = null;
		String sqlDeviceStatus = null;
		boolean opersituation = true;
		DevicesOperateBean operBean = (DevicesOperateBean)context.getAttribute("DevOper");
		if (null == operBean) {
			logger.info("Something wrong with the timer");
			return;
		}
		
		operBean.scanTimeOutDev(offlineDevs);
		
		try {
			//用zwwl所有的终端与所有下线终端做对比
			zwwldev = ZwwldevicesDAO.getAllZwwldevices();
			 for(Zwwldevices devices : zwwldev){
				 opersituation = false;
				    //下线终端属于zwwl
				    for(String device : offlineDevs){
				    	
				      if(devices.getDevid().equalsIgnoreCase(device)){
					    	 DEVICE_STATUES = "0";
					    	 sqlDeviceStatus = devices.getStatus();
					    	 logger.debug("Statues of offline device {} is {} before",devices.getDevid(),sqlDeviceStatus);
					    	 if(sqlDeviceStatus.equals("1")){
					    		 ZwwlSender.sendStatusEvent(device,DEVICE_STATUES,devices.getTenantId());  
							     devices.setStatus(DEVICE_STATUES);
							     ZwwldevicesDAO.update(devices);
							     logger.info("Updating device's {} statues is {} now",devices.getDevid(),devices.getStatus());
					    	 }else{
					    		 logger.debug("this  device was offline before");
					    		 }
					         opersituation = true;
					         break;  
				        }				      
				      }
				    //if not found
				    if((opersituation == false) && (devices.getDevid().length() != 12)){					
						 DEVICE_STATUES = "1";
						 sqlDeviceStatus = devices.getStatus();
						 logger.info("Statues of online device {} is {} before",devices.getDevid(),sqlDeviceStatus);
					     if(sqlDeviceStatus.equals("0")){
					    	ZwwlSender.sendStatusEvent(devices.getDevid(),DEVICE_STATUES,devices.getTenantId());
						    devices.setStatus(DEVICE_STATUES);
						    ZwwldevicesDAO.update(devices);
						    logger.info("Updating device's {} statues is {} now",devices.getDevid(),devices.getStatus());
					 }	 
				 }
			  }
		} catch (Exception e1) {
			logger.error("query zwwl devices error");
			e1.printStackTrace();
		}
	  
		//System.out.println("Loop scanTimeOutDev");
		// LansUtil.LogDebug("DevMonitor Run:" + new Date());
		JSONObject jsonMsg = new JSONObject();
		for (String curDev : offlineDevs) {
			jsonMsg.element("DevEUI", curDev);
			jsonMsg.element("msgType", "TIMEOUT");
			try {
				notifyObserver(curDev, jsonMsg);
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}
		}
    }

}
