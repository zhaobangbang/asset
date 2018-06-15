package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class EndDevReg implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevReg.class);
	byte type = TVMsgDefs.UL_DEV_REG; //4bits
	public byte adr; //1bit
	public byte mode; //3bits
	public byte smode; //1byte
	public byte power; //5bits
	public byte rfu1; //3bits
	public byte dr; //4bits
	public byte lostpoint; //1bit
	public byte selfadapt; //1bit
	public byte oneoff; //1bit
	public byte alreport; //1bit
	public short gps; //2bytes
	public byte hb; //1byte
	public short crc; //2bytes
	
	
	public EndDevReg() {
		this.adr = 0;
		this.mode = 0;
		this.smode = 0;
		this.power = 0;
		this.rfu1 = 0;
		this.dr = 0;
		this.lostpoint = 0;
		this.selfadapt = 0;
		this.oneoff = 0;
		this.alreport = 0;
		this.gps = 0;
		this.hb = 0;
		this.crc = 0;
	}
	
	public EndDevReg(byte adr, byte mode, byte smode, byte power, byte rfu1, byte dr,
					 byte lostpoint, byte selfadapt, byte oneoff, byte alreport, short gps, byte hb, short crc) {
		this.adr = adr;
		this.mode = mode;
		this.smode = smode;
		this.power = power;
		this.rfu1 = rfu1;
		this.dr = dr;
		this.lostpoint = lostpoint;
		this.selfadapt = selfadapt;
		this.oneoff = oneoff;
		this.alreport = alreport;
		this.gps = gps;
		this.hb = hb;
		this.crc = crc;
	}
	
	public EndDevReg(byte adr, byte mode, byte smode, byte power, byte rfu1, byte dr,
			 byte lostpoint, byte selfadapt, Byte oneoff, byte alreport, short gps, byte hb) {
		this.adr = adr;
		this.mode = mode;
		this.smode = smode;
		this.power = power;
		this.rfu1 = rfu1;
		this.dr = dr;
		this.lostpoint = lostpoint;
		this.selfadapt = selfadapt;
		this.oneoff = oneoff;
		this.alreport = alreport;
		this.gps = gps;
		this.hb = hb;
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		return 9;
	}

	@Override
	public byte[] getBytes() {
		byte[] toRet = new byte[getTotalLength()];

		int idx = 0;
		toRet[idx++] = (byte) ((type << 4) | (adr << 3) | mode);
		toRet[idx++] = smode;
		toRet[idx++] = (byte) ((power << 3) | rfu1);
		toRet[idx++] = (byte) ((dr << 4) | (lostpoint << 3) | (selfadapt << 2) | (oneoff << 1) | alreport);
		toRet[idx++] = (byte) ((gps >> 8) & 0xFF);
		toRet[idx++] = (byte) (gps & 0xFF);
		toRet[idx++] = hb;
		toRet[idx++] = (byte) ((crc >> 8) & 0xFF);
		toRet[idx++] = (byte) (crc & 0xFF);
		
		return toRet;
	}

	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		byte bType = (byte) ((tvMsg[pos] >> 4) & 0x0F);
		if (type != bType) {
			return null;
		}
		
		byte bAdr = (byte) ((tvMsg[pos] >> 3) & 0x1);
		byte bMode = (byte) (tvMsg[pos] & 0x07);
		byte bSmode = tvMsg[pos+1];
		if(bSmode != 0x01 && bSmode != 0x02 && bSmode != 0x04 && bSmode != 0x08 &&
				bSmode != 0x10 && bSmode != 0x20 && bSmode != 0x40){
				return null;
		}
		
		byte bPower = (byte) ((tvMsg[pos+2] >> 3) & 0x1F);
		byte bRfu1 = (byte) (tvMsg[pos+2] & 0x07);
		if(0 != bRfu1 && 4 != bRfu1){//4ΪCLAA modeD
			return null;
		}
		byte bDr = (byte) ((tvMsg[pos+3] >> 4) & 0x0F);
		byte bLostPoint = (byte) ((tvMsg[pos+3] >> 3) & 0x01);
		byte bSelfAdapt = (byte) ((tvMsg[pos+3] >> 2) & 0x01);
		byte bOneoff = (byte) ((tvMsg[pos+3] >> 1) & 0x01);
		byte balreport = (byte) (tvMsg[pos+3] & 0x01);
		short bGps = (short) (((tvMsg[pos+4] << 8) & 0xff00) | (tvMsg[pos+5] & 0xff));
		byte bHb = tvMsg[pos+6];
		short bCrc = (short) (((tvMsg[pos+7] << 8) & 0xff00) | (tvMsg[pos+8] & 0xff));
		
		IEndDevItfTV toRet = new EndDevReg(bAdr, bMode, bSmode, bPower, bRfu1, bDr,
							bLostPoint, bSelfAdapt, bOneoff, balreport, bGps, bHb, bCrc);
		
		return toRet;
	}

	@Override
	public void showTV() {
		logger.info("adr {} mode {} smode {} power {} rfu1 {} dr {} lostpoint {} "
				+ "selfadapt {} oneoff {} alreport {} gps {} hb {} crc {}", 
				adr, mode, smode, power, rfu1, dr, lostpoint, selfadapt, oneoff, alreport, 
				Integer.toHexString(gps), hb, Integer.toHexString(crc));
	}

}
