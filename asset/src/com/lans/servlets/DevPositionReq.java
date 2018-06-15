package com.lans.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.DevParamSetting;
import com.lans.controller.networkgw.msghandler.CmdBufService;

@WebServlet("/DevPositionReq.do")
public class DevPositionReq extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(DevPositionReq.class);
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		
	   }
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			String usrname = (String) reqSession.getAttribute("usrname");
			if(usrname != null)
			{
				if(usrname.equals("guest"))		
					return;
			}
			else
			{
				return;
			}
		}
		else
		{
			return;
		}
		String parameter =request.getParameter("number");
		int Reg = 0;
		if(null == parameter)//如果未带参数，则默认为位置请求
		{
			Reg = 2;
		}
		else {
			Reg = Integer.valueOf(parameter);		
		}
		
		if(Reg==1){
			String DevEUI = request.getParameter("DEVEUI");
			if(DevEUI == null){
				   logger.info("the DevEUI is {}!",DevEUI);
					String result = "['Fail to acquire the DevEUI!']";
					response.getWriter().write(result);
					return;
			   }
			CmdBufService.getInstance().addReqDevRegistration(DevEUI);
			response.getWriter().write("['Success to register requestion!']");
		}		
		else if(Reg==2){
			String DevEUI = request.getParameter("DEVEUI");
			if(DevEUI == null){
				   logger.info("the DevEUI is {}!",DevEUI);
					String result = "['Fail to acquire the DevEUI!']";
					response.getWriter().write(result);
					return;
			}
			DevParamSetting devParam = new DevParamSetting();
			devParam.readDevParams(DevEUI);
			
			if(devParam.ONEOFF == 0)
			{
				logger.info("ONEOFF {} is not working, so that Failing to ReqDevRestart!",devParam.ONEOFF);
				String result = "['Device is not working in ONEOFF mode!']";
				response.getWriter().write(result);
				return;
			}
			else
			{
				CmdBufService.getInstance().addPosReqCmdOfDev(DevEUI);
				logger.info("Success to PosReqCmdOfDev! ");
				response.getWriter().write("['Success to request position!']");
			}		
		}else if(Reg==3){
			String DevEUI = request.getParameter("DEVEUI");
			if(DevEUI == null){
				   logger.info("the DevEUI is {}!",DevEUI);
					String result = "['Fail to acquire the DevEUI!']";
					response.getWriter().write(result);
					return;
			}
			DevParamSetting devParam = new DevParamSetting();
			devParam.readDevParams(DevEUI);
			CmdBufService.getInstance().addReqDevRestart(DevEUI);
			logger.info("Success to ReqDevRestart! ");
			response.getWriter().write("['Success to reset device!']");;
			
			
		}
	}
}
