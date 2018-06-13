package com.lansitec.controller.networkgw.msghandler;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.controller.networkgw.tvmessages.DLAck;
import com.lansitec.controller.networkgw.tvmessages.EndDevAlarm;
import com.lansitec.dao.WarnRecordDAO;
import com.lansitec.dao.beans.WarnRecord;
import com.lansitec.enumlist.WarnType;
import com.lansitec.servlets.DevMsgHandler;

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
    	boolean alarmOn = false;
    	String sql = null;
    	List<WarnRecord> warnRecordsList = null;
   		try{
   			alarmType = WarnType.求救.toString();
   			//查看此设备告警是否依然存在
   	    	sql = "select * from warn_record_tbl where deveui=\""+deveui+
   	    			       "\" and type=\"" + alarmType + "\" and warn_on='1' ORDER BY warn_stime desc";
   	    	String limitName = "limit";
   	    	warnRecordsList = WarnRecordDAO.getWarnRecordsListByHQL(sql,limitName);
   	    	if(!warnRecordsList.isEmpty()){
   	    		alarmOn = true;
   	    	}
   	    	
   		  }catch(Exception ex) {
   				logger.warn("EndDevAlarmHandler {},无此设备号：" + deveui);
   				return;
   			}
   		
		if((devAlarm.alarm & 0x1) == 1) //SOS
		{		
        	logger.info("{} :SOS alarm received.", deveui);           	
      		
       		//如果告警在数据库中不存在则添加
       		if(!alarmOn)
       		{
       	    	WarnRecord warnRecord = new WarnRecord(deveui, WarnType.valueOf(alarmType),"终端发出求救信息",LocalDateTime.now(),true);
            	WarnRecordDAO.create(warnRecord);
       		}
		}
		else
		{
			//如果告警在数据库中存在则将其置为false
			
        	if(alarmOn)
        	{
       	       for(WarnRecord warnRecord : warnRecordsList){
       	    	   warnRecord.setDescription("终端求救信息结束");
       	    	   warnRecord.setWarn_etime(LocalDateTime.now());
       	    	   warnRecord.setWarn_on(false);
       	    	   WarnRecordDAO.update(warnRecord);
       	       }            		
        	}
        	
		}
		
		DLAck dlAck = new DLAck((byte)0, devAlarm.msgid);
		l3.sendTVMsgToEndDev(devInfo.getEui(), (byte)21, dlAck);
	}
}
