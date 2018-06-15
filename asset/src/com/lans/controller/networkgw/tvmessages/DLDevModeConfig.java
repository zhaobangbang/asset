package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class DLDevModeConfig implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(DLDevModeConfig.class);
	byte type = TVMsgDefs.DL_MODE_CFG;//4bits
	byte lostpoint;//1bit
	byte selfadapt;//1bit
	byte oneoff;//1bit
	byte alreport;//1bit
	short gps;//2bytes
	byte hb;//1byte
	
	public DLDevModeConfig() {
		this.lostpoint = 0;
		this.selfadapt = 0;
		this.oneoff = 0;
		this.alreport = 0;
		this.gps = 0;
		this.hb = 0;
	}
	
	public DLDevModeConfig(byte lostpoint, byte selfadapt, byte oneoff,byte alreport,short gps, byte hb) {
		this.lostpoint = (byte) (lostpoint & 0x1);
		this.selfadapt = (byte) (selfadapt & 0x1);
		this.oneoff = (byte) (oneoff & 0x1);
		this.alreport = (byte)(alreport & 0x1);
		this.gps = gps;
		this.hb = hb;
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		return 4;
	}

	@Override
	public byte[] getBytes() {
		byte[] toRet = new byte[getTotalLength()];
		
		int idx = 0;
		toRet[idx++] = (byte) ((type << 4) | (lostpoint << 3) 
								| (selfadapt << 2) | (oneoff << 1) | alreport);
		toRet[idx++] = (byte) ((gps >> 8) & 0xFF);
		toRet[idx++] = (byte) (gps & 0xFF);
		toRet[idx++] = hb;
		
		return toRet;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		// no need for DL message
		return null;
	}

	@Override
	public void showTV() {
		logger.info("lostpoint {} selfadapt {} oneoff {} alreport {} gps {} hb {}", 
					lostpoint, selfadapt, oneoff, alreport, gps, hb);
	}

}
