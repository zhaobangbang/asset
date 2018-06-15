package com.lans.controller.networkgw.msghandler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.DevParamSetting;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.DLDevLoraConfig;
import com.lans.controller.networkgw.tvmessages.DLDevModeConfig;
import com.lans.controller.networkgw.tvmessages.DLDevRegResult;
import com.lans.controller.networkgw.tvmessages.EndDevReg;
import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

import net.sf.json.JSONObject;

public class EndDevRegHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevRegHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevRegHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}

	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_REG) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevReg regMsg = (EndDevReg) upMsg;
		//check if the dev is registered to our system
		byte result = 0;
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		
		DevParamSetting devParam = new DevParamSetting();
		devParam.readDevParams(deveui);
		
		JSONObject jsonMsg = new JSONObject();
    	jsonMsg.element("DevEUI", deveui);
    	jsonMsg.element("msgType", "REG");
    	   	
        try {
			DevMsgHandler.updateToObserver(deveui, jsonMsg);
			DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		if(devParam.CRC == 0 || devParam.S_DRSCHEME != regMsg.smode) //First time to register or supported mode changed.
		{
			int update=0;
			if(devParam.CRC != 0)
				update = 1;//¸üÐÂ
			
			devParam.ADR = regMsg.adr;
			devParam.CLAAMODE = regMsg.rfu1;
			devParam.DATARATE = regMsg.dr;
			devParam.DRSCHEME = regMsg.mode;
			devParam.GPS = regMsg.gps;
			devParam.HEARTBEAT = regMsg.hb;
			devParam.LOSTPOINT = regMsg.lostpoint;
			devParam.ONEOFF = regMsg.oneoff;
			devParam.ALREPORT = regMsg.alreport;
			devParam.POWER = regMsg.power;
			devParam.S_DRSCHEME = regMsg.smode;
			devParam.SELFADAPT = regMsg.selfadapt;
			devParam.CRC = regMsg.crc;
			
			if(devParam.writeDevParams(deveui,update) != 0)
			{
				logger.error("Fail to register, update database error({})", deveui);
				result = 3; 
			}
		}
		else
		{
			if(regMsg.crc != devParam.CRC)//Params changed
			{
				byte[] params = new byte[20];
				int len = 0;
				
				DLDevRegResult rslt = new DLDevRegResult(result);
				byte[] bRslt = rslt.getBytes();
				System.arraycopy(bRslt, 0, params, 0, bRslt.length);
				len += bRslt.length;
				
				if(devParam.ADR != regMsg.adr || devParam.CLAAMODE != regMsg.rfu1 || devParam.DATARATE != regMsg.dr ||
					devParam.DRSCHEME != regMsg.mode || devParam.POWER != regMsg.power || devParam.S_DRSCHEME != regMsg.smode)
				{
					DLDevLoraConfig devLora = new DLDevLoraConfig(devParam.ADR, devParam.CLAAMODE, devParam.DATARATE,
																(byte)0, devParam.DRSCHEME, devParam.POWER);
					byte[] bDevLora = devLora.getBytes();
					int lenDevLora = bDevLora.length;
					System.arraycopy(bDevLora, 0, params, len, lenDevLora);
					len += bDevLora.length;
				}
				
				if(devParam.GPS != regMsg.gps || devParam.HEARTBEAT != regMsg.hb || devParam.LOSTPOINT != regMsg.lostpoint ||
						devParam.ONEOFF != regMsg.oneoff || devParam.SELFADAPT != regMsg.selfadapt)
				{
					DLDevModeConfig devMode = new DLDevModeConfig(devParam.LOSTPOINT, devParam.SELFADAPT, devParam.ONEOFF,
							                                      devParam.ALREPORT,devParam.GPS, (byte) devParam.HEARTBEAT);
					byte[] bDevMode = devMode.getBytes();
					int lenDevMode = bDevMode.length;
					System.arraycopy(bDevMode, 0, params, len, lenDevMode);
					len += bDevMode.length;
				}
				
				byte[] sentParams = new byte[len];
				System.arraycopy(params, 0, sentParams, 0, len);
				l3.sendRawBytesToEndDev(devInfo.getEui(), (byte) 21, sentParams);
				return;
			}
			else
			{
				result = 0;
			}
		}
		//reply with result
		DLDevRegResult rslt = new DLDevRegResult(result);
		l3.sendTVMsgToEndDev(devInfo.getEui(), (byte) 21, rslt);

		//record the device information
	}

}
