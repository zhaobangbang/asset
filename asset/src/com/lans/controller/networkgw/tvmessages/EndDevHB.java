package com.lans.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class EndDevHB implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevHB.class);
	public byte type = TVMsgDefs.UL_DEV_HB; //4bits
	public byte ver; //4bits
	public byte vol; //1byte
	public byte rssi;//1byte
	public short snr; //2byte,introduced since version 1.0
	public byte gpsstatus;//4bits
	public byte vibstatus;//4bits
	public byte chgstatus;//4bits
	byte rfu1;//4bits
	public short usrdata;//用户定义的数据，由版本号决定
	public short crc;//2bytes
	
	public EndDevHB() {
		ver = 0;
		vol = 0;
		rssi = 0;
		snr = 0;
		gpsstatus = 0;
		vibstatus = 0;
		chgstatus = 0;
		rfu1 = 0;
		usrdata = 0;
		crc = 0;
	}
	
	public EndDevHB(byte ver, byte vol, byte rssi, short snr, byte gpsstatus, byte vibstatus,
						byte chgstatus, byte rfu1, short usrdata, short crc) {
		this.ver = ver;
		this.vol = vol;
		this.rssi = rssi;
		this.snr = snr;
		this.gpsstatus = gpsstatus;
		this.vibstatus = vibstatus;
		this.chgstatus = chgstatus;
		this.rfu1 = rfu1;
		this.usrdata = usrdata;
		this.crc = crc;		
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
	    if(ver == 0)
	    	return 7;
	    else if(ver == 1)
	    	return 9; //include snr
	    else if(ver == 2)
	    	return 11; //include usrdata
	    return 0;
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
		//>0, 引入SNR
		//==2, 引入牛计步模式
		if(0 != bVer && 1 != bVer && 2 != bVer){
			return null;
		}
		
		byte bVol = tvMsg[pos+1];
		byte bRssi = tvMsg[pos+2];
		short bSnr = 0;
		if(bVer > 0)
		{
			bSnr = (short)  (((tvMsg[pos+3] << 8) & 0xff00) | (tvMsg[pos+4] & 0xff));
			pos += 2;
		}
		byte bGpsstatus = (byte) ((tvMsg[pos+3] >> 4) & 0x0F);
		byte bVibstatus = (byte) (tvMsg[pos+3] & 0x0F);
		if(bVibstatus < 0 || bVibstatus > 9){
			return null;
		}
		
		byte bChgstatus = (byte) ((tvMsg[pos+4] >> 4) & 0x0F);
		byte bRfu1 = (byte) (tvMsg[pos+4] & 0x0F);
		if(0 != bRfu1){
			return null;
		}
		short bUsrdata = 0;
		if(bVer == 2)
		{
			bUsrdata = (short)  (((tvMsg[pos+5] << 8) & 0xff00) | (tvMsg[pos+6] & 0xff));
			pos += 2;
		}		
		short bCrc = (short) (((tvMsg[pos+5] << 8) & 0xff00) | (tvMsg[pos+6] & 0xff));
		
		EndDevHB toRet = new EndDevHB(bVer, bVol, bRssi, bSnr, bGpsstatus, bVibstatus,
							bChgstatus, bRfu1, bUsrdata, bCrc);
		
		return toRet;
	}

	@Override
	public void showTV() {
		logger.info("vol {} rssi {} snr {} gpsstatus {} vibstatus {} chgstatus {}"
				+ " rfu1 {} usrdata {} crc {}", vol, rssi, snr, gpsstatus, vibstatus, chgstatus,
				rfu1, usrdata, crc);
	}

}
