package com.lansitec.controller.networkgw.msghandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.controller.networkgw.tvmessages.EndDevHistoryPosList;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.servlets.DevMsgHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EndDevHistoryPosHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevHistoryPosHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevHistoryPosHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_LOST_POS) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevHistoryPosList devHistoryPosList = (EndDevHistoryPosList)upMsg;
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		int gpsN = devHistoryPosList.locList.size() + 1;
		
		double[] xGPSA = new double[gpsN];
		double[] yGPSA = new double[gpsN];	
		int[] timeA = new int[gpsN];
		String[] unixTime = new String[gpsN];
		
		float[] speedA = new float[gpsN];
 	   	
		for(int i = 0; i < gpsN; i++)
		{
			double xGPS = 0; 
			double yGPS = 0; 
			int time = 0;
			float speed = -1;
			
			if(i==0)
			{
				xGPS = devHistoryPosList.longitude;
				yGPS = devHistoryPosList.latitude;
				time = devHistoryPosList.time;
				speed = devHistoryPosList.speed;
			}
			else
			{
				xGPS = devHistoryPosList.locList.get(i-1).getLongi();
				yGPS = devHistoryPosList.locList.get(i-1).getLati();
				time = devHistoryPosList.locList.get(i-1).getTime();
				speed = devHistoryPosList.locList.get(i-1).getSpeed();
			}
			xGPSA[i] = xGPS;
			yGPSA[i] = yGPS;
			timeA[i] = time;
			unixTime[i] = Integer.toString(time);
			speedA[i] = speed;
			/*
            String unixTime = Integer.toString(time);
            //BaiduApi.AddTrack(deveui, xGPS, yGPS, unixTime);
            //BaiduApi.queryAlarm(deveui);
            
            if(i <= devHistoryPosList.locList.size())
            {
            	try   
            	{   
            	Thread.currentThread();
    			Thread.sleep(50);//ºÁÃë   
            	}   
            	catch(Exception e){} 
            }
            */
		}	

		
	    String mapid = "";
    	DevInfo devInfos;
		try {
			devInfos = DevInfoDAO.getDevInfoByDeveui(deveui);
			if(null == devInfos){
	    		logger.error("fail to get the devInfos {} by the deveui {}",devInfos,deveui);
	    		
	    	}else{
	    		mapid = devInfos.getMapid();
	    	}
			JSONArray newArray = new JSONArray();
        	JSONObject newObj = new JSONObject();
        	newObj.put("DevEUI", deveui);
        	newObj.put("msgType", "HIS");
        	
        	for(int i=0; i < xGPSA.length; i++)
        	{
        		JSONObject newXY = new JSONObject();
        		String xGPS = Double.toString(xGPSA[i]);
        		String yGPS = Double.toString(yGPSA[i]);
        		newXY.put("xGPS", xGPS);
        		newXY.put("yGPS", yGPS);
        		newXY.put("time", timeA[i]);
        		newXY.put("speed", speedA[i]);
        		newArray.add(newXY);
        		
        		java.util.Date date = new java.util.Date((long)timeA[i]*1000);
        		Instant instant = date.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
                PositionRecord positionRecord = new PositionRecord(deveui, mapid, (float)yGPSA[i], (float)xGPSA[i], localDateTime);
            	PositionRecordDAO.create(positionRecord);
        	}

        	newObj.put("GPS", newArray.toString());
        	
        	DevMsgHandler.updateToMapId(deveui, newObj);
    		//DevMsgHandler.updateToObserver(deveui, newObj);
    		DevMsgHandler.updateDevToObserver(deveui, newObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
