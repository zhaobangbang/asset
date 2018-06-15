package com.lans.controller.networkgw.tvmessages;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class EndDevCoordinate implements IEndDevItfTV{
	Logger logger = LoggerFactory.getLogger(EndDevCoordinate.class);
    byte type = TVMsgDefs.UL_DEV_COORDINATE;//4bits
    byte length;//4bites
    public List<ScannedBeacon> beaconList; //5bytes*length   major:2bytes   minor:2bytes   rssi:1byte
    
    public EndDevCoordinate() {
		length = 0;
		beaconList = null;
	}
    
    public EndDevCoordinate(byte length,List<ScannedBeacon> beaconList){
    	this.length = length;
    	this.beaconList = beaconList;
    }
    
	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		byte bType = (byte) ((tvMsg[pos] >> 4) & 0X0F );
		if(type != bType){
			return null;
		}
		byte bLength = (byte) (tvMsg[pos] & 0X0F);
		
		if(tvMsg.length != bLength*5 +1 + (bLength+1) / 2)
		{
			logger.error("wrong length, expect {}, actual {}", bLength*5+1 + (bLength+1) / 2, tvMsg.length);
			return null;
		}
		List<ScannedBeacon> list = new LinkedList<ScannedBeacon>();
		for(byte idx = 0; idx < bLength; idx++){
			int major = (int) ((tvMsg[pos+1+5*idx] & 0xFF)<<8 | (tvMsg[pos+2+5*idx] & 0xFF));//major always be 1~255
			int minor = (int) ((tvMsg[pos+3+5*idx] & 0xFF)<<8 | (tvMsg[pos+4+5*idx] & 0xFF));
			int rssi = (byte) (tvMsg[pos+5+5*idx] & 0xFF);
			
			ScannedBeacon beacon = new ScannedBeacon(major, minor, rssi);
			list.add(beacon);
		}
		EndDevCoordinate toRet = new EndDevCoordinate(bLength, list);
		return toRet;
	}

	@Override
	public byte[] getBytes() {
		// no need for UL message
		return null;
	}

	@Override
	public int getTotalLength() {
		int locShiftSize = 0;
		if (beaconList != null) {
			locShiftSize = beaconList.size()*5;
		}
		
		return 1 + locShiftSize;
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
	public byte getType() {
		return type;
	}

	@Override
	public void showTV() {
		logger.info("length {} ",length);
		if(null != beaconList){
			for(ScannedBeacon item : beaconList){
				logger.info("major {} minor {} rssi {}",
						item.getMajor(), item.getMinor(), item.getRssi());
			}
		}
	}

}
