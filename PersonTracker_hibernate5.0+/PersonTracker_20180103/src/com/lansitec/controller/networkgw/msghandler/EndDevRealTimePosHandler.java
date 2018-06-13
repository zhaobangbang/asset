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
import com.lansitec.controller.networkgw.tvmessages.EndDevRealTimePos;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.servlets.DevMsgHandler;

import net.sf.json.JSONObject;

public class EndDevRealTimePosHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevRealTimePosHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevRealTimePosHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_POS_UNACK) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevRealTimePos posMsg = (EndDevRealTimePos) upMsg;
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		double xGPS = posMsg.longitude;
		double yGPS = posMsg.latitude;
		int time = posMsg.time;
		logger.debug("EndDevRealTimePosHandler Entry");
    	JSONObject jsonMsg = new JSONObject();
    	
    	jsonMsg.element("DevEUI", deveui);
    	jsonMsg.element("msgType", "LOC");
    	jsonMsg.element("xGPS", xGPS);
    	jsonMsg.element("yGPS", yGPS);
    	jsonMsg.element("rawxGPS", xGPS);
    	jsonMsg.element("rawyGPS", yGPS);
    	jsonMsg.element("time", time);
    	GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
    	posBean.addDevicePosition(deveui, (float)xGPS, (float)yGPS, new Date((long)time*1000));
        String mapid = null;
    	try {
    		DevInfo deInfos = DevInfoDAO.getDevInfoByDeveui(deveui);
	        if(null == deInfos){
	        	logger.error("Fail to get the devInfos {} by the deveui {}",deInfos,deveui);
	        	mapid = "";
	        }else{
	        	mapid = deInfos.getMapid();
	        }
			java.util.Date date = new java.util.Date((long)time * 1000);
			Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
	    	PositionRecord positionRecord = new PositionRecord(deveui, mapid, (float)yGPS, (float)xGPS, localDateTime);
	    	PositionRecordDAO.create(positionRecord);
	    	DevMsgHandler.updateToMapId(deveui, jsonMsg);
			//DevMsgHandler.updateToObserver(deveui, jsonMsg);
			DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
        //check if pending command request buffered
        String dev = devInfo.getEui();
        if (CmdBufService.getInstance().cmdOfDevExist(deveui)) {
        	logger.info("cmd sent {}", deveui);
        	IEndDevItfTV command = CmdBufService.getInstance().getHeadCmdOfDev(deveui);
        	
        	l3.sendTVMsgToEndDev(dev, (byte)21, command);
        
		logger.debug("EndDevRealTimePosHandler Exit"); 
	}
  }
}
