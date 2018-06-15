package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class DLDevRegResult implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(DLDevRegResult.class);
	byte type = TVMsgDefs.DL_REG_RESULT;//4bits
	byte result;//4bits
	
	public DLDevRegResult() {
		
	}
	
	public DLDevRegResult(byte result) {
		this.result = (byte) (result & 0xF);
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		return 1;
	}

	@Override
	public byte[] getBytes() {
		byte[] toRet = new byte[getTotalLength()];
		
		int idx = 0;
		toRet[idx++] = (byte) ((type << 4) | result);
		return toRet;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		// no need for DL messages
		return null;
	}

	@Override
	public void showTV() {
		logger.info("result {}", result);
	}

}
