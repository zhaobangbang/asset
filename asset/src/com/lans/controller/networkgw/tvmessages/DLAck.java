package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class DLAck implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(DLAck.class);
	byte type = TVMsgDefs.DL_ACK;
	byte rfu1;//4bits
	byte msgid;//1byte
	
	public DLAck() {
		this.rfu1 = 0;
		this.msgid = 0;
	}
	
	public DLAck(byte rfu1, byte msgid) {
		this.rfu1 = (byte) (rfu1 & 0xF);
		this.msgid = msgid;
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		return 2;
	}

	@Override
	public byte[] getBytes() {
		byte[] toRet = new byte[getTotalLength()];
		
		int idx = 0;
		toRet[idx++] = (byte) ((type << 4) | rfu1);
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
		logger.info("rfu1 {} msgid {}", rfu1, msgid);
	}

}
