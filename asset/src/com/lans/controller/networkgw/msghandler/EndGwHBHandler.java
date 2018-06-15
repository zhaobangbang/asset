package com.lans.controller.networkgw.msghandler;

import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

public class EndGwHBHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndGwHBHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndGwHBHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_GW_HB) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {		       
        //check if pending command request buffered
        String sn = devInfo.getEui(); //NOTES: dev and deveui may be different 
        
	    java.util.Date date = new java.util.Date();
	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "update gateway_tbl set lasttime='"+ shortDF.format(date)+"', firsttime= (case when firsttime is NULL then lasttime else firsttime end) where sn='"+sn+"'";
		
		DataBaseMgr db = DataBaseMgr.getInstance();
		int rowAffected = db.executeUpdate(sql);

		if (0 == rowAffected) {
			logger.warn("EndDevGwHandler: Fail to update. {}", sn);
		}
	}
}
