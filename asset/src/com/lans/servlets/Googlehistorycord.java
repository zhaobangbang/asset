package com.lans.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.dao.beans.GoogleGpsPoint;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class Googlepoint
 */
@WebServlet("/Googlehistorycord.do")
public class Googlehistorycord extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(Googlehistorycord.class); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Googlehistorycord() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("name");
		String deveui = request.getParameter("dev");
		String enddatetime = request.getParameter("dt1");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
		Date date = null;   
		String startdatetime = null;
		try {
			date = sdf.parse(enddatetime);
			 long nowtime = date.getTime();
		     long time = nowtime - 86400000*1;
		     Date beforetime = new Date(time);
		     startdatetime = sdf.format(beforetime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
	   	
	   	String sql = "select latitude,longitude,time from gps_tbl where owner='"+username+"' and  deveui='"+deveui+"' and time BETWEEN '"+startdatetime+"' and '"+enddatetime+"'ORDER BY time ";
        List<String> latitudeList = new ArrayList<String>();
        List<String> longitudeList = new ArrayList<String>();
        List<String> timeList = new ArrayList<String>();
        ResultSet rs = null;
		SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//格式化rs.getFloat()数据，获取小数点后六位
		DecimalFormat form=new DecimalFormat("0.000000");  
		try{
			rs = db.executeQuery(sql);
	       	while (rs.next()) {
	       		String Googlelati = form.format(rs.getFloat("latitude"));
	       		String Googlelong = form.format(rs.getFloat("longitude"));
	       		Date Googletime = rs.getDate("time");
   		        String Googletimestr = shortDF.format(Googletime);
	       		latitudeList.add(Googlelati);
	       		longitudeList.add(Googlelong);
	       		timeList.add(Googletimestr);
	       }
			rs.close();
	    } catch(SQLException ex) {
	    	logger.error("GPShistorycord doGet query error:" + ex.getMessage());
	    }
		GoogleGpsPoint  GoogleGps[] = new GoogleGpsPoint[latitudeList.size()];
		for(int i=0;i<GoogleGps.length;i++){
			GoogleGps[i] = new GoogleGpsPoint(latitudeList.get(i),longitudeList.get(i),timeList.get(i));
		}
   	    JSONArray jsonRsp = JSONArray.fromObject(GoogleGps);
	    response.setContentType("text/html; charset=utf-8");
		//Object jsonRsp = null;
	   logger.info("doGet respond in Googlepoint.do "+jsonRsp.toString());
	   response.getWriter().write(jsonRsp.toString());
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				doGet(request, response);
		
	}

}
