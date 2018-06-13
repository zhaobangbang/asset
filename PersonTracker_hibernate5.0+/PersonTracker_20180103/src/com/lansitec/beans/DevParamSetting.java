package com.lansitec.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.common.CRC16;
import com.lansitec.controller.networkgw.msghandler.EndDevHBHandler;
import com.lansitec.dao.DevConfigDAO;
import com.lansitec.dao.beans.DevConfig;


public class DevParamSetting {
	public byte ADR = 0;
	public byte CLAAMODE = 0;
	public byte DATARATE = 0;
	public byte DRSCHEME = 0;
	public byte S_DRSCHEME = 0;
	public byte POWER = 0;
	public byte LOSTPOINT = 0;
	public byte SELFADAPT = 0;
	public byte ONEOFF = 0;
	public byte ALREPORT = 0;
	public short GPS = 0;
	public short HEARTBEAT = 0;
	public short CRC = 0;
	
	Logger logger = LoggerFactory.getLogger(DevParamSetting.class);
	
	public DevParamSetting() {
		// TODO Auto-generated constructor stub
	}
	
	public void readDevParams(String deveui){	
		
		DevConfig devcfg = null;
		try {
			devcfg = DevConfigDAO.getDevConfigByDevice(deveui);
			if(null == devcfg){
				logger.error("the deveui {} don't have cfg data! so Fail to get data!",deveui);
			}else{
				ADR = devcfg.getADR();
				CLAAMODE = devcfg.getRFU();
				DATARATE = devcfg.getDR();
				DRSCHEME = devcfg.getMODE();
				S_DRSCHEME = devcfg.getS_MODE();
				POWER = devcfg.getPOWER();
				LOSTPOINT = devcfg.getLOSTPOINT();
				SELFADAPT = devcfg.getSELFADAPT();
				ONEOFF = devcfg.getONEOFF();
				ALREPORT = devcfg.getALREPORT();
				GPS = devcfg.getGPS();
				HEARTBEAT = devcfg.getHB();
				CRC = devcfg.getCRC16();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	public int writeDevParams(String deveui,int update)
	{   
		int result = 1;
		byte[] params = new byte[7];
		params[0] =  (byte) (((ADR << 3) & 0x8) | (DRSCHEME & 0x7)) ;
		params[1] = S_DRSCHEME;
		params[2] = (byte) (((POWER << 3) & 0xF8) | (CLAAMODE & 0x7));
		params[3] = (byte) (((DATARATE << 4) & 0xF0) | ((LOSTPOINT << 3) & 0x8) | ((SELFADAPT << 2) & 0x4) | ((ONEOFF << 1) & 0x2)|(ALREPORT) & 0x1) ;
		params[4] = (byte)((GPS >> 8)  & 0xFF);
		params[5] = (byte)(GPS & 0xFF);
		params[6] = (byte)HEARTBEAT;
		
		CRC = (short)CRC16.calcCrc16(params);
		EndDevHBHandler.setEndDevParamStatus(deveui, false);
		if(update == 0){
			  DevConfig devcfg = new DevConfig(deveui, ADR, CLAAMODE, DATARATE, DRSCHEME, S_DRSCHEME, POWER, LOSTPOINT, SELFADAPT, ONEOFF, ALREPORT, GPS, HEARTBEAT, CRC);
			  DevConfigDAO.create(devcfg);
			  logger.info("Success to add the deveui's {} devcfg data!",deveui);
			  result = 0;
		}
		if(update == 1){
			 DevConfig devConfig = null;
			try {
				devConfig = DevConfigDAO.getDevConfigByDevice(deveui);
				if(null == devConfig){
					 logger.error("Fail to get the deveui's {} cfgdata!",deveui);
				 }else{
					 devConfig.setADR(ADR);
					 devConfig.setRFU(CLAAMODE);
					 devConfig.setDR(DATARATE);
					 devConfig.setMODE(DRSCHEME);
					 devConfig.setS_MODE(S_DRSCHEME);
					 devConfig.setPOWER(POWER);
					 devConfig.setLOSTPOINT(LOSTPOINT );
					 devConfig.setSELFADAPT(SELFADAPT);
					 devConfig.setONEOFF(ONEOFF);
					 devConfig.setALREPORT(ALREPORT);
					 devConfig.setGPS(GPS);
					 devConfig.setHB(HEARTBEAT);
					 devConfig.setCRC16(CRC);
					 DevConfigDAO.update(devConfig);
					 logger.info("Success to update the deveui's {} devcfg data!",deveui);
					 result = 0;
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
		     /*sql = "update dev_config set ADR=\""+ADR+"\", RFU=\""+CLAAMODE+"\", DR=\""+DATARATE
				+ "\", MODE=\"" + DRSCHEME + "\",S_MODE=\"" + S_DRSCHEME + "\", POWER=\"" + POWER + "\", LOSTPOINT=\"" + LOSTPOINT 
				+ "\", SELFADAPT=\"" + SELFADAPT + "\", ONEOFF=\"" + ONEOFF + "\",ALREPORT=\"" + ALREPORT + "\", GPS=\"" + GPS +
				"\", HB=\"" + HEARTBEAT + "\",CRC16=\"" + CRC + "\" where deveui=\""+deveui+"\"";*/
			
		}
		return result;
	}
}

