package com.lansitec.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansitec.controller.networkgw.TVMsgDefs;

public class DLSlotConfig implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(DLSlotConfig.class);
	byte type = TVMsgDefs.DL_SLOT; //4bits
	byte command; //4 bits
	short interval; //2 bytes
	
	public static final int CMD_DELAY_SEND = 0x1;
	public static final int CMD_AHEAD_SEND = 0x2;
	public static final int CMD_OPEN_RX2 = 0x3;
	public static final int CMD_CLOSE_RX2 = 0x4;
	
	public DLSlotConfig() {
		command = 0;
		interval = 0;
	}
	
	public DLSlotConfig(byte command, short interval) {
		this.command = command;
		this.interval = interval;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes() {
		byte[] toRet = new byte[getTotalLength()];
		
		int idx = 0;
		toRet[idx++] = (byte) ((type << 4) | command);
		toRet[idx++] = (byte) ((interval >> 8) & 0xFF);
		toRet[idx++] = (byte) (interval & 0xFF);
		
		return toRet;
	}

	@Override
	public int getTotalLength() {
		return 3;
	}

	@Override
	public byte getType() {
		return type;
	}

	@Override
	public void showTV() {
		logger.info("DLSlotConfig: command {} gap {}", command, interval);
	}

}
