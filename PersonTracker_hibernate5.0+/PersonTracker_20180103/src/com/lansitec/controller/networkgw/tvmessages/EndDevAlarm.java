package com.lansitec.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansitec.controller.networkgw.TVMsgDefs;

public class EndDevAlarm implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevAlarm.class);
	byte type = TVMsgDefs.UL_DEV_ALARM; //4bits
	public byte alarm;//4bits
	public byte msgid;//1byte
	
	public EndDevAlarm() {
		alarm = 0;
		msgid = 0;
	}
	
	public EndDevAlarm(byte alarm, byte msgid) {
		this.alarm = alarm;
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
		
		byte bAlarm = (byte) (tvMsg[pos] & 0x0F);
		byte bMsgid = tvMsg[pos+1];
		
		EndDevAlarm toRet = new EndDevAlarm(bAlarm, bMsgid);
		return toRet;
	}

	@Override
	public void showTV() {
		logger.info("alarm {} msgid {}", alarm, msgid);
	}

}
