package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class DLDevLoraConfig implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(DLDevRegResult.class);
	byte type = TVMsgDefs.DL_LORA_CFG;//4bits
	byte adr;//1bit
	byte rfu1;//3bits
	byte dr;//4bits
	byte rfu2;//4bits
	byte mode;//3bits
	byte power;//5bits
	
	public DLDevLoraConfig() {
		this.adr = 0;
		this.rfu1 = 0;
		this.dr = 0;
		this.rfu2 = 0;
		this.mode = 0;
		this.power = 0;
	}
	
	public DLDevLoraConfig(byte adr, byte rfu1, byte dr, byte rfu2, byte mode, byte power) {
		this.adr = (byte) (adr & 0x1);
		this.rfu1 = (byte) (rfu1 & 0x7);
		this.dr = (byte) (dr & 0xF);
		this.rfu2 = (byte) (rfu2 & 0xF);
		this.mode = (byte) (mode & 0x7);
		this.power = (byte) (power & 0x1F);
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		return 3;
	}

	@Override
	public byte[] getBytes() {
		byte[] toRet = new byte[getTotalLength()];
		
		int idx = 0;
		toRet[idx++] = (byte) ((type << 4) | (adr << 3) | rfu1);
		toRet[idx++] = (byte) ((dr << 4) | rfu2);
		toRet[idx++] = (byte) ((mode << 5) | power);
		
		return toRet;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		// no need for DL message
		return null;
	}

	@Override
	public void showTV() {
		logger.info("adr {} rfu1 {} dr {} rfu2 {} mode {} power {}",
				    adr, rfu1, dr, rfu2, mode, power);
	}

}
