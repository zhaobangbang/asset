package com.lans.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class QueryDeviceAndGPS
 */
@WebServlet("/QueryDeviceAndGPS.do")
public class QueryDeviceAndGPS extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(QueryDeviceAndGPS.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryDeviceAndGPS() {
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
		String mapId = request.getParameter("mapid");
		JSONArray jArray = new JSONArray();
		JSONObject jsonMsg = new JSONObject();
		ServletContext ctx = request.getServletContext();
		DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		String sql = "select * from dev_list_tbl where map_id = '" + mapId + "'";
		String deveui = null;
		String alias = null;
		String worktype = null;
		String lTime = null;
		String xGPS = null;
		String yGPS = null;
		String result = null;
		//格式化rs.getFloat()数据，获取小数点后六位
		DecimalFormat form=new DecimalFormat("0.00000000");  
		try{
			ResultSet rs = db.executeQuery(sql);
			if(null != rs){
			while(rs.next())
			{
				deveui=  rs.getString("deveui");
				alias = rs.getString("alias");
				worktype = rs.getString("worktype");
				sql = "select * from gps_tbl where deveui = \'" + deveui + "\' order by time desc limit 1";
				ResultSet rs1 = db.executeQuery(sql);
					if(null != rs1 && rs1.next()){
					yGPS = form.format(rs1.getFloat("latitude"));
					xGPS = form.format(rs1.getFloat("longitude"));
					lTime = rs1.getString("time");
				rs1.close();
					}

				jsonMsg.element("deveui", deveui);
				jsonMsg.element("alias", alias);
				jsonMsg.element("worktype", worktype);
				jsonMsg.element("time", lTime);
				jsonMsg.element("xGPS", xGPS);
				jsonMsg.element("yGPS", yGPS);
				jArray.add(jsonMsg);
			}
			
			rs.close();
			}
			result = jArray.toString();
  			}catch(SQLException ex){
  				logger.error("executeQuery:" + ex.getMessage());
  				
  			}
		   response.setContentType("text/html; charset=utf-8");
		   response.getWriter().write(result);
		
	}

}
