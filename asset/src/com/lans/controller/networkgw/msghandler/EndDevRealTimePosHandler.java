package com.lans.controller.networkgw.msghandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlSender;
import com.lans.beans.GpsNodeStatusBean;
import com.lans.common.BaiduApi;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.EndDevRealTimePos;
import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

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
		
		String xyresult = BaiduApi.GPSTransfer(xGPS, yGPS);

    	if(xyresult.equals(""))
    	{
    		logger.warn("EndDevRealTimePosHandler：Fail to parse GPS data from Baidu, longi {}, lati {}.", xGPS, yGPS);
    		return;
    	}

    	JSONObject jsonMsg = new JSONObject();
    	
    	jsonMsg.element("DevEUI", deveui);
    	jsonMsg.element("msgType", "LOC");
    	
    	String xBaidu="0";
    	String yBaidu="0";
        JSONObject xyjsonObj = JSONObject.fromObject(xyresult);
        String status = xyjsonObj.get("status").toString();
        if(status.equals("0"))
        {
        	String xyBaidu = xyjsonObj.get("result").toString();
        	xyBaidu = xyBaidu.replace("[","");
        	xyBaidu = xyBaidu.replace("]", "");
        	JSONObject xy = JSONObject.fromObject(xyBaidu);
        	xBaidu = xy.get("x").toString();
        	yBaidu = xy.get("y").toString();
        	jsonMsg.element("xGPS", xBaidu);
        	jsonMsg.element("yGPS", yBaidu);
        	jsonMsg.element("rawxGPS", xGPS);
        	jsonMsg.element("rawyGPS", yGPS);
        	jsonMsg.element("time", time);
        	GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
        	posBean.addDevicePosition(deveui, Float.parseFloat(xBaidu), Float.parseFloat(yBaidu), new Date((long)time*1000));
        }
        else
        {
        	logger.info("EndDevRealTimePosHandler：Baidu API error, message: {}", xyjsonObj.get("message"));
        	//return;
        }
    	
        String sql = "select * from dev_list_tbl where deveui=\""+deveui+"\"";
    	String owner = null;

	   	DataBaseMgr db = DataBaseMgr.getInstance();
	   	
    	ResultSet rs = db.executeQuery(sql);
   		try{
   			rs.beforeFirst();
   			if (rs.next()) {
   				owner = rs.getString("owner");
   			}
   			else
   			{
   				logger.error("enddevRealTime(),无此设备号：{}", deveui);
   				return;
   			}
   		  }catch(SQLException ex) {
   			logger.error("EndDevRealTimePosHandler:"+ ex.getMessage());
			return;
   			}
		java.util.Date date = new java.util.Date((long)time * 1000);
	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	sql = "insert into gps_tbl(owner,deveui,latitude,longitude,baidulati,baidulong, type, time) values('"+owner + "','" + deveui+
			       "','" + yGPS + "','" + xGPS + "','" + yBaidu + "','" + xBaidu + "','实时','" + shortDF.format(date) + "')";
    	int affectedRow = db.executeUpdate(sql);

    	if(affectedRow == 0)
    	{
    		logger.warn("{} :Fail to add realtime GPS in database.", deveui); 
    	}
    	
    	try {
			DevMsgHandler.updateToObserver(deveui, jsonMsg);
			DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
			ZwwlSender.pushOutDoor(deveui, String.valueOf(xGPS), String.valueOf(yGPS));
		} catch (Exception e) {
			e.printStackTrace();
		}

        //check if pending command request buffered
        String dev = devInfo.getEui();
        if (CmdBufService.getInstance().cmdOfDevExist(deveui)) {
        	logger.info("cmd sent {}", deveui);
        	IEndDevItfTV command = CmdBufService.getInstance().getHeadCmdOfDev(deveui);
        	
        	l3.sendTVMsgToEndDev(dev, (byte)21, command);
        }
        
        String unixTime = Integer.toString(time);
        BaiduApi.AddTrack(deveui, xGPS, yGPS, unixTime);
        BaiduApi.queryAlarm(deveui);
        
		logger.debug("EndDevRealTimePosHandler Exit"); 
	}
}
