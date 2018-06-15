package com.lans.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.lans.beans.DevParamSetting;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.msghandler.EndDevHBHandler;

/**
 * Servlet implementation class DevCommSetting
 */
@WebServlet(description = "终端通信参数设置", urlPatterns = { "/DevCommSetting.do" })
public class DevCommSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(DevCommSetting.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DevCommSetting() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("receive do get in DevCommSetting.do");
		
		String deveui = request.getParameter("deveui");
		
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");

		String sql = "select * from dev_config where deveui=\""+deveui+"\"";
   		JSONObject jsonRsp = new JSONObject();
   		
		ResultSet rs = db.executeQuery(sql);
		try{
	       	rs.beforeFirst();
	       	if (rs.next()) {
	      		
	      		int value = rs.getInt("MODE");
	      		jsonRsp.put("MODE", value);
	      		
	      		value = rs.getInt("RFU");
	      		jsonRsp.put("RFU", value);
	      		
	      		value = rs.getInt("ADR");
	      		jsonRsp.put("ADR", value);
	      		
	      		value = rs.getInt("DR");
	      		jsonRsp.put("DR", value);
	      		
	      		value = rs.getInt("POWER");
	      		jsonRsp.put("POWER", value);
	}
	       	else {
	      		jsonRsp.put("MODE", "not found");
	      		jsonRsp.put("RFU", "not found");
	      		jsonRsp.put("ADR", "not found");
	      		jsonRsp.put("DR", "not found");
	      		jsonRsp.put("POWER", "not found");
	       	}
	       	rs.close();
	    } catch(SQLException ex) {
	    	logger.error("DevCommSetting doGet query error:" + ex.getMessage());
	}

		logger.info("doGet respond in DevCommSetting.do "+jsonRsp.toString());
		response.getWriter().write(jsonRsp.toString());
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
			result = "['1']"; //Dev not registered.
			response.getWriter().write(result);
			return;
		}
		
		if(((1 << iMODE-1) & devParam.S_DRSCHEME) == 0) //Data rate scheme not support.
		{
			result = "['2']"; 
			response.getWriter().write(result);
			return;
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
				EndDevHBHandler.setEndDevParamStatus(DevEUI, false);
				result = "['0']"; 
				response.getWriter().write(result);
			}
			else
			{
				result = "['3']"; //更新数据库失败
				response.getWriter().write(result);
			}
		}
		else {
			result = "['4']"; //数据没有变化不需要更新数据库
			response.getWriter().write(result);
		}
	}

}
