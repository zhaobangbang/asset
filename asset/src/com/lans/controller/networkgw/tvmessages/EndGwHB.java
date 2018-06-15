package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class EndGwHB implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndGwHB.class);
	public byte type = TVMsgDefs.UL_GW_HB; //4bits
	public byte ver; //4bits
	
	public EndGwHB() {
		ver = 0;
	}
	
	public EndGwHB(byte ver) {
		this.ver = ver;	
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
		// no need for UL message
		return null;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		byte bType = (byte) ((tvMsg[pos] >> 4) & 0x0F);
		if (type != bType) {
			return null;
		}
		
		byte bVer = (byte) (tvMsg[pos] & 0x0F);
		
		EndGwHB toRet = new EndGwHB(bVer);
		
		return toRet;
	}

	@Override
	public void showTV() {
		
	}

}
