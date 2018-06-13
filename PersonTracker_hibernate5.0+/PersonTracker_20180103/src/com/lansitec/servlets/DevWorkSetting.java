package com.lansitec.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.beans.DevParamSetting;
import com.lansitec.common.QueryResult;
import com.lansitec.dao.DevConfigDAO;
import com.lansitec.dao.beans.DevConfig;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/DevWorkSetting")
public class DevWorkSetting {
	private Logger logger = LoggerFactory.getLogger(DevWorkSetting.class);
	
	@RequestMapping(value="doGet",method=RequestMethod.GET)
	@ResponseBody
	protected String doGet(HttpServletRequest request,@RequestParam String deveui){
		DevConfig devcfg = null;
		JSONObject jsonRsp = new JSONObject();
		try {
			if(deveui.equals("选择设备")){
			 	logger.error("the deveui {} doesn't exist in dev_cfg_tbl!",deveui);
			 	return "fail";
			}
			devcfg = DevConfigDAO.getDevConfigByDevice(deveui);
			if(null == devcfg){
				logger.error("the deveui {} don't have cfg data! so Fail to get data!",deveui);
				jsonRsp.put("LOSTPOINT", "not found");
	      		jsonRsp.put("SELFADAPT", "not found");
	      		jsonRsp.put("ONEOFF", "not found");
	      		jsonRsp.put("ALREPORT", "not found");
	      		jsonRsp.put("GPS", "not found");
	      		jsonRsp.put("HB", "not found");
			}else{
	      		jsonRsp.put("LOSTPOINT", devcfg.getLOSTPOINT());
	      		jsonRsp.put("SELFADAPT", devcfg.getSELFADAPT());
	      		jsonRsp.put("ONEOFF", devcfg.getONEOFF());
	      		jsonRsp.put("ALREPORT", devcfg.getALREPORT());
	      		jsonRsp.put("GPS", devcfg.getGPS());
	      		jsonRsp.put("HB", devcfg.getHB());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonRsp.toString();
	}
	
	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	protected String doPost(HttpServletRequest request) throws IOException{
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			String usrname = (String) reqSession.getAttribute("usrname");
			if(usrname != null)
			{
				if(usrname.equals("guest"))		
					return null;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null ;
		}
		
		String sLOSTPOINT = request.getParameter("LOSTPOINT");
		String sSELFADAPT = request.getParameter("SELFADAPT");
		String sONEOFF = request.getParameter("ONEOFF");
		String sALREPORT = request.getParameter("ALREPORT");
		String sGPS = request.getParameter("GPS");
		String sHB = request.getParameter("HEARTBEAT");
		String DevEUI = request.getParameter("DEVEUI");
		
		int iLOSTPOINT = Integer.parseInt(sLOSTPOINT);
		int iSELFADAPT = Integer.parseInt(sSELFADAPT);
		int iONEOFF = Integer.parseInt(sONEOFF);
		int iALREPORT = Integer.parseInt(sALREPORT);
		int iGPS = Integer.parseInt(sGPS);
		int iHB = Integer.parseInt(sHB);
		
		String result = null;
		
		DevParamSetting devParam = new DevParamSetting();
		devParam.readDevParams(DevEUI);
		
		//Step2: whether the Node has registered, if not, respond fail.
		if(devParam.CRC == 0)
		{
			return result = "1";
		}
			
		if(iLOSTPOINT != devParam.LOSTPOINT || iSELFADAPT != devParam.SELFADAPT || iONEOFF != devParam.ONEOFF
				||iALREPORT != devParam.ALREPORT ||iGPS != devParam.GPS || iHB != devParam.HEARTBEAT)
		{
			devParam.LOSTPOINT = (byte)iLOSTPOINT;
			devParam.SELFADAPT = (byte)iSELFADAPT;
			devParam.ONEOFF = (byte)iONEOFF;
			devParam.ALREPORT = (byte)iALREPORT;
			devParam.GPS = (short)iGPS;
			QueryResult.OnlineDevCfg(DevEUI, (short)iHB);//上线终端配置心跳
			devParam.HEARTBEAT 	  = (short)iHB;
			int update=1;
			if(devParam.writeDevParams(DevEUI,update) == 0) //更新成功
			{
				result = "0";
			}
			else
			{
				result = "3"; //更新数据库失败
			}
		}
		else {
			result = "4"; //数据没有变化不需要更新数据库
		}
	   return result;
	}
}
