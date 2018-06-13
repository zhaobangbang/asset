package com.lansitec.infrastructure.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.beans.DevicesOperateBean;
import com.lansitec.beans.OnlineUserBean;

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
		
		DevicesOperateBean operBean = (DevicesOperateBean)context.getAttribute("DevOper");
		if (null == operBean) {
			logger.info("Something wrong with the timer");
			return;
		}
		
		operBean.scanTimeOutDev(offlineDevs);
		
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
