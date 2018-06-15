package com.lans.controller.networkgw.msghandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.DLAck;
import com.lans.controller.networkgw.tvmessages.EndDevAlarm;
import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

public class EndDevAlarmHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevAlarmHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevAlarmHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_ALARM) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevAlarm devAlarm = (EndDevAlarm)upMsg;
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		String alarmType = null;
		
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
   				logger.warn("EndDevAlarmHandler {},无此设备号：" + deveui);
   				return;
   			}
   		
   		alarmType = "SOS";
		//查看此设备告警是否依然存在
    	sql = "select * from warning_record_tbl where deveui=\""+deveui+
    			       "\" and warn_desc=\"" + alarmType + "\" and warn_on='1'";
	    rs = db.executeQuery(sql);
   		try{
   			rs.beforeFirst();
   			if (rs.next()) {
   			}
   		  }catch(SQLException ex) {
   				logger.warn("EndDevAlarmHandler, {},database error", deveui);
   				ex.printStackTrace();
   				return;
   		} 
   		
		if((devAlarm.alarm & 0x1) == 1) //SOS
		{		
        	logger.info("{} :SOS alarm received.", deveui);           	
      		
       		//如果告警在数据库中不存在则添加
       		//if(!alarmOn)
       		{
       	    	java.util.Date date = new java.util.Date();
       	    	SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	sql = "insert into warning_record_tbl(usrname,deveui,warn_desc, warn_stime,warn_on) values('"+owner + "','" + deveui+
     			       "','" + alarmType + "','" + shortDF.format(date) + "','1')";
            	int affectedRow = db.executeUpdate(sql);

            	if(affectedRow == 0)
            	{
            		logger.warn("{}:Fail to add alarm in database.", deveui); 
            	}
       		}
		}
		else
		{
			//如果告警在数据库中存在则将其置为false
			/*
        	if(alarmOn)
        	{
       	    	java.util.Date date = new java.util.Date();
       	    	SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            	sql = "update warning_record_tbl set warn_etime='" +shortDF.format(date) +"', warn_on='0' where deveui=\""+deveui+
    			       "\" and warn_desc=\"" + alarmType + "\" and warn_on='1'";
            	
             	int affectedRow = db.executeUpdate(sql);

             	if(affectedRow == 0)
             	{
             		logger.warn("{}:Fail to add alarm in database.", deveui); 
             	}              		
        	}
        	*/
		}
		
		DLAck dlAck = new DLAck((byte)0, devAlarm.msgid);
		l3.sendTVMsgToEndDev(devInfo.getEui(), (byte)21, dlAck);
	}
}
