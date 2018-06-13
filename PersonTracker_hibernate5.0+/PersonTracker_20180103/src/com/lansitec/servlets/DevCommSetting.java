package com.lansitec.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lansitec.beans.DevParamSetting;
import com.lansitec.dao.DevConfigDAO;
import com.lansitec.dao.beans.DevConfig;

//终端通信参数设置
@Controller
@RequestMapping("/DevCommSetting")
public class DevCommSetting {
	private Logger logger = LoggerFactory.getLogger(DevCommSetting.class);
	@RequestMapping(value="doGet",method=RequestMethod.GET)
	@ResponseBody
	protected String doGet(@RequestParam String deveui){
		logger.info("receive do get in DevCommSetting.do");
		DevConfig devConfig = null;
		JSONObject jsonRsp = new JSONObject();
		try {
			 if(deveui.equals("选择设备")){
			 	logger.error("the deveui {} doesn't exist in dev_cfg_tbl!",deveui);
			 	return "fail";
			 }
		     devConfig = DevConfigDAO.getDevConfigByDevice(deveui);
		     if(null == devConfig){
		    	 logger.error("Fail to get the deveui's {} cfgdata!",deveui);
		    	 jsonRsp.put("MODE", "not found");
		      	 jsonRsp.put("RFU", "not found");
		      	 jsonRsp.put("ADR", "not found");
		      	 jsonRsp.put("DR", "not found");
		      	 jsonRsp.put("POWER", "not found");
		    
		     }else{
	      		jsonRsp.put("MODE", devConfig.getMODE());
	      		jsonRsp.put("RFU", devConfig.getRFU());
	      		jsonRsp.put("ADR", devConfig.getADR());
	      		jsonRsp.put("DR", devConfig.getDR());
	      		jsonRsp.put("POWER", devConfig.getPOWER());
		     }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonRsp.toString();
	}
	
	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	protected String doPost(HttpServletRequest  request){
		//Step1: Parse the parameters
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
			return null;
		}
		
		String sADR = request.getParameter("ADR");
		String sMODE = request.getParameter("DRSCHEME");
		String sRFU = request.getParameter("CLAAMODE");
		String sDR = request.getParameter("DATARATE");
		String sPOWER = request.getParameter("POWER");
		String DevEUI = request.getParameter("DEVEUI");
		
		if(sRFU.equals(""))
			sRFU="0";
		
		if(sDR.equals(""))
			sDR="0";
		
		int iADR = Integer.parseInt(sADR);
		int iMODE = Integer.parseInt(sMODE);
		int iRFU = Integer.parseInt(sRFU);
		int iDR = Integer.parseInt(sDR);
		int iPOWER = Integer.parseInt(sPOWER);
		
		String result = null;
				
		DevParamSetting devParam = new DevParamSetting();
		devParam.readDevParams(DevEUI);
		
		//Step2: whether the Node has registered, if not, respond fail.
		if(devParam.CRC == 0)
		{
			return result = "1"; //Dev not registered.
		}
		
		if(((1 << iMODE-1) & devParam.S_DRSCHEME) == 0) //Data rate scheme not support.
		{
			return result = "2"; 
		}
		
		if(iADR != devParam.ADR || iMODE != devParam.DRSCHEME || iRFU != devParam.CLAAMODE
				|| iDR != devParam.DATARATE || iPOWER != devParam.POWER)
		{
			devParam.ADR = (byte)iADR;
			devParam.DRSCHEME = (byte)iMODE;
			devParam.CLAAMODE = (byte)iRFU;
			devParam.DATARATE = (byte)iDR;
			devParam.POWER 	  = (byte)iPOWER;
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
