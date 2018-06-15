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
		//Workaround, �������Ϊ8��˵��������������Ϣ���������´���
		if(eui.length() == 8){
			return true;//����
		}			
		else if(eui.length() == 16 )
		{
			//����tracker��ص��豸�Ÿ�ʽΪ004a77021103xxxx(OTAA)��00000000xxxxxxxxx(ABP)
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
	    	operBean.updateLastMsgTime(deveui, new Date());//�豸����һ��ʱ�������ݽ����Ϊ���ߡ�
	    	operBean.updateOpr(deveui, DevOpr.REG);
	    	return true;
		}
		return false;
	}
}
