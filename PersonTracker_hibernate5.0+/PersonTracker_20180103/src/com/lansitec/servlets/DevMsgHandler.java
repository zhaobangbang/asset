package com.lansitec.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itfraw.IEndDevRawMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansitec.beans.DevicesOperateBean;
import com.lansitec.beans.OnlineUserBean;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.enumlist.DevOpr;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
	
    
	
	/* Inform the mapid who own this device.*/
	public static void updateToMapId(String deveui,JSONObject jsonMsg){
		try {
			String mapid = null;
			DevInfo devInfo = DevInfoDAO.getDevInfoByDeveui(deveui);
			if(null == devInfo){
				logger.error("Fail to get the devInfo {} by the deveui {}",devInfo,deveui);
				return;
			}
			mapid = devInfo.getMapid();
			// set mapname before in onlineMap
			MapInfo mapInfo = MapInfoDAO.getMapInfoByMapid(mapid);
			String mapName = mapInfo.getName();
			OnlineUserBean onlineBean = OnlineUserBean.getInstance();
			ArrayList<Session> sessList = onlineBean.getUserSession(mapName);
			//允许同一个账户在不同的地方登录.
    		for(Session sess : sessList)
    		{
    			if ((null != sess) && (sess.isOpen())){
    				sess.getBasicRemote().sendText(jsonMsg.toString()); 
    				logger.info("send to mapid {} with {}", mapid, jsonMsg.toString());
    			}
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* Inform the observers who own this device.*/
    /*public static void updateToObserver(String dev, JSONObject jsonMsg) throws IOException{
    	
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
    				logger.info("send to mapid {} with {}", obser, jsonMsg.toString());
    			}
    		}
    	}
    }*/
	
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
    /*
	public void updateDebugToObserver(String dev, JSONObject jsonMsg) throws IOException{
    	OnlineUserBean onlineBean =  OnlineUserBean.getInstance();

    	ArrayList<Session> sessList = onlineBean.getDevSession(dev);
    	for(Session sess : sessList)
    	{
    		if ((null != sess) && (sess.isOpen()) && onlineBean.isDebugSession(sess)){
    			sess.getBasicRemote().sendText(jsonMsg.toString()); 
    			logger.info("send to "+dev+" with "+jsonMsg.toString());
    		}
    	}
    }
    */
	@Override
	public void onEndDevMsg(EndDevEnvInfo info, String msg) {
		try {
			onDevMsg(info, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onDevMsg(EndDevEnvInfo info, String msg) throws Exception {
		String debugStr = null;

        BASE64Decoder decoder = new BASE64Decoder();
        BASE64Encoder encoder = new BASE64Encoder();
        
        byte[] aLoraData = decoder.decodeBuffer(msg);
        String dencryptDevEUI = toDevEui(info.getEui());

        String Datr = "Datr";
        String Rssi = "Rssi";
        String Freq = "Freq";
        String Recv = "Recv";
        
        String inputMsg = "0x";
        for (int i = 0; i < aLoraData.length; i++)
        {
            String hex = Integer.toHexString(aLoraData[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            inputMsg += hex;
        }
		logger.info("######*****Received Msg {} : {}*****#####", dencryptDevEUI, inputMsg);

        debugStr = "\r\n<<" + dencryptDevEUI + "\r\n速率:" + Datr + " RSSI:"+Rssi + " 频点:"+Freq + "\r\n时间:"+Recv; 
        
        if(aLoraData.length < 1)//double contain 8 bytes.
        {
        	logger.warn("Incomplete Lora data received");
        	return;
        }

        byte[] msgType = new byte[1];
    
        System.arraycopy(aLoraData,0, msgType, 0, 1);
        int mType = Integer.parseInt(new BigInteger(1, msgType).toString(10));
        
        debugStr += " 类型:" + mType;
        debugStr += "\r\n接收数据:" + new BigInteger(1, aLoraData).toString(16);
    	JSONObject jsonMsg = new JSONObject();
    	//JSONObject jsonTrace = new JSONObject();
    	//JSONObject jsonRsp = new JSONObject();
    	
    	jsonMsg.element("DevEUI", dencryptDevEUI);
    	jsonMsg.element("msgType", mType);
		jsonMsg.element("voltage", 0);
		jsonMsg.element("gps", "");
		jsonMsg.element("vib", "");
		jsonMsg.element("chg", "");
		jsonMsg.element("usb", "");
		jsonMsg.element("power", "");
    	//jsonTrace.element("DevEUI", dencryptDevEUI);
    	//jsonTrace.element("msgType", "DEBUG");
    	//jsonRsp.element("eui", dencryptDevEUI);

    	/*未在服务器中登录的设备不允许接入 */
    	/* Not used, the device must be valid since it's forwarded by network server.
    	 *     	
    	
    	ObserverInfoBean obInfo = (ObserverInfoBean)ctx.getAttribute("obsInfo");

    	if(!obInfo.devValid(dencryptDevEUI))
    	{
    		LansUtil.LogWarning("Unknown device:" + dencryptDevEUI);
    		jsonRsp.element("body", "01+KO");
    		
    		debugStr += "\r\n>>" + dencryptDevEUI + "\r\n发送数据:" + "01+KO";
    		
    		jsonTrace.element("TRACE", debugStr);

    		updateDebugToObserver(request, dencryptDevEUI, jsonTrace);
    		response.getWriter().write(jsonRsp.toString());
    		return; 
    	}
    	*/
    	DevicesOperateBean operBean = DevicesOperateBean.getInstance();
    	operBean.updateLastMsgTime(dencryptDevEUI, new Date());
    	
        if(0x01 == mType)
        {//注册消息
            String strResp = "01+OK";
        	debugStr += "\r\n>>" + dencryptDevEUI + "\r\n发送数据:" + "01+OK";
        	
        	if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.OFFLINE)
        	{
            	if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.LOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.LOCATE);
            		strResp += "+LOCATE";
            	}
            	else if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.UNLOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.UNLOCATE);
            		strResp += "+UNLOCATE";
            	}
            	else
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.REG);
            	}            	
        	}
        	else if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.LOCATE)
        	{
        		strResp += "+LOCATE";
        	}
        	else if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.UNLOCATE)
        	{
        		strResp += "+UNLOCATE";
        	}
        	else if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.DATARCV)
        	{//Are you kidding me? Mote reset while server is in DATARCV status. Ok, go on locate after mote reset.
        		operBean.updateOpr(dencryptDevEUI, DevOpr.LOCATE);
        	}
    		
    		jsonMsg.element("State", operBean.getDevOprStr(dencryptDevEUI));  
    		updateToMapId(dencryptDevEUI, jsonMsg);
        	//updateToObserver(dencryptDevEUI, jsonMsg);       	
        }
        else if(0x11 == mType ) //GPS位置更新
        {//  
        	//if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.OFFLINE)
        	//{
        	//	operBean.updateOpr(dencryptDevEUI, DevOpr.REG);
        	//}
        	
            if(aLoraData.length < 9) //No completed GPS data received.
            {           	
            	logger.error("Data length is too short:" + aLoraData.length);
            }
            else
            {
                byte[] tGPS = new byte[4];
                float xGPS = 0;
                float yGPS = 0;
      
            	System.arraycopy(aLoraData,1, tGPS, 0, 4);
            	int ixGPS = Integer.parseUnsignedInt(new BigInteger(1, tGPS).toString(10));
            	yGPS = Float.intBitsToFloat(ixGPS);
        
            	System.arraycopy(aLoraData,5, tGPS, 0, 4);
            	int iyGPS = Integer.parseUnsignedInt(new BigInteger(1, tGPS).toString(10));
            	xGPS = Float.intBitsToFloat(iyGPS);
                
            	if(aLoraData.length >= 18)//Completed Temperature/Humidity data received.
            	{
            		float v;
            		System.arraycopy(aLoraData, 9, tGPS, 0, 4);
            		int iv = Integer.parseInt(new BigInteger(1, tGPS).toString(10));
            		v = Float.intBitsToFloat(iv);
            		
            		int rssi, gpsStatus, vibStatus, chgStatus;
            		byte[] tStatus = new byte[1];
            		System.arraycopy(aLoraData, 13, tStatus, 0, 1);
            		rssi = tStatus[0];
            		System.arraycopy(aLoraData, 14, tStatus, 0, 1);
            		gpsStatus = tStatus[0];
            		System.arraycopy(aLoraData, 15, tStatus, 0, 1);
            		vibStatus = tStatus[0];
            		System.arraycopy(aLoraData, 16, tStatus, 0, 1);
            		chgStatus = tStatus[0];
            		
            		parseTermStatus(jsonMsg, gpsStatus, vibStatus, chgStatus);
            		DecimalFormat dFormat = new DecimalFormat(".00");
            		jsonMsg.element("voltage", dFormat.format(v) + "V");
            		jsonMsg.element("rssi", "-" + rssi + "dbm");
            	}
           	
            	operBean.updateOpr(dencryptDevEUI, DevOpr.DATARCV);
        		
            	jsonMsg.element("State", operBean.getDevOprStr(dencryptDevEUI));
            	updateToMapId(dencryptDevEUI, jsonMsg);
            	//updateToObserver(dencryptDevEUI, jsonMsg);
            	updateDevToObserver(dencryptDevEUI, jsonMsg);
            	      		
            	debugStr += "\r\n";
        		//jsonTrace.element("TRACE", debugStr);
        		//updateDebugToObserver(dencryptDevEUI, jsonTrace);
        		
        		/*将位置信息记录在Baidu服务器鹰眼系统中，最后一步做，以避免对其它过程的影响*/            
                //SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Date dt = null;
                //try{
                //	dt = sd.parse(Recv);
                //}catch(Exception e){}
                //long trackTime = dt.getTime() / 1000;
        
        		return;
            }
        }
        else if(0x12 == mType)
        {//GPS is locating, but no GPS data received.
        	/*
        	if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.OFFLINE)
        	{
            	if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.LOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.LOCATE);
            	}
            	else if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.UNLOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.UNLOCATE);
            	}
            	else
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.REG);
            	}            	
        	}
        	*/
        	
            if(aLoraData.length >= 10)
            {
            	float v;
            	byte[] bV = new byte[4];
        		System.arraycopy(aLoraData, 1, bV, 0, 4);
        		int iv = Integer.parseInt(new BigInteger(1, bV).toString(10));
        		v = Float.intBitsToFloat(iv);
        		
        		int rssi, gpsStatus, vibStatus, chgStatus;
        		byte[] tStatus = new byte[1];
        		System.arraycopy(aLoraData, 5, tStatus, 0, 1);
        		rssi = tStatus[0];
        		System.arraycopy(aLoraData, 6, tStatus, 0, 1);
        		gpsStatus = tStatus[0];
        		System.arraycopy(aLoraData, 7, tStatus, 0, 1);
        		vibStatus = tStatus[0];
        		System.arraycopy(aLoraData, 8, tStatus, 0, 1);
        		chgStatus = tStatus[0];
        		
        		parseTermStatus(jsonMsg, gpsStatus, vibStatus, chgStatus);
        		DecimalFormat dFormat = new DecimalFormat(".00");
        		jsonMsg.element("voltage", dFormat.format(v) + "V");    
        		jsonMsg.element("rssi", "-" + rssi + "dbm");  
            	//GpsNodeStatusBean posBean = (GpsNodeStatusBean)ctx.getAttribute("GpsNodeStatus");
            	
            	//Keep there, maybe client need to search the history data.
            	//posBean.addDevicePosition(dencryptDevEUI, xGPS, yGPS, new Date());
        		operBean.updateOpr(dencryptDevEUI, DevOpr.LOCATE);
        		
            	jsonMsg.element("State", operBean.getDevOprStr(dencryptDevEUI));
            	updateToMapId(dencryptDevEUI, jsonMsg);
            	//updateToObserver(dencryptDevEUI, jsonMsg);
            	//updateDevToObserver(dencryptDevEUI, jsonMsg);
            }	
        }
        else if(0x13 == mType)
        {//GPS is locating, but no GPS data received.
        	/*
        	if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.OFFLINE)
        	{
            	if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.LOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.LOCATE);
            	}
            	else if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.UNLOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.UNLOCATE);
            	}
            	else
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.REG);
            	}            	
        	}
        	*/
            if(aLoraData.length >= 9)
            {
            	float v;
            	byte[] bV = new byte[4];
        		System.arraycopy(aLoraData, 1, bV, 0, 4);
        		int iv = Integer.parseInt(new BigInteger(1, bV).toString(10));
        		v = Float.intBitsToFloat(iv);
        		
        		int rssi, gpsStatus, vibStatus, chgStatus;
        		byte[] tStatus = new byte[1];
        		System.arraycopy(aLoraData, 5, tStatus, 0, 1);
        		rssi = tStatus[0];
        		System.arraycopy(aLoraData, 6, tStatus, 0, 1);
        		gpsStatus = tStatus[0];
        		System.arraycopy(aLoraData, 7, tStatus, 0, 1);
        		vibStatus = tStatus[0];
        		System.arraycopy(aLoraData, 8, tStatus, 0, 1);
        		chgStatus = tStatus[0];
        		
        		parseTermStatus(jsonMsg, gpsStatus, vibStatus, chgStatus);
        		DecimalFormat dFormat = new DecimalFormat(".00");
        		jsonMsg.element("voltage", dFormat.format(v) + "V"); 
        		jsonMsg.element("rssi", "-" + rssi + "dbm");
            	//GpsNodeStatusBean posBean = (GpsNodeStatusBean)ctx.getAttribute("GpsNodeStatus");
            	
            	//Keep there, maybe client need to search the history data.
            	//posBean.addDevicePosition(dencryptDevEUI, xGPS, yGPS, new Date());
        		operBean.updateOpr(dencryptDevEUI, DevOpr.UNLOCATE);
        		
            	jsonMsg.element("State", operBean.getDevOprStr(dencryptDevEUI));
            	updateToMapId(dencryptDevEUI, jsonMsg);
            	//updateToObserver(dencryptDevEUI, jsonMsg);
            	//updateDevToObserver(dencryptDevEUI, jsonMsg);
            }	
        }
        else if(0x21 == mType)
        {//Heart Beat message received.
        	String strResp = null;
    		
        	logger.info("updateOpr: Heartbeat");
        	if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.OFFLINE)
        	{
            	if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.LOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.LOCATE);
            		strResp = "+LOCATE";
            	}
            	else if(operBean.getDevPreOpr(dencryptDevEUI) == DevOpr.UNLOCATE)
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.UNLOCATE);
            		strResp = "+UNLOCATE";
            	}
            	else
            	{
            		operBean.updateOpr(dencryptDevEUI, DevOpr.REG);
            	}            	
        	}
        	else if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.LOCATE)
        	{
        		strResp = "+LOCATE";
        	}
        	else if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.UNLOCATE)
        	{
        		strResp = "+UNLOCATE";
        	}
        	else if(operBean.getDevOpr(dencryptDevEUI) == DevOpr.DATARCV)
        	{//Are you kidding me? Mote reset while server is in DATARCV status. Ok, go on locate after mote reset.
        		operBean.updateOpr(dencryptDevEUI, DevOpr.LOCATE);
        		strResp = "+LOCATE";
        	}
        	else {
        		operBean.updateOpr(dencryptDevEUI, DevOpr.REG);
			}
            if(aLoraData.length >= 9)
            {
            	float v;
            	byte[] bV = new byte[4];
        		System.arraycopy(aLoraData, 1, bV, 0, 4);
        		int iv = Integer.parseInt(new BigInteger(1, bV).toString(10));
        		v = Float.intBitsToFloat(iv);
        		
        		int rssi, gpsStatus, vibStatus, chgStatus;
        		byte[] tStatus = new byte[1];
        		System.arraycopy(aLoraData, 5, tStatus, 0, 1);
        		rssi = tStatus[0];
        		System.arraycopy(aLoraData, 6, tStatus, 0, 1);
        		gpsStatus = tStatus[0];
        		System.arraycopy(aLoraData, 7, tStatus, 0, 1);
        		vibStatus = tStatus[0];
        		System.arraycopy(aLoraData, 8, tStatus, 0, 1);
        		chgStatus = tStatus[0];
        		
        		parseTermStatus(jsonMsg, gpsStatus, vibStatus, chgStatus);
        		DecimalFormat dFormat = new DecimalFormat(".00");
        		jsonMsg.element("voltage", dFormat.format(v) + "V"); 
        		jsonMsg.element("rssi", "-" + rssi + "dbm");
            	//GpsNodeStatusBean posBean = (GpsNodeStatusBean)ctx.getAttribute("GpsNodeStatus");
            	
            	//Keep there, maybe client need to search the history data.
            	//posBean.addDevicePosition(dencryptDevEUI, xGPS, yGPS, new Date());
            }  
            
            logger.info("####****Current status {} ： {}", dencryptDevEUI, operBean.getDevOprStr(dencryptDevEUI));

            jsonMsg.element("State", operBean.getDevOprStr(dencryptDevEUI));
            updateToMapId(dencryptDevEUI, jsonMsg);
            //updateToObserver(dencryptDevEUI, jsonMsg);
            //updateDevToObserver(dencryptDevEUI, jsonMsg);	
        }
        else
        {
        	logger.info("Unknown message type received:" + mType);
        }
        
        //debugStr += "\r\n";
		//jsonTrace.element("TRACE", debugStr);
		//updateDebugToObserver(dencryptDevEUI, jsonTrace);
	}

}
