package com.lans.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.DevOpr;
import com.lans.beans.DevicesOperateBean;
import com.lans.beans.ObserverInfoBean;
import com.lans.servlets.DevMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IMsgPreValidator;

public class DevMsgValidator implements IMsgPreValidator {
    private Logger logger = LoggerFactory.getLogger(DevMsgValidator.class);
	@Override
	public boolean isValidMsg(EndDevEnvInfo devInfo, String payload) {
		String eui = devInfo.getEui();
		//Workaround, 如果长度为8，说明是网关心跳消息，继续往下处理
		if(eui.length() == 8){
			return true;//网关
		}			
		else if(eui.length() == 16 )
		{
			//所有tracker相关的设备号格式为004a77021103xxxx(OTAA)或00000000xxxxxxxxx(ABP)
			if(!eui.substring(0, 12).equalsIgnoreCase("004a77021103") &&
					!eui.substring(0, 8).equalsIgnoreCase("00000000"))
				{
					return false;
				}
				if (!ObserverInfoBean.getInstance().devValid(eui)) {
					logger.error("eui {} not belongs to us", eui);
					return false;
				}
			
			DevicesOperateBean operBean = DevicesOperateBean.getInstance();
			String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
	    	operBean.updateLastMsgTime(deveui, new Date());//设备连续一段时间无数据将标记为离线。
	    	operBean.updateOpr(deveui, DevOpr.REG);
	    	return true;
		}
		return false;
	}
}
