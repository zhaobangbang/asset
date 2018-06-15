package com.lans.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lans.beans.DevParamSetting;
import com.lans.beans.DevicesOperateBean;
import com.lans.common.UserMgrDevAccess;
import com.lans.controller.networkgw.msghandler.EndDevHBHandler;

/**
 * Servlet implementation class UpdateUserDevCommSetting
 */
@WebServlet("/UpdateUserDevCommSetting.do")
public class UpdateUserDevCommSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUserDevCommSetting() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				//Step1: Parse the parameters

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
				String username = request.getParameter("usrname");
				String sADR = request.getParameter("ADR");
				String sMODE = request.getParameter("DRSCHEME");
				String sRFU = request.getParameter("CLAAMODE");
				String sDR = request.getParameter("DATARATE");
				String sPOWER = request.getParameter("POWER");
				
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
				List<String> userDevList = UserMgrDevAccess.getUserDev(request,username);
				if(userDevList.size()!=0){
					 for (String devEUI : userDevList) {
						      DevParamSetting devParam = new DevParamSetting();
						      devParam.readDevParams(devEUI);
						      devParam.ADR = (byte)iADR;
						      devParam.DRSCHEME = (byte)iMODE;
						      devParam.CLAAMODE = (byte)iRFU;
						      devParam.DATARATE = (byte)iDR;
						      devParam.POWER  = (byte)iPOWER;
						      int update=1;
						      if(devParam.writeDevParams(devEUI,update) == 0) //update success
						      {
						    	  EndDevHBHandler.setEndDevParamStatus(devEUI, false);
						    	  DevicesOperateBean devperate = DevicesOperateBean.getInstance();
						    	  devperate.updateLastMsgTime(devEUI, new Date());
							      result = "['0']"; 
						      }
						      else 
						      {   update=0;
						          devParam.writeDevParams(devEUI,update);
							      result = "['0']"; 
						      }
					 }
						 response.getWriter().write(result); 
				 }
		          else{
			          result = "['4']"; //username dosen't have deveui
			          response.getWriter().write(result); 
		            } 
					 
						 
       }
	}


