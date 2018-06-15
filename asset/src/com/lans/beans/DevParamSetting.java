package com.lans.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.CRC16;
import com.lans.common.DataBaseMgr;

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
	   	DataBaseMgr db = DataBaseMgr.getInstance();

	   	logger.info("read dev params in DevParamSetting\n");
		String sql = "select * from dev_config where deveui = \"" + deveui + "\"";
   		
		ResultSet rs = db.executeQuery(sql);
		try{
	       	rs.beforeFirst();
	       	if (rs.next()) {
	      		ADR = rs.getByte("ADR");
	      		CLAAMODE = rs.getByte("RFU");
	      		DATARATE = rs.getByte("DR");
	      		DRSCHEME = rs.getByte("MODE");
	      		S_DRSCHEME = rs.getByte("S_MODE");
	      		POWER = rs.getByte("POWER");
	      		LOSTPOINT = rs.getByte("LOSTPOINT");
	      		SELFADAPT = rs.getByte("SELFADAPT");
	      		ONEOFF = rs.getByte("ONEOFF");
	      		ALREPORT = rs.getByte("ALREPORT");
	      		GPS = rs.getShort("GPS");
	      		HEARTBEAT = rs.getShort("HB");
	      		CRC = rs.getShort("CRC16");
	       	}
	       	else {

	       	}
	    } catch(SQLException ex) {
	    	logger.warn("DevParamSetting query error:" + ex.getMessage());
	    } 
	}
	/*
	 * update 0:²åÈë 1£º¸üÐÂ
	 */
	public int writeDevParams(String deveui,int update)
	{
		byte[] params = new byte[7];
		params[0] =  (byte) (((ADR << 3) & 0x8) | (DRSCHEME & 0x7)) ;
		params[1] = S_DRSCHEME;
		params[2] = (byte) (((POWER << 3) & 0xF8) | (CLAAMODE & 0x7));
		params[3] = (byte) (((DATARATE << 4) & 0xF0) | ((LOSTPOINT << 3) & 0x8) | ((SELFADAPT << 2) & 0x4) | ((ONEOFF << 1) & 0x2)|(ALREPORT) & 0x1) ;
		params[4] = (byte)((GPS >> 8)  & 0xFF);
		params[5] = (byte)(GPS & 0xFF);
		params[6] = (byte)HEARTBEAT;
		
		CRC = (short)CRC16.calcCrc16(params);
		
		String sql = null;
		if(update == 0){
			   sql ="insert into dev_config (deveui,ADR,RFU,DR,MODE,S_MODE,POWER,LOSTPOINT,SELFADAPT,ONEOFF,ALREPORT,GPS,HB,CRC16) values('"+deveui+"','"+ADR+"','"+CLAAMODE+"','"+DATARATE
					    +"','"+DRSCHEME +"','"+S_DRSCHEME +"','"+POWER+"','"+LOSTPOINT+"','"+SELFADAPT+"','"+ONEOFF+"','"+ALREPORT+"','"+GPS
					    +"','"+HEARTBEAT+"','"+CRC+"')"; 

		}
		if(update == 1){
		     sql = "update dev_config set ADR=\""+ADR+"\", RFU=\""+CLAAMODE+"\", DR=\""+DATARATE
				+ "\", MODE=\"" + DRSCHEME + "\",S_MODE=\"" + S_DRSCHEME + "\", POWER=\"" + POWER + "\", LOSTPOINT=\"" + LOSTPOINT 
				+ "\", SELFADAPT=\"" + SELFADAPT + "\", ONEOFF=\"" + ONEOFF + "\",ALREPORT=\"" + ALREPORT + "\", GPS=\"" + GPS +
				"\", HB=\"" + HEARTBEAT + "\",CRC16=\"" + CRC + "\" where deveui=\""+deveui+"\"";
		}
		logger.info("write dev params in DevParamSetting. sql {}\n", sql);
		DataBaseMgr db = DataBaseMgr.getInstance();
		int rowAffected = db.executeUpdate(sql);

		if (0 == rowAffected) {
			return 1;
		}
		else {
			return 0;
		}
	}
}

