package com.lans.controller.networkgw.tvmessages;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lansi.msghandle.itftv.IEndDevItfTV;

public class EndDevBracelet implements IEndDevItfTV {
	Logger logger = LoggerFactory.getLogger(EndDevBracelet.class);
	public byte type = TVMsgDefs.UL_DEV_BRACELET;//4bites
    public byte length;//4bites
    public List<ScannedBracelet> braceletList;//16bytes*length
    public byte[] TMOFF;
    
    public EndDevBracelet(){
    	length = 0;
    	braceletList = null;
    	TMOFF = null;
    }
    
    public EndDevBracelet(byte length,List<ScannedBracelet> braceletList,byte[] TMOFF){
    	this.length = length;
    	this.braceletList = braceletList;
    	this.TMOFF = TMOFF;
    }
    
	@Override
	public IEndDevItfTV fromBytes(byte[] tvMsg, int pos) {
		byte bType = (byte) ((tvMsg[pos] >> 4) & 0X0F);
		if(type != bType){
			return null;
		}
		
		byte bLength = (byte) (tvMsg[pos] & 0x0F);
		List<ScannedBracelet> braceletList = new LinkedList<ScannedBracelet>();
		for(int idx= 0; idx < bLength; idx++){
			int bMacOne = ((tvMsg[pos+1+16*idx] & 0xff) << 24) | ((tvMsg[pos+2+16*idx] & 0xff) << 16) | ((tvMsg[pos+3+16*idx] & 0xff) << 8) | ((tvMsg[pos+4+16*idx] & 0xff))  ; 
			short bMacTwo = (short) ((tvMsg[pos+5+16*idx] << 8) & 0xff00 | (tvMsg[pos+6+16*idx] & 0xff));
			byte bHB = tvMsg[pos+7+16*idx];
			short bSteps = (short) (((tvMsg[pos+8+16*idx] << 8) & 0xff00) | (tvMsg[pos+9+16*idx] & 0x00ff));
			byte bBat = tvMsg[pos+10+16*idx];
			byte bSysp = tvMsg[pos+11+16*idx];
			byte bDiap = tvMsg[pos+12+16*idx];
			short bCalo = (short) ((tvMsg[pos+13+16*idx] << 8) & 0xff00 | (tvMsg[14+16*idx] & 0x00ff));
			byte bRssi = tvMsg[pos+15+16*idx];
			byte bSos = tvMsg[pos+16+16*idx]; 
			
			ScannedBracelet bracelet = new ScannedBracelet(bMacOne, bMacTwo, bHB, bSteps, bBat, bSysp, bDiap, bCalo, bRssi, bSos);
			braceletList.add(bracelet);
		}
		
		int jlength = 0;
		if(bLength % 2 == 0){
			jlength = bLength/2;
		}else if(bLength % 2 != 0){
			jlength = bLength/2+1;
		}
		byte[] bTime = new byte[jlength];
		if(braceletList != null){
			System.arraycopy(tvMsg, pos+1+braceletList.size()*16, bTime, 0, jlength);
		}
		EndDevBracelet toRet = new EndDevBracelet(bLength, braceletList,bTime);
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
		if(braceletList != null){
			locShiftSize = braceletList.size()*16;
		}
		return 1+locShiftSize;
	}
	
	public ScannedBracelet getRssiBiggest(){
		if(braceletList != null){
			ScannedBracelet rtnbracelet = braceletList.get(0);
			int rssi = -200;
			for(ScannedBracelet item : braceletList){
				if(item.getRssi() > rssi){
					rssi = item.getRssi();
					rtnbracelet = item;
				}
			}
			return rtnbracelet;
		}
		return null;
	}

	@Override
	public byte getType() {
		return type;
	}

	@Override
	public void showTV() {
		logger.info("length {} braceletList {} ",length,braceletList);

	}
    
}
