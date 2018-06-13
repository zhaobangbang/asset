package com.lansitec.controller.networkgw.tvmessages;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.infrastructure.util.IntBytesConverter;

public class EndDevHistoryPosList implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevHistoryPosList.class);
	byte type = TVMsgDefs.UL_DEV_LOST_POS; //4bits
	byte length;//4bits
	public double longitude; //4bytes;
	public double latitude;//4bytes;
	public int time;//4bytes;
	public float speed;
	public List<LocShift> locList; //6bytes*length
	
	public EndDevHistoryPosList() {
		length = 0;
		longitude = 0;
		latitude = 0;
		time = 0;
		speed = -1;
		locList = null;
	}
	
	public EndDevHistoryPosList(byte length, double longi, double lati, int time, 
								List<LocShift> locList) {
		this.length = length;
		this.longitude = longi;
		this.latitude = lati;
		this.time = time;
		this.locList = locList;
		this.speed = -1;
	}
	public EndDevHistoryPosList(byte length, double longi, double lati, int time, float speed,
			List<LocShift> locList) {
		this.length = length;
		this.longitude = longi;
		this.latitude = lati;
		this.time = time;
		this.speed = speed;
		this.locList = locList;
}
	@Override
	public byte getType() {
		return type;
	}

	@Override
	public int getTotalLength() {
		int locShiftSize = 0;
		if (locList != null) {
			locShiftSize = locList.size()*6;
		}
		
		return 13 + locShiftSize;
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
		int bodyLen = tvMsg.length - pos - 1;
		byte bLength = (byte) (tvMsg[pos] & 0x0F);
		if(bLength == 0xF)//It's P2P message, need to calcuate the real length.
		{
			int offLen = bodyLen - 12;
			if(offLen == 0)
			{
				bLength = 0;
			}
			else
			{
				bLength = (byte) (offLen / 8);
			}
			byte[] bLongi = new byte[4];
			System.arraycopy(tvMsg, pos + 1, bLongi, 0, 4);
			int iLongi = IntBytesConverter.bytes2Int(bLongi);
			double  longi = Float.intBitsToFloat(iLongi);
			BigDecimal   b   =   new   BigDecimal(longi);//BigDecimal 类使用户能完全控制舍入行为
			longi   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
			
			byte[] bLati = new byte[4];
			System.arraycopy(tvMsg, pos + 5, bLati, 0, 4);
			int iLati = IntBytesConverter.bytes2Int(bLati);
			double lati = Float.intBitsToFloat(iLati);
			b   =   new   BigDecimal(lati);
			lati   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
			
			byte[] bTime = new byte[4];
			System.arraycopy(tvMsg, pos + 9, bTime, 0, 4);
			int iTime = IntBytesConverter.bytes2Int(bTime);
			
			byte[] bSpeed = new byte[2];
			System.arraycopy(tvMsg, pos + 13, bSpeed, 0, 2);
			float fSpeed = (float) (IntBytesConverter.bytes2Short(bSpeed) / 100.0);
			
			List<LocShift> list = new LinkedList<LocShift>();
			int iPrevLongi = iLongi;
			int iPrevLati = iLati;
			int iPrevTime = iTime;
			for (byte idx = 0; idx < bLength; idx++) {
				short longiShift = (short) (((tvMsg[pos+15+8*idx] & 0xFF)<<8) | (tvMsg[pos+16+8*idx] & 0xFF));
				short latiShift = (short) ((tvMsg[pos+17+8*idx] & 0xFF)<<8 | (tvMsg[pos+18+8*idx] & 0xFF));
				short timeShift = (short) ((tvMsg[pos+19+8*idx] & 0xFF)<<8 | (tvMsg[pos+20+8*idx] & 0xFF));
				short speed = (short) ((tvMsg[pos+21+8*idx] & 0xFF)<<8 | (tvMsg[pos+22+8*idx] & 0xFF));
				//next location/time is based on previous one
				double nextLongi = Float.intBitsToFloat(iPrevLongi + longiShift);
				double nextLati = Float.intBitsToFloat(iPrevLati + latiShift);
				int nextTime = iPrevTime + timeShift;
				
				b   =   new   BigDecimal(nextLongi);
				nextLongi   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
				b   =   new   BigDecimal(nextLati);
				nextLati   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
				
				LocShift shift = new LocShift(nextLongi, nextLati, nextTime,(float) (speed/100.0));
				list.add(shift);
				
				iPrevLongi = iPrevLongi + longiShift;
				iPrevLati = iPrevLati + latiShift;
				iPrevTime = iPrevTime + timeShift;
			}
			
			EndDevHistoryPosList toRet = new EndDevHistoryPosList(bLength, longi, lati, iTime, fSpeed,list);
			return toRet;
		}
		else
		{
			byte[] bLongi = new byte[4];
			System.arraycopy(tvMsg, pos + 1, bLongi, 0, 4);
			int iLongi = IntBytesConverter.bytes2Int(bLongi);
			double  longi = Float.intBitsToFloat(iLongi);
			BigDecimal   b   =   new   BigDecimal(longi);//BigDecimal 类使用户能完全控制舍入行为
			longi   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
			
			byte[] bLati = new byte[4];
			System.arraycopy(tvMsg, pos + 5, bLati, 0, 4);
			int iLati = IntBytesConverter.bytes2Int(bLati);
			double lati = Float.intBitsToFloat(iLati);
			b   =   new   BigDecimal(lati);
			lati   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
			
			byte[] bTime = new byte[4];
			System.arraycopy(tvMsg, pos + 9, bTime, 0, 4);
			int iTime = IntBytesConverter.bytes2Int(bTime);
			
			List<LocShift> list = new LinkedList<LocShift>();
			int iPrevLongi = iLongi;
			int iPrevLati = iLati;
			int iPrevTime = iTime;
			for (byte idx = 0; idx < bLength; idx++) {
				short longiShift = (short) (((tvMsg[pos+13+6*idx] & 0xFF)<<8) | (tvMsg[pos+14+6*idx] & 0xFF));
				short latiShift = (short) ((tvMsg[pos+15+6*idx] & 0xFF)<<8 | (tvMsg[pos+16+6*idx] & 0xFF));
				short timeShift = (short) ((tvMsg[pos+17+6*idx] & 0xFF)<<8 | (tvMsg[pos+18+6*idx] & 0xFF));
	
				//next location/time is based on previous one
				double nextLongi = Float.intBitsToFloat(iPrevLongi + longiShift);
				double nextLati = Float.intBitsToFloat(iPrevLati + latiShift);
				int nextTime = iPrevTime + timeShift;
				
				b   =   new   BigDecimal(nextLongi);
				nextLongi   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
				b   =   new   BigDecimal(nextLati);
				nextLati   =   b.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
				
				LocShift shift = new LocShift(nextLongi, nextLati, nextTime);
				list.add(shift);
				
				iPrevLongi = iPrevLongi + longiShift;
				iPrevLati = iPrevLati + latiShift;
				iPrevTime = iPrevTime + timeShift;
			}
			
			EndDevHistoryPosList toRet = new EndDevHistoryPosList(bLength, longi, lati, iTime, list);
			return toRet;
		}
	}

	@Override
	public void showTV() {
		logger.info("longi {} lati {} time {} length {}", longitude, latitude, time, length);
		
		if (locList != null) {
			for (LocShift item : locList) {
				logger.info("longishift {} latishift {} timeshift {}",
						item.getLongi(), item.getLati(), item.getTime());
			}
		}
	}

}
