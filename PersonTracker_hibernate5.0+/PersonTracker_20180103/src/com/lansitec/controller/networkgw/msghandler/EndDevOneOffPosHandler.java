package com.lansitec.controller.networkgw.msghandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;
import com.lansitec.beans.GpsNodeStatusBean;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.controller.networkgw.tvmessages.DLAck;
import com.lansitec.controller.networkgw.tvmessages.EndDevOneOffPos;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.servlets.DevMsgHandler;

import net.sf.json.JSONObject;

public class EndDevOneOffPosHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevOneOffPosHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevOneOffPosHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_POS_ACK) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevOneOffPos posMsg = (EndDevOneOffPos)upMsg;
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		float xGPS = posMsg.longitude;
		float yGPS = posMsg.latitude;
		int time = posMsg.time;
		
    	//Respond ASAP
		DLAck dlAck = new DLAck((byte)0, posMsg.msgid);
		l3.sendTVMsgToEndDev(devInfo.getEui(), (byte)21, dlAck);
		
		
    	JSONObject jsonMsg = new JSONObject();
    	
    	jsonMsg.element("DevEUI", deveui);
    	jsonMsg.element("msgType", "LOC");
    	jsonMsg.element("xGPS", xGPS);
    	jsonMsg.element("yGPS", yGPS);
    	jsonMsg.element("time", time);
        
        GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
        posBean.addDevicePosition(deveui, xGPS, yGPS, new Date((long)time*1000));
        	
    	try {
    		 String mapid = null;
             DevInfo devInfos = DevInfoDAO.getDevInfoByDeveui(deveui);
             if(null == devInfos){
            	 logger.error("fail to get the devInfos {} by the deveui {}",devInfos,deveui);
            	 mapid = "";
             }else{
            	 mapid = devInfos.getMapid();
             }
    		java.util.Date date = new java.util.Date((long)time * 1000);
    		Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
            PositionRecord positionRecord = new PositionRecord(deveui, mapid, yGPS, xGPS, localDateTime);
            PositionRecordDAO.create(positionRecord);
            DevMsgHandler.updateToMapId(deveui, jsonMsg);
			//DevMsgHandler.updateToObserver(deveui, jsonMsg);
			DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
