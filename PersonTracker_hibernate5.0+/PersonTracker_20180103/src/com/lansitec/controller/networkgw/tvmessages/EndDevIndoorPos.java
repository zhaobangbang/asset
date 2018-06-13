package com.lansitec.controller.networkgw.tvmessages;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.infrastructure.util.IntBytesConverter;

public class EndDevIndoorPos implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevIndoorPos.class);
	byte type = TVMsgDefs.UL_DEV_INDOOR_POS; //4bits
	byte length;//4bits
	public byte move; //1byte
	public int time; //4bytes
	public List<ScannedBeacon> beaconList; //5bytes*length
	
	public EndDevIndoorPos() {
		length = 0;
		move = 0;
		time = 0;
		beaconList = null;
	}
	
	public EndDevIndoorPos(byte length, byte move, int time, List<ScannedBeacon> bList) {
		this.length = length;
		this.move = move;
		this.time = time;
		this.beaconList = bList;
	}
	
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		int locShiftSize = 0;
		if (beaconList != null) {
			locShiftSize = beaconList.size()*5;
		}
		
		return 2 + 4+ locShiftSize;
	}

	public ScannedBeacon getRSSIBiggest(){
		if (beaconList != null) {
			ScannedBeacon rtnBeacon = beaconList.get(0);
			int rssi = -200;
			for (ScannedBeacon item : beaconList) {
				if(item.getRssi() > rssi)
				{
					rssi = item.getRssi();
					rtnBeacon = item;
				}
			}
			return rtnBeacon;
		}
		return null;
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
		
		byte bLength = (byte) (tvMsg[pos] & 0x0F);
		byte bMove = (byte) (tvMsg[pos+1] & 0x0F);
		
		byte[] bTime = new byte[4];
		System.arraycopy(tvMsg, pos + 2, bTime, 0, 4);
		int iTime = IntBytesConverter.bytes2Int(bTime);
		
		List<ScannedBeacon> list = new LinkedList<ScannedBeacon>();
		for (byte idx = 0; idx < bLength; idx++) {
			int major = (int) (((tvMsg[pos+6+5*idx] & 0xFF)<<8) | (tvMsg[pos+7+5*idx] & 0xFF));
			int minor = (int) ((tvMsg[pos+8+5*idx] & 0xFF)<<8 | (tvMsg[pos+9+5*idx] & 0xFF));
			int rssi = (byte) (tvMsg[pos+10+5*idx] & 0xFF);
			
			ScannedBeacon beacon = new ScannedBeacon(major, minor, rssi);
			list.add(beacon);
		}
		
		EndDevIndoorPos toRet = new EndDevIndoorPos(bLength, bMove, iTime, list);
		return toRet;
	}

	@Override
	public void showTV() {
		logger.info("length {} move {} time {}", length, move, time);
		
		if (beaconList != null) {
			for (ScannedBeacon item : beaconList) {
				logger.info("major {} minor {} rssi {}",
						item.getMajor(), item.getMinor(), item.getRssi());
			}
		}
	}

}
