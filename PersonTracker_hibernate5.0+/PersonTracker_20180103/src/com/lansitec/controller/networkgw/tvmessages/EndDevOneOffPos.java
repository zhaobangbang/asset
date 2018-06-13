package com.lansitec.controller.networkgw.tvmessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.infrastructure.util.IntBytesConverter;

public class EndDevOneOffPos implements IEndDevItfTV{
    //On demand position
	Logger logger = LoggerFactory.getLogger(EndDevRealTimePos.class);
	byte type = TVMsgDefs.UL_DEV_POS_ACK; //4bits
	byte rfu1; //4bits
	public byte msgid; //1byte
	public float longitude;//4bytes
	public float latitude;//4bytes
	public int time;//4bytes
	
	public EndDevOneOffPos() {
		rfu1 = 0;
		msgid = 0;
		longitude = 0;
		latitude = 0;
		time = 0;
	}
	
	public EndDevOneOffPos(byte rfu1, byte msgid, 
							float longitude, float latitude, int time) {
		this.rfu1 = rfu1;
		this.msgid = msgid;
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
		return 14;
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
		byte bMsgid = tvMsg[pos+1];
		
		byte[] bLongi = new byte[4];
		System.arraycopy(tvMsg, pos + 2, bLongi, 0, 4);
		int iLongi = IntBytesConverter.bytes2Int(bLongi);
		float longi = Float.intBitsToFloat(iLongi);

		byte[] bLati = new byte[4];
		System.arraycopy(tvMsg, pos + 6, bLati, 0, 4);
		int iLati = IntBytesConverter.bytes2Int(bLati);
		float lati = Float.intBitsToFloat(iLati);

		byte[] bTime = new byte[4];
		System.arraycopy(tvMsg, pos + 10, bTime, 0, 4);
		int iTime = IntBytesConverter.bytes2Int(bTime);
		
		EndDevOneOffPos toRet = new EndDevOneOffPos(bRfu1, bMsgid, longi, lati, iTime);
		
		return toRet;
	}

	@Override
	public void showTV() {
		logger.info("rfu1 {} msgid {} longi {} lati {} time {}",
				rfu1, msgid, latitude, longitude, time);
	}


}
