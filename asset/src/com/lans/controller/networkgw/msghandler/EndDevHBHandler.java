package com.lans.controller.networkgw.msghandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlSender;
import com.lans.beans.DevParamSetting;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.DLCommandReq;
import com.lans.controller.networkgw.tvmessages.DLDevLoraConfig;
import com.lans.controller.networkgw.tvmessages.DLDevModeConfig;
import com.lans.controller.networkgw.tvmessages.EndDevHB;

import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

import net.sf.json.JSONObject;
class EndDevParamSent{
	public String DevEUI;
	public boolean paramSent;
	public int count;
	public int count1;//用于收到位置数据时发送配置参数的确认
	EndDevParamSent(String DevEUI)
	{
		this.DevEUI = DevEUI;
		this.paramSent = true;
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
		float gwRssi = devInfo.getRssi();
		float gwSnr = devInfo.getSnr();
		
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
			}
			getEndDevParamStatus(deveui).paramSent =true;
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
    	
    	int v = hbMsg.vol;
		int rssi = hbMsg.rssi & 0xff;
		float snr = (float) (hbMsg.snr/100.0);
		
		DevMsgHandler.parseTermStatus(jsonMsg, hbMsg.gpsstatus, hbMsg.vibstatus, hbMsg.chgstatus);
		jsonMsg.element("voltage", v + "%"); 
		if(hbMsg.snr != 0)
			jsonMsg.element("rssi", "-" + rssi + "dbm/snr(" + snr + ")");
		else
			jsonMsg.element("rssi", "-" + rssi + "dbm");
		
		String userdata = "";
		int iUsrData = hbMsg.usrdata&0x0FFFF;
		if(iUsrData != 0)
		{//在中物物联项目中，此字段定义为牛的步行次数
			jsonMsg.element("usrdata", iUsrData);
			userdata = "," + iUsrData + "步";
		}
		
        try {
                if(iUsrData != 0)
        	ZwwlSender.pushSteps(deveui, iUsrData);
			DevMsgHandler.updateToObserver(deveui, jsonMsg);
			DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        //check if pending command request buffered
        String dev = devInfo.getEui(); //NOTES: dev and deveui may be different 
        if (CmdBufService.getInstance().cmdOfDevExist(deveui)) {
        	IEndDevItfTV command = CmdBufService.getInstance().getHeadCmdOfDev(deveui);
        	
        	l3.sendTVMsgToEndDev(dev, (byte)21, command);
        	
        	if(((DLCommandReq)command).getCommand() == 3)//重启终端
        	    CmdBufService.getInstance().rmHeadCmdOfDev(dev);
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
        
	    java.util.Date date = new java.util.Date();
	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "update dev_list_tbl set battery='"+ battStatus + "', rssi='-"+ rssi+ "', snr='"+ snr+ "', statustime='"+ shortDF.format(date)+"' where deveui='"+deveui+"'";
		
		DataBaseMgr db = DataBaseMgr.getInstance();
		int rowAffected = db.executeUpdate(sql);

		if (0 == rowAffected) {
			logger.warn("EndDevHBHandler: Fail to update battery/rssi. {}", deveui);
		}
		
		sql = "select * from dev_list_tbl where deveui=\""+deveui+"\"";
	    String owner = null;
		   	
	    ResultSet rs = db.executeQuery(sql);
	   	try{
	   			rs.beforeFirst();
	   			if (rs.next()) {
	   				owner = rs.getString("owner");
	   			}
	   			else
	   			{
	   				logger.error("EndDevHBHandler(),无此设备号：{}", deveui);
	   				return;
	   			}
	   		  }catch(SQLException ex) {
	   			logger.error("EndDevHBHandler:"+ ex.getMessage());
				return;
	   			}

	    sql = "insert into status_record_tbl(owner,deveui,battery,gwrssi, gwsnr,rssi,snr, gps,vib,time) values('"+owner + "','" + deveui+
				       "','" + v + "','" + gwRssi + "','" + gwSnr +  "','-" + rssi + "','" + snr + "','" + jsonMsg.getString("gps") + userdata + 
						"','"+ hbMsg.vibstatus + "','" + shortDF.format(date) + "')";
	    rowAffected = db.executeUpdate(sql);

	    if(rowAffected == 0)
	    {
	    	logger.warn("{} :Fail to add status record in database.", deveui); 
	    }
	}
}
