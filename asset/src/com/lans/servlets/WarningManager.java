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

import com.lans.common.BaiduApi;
import com.lans.common.DataBaseMgr;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class WarningManager
 */
@WebServlet("/WarningManager.do")
public class WarningManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(WarningManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WarningManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usrName = request.getParameter("usrname");
		String deveui = request.getParameter("deveui");
		
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");

	   	logger.info("doGet in WarningManager.do "+usrName);
		String sql = "select * from dev_list_tbl where deveui = \"" + deveui + "\"";
   		
		JSONObject jsonRsp = new JSONObject();
   		
		ResultSet rs = db.executeQuery(sql);
		try{
	       	rs.beforeFirst();
	       	if (rs.next()) {
	      		String value = rs.getString("warn_inter");
	      		jsonRsp.put("warn_inter", value);
	      		
	      		value = rs.getString("warn_stime");
	      		jsonRsp.put("warn_stime", value);
	      		
	      		value = rs.getString("warn_etime");
	      		jsonRsp.put("warn_etime", value);
	      		
	      		//value = rs.getString("warn_sdate");
	      		//jsonRsp.put("warn_sdate", value);
	      		
	      		value = rs.getString("warn_tel1");
	      		jsonRsp.put("warn_tel1", value);
	      		
	      		value = rs.getString("warn_tel2");
	      		jsonRsp.put("warn_tel2", value);	      		
	       	}
	       	else {
	       		jsonRsp.put("warn_inter", "not found");
	       	}
	       	rs.close();
	    } catch(SQLException ex) {
	    	logger.error("WarningManager doGet query error:" + ex.getMessage());
	    }

		logger.info("doGet respond in WarningManager.do "+jsonRsp.toString());
		response.getWriter().write(jsonRsp.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info(" doPost in WarningManager.do ");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

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
		String operateType = request.getParameter("operate");
		
		String eye_lon=null, eye_lat=null, eye_radius=null,dev_alias=null, deveui=null, owner,warn_inter=null, 
				  warn_stime=null, warn_etime=null, warn_tel1=null, warn_tel2=null;
	    deveui = request.getParameter("deveui");
	    owner = request.getParameter("owner");
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
	   	
		String sql = "select * from dev_fence_tbl where deveui=\""+deveui+"\"";

		boolean fence_on = false;
		String stime=null,etime=null;
		int validday=0, radius=0;
		float xGPS=0, yGPS=0;
		int fence_id = 0;
		String alias = null;//设备别名
		
		//获得当前围栏信息
   		ResultSet rs = db.executeQuery(sql);
   		try{
   			rs.beforeFirst();
   			if (rs.next()) {
   				fence_on = rs.getBoolean("fence_on");
   				stime = rs.getString("warn_stime");
   				etime = rs.getString("warn_etime");
   				validday = rs.getInt("warn_inter");
   				xGPS = rs.getFloat("eye_lon");
   				yGPS = rs.getFloat("eye_lat");
   				radius = rs.getInt("eye_radius");
   				//sDate = rs.getDate("warn_sdate");
   				fence_id = rs.getInt("fence_id");
   			}
   			else
   			{
   				logger.error("无此设备号：" + deveui);
   				response.getWriter().write("['未查到此设备记录！']");
   				return;
   			}
   			rs.close();
   		}catch(SQLException ex) {
   			logger.error("WarningManager doPost query error:" + ex.getMessage());
   			response.getWriter().write("['访问数据库异常！']");
   			return;
   		}
   		
   		boolean entity_changed = false;
   		boolean fence_changed = false;
		if(null != operateType && operateType.equals("center"))
		{
			eye_lon = request.getParameter("eye_lon");
			eye_lat = request.getParameter("eye_lat");
			eye_radius = request.getParameter("eye_radius");
			dev_alias  = request.getParameter("alias");
			
			if(null == eye_lon)
				eye_lon = "0";
			if(null == eye_lat)
				eye_lat = "0";
			if(null == eye_radius)
				eye_radius = "1000";
			if(null == dev_alias)
				dev_alias = deveui;
			
			float f_eye_lon = Float.parseFloat(eye_lon);
			float f_eye_lat = Float.parseFloat(eye_lat);
			int   i_eye_radius = Integer.parseInt(eye_radius);
			
			if(!dev_alias.equals(alias))
			{
				entity_changed = true;
				alias = dev_alias;
			}
			if(f_eye_lon != xGPS || f_eye_lat != yGPS || i_eye_radius != radius)
			{
				fence_changed = true;
				xGPS = f_eye_lon;
				yGPS = f_eye_lat;
				radius = i_eye_radius;
				
				sql = "update dev_fence_tbl set eye_lon='" + xGPS  + "', eye_lat ='" + yGPS + "', eye_radius = '" + radius + "' where deveui =\"" + deveui + "\"";
				int rowAffected = db.executeUpdate(sql);
				if (0 == rowAffected) {
					logger.error("WarningManager, fail to update fence data");
					response.getWriter().write("['更新围栏失败，数据库中更新失败！']");
					return;
				}
			}
		}
		else
		{
			warn_inter = request.getParameter("warn_inter");
			warn_stime = request.getParameter("warn_stime");
			warn_etime = request.getParameter("warn_etime");
			//warn_sdate = request.getParameter("warn_sdate");
			warn_tel1  = request.getParameter("warn_tel1");
			warn_tel2  = request.getParameter("warn_tel2");
			
			int i_warn_inter = Integer.parseInt(warn_inter);
		
   			if(!stime.equals(warn_stime) || !etime.equals(warn_etime) || validday != i_warn_inter)
   			{
   				fence_changed = true;
   				stime = warn_stime;
   				etime = warn_etime;
   				validday = i_warn_inter;
   			}
		}
		
		/*创建围栏*/
   		/*Step0：创建围栏条件是否具备*/
   		if((xGPS < 0.01 && yGPS < 0.01 ))
   		{
   			radius = 0;//经纬度设为0的话认为是删除围栏
   		}
   		
		/* Step1：是否实体已创建*/
   		if(fence_on)//已经创建
   		{
   			if(entity_changed)
   			{//轨迹实体是否已经变化
   				if(!BaiduApi.UpdateEntity(deveui, alias,owner))
   				{
   					logger.error("Fail to update entity!");
  	   				response.getWriter().write("['更新轨迹实体失败！']");
   	   				return;	
   				}
   			}
   			//是否围栏参数已经改变，改变的话则更新围栏
   			if(fence_changed)
   			{
   				if(radius > 0)
   				{
   					if(!BaiduApi.UpdateFence(fence_id, deveui, stime, etime, validday, xGPS, yGPS, radius))
   					{
   						logger.error("更新围栏失败!");
   						response.getWriter().write("['更新围栏失败！']");
   						return;	
   					}
   				}
				else 
				{//如果半径为0，则认为用户试图删除围栏
					if(!BaiduApi.DeleteFence(fence_id))
					{
   						logger.error("删除围栏失败!");
   						response.getWriter().write("['删除围栏失败！']");
   						return;		
					}
					else
					{//更新表格中的记录
	   					sql = "update dev_fence_tbl set fence_on='0', fence_id='0' where deveui =\"" + deveui + "\"";
	   					int rowAffected = db.executeUpdate(sql);
	   					if (0 == rowAffected) {
	   						logger.error("WarningManager, fail to set fence_on as off");
	   						response.getWriter().write("['删除围栏成功，数据库中更新失败！']");
	   						return;
	   					}
					}
				}
   			}
   		}
   		else
   		{//没有创建围栏，则添加围栏
   			if(radius > 0)
   			{
   				int fenceID = BaiduApi.AddFence(deveui, stime, etime, validday, xGPS, yGPS, radius);
   				if( fenceID > 0)
   				{//如果添加围栏成功，则将告警参数记入数据库
   					sql = "update dev_fence_tbl set fence_on='1', fence_id='" + fenceID + "' where deveui =\"" + deveui + "\"";
   					int rowAffected = db.executeUpdate(sql);
   					if (0 == rowAffected) {
   						logger.error("WarningManager, fail to set fence_on as on");
   						response.getWriter().write("['添加围栏失败！']");
   						return;
   					}
   				}
   				else 
   				{
   					response.getWriter().write("['创建围栏失败！']");
   					return;	
   				}
   			}
   		}
		/*创建围栏结束*/
		
   		if(null == operateType)
   		{
   			sql = "update dev_fence_tbl set warn_inter=\""+validday+"\", warn_stime=\""+stime+"\", warn_etime=\""+etime
				+ "\", warn_tel1=\"" + warn_tel1 + "\", warn_tel2=\"" + warn_tel2
				+ "\" where deveui =\"" + deveui + "\"";
   			int rowAffected = db.executeUpdate(sql);
		
   			if (0 == rowAffected) {
   				response.getWriter().write("['fail']");
   			}
   			else {
   				response.getWriter().write("['ok']");
   			}
   		}
   		else
   		{
			response.getWriter().write("['ok']");
   		} 			
	}
}
