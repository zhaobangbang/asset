package com.lansitec.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansitec.controller.networkgw.TVMsgDefs;

public class EndDevAck implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevAck.class);
	public byte type = TVMsgDefs.UL_DEV_ACK; //4bits
	public byte result;//4bits
	public byte msgid;//1byte
	
	public EndDevAck() {
		result = 0;
		msgid = 0;
	}
	
	public EndDevAck(byte result, byte msgid) {
		this.result = result;
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
		// no need for UL message
		return null;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		byte bType = (byte) ((tvMsg[pos] >> 4) & 0x0F);
		if (type != bType) {
			return null;
		}
		
		byte bResult = (byte) (tvMsg[pos] & 0x0F);
		byte bMsgid = tvMsg[pos+1];
		
		EndDevAck toRet = new EndDevAck(bResult, bMsgid);
		return toRet;
	}

	@Override
	public void showTV() {
		logger.info("result {} msgid {}", result, msgid);
	}

}
