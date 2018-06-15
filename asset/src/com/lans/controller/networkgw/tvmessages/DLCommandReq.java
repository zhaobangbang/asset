package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class DLCommandReq implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(DLCommandReq.class);
	public byte type = TVMsgDefs.DL_COMMAND_REQ;
	public byte command;//4bits
	public byte msgid;//1byte
	
	public DLCommandReq() {
		this.command = 0;
		this.msgid = 0;
	}
	
	public DLCommandReq(byte command, byte msgid) {
		this.command = (byte) (command & 0xF);
		this.msgid = msgid;
	}
	
	@Override
	public byte getType() {
		return type;
	}

	public byte getCommand() {
		return command;
	}
	
	@Override
	public int getTotalLength() {
		return 2;
	}

	@Override
	public byte[] getBytes() {
		byte[] toRet = new byte[getTotalLength()];
		
		int idx = 0;
		toRet[idx++] = (byte) ((type << 4) | command);
		toRet[idx++] = msgid;
		
		return toRet;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		//no need for DL message
		return null;
	}

	@Override
	public void showTV() {
		logger.info("command {} msgid {}", command, msgid);
	}

}
