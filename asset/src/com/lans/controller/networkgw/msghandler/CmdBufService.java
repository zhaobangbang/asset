package com.lans.controller.networkgw.msghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.tvmessages.DLCommandReq;
import com.lans.infrastructure.util.SendBuffer;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class CmdBufService {
	Logger logger = LoggerFactory.getLogger(CmdBufService.class);
	SendBuffer buffer;
	public static CmdBufService instance = null;

	public CmdBufService() {
		buffer = new SendBuffer("SMSSendBuffer");
	}

	public static CmdBufService getInstance() {
		if (instance == null) {
			instance = new CmdBufService();
		}

		return instance;
	}
	
	public void addPosReqCmdOfDev(String eui) {
		byte command = 1;
		byte msgId = MessageIdGenerater.getMessageId(eui);
		
		DLCommandReq req = new DLCommandReq(command, msgId);
		addCmdOfDevToTail(eui, "user", req);
	}

	public void addReqDevRegistration(String eui){
		byte command = 2;
		byte msgId = MessageIdGenerater.getMessageId(eui);
		
		DLCommandReq req = new DLCommandReq(command, msgId);
		addCmdOfDevToTail(eui, "user", req);
		
	}

	public void addReqDevRestart(String eui){
		byte command = 3;
		byte msgId = MessageIdGenerater.getMessageId(eui);
		
		DLCommandReq req = new DLCommandReq(command, msgId);
		addCmdOfDevToTail(eui, "user", req);
	}

	public void addCmdOfDevToTail(String dest, String src, IEndDevItfTV msg) {
		buffer.addBufferMsgToTail(dest, src, msg);
		logger.info("cmd dest {} src {} added to buffer", dest, src);
	}

	public IEndDevItfTV getHeadCmdOfDev(String dest) {
		return (IEndDevItfTV) buffer.getMsgOfBufferHead(dest);
	}

	public void rmHeadCmdOfDev(String dest) {
		buffer.rmHeadOfBuffer(dest);
		logger.info("cmd dest {} removed from buffer", dest);
	}

	public boolean cmdOfDevExist(String dest) {
		return !buffer.isReceiverBufferEmpty(dest);
	}
}
