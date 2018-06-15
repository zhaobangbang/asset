package com.lans.servlets;

import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.DevOpr;
import com.lans.beans.DevicesOperateBean;
import com.lans.beans.GpsNodeStatusBean;
import com.lans.common.DataBaseMgr;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class GetRegisterDevices
 */
@WebServlet("/GetRegisterDevices.do")
public class GetRegisterDevices extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(GetRegisterDevices.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetRegisterDevices() {
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
		String type = request.getParameter("type");
		String usrname = request.getParameter("usrname");
		String mapid = request.getParameter("mapid");
	   	String result = null;
	   	ResultSet rs = null;
		if(type.equals("ALL"))
		{
			ServletContext ctx = request.getServletContext();
			DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
			JSONObject jObject = new JSONObject();
			
			String sql = "select * from dev_list_tbl where deveui in (select deveui from dev_usr_tbl where username = '" + usrname + "') and postype = '室外'";	
			
			result = "[";
			//获得室外型终端
			try{
				rs = db.executeQuery(sql);
				if(null != rs){
				while(rs.next())
				{
					result += "'" + rs.getString("deveui") + "',";
				}
					rs.close();
				}

				result += "]";

				jObject.element("outdoor", result);
	  			}catch(SQLException ex){
	  				logger.error("executeQuery:" + ex.getMessage());
	  				result=null;
	  			}
			
           sql = "select * from dev_list_tbl where deveui in (select deveui from dev_usr_tbl where username = '" + usrname + "') and (postype = '室内三点' or postype = '室内区域')";	
			
			result = "[";
			//获得室内型终端
			try{
				rs = db.executeQuery(sql);
				if(null != rs){
					while(rs.next())
					{
						result += "'" + rs.getString("deveui") + "',";
					}
				rs.close();
				}
				result += "]";

				jObject.element("indoor", result);
	  			}catch(SQLException ex){
	  				logger.error("executeQuery:" + ex.getMessage());
	  				result=null;
	  			}

			sql = "select * from dev_list_tbl where deveui in (select deveui from dev_usr_tbl where username = '" + usrname + "') and postype like '室内外%'";	
			
			result = "[";
			//获得室内外型终端
			try{
				rs = db.executeQuery(sql);
				if(null != rs){
					while(rs.next())
					{
						result += "'" + rs.getString("deveui") + "',";
					}	
					rs.close();
				}

				result += "]";

				jObject.element("outin", result);
	  			}catch(SQLException ex){
	  				logger.error("executeQuery:" + ex.getMessage());
	  				result=null;
	  			}
			
			result = jObject.toString();
		}
		if(type.equals("GW"))
		{
			ServletContext ctx = request.getServletContext();
			DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
			JSONObject jObject = new JSONObject();
			
			String sql = "select * from dev_list_tbl where deveui in (select deveui from dev_usr_tbl where username = '" + usrname + "') and postype = '网关'";	
			
			result = "[";
			//获得室外型终端
			try{
				rs = db.executeQuery(sql);
				if(null != rs){
					while(rs.next())
					{
						result += "'" + rs.getString("deveui") + "',";
					}	
					rs.close();
				}

				result += "]";

				jObject.element("gateway", result);
	  			}catch(SQLException ex){
	  				logger.error("executeQuery:" + ex.getMessage());
	  				result=null;
	  			}      
			
     			result = jObject.toString();
		}
		else if(type.equals("REG"))
		{
			ServletContext ctx = request.getServletContext();
		   	DevicesOperateBean db = (DevicesOperateBean)ctx.getAttribute("DevOper");
			result = db.getRegisterDev(usrname);
		}
		else if(type.equals("STE"))
		{
			ServletContext ctx = request.getServletContext();
		   	DevicesOperateBean db = (DevicesOperateBean)ctx.getAttribute("DevOper");
		   	DevOpr devStatus = db.getDevOpr(usrname);
		   	String status = null;

		   	if(DevOpr.LOCATE == devStatus) 
		   		status = "LOCATE";
		   	else if(DevOpr.UNLOCATE == devStatus)
		   		status = "UNLOCATE";
		   	else if(DevOpr.REG == devStatus)
		   		status = "REG";
		   	else if(DevOpr.DATARCV == devStatus)
		   		status = "DATARCV";
		   	else if(DevOpr.OFFLINE == devStatus)
		   		status = "OFFLINE";
		   		
		   	result = "['" + status + "']";
		   	
		}
		else if(type.equals("POS")) //最后一次位置及状态
		{ //默认请求室外设备位置
			ServletContext ctx = request.getServletContext();
			DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
			System.out.println(new Date().toString());
			String sql = "select * from dev_list_tbl where deveui in (select deveui from dev_usr_tbl where username = '" + usrname + "') and (postype = '室内外三点' or postype = '室内外区域' or postype = '室外') order by postype";
			
			if(null != mapid)
			{//请求室内型设备位置
				sql = "select * from dev_list_tbl where deveui in (select deveui from dev_usr_tbl where username = '" + usrname + "') and (postype = '室内外三点' or postype = '室内外区域' or postype = '室内') and map_id='" + mapid + "'";
			}
			GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
			JSONArray jArray = new JSONArray();
			StringBuffer devList= new StringBuffer();
			try{
				rs = db.executeQuery(sql);
				if(null != rs)
				{
				rs.beforeFirst();

				while(rs.next())
				{
					String devId = rs.getString("deveui");
					devList.append("\'"+devId+"\',");
					JSONObject jsonMsg = new JSONObject();
					jsonMsg.element("deveui", devId);
					jArray.add(jsonMsg);
			   }
			   rs.close();
				}
				
			   if(devList.length() > 1)
			   {
				   devList.deleteCharAt(devList.length()-1);
			   }
			   else{
				    result = "[]";
					response.setContentType("text/html; charset=utf-8");
					response.getWriter().write(result);
					return;
			   }
			}catch(SQLException ex){
  				logger.error("executeQuery:" + ex.getMessage());
  				result=null;
  			}
			System.out.println(new Date().toString());
			String strDevList = devList.toString();
			//get x,y
			sql = "select deveui,baidulati,baidulong,time from gps_tbl where id in (select MAX(id) from gps_tbl where deveui in (" +strDevList + ") group by deveui)";	   		
			rs = db.executeQuery(sql);
			try{
		       	rs.beforeFirst();
		       	while(rs.next()) {
		       		String devId = rs.getString("deveui");
		       		float x = posBean.getFirstPosX(devId);
					float y = posBean.getFirstPosY(devId);
					if(x < 0.01 && y < 0.01 && x > -0.01 && y > -0.01)
					{
		      		    x = rs.getFloat("baidulong");
		      		    y = rs.getFloat("baidulati");
					}
					for(int i=0; i<jArray.size(); i++)
					{
						JSONObject jObj = jArray.getJSONObject(i);
						if(devId.equals(jObj.getString("deveui")))
						{
							jObj.element("X", x);
							jObj.element("Y", y);
							break;
						}
					}
		       	}
		       	rs.close();
			 } catch(SQLException ex) {
			    	logger.error("GetRegisterDevices doPost Get POS error:" + ex.getMessage());
			}
			System.out.println(new Date().toString());
			sql = "select * from (select * from status_record_tbl order by id desc)T where deveui in(" + strDevList + ") group by deveui";	   		
	   		int battery = 0;
	   		int rssi = 0;
	   		String gps = "";
	   		int vib = 0;
	   		float snr = 0;
	   		String time = "";
			rs = db.executeQuery(sql);
			try{
		       	rs.beforeFirst();
		       	while(rs.next()) {
		       		String devId = rs.getString("deveui");
					for(int i=0; i<jArray.size(); i++)
					{
						JSONObject jObj = jArray.getJSONObject(i);
						if(devId.equals(jObj.getString("deveui")))
						{
				      		battery = rs.getInt("battery");
				      		rssi = rs.getInt("rssi");
				      		gps = rs.getString("gps");
				      		snr = rs.getFloat("snr");
				      		vib = rs.getInt("vib");
				      		time = rs.getString("time");
				     		if(vib==0)
				     		{
				     			jObj.element("vib", "静止");
				     		}
				     		else
				     		{
				     			jObj.element("vib", "运动强度:"+vib);
				     		}

				      		jObj.element("voltage", battery+"%");
							jObj.element("gps", gps);
							jObj.element("rssi", rssi+"dbm");
							jObj.element("snr", snr);
							jObj.element("time", time);
							
							break;
						}
					}
		       	}
		       	rs.close();
			 } catch(SQLException ex) {
			    	logger.error("GetRegisterDevices doPost Get device status error:" + ex.getMessage());
			}
			System.out.println(new Date().toString());
			sql = "select deveui,battery from dev_list_tbl where deveui in(" + strDevList + ")";	   		
	   		String batStatus="";
	   		
	   		rs = db.executeQuery(sql);
			try{
				rs.beforeFirst();
		       	while(rs.next()) {
		       		String devId = rs.getString("deveui");
					for(int i=0; i<jArray.size(); i++)
					{
						JSONObject jObj = jArray.getJSONObject(i);
						if(devId.equals(jObj.getString("deveui")))
						{
				      		batStatus = rs.getString("battery");
				      		if(null == batStatus)
				      			batStatus = "";
				      		else if(!batStatus.equals("充电中"))
				      			batStatus = "电池供电";
				      		jObj.element("usb", batStatus);
							break;
						}
					}
		       	}
		       	rs.close();	       	
			 } catch(SQLException ex) {
			    	logger.error("GetRegisterDevices doPost Get battery status error:" + ex.getMessage());
			}
			System.out.println(new Date().toString());
			DevicesOperateBean db1 = (DevicesOperateBean)ctx.getAttribute("DevOper");
			for(int i=0; i<jArray.size(); i++)
			{
				JSONObject jObj = jArray.getJSONObject(i);
				String devId = jObj.getString("deveui");
				DevOpr devStatus = db1.getDevOpr(devId);
			   	String status = null;

			   	if(DevOpr.LOCATE == devStatus) 
			   		status = "LOCATE";
			   	else if(DevOpr.UNLOCATE == devStatus)
			   		status = "UNLOCATE";
			   	else if(DevOpr.REG == devStatus)
			   		status = "REG";
			   	else if(DevOpr.DATARCV == devStatus)
			   		status = "DATARCV";
			   	else if(DevOpr.OFFLINE == devStatus)
			   		status = "OFFLINE";
			   	jObj.element("status", status);
			}
			result = jArray.toString(); 	
		}
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(result);		
	}

}
