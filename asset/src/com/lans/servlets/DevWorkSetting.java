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

import com.lans.beans.DevParamSetting;
import com.lans.common.DataBaseMgr;
import com.lans.common.QueryResult;
import com.lans.controller.networkgw.msghandler.EndDevHBHandler;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class DevWorkSetting
 */
@WebServlet("/DevWorkSetting.do")
public class DevWorkSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(DevWorkSetting.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DevWorkSetting() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String deveui = request.getParameter("deveui");
		
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");

	   	logger.info("doGet in DevWorkSetting.do "+deveui);
		String sql = "select * from dev_config where deveui=\""+deveui+"\"";
   		JSONObject jsonRsp = new JSONObject();
   		
		ResultSet rs = db.executeQuery(sql);
		try{
	       	rs.beforeFirst();
	       	if (rs.next()) {
	      		
	      		int value = rs.getInt("LOSTPOINT");
	      		jsonRsp.put("LOSTPOINT", value);
	      		
	      		value = rs.getInt("SELFADAPT");
	      		jsonRsp.put("SELFADAPT", value);
	      		
	      		value = rs.getInt("ONEOFF");
	      		jsonRsp.put("ONEOFF", value);
	      		
	      		value = rs.getInt("ALREPORT");
	      		jsonRsp.put("ALREPORT", value);
	      		
	      		value = rs.getInt("GPS");
	      		jsonRsp.put("GPS", value);
	      		
	      		value = rs.getInt("HB");
	      		jsonRsp.put("HB", value);
	       	}
	       	else {
	      		jsonRsp.put("LOSTPOINT", "not found");
	      		jsonRsp.put("SELFADAPT", "not found");
	      		jsonRsp.put("ONEOFF", "not found");
	      		jsonRsp.put("ALREPORT", "not found");
	      		jsonRsp.put("GPS", "not found");
	      		jsonRsp.put("HB", "not found");
	       	}
	       	rs.close();
	    } catch(SQLException ex) {
	    	logger.error("DevWorkSetting doGet query error:" + ex.getMessage());
	    }

	   	logger.info("doGet respond in DevWorkSetting.do "+jsonRsp.toString());
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
			result = "['1']"; //Dev not registered.
			response.getWriter().write(result);
			return;
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
