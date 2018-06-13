package com.lansitec.controller.networkgw.msghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.controller.networkgw.tvmessages.DLCommandReq;
import com.lansitec.controller.networkgw.tvmessages.EndDevAck;

public class EndDevAckHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevAckHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevAckHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_ACK) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		//check if it matches the head of buffered command message
		String dev = devInfo.getEui().toLowerCase();
        if (!CmdBufService.getInstance().cmdOfDevExist(dev)) {
        	return;
        }

        DLCommandReq command = (DLCommandReq)CmdBufService.getInstance().getHeadCmdOfDev(dev);
        byte msgId = command.msgid;

        EndDevAck ackMsg = (EndDevAck)upMsg;
        byte ackMsgId = ackMsg.msgid;
        
        if (msgId == ackMsgId) {
        	CmdBufService.getInstance().rmHeadCmdOfDev(dev);
        }
	}

}
