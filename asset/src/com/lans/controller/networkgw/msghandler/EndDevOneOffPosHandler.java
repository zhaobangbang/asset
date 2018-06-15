package com.lans.controller.networkgw.msghandler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.GpsNodeStatusBean;
import com.lans.common.BaiduApi;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.DLAck;
import com.lans.controller.networkgw.tvmessages.EndDevOneOffPos;
import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

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
		
		String xyresult = BaiduApi.GPSTransfer(xGPS, yGPS);

    	if(xyresult.equals(""))
    	{
    		logger.warn("EndDevOneOffPosHandler：Fail to parse GPS data from Baidu, longi {}, lati {}.", xGPS, yGPS);
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
        	jsonMsg.element("time", time);
        	GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
        	posBean.addDevicePosition(deveui, Float.parseFloat(xBaidu), Float.parseFloat(yBaidu), new Date((long)time*1000));
        	
            String unixTime = Integer.toString(time);

			//Date d = new Date((long)time*1000);
            //logger.info("time: {}",d.getTime());
            
            
            BaiduApi.AddTrack(deveui, xGPS, yGPS, unixTime);
            BaiduApi.queryAlarm(deveui);
        }
        else
        {
        	logger.warn("EndDevOneOffPosHandler：Baidu API error, message: {}", xyjsonObj.get("message"));
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
   		  }catch(SQLException ex) {
   				logger.error("enddevRealTime(),无此设备号：" + deveui);
   				return;
   			}
		java.util.Date date = new java.util.Date((long)time * 1000);
	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	sql = "insert into gps_tbl(owner,deveui,latitude,longitude,baidulati,baidulong, type, time) values('"+owner + "','" + deveui+
			       "','" + yGPS + "','" + xGPS + "','" + yBaidu + "','" + xBaidu + "','请求','" + shortDF.format(date) + "')";
    	int affectedRow = db.executeUpdate(sql);

    	if(affectedRow == 0)
    	{
    		logger.warn("{}:Fail to add Oneoff GPS in database.", deveui); 
    	}
    	
    	try {
			DevMsgHandler.updateToObserver(deveui, jsonMsg);
			DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
