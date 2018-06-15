package com.lans.controller.networkgw.tvmessages;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.infrastructure.util.IntBytesConverter;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class EndDevRealTimePos implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevRealTimePos.class);
	byte type = TVMsgDefs.UL_DEV_POS_UNACK; //4bits
	byte rfu1; //4bits
	public double longitude;
	public double latitude;
	public int time;//4bytes
	
	public EndDevRealTimePos() {
		rfu1 = 0;
		longitude = 0;
		latitude = 0;
		time = 0;
	}
	
	public EndDevRealTimePos(byte rfu1, double longitude, double latitude, int time) {
		this.rfu1 = rfu1;
		this.longitude = longitude;
		this.latitude = latitude;
		this.time = time;
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		return 13;
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
		
		byte bRfu1 = (byte) (tvMsg[pos] & 0x0F);
		if(0 != bRfu1){
			return null;
		}
		byte[] bLongi = new byte[4];
		System.arraycopy(tvMsg, pos + 1, bLongi, 0, 4);
		int iLongi = IntBytesConverter.bytes2Int(bLongi);
		//int iLongi = Integer.parseInt(new BigInteger(1, bLongi).toString(10));
		double longi = Float.intBitsToFloat(iLongi);
		BigDecimal   b   =   new   BigDecimal(longi);
		longi   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
		
		byte[] bLati = new byte[4];
		System.arraycopy(tvMsg, pos + 5, bLati, 0, 4);
		int iLati = IntBytesConverter.bytes2Int(bLati);
		//int iLati = Integer.parseInt(new BigInteger(1, bLati).toString(10));
		double lati = Float.intBitsToFloat(iLati);
		b   =   new   BigDecimal(lati);
		lati   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
		
		byte[] bTime = new byte[4];
		System.arraycopy(tvMsg, pos + 9, bTime, 0, 4);
		int iTime = IntBytesConverter.bytes2Int(bTime);
		
		EndDevRealTimePos toRet = new EndDevRealTimePos(bRfu1, longi, lati, iTime);
		
		return toRet;
	}

	@Override
	public void showTV() {
		logger.info("rfu1 {} longi {} lati {} time {}",	rfu1, latitude, longitude, time);
	}

}
