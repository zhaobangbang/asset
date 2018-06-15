package com.lans.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lans.beans.DevParamSetting;
import com.lans.common.QueryResult;
import com.lans.common.UserMgrDevAccess;
import com.lans.controller.networkgw.msghandler.EndDevHBHandler;

/**
 * Servlet implementation class UpdateUserDevWorkSetting
 */
@WebServlet("/UpdateUserDevWorkSetting.do")
public class UpdateUserDevWorkSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUserDevWorkSetting() {
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
		String username = request.getParameter("usrname");
		String sLOSTPOINT = request.getParameter("LOSTPOINT");
		String sSELFADAPT = request.getParameter("SELFADAPT");
		String sONEOFF = request.getParameter("ONEOFF");
		String sALREPORT = request.getParameter("ALREPORT");
		String sGPS = request.getParameter("GPS");
		String sHB = request.getParameter("HEARTBEAT");
		
		int iLOSTPOINT = Integer.parseInt(sLOSTPOINT);
		int iSELFADAPT = Integer.parseInt(sSELFADAPT);
		int iONEOFF = Integer.parseInt(sONEOFF);
		int iALREPORT = Integer.parseInt(sALREPORT);
		int iGPS = Integer.parseInt(sGPS);
		int iHB = Integer.parseInt(sHB);
		String result = null;
		List<String> userDevlist = UserMgrDevAccess.getUserDev(request, username);
		if(userDevlist.size()!=0){
		  for(String devEUI:userDevlist){
		     DevParamSetting devParam = new DevParamSetting();
             devParam.readDevParams(devEUI);
		     devParam.LOSTPOINT = (byte)iLOSTPOINT;
		     devParam.SELFADAPT = (byte)iSELFADAPT;
		     devParam.ONEOFF = (byte)iONEOFF;
		     devParam.ALREPORT = (byte)iALREPORT;
		     devParam.GPS = (short)iGPS;
		     QueryResult.OnlineDevCfg(devEUI, (short)iHB);//上线终端配置心跳
		     devParam.HEARTBEAT 	  = (short)iHB;
		     int update=1;
		     if(devParam.writeDevParams(devEUI,update) == 0) //更新成功
		     {
		    	EndDevHBHandler.setEndDevParamStatus(devEUI, false);
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
