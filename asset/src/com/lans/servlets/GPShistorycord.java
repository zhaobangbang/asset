package com.lans.servlets;


import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.lans.beans.BaiduGpsPoint;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class GPShistorycord
 */
@WebServlet("/GPShistorycord.do")
public class GPShistorycord extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger  logger = LoggerFactory.getLogger(GPShistorycord.class); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GPShistorycord() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  String username = request.getParameter("name");
		  String deveui = request.getParameter("dev");
		  String startdatetime = request.getParameter("dt1");
		  String enddatetime = request.getParameter("dt2");
		 
		  
		  ServletContext ctx = request.getServletContext();
		  DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		  
	      response.setCharacterEncoding("UTF-8");
		  response.setContentType("text/html;charset=UTF-8");
		 
		  String sql="select baidulati,baidulong from gps_tbl where owner='"+username+"' and  deveui='"+deveui+"' and time BETWEEN '"+startdatetime+"' and '"+enddatetime+"'ORDER BY time ";
		  ResultSet rs = null;
			List <String> baidulati = new ArrayList<String>();
			List <String> baidulong = new ArrayList<String>();
		  //格式化rs.getFloat()数据，获取小数点后六位
		  DecimalFormat form=new DecimalFormat("0.000000");  
		try{
			rs = db.executeQuery(sql);
	       	while (rs.next()) {
	       		  String baidulat = form.format(rs.getFloat("baidulati"));
	       		  String baidulng = form.format(rs.getFloat("baidulong"));
	       		baidulati.add(baidulat);
	       		baidulong.add(baidulng);
	       }
			rs.close();
	    } catch(SQLException ex) {
	    	logger.error("GPShistorycord doGet query error:" + ex.getMessage());
	    }
		
		 BaiduGpsPoint  baiduGps[] = new BaiduGpsPoint[baidulati.size()];
       	for(int i=0;i<baiduGps.length;i++){
       		baiduGps[i] = new BaiduGpsPoint(baidulati.get(i),baidulong.get(i));
       	}
       	JSONArray jsonRsp = JSONArray.fromObject(baiduGps);
		response.setContentType("text/html; charset=utf-8");
		//System.out.println("doGet respond in GPShistorycord.do "+jsonRsp.toString());
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
