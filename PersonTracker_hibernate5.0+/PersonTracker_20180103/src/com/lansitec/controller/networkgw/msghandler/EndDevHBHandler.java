package com.lansitec.controller.networkgw.msghandler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;
import com.lansitec.beans.DevParamSetting;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.controller.networkgw.tvmessages.DLDevLoraConfig;
import com.lansitec.controller.networkgw.tvmessages.DLDevModeConfig;
import com.lansitec.controller.networkgw.tvmessages.EndDevHB;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.LogRecordDAO;
import com.lansitec.dao.StatusRecordDAO;
import com.lansitec.dao.WarnRecordDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.LogRecord;
import com.lansitec.dao.beans.StatusRecord;
import com.lansitec.dao.beans.WarnRecord;
import com.lansitec.enumlist.LogObj;
import com.lansitec.enumlist.LogType;
import com.lansitec.enumlist.WarnType;
import com.lansitec.servlets.DevMsgHandler;

import net.sf.json.JSONObject;
class EndDevParamSent{
	public String DevEUI;
	public boolean paramSent;
	public int count;
	public int count1;//用于收到位置数据时发送配置参数的确认
	EndDevParamSent(String DevEUI)
	{
		this.DevEUI = DevEUI;
		this.paramSent = false;
		this.count = 0;
		this.count1 = 0;
	}
}
public class EndDevHBHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevHBHandler.class);
	IGateWayConnLayer3 l3;
	public static Set<EndDevParamSent> DevParams = new HashSet<EndDevParamSent>();
	
	public EndDevHBHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_HB) {
			return true;
		}

		return false;
	}


	
	public static boolean devExist(String dev){
		for(EndDevParamSent curDev: DevParams){
			if(curDev.DevEUI.equals(dev))
				return true;
		}
		return false;
	}
	
	public static EndDevParamSent getEndDevParamStatus(String DevEUI)
	{
		if(!devExist(DevEUI))
		{
			EndDevParamSent devParam = new EndDevParamSent(DevEUI);
			DevParams.add(devParam);
			return devParam;
		}
		else
		{
			for(EndDevParamSent curDev: DevParams){
				if(curDev.DevEUI.equals(DevEUI))
					return curDev;
			}
		}
		
		return null;
	}
	public static void setEndDevParamStatus(String DevEUI, boolean paramSent)
	{
		if(!devExist(DevEUI))
		{
			EndDevParamSent devParam = new EndDevParamSent(DevEUI);
			devParam.paramSent = paramSent;
			DevParams.add(devParam);
		}
		else
		{
			for(EndDevParamSent curDev: DevParams){
				if(curDev.DevEUI.equals(DevEUI))
					curDev.paramSent = paramSent;
			}
		}
	}
	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		
		EndDevHB hbMsg = (EndDevHB)upMsg;
		DevParamSetting devParam = new DevParamSetting();
		devParam.readDevParams(deveui);
		
		if(getEndDevParamStatus(deveui).paramSent == false)
		{		
		getEndDevParamStatus(deveui).count = 0;
		
		if(devParam.CRC != 0 && hbMsg.crc != devParam.CRC)//Params changed
		{
			byte[] params = new byte[20];
			int len = 0;		

			DLDevLoraConfig devLora = new DLDevLoraConfig(devParam.ADR, devParam.CLAAMODE, devParam.DATARATE,
														(byte)0, devParam.DRSCHEME, devParam.POWER);
			byte[] bDevLora = devLora.getBytes();
			System.arraycopy(bDevLora, 0, params, len, bDevLora.length);
			len += bDevLora.length;		

			DLDevModeConfig devMode = new DLDevModeConfig(devParam.LOSTPOINT, devParam.SELFADAPT, devParam.ONEOFF,
											             devParam.ALREPORT,devParam.GPS, (byte)devParam.HEARTBEAT);
			byte[] bDevMode = devMode.getBytes();
			System.arraycopy(bDevMode, 0, params, len, bDevMode.length);
			len += bDevMode.length;
			
			byte[] sentParams = new byte[len];
			System.arraycopy(params, 0, sentParams, 0, len);
			l3.sendRawBytesToEndDev(devInfo.getEui(), (byte) 21, sentParams);
			getEndDevParamStatus(deveui).paramSent =true;
		}
		}
		
		//每收到2次心跳消息发一次配置请求
		if(getEndDevParamStatus(deveui).paramSent)
		{
			if(hbMsg.crc != devParam.CRC)//Params changed
			{
				getEndDevParamStatus(deveui).count++;
			
				if(getEndDevParamStatus(deveui).count >= 2)
				{
					getEndDevParamStatus(deveui).paramSent = false;
					getEndDevParamStatus(deveui).count = 0;
				}
			}
		}
		//提取其它数据
		JSONObject jsonMsg = new JSONObject();
    	jsonMsg.element("DevEUI", deveui);
    	jsonMsg.element("msgType", "HB");
    	int batteryLow = 20;
    	int v = hbMsg.vol;
    	if(v < batteryLow ){
    		//SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		WarnRecord warnRecord = new WarnRecord(deveui, WarnType.valueOf("电量不足"), "电量不足，需充电！", LocalDateTime.now(), false);
    		WarnRecordDAO.create(warnRecord);
    		logger.info("get the warnInfo of the deveui {} warnType {} ",deveui,WarnType.valueOf("电量不足"));
    	}else{
    		try {
				WarnRecord warnRecord = WarnRecordDAO.getWarnRecordByDeveui(deveui);
				if(null != warnRecord){
					if(!warnRecord.isWarn_on()){
						warnRecord.setWarn_on(true);
						warnRecord.setDescription("电量值不在告警范围内，告警取消！");
						warnRecord.setWarn_etime(LocalDateTime.now());
						WarnRecordDAO.update(warnRecord);
						logger.info("Canceling the warnInfo of the deveui {}!",deveui);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		int rssi = hbMsg.rssi & 0xff;
		String devRssi = null;
		float snr = (float) (hbMsg.snr/100.0);
		
		DevMsgHandler.parseTermStatus(jsonMsg, hbMsg.gpsstatus, hbMsg.vibstatus, hbMsg.chgstatus);
		jsonMsg.element("voltage", v + "%"); 
		if(hbMsg.snr != 0){
			jsonMsg.element("rssi", "-" + rssi + "dbm/snr(" + snr + ")");
		    devRssi = "-" + rssi + "dbm/snr(" + snr + ")";
		}
		else{
			jsonMsg.element("rssi", "-" + rssi + "dbm");
			devRssi = "-" + rssi + "dbm";
		}
		
		String userdata = "";
		int iUsrData = hbMsg.usrdata&0x0FFFF;
		if(iUsrData != 0)
		{//在中物物联项目中，此字段定义为牛的步行次数
			jsonMsg.element("usrdata", iUsrData);
			userdata = "," + iUsrData + "步";
		}
		
        try {
        	 DevMsgHandler.updateToMapId(deveui, jsonMsg);
			 //DevMsgHandler.updateToObserver(deveui, jsonMsg);
			 DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        //check if pending command request buffered
        String dev = devInfo.getEui(); //NOTES: dev and deveui may be different 
        if (CmdBufService.getInstance().cmdOfDevExist(deveui)) {
        	IEndDevItfTV command = CmdBufService.getInstance().getHeadCmdOfDev(deveui);
        	
        	l3.sendTVMsgToEndDev(dev, (byte)21, command);
        }
        
        String battStatus;
        if(hbMsg.chgstatus == 5)
        {
        	battStatus = "充电中";
        }
        else
        {
        	battStatus = v + "%";
        }
        
        LocalDateTime currentTime = LocalDateTime.now();
	    DevInfo devInfoData = null;
	    int rowAffected = 0;
	    String owner = null;
	    try {
			 devInfoData = DevInfoDAO.getDevInfoByDeveui(deveui);
			 if(null == devInfoData){
				 logger.error("Fail to get data by the deveui {} in dev_info_tbl!",deveui);
			 }else{
				 //update data in dev_info_tbl 
				 devInfoData.setBattery(battStatus);
				 devInfoData.setRssi((short)rssi);
				 devInfoData.setSnr(snr);
				 devInfoData.setStatustime(currentTime); 
				 DevInfoDAO.update(devInfoData);
				 
				 //add data in status_record_tbl
				 //hbMsg.showTV();
				// byte status = hbMsg.vibstatus;
				 //logger.info("devstatus {}",status);
				 StatusRecord statusRecord = new StatusRecord(deveui, (byte)v, Short.parseShort("-"+Integer.toString(rssi)), (byte)snr, hbMsg.vibstatus, LocalDateTime.now());
				 StatusRecordDAO.create(statusRecord);
				 
				 rowAffected = 1;
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


		if (0 == rowAffected) {
			logger.warn("EndDevHBHandler: Fail to update battery/rssi. {}", deveui);
		}
		
		owner = devInfoData.getOwner();
		if((null == owner) || (owner.equals("")) ){
			logger.info("Fail to get owner {} by deveui {} in dev_info_tbl!",owner,deveui);
		}else{
			LogRecord logRecord = new LogRecord(owner, LogObj.终端, LogType.修改, owner, "修改纪录", LocalDateTime.now());
			LogRecordDAO.create(logRecord);
			rowAffected = 1;
		}

	    if(rowAffected == 0)
	    {
	    	logger.warn("{} :Fail to add status record in database.", deveui); 
	    }
	}
}
