package com.lans.servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.ObserverInfoBean;
import com.lans.beans.OnlineUserBean;
import com.lansi.msghandle.itfraw.IEndDevRawMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;

import net.sf.json.JSONObject;

public class DevMsgHandler implements IEndDevRawMsgHandler {
	static Logger logger = LoggerFactory.getLogger(DevMsgHandler.class);
	
	public DevMsgHandler() {
	}

	public static String toDevEui(String moteeui) {
		String eui = moteeui;
		int euiLen = moteeui.length();
		for (int zeroN = euiLen; zeroN < 16; zeroN++) {
			eui = "0"+eui;
		}
		
		return eui.toLowerCase();
	}
	
	public static String fromDevEui(String moteeui) {
		String eui = null;
		int euiLen = 0;
		
		for (int zeroN = 0; zeroN < moteeui.length(); zeroN++) {
			if(moteeui.charAt(zeroN) == '0')
			{
				euiLen++;
			}
			else
			{
				break;
			}
		}
		eui = moteeui.substring(euiLen);
		
		return eui.toLowerCase();
	}
	
	public static void parseTermStatus(JSONObject jsonMsg, int gpsStatus, int vibStatus, int chgStatus)
	{
		String gpsStr, vibStr, chgStr, usbStr;
		
		switch(gpsStatus)
		{
		case 0:
			gpsStr = "停止定位";
			break;
		case 1:
			gpsStr = "启动定位";
			break;
		case 2:
			gpsStr = "正在定位";
			break;
		case 3:
			gpsStr = "定位成功";
			break;
		case 9:
			gpsStr = "无GPS信号";
			break;
		default:
			gpsStr = "未知状态";
			break;
        }
   		switch(vibStatus)
		{
		case 0:
			vibStr = "静止";
			break;
		default:
			vibStr = "运动强度:"+vibStatus;
			break;
        }
   		
   		if(chgStatus == 0x5)
   		{
   			chgStr = "充电中";
   		}
   		else if(chgStatus == 0x6)
   		{
   			chgStr = "电池已充满";
   		} 
   		else {
			chgStr = "未充电";
		}
   		
   		if(chgStatus == 0)
   		{
   			usbStr = "电池供电";
   		}
   		else
   		{
   			if(chgStatus == 5)
   				usbStr = "充电中";
   			else if(chgStatus == 6) {
				usbStr = "电池已充满";
			}
   			else {
				usbStr="状态未知";
			}
   		}

		jsonMsg.element("gps", gpsStr);
		jsonMsg.element("vib", vibStr);
		jsonMsg.element("chg", chgStr);
		jsonMsg.element("usb", usbStr);		
	}
	
	/* Inform the map who own this device.*/
    public static void updateToMap(String mapid, JSONObject jsonMsg) throws IOException{
    	OnlineUserBean onlineBean = OnlineUserBean.getInstance();

    	ArrayList<Session> sessList = onlineBean.getUserSession(mapid);
    		//允许同一个地图被不同的人在不同的地方登录.
    		for(Session sess : sessList)
    		{
    			if ((null != sess) && (sess.isOpen())){
    				sess.getBasicRemote().sendText(jsonMsg.toString()); 
    				logger.info("send to {} with {}", mapid, jsonMsg.toString());
    			}
    		}
    }

	/* Inform the observers who own this device.*/
    public static void updateToObserver(String dev, JSONObject jsonMsg) throws IOException{
    	
    	//System.out.println("update to observers "+jsonMsg);
    	ObserverInfoBean userBean = ObserverInfoBean.getInstance();
    	OnlineUserBean onlineBean = OnlineUserBean.getInstance();
    	ArrayList<String> strArray = userBean.getObservers(dev);
       
    	for (String obser : strArray){
    		ArrayList<Session> sessList = onlineBean.getUserSession(obser);
    		//允许同一个账户在不同的地方登录.
    		for(Session sess : sessList)
    		{
    			if ((null != sess) && (sess.isOpen())){
    				sess.getBasicRemote().sendText(jsonMsg.toString()); 
    				logger.info("send to {} with {}", obser, jsonMsg.toString());
    			}
    		}
    	}
    }
	
   	/*Inform the observe who want to see this device*/
    public static void updateDevToObserver( String dev, JSONObject jsonMsg) throws IOException{
    	OnlineUserBean onlineBean = OnlineUserBean.getInstance();

    	ArrayList<Session> sessList = onlineBean.getDevSession(dev);
    	for(Session sess : sessList)
    	{
    		if ((null != sess) && (sess.isOpen())){
    			sess.getBasicRemote().sendText(jsonMsg.toString()); 
    			logger.info("send to "+dev+" with "+jsonMsg.toString());
    		}
    	}
    }

	@Override
	public void onEndDevMsg(EndDevEnvInfo arg0, String arg1) {
		// TODO Auto-generated method stub
            	
            }  
}
