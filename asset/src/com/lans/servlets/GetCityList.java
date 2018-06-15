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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;

/**
 * Servlet implementation class GetCityList
 */
@WebServlet("/GetCityList.do")
public class GetCityList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(GetCityList.class);   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCityList() {
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
 
	   	String result = null;
		if(type.equals("ALL"))
		{
			ServletContext ctx = request.getServletContext();
			DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
			String sql = "select * from city_tbl;";
			ResultSet rs = null;
			result = "[";
			try{
				rs = db.executeQuery(sql);
				while(rs.next())
				{
					result += "'" + rs.getString("city") + "',";
				}
				result += "]";
				rs.close();
	  			}catch(SQLException ex){
	  				logger.error("executeQuery:" + ex.getMessage());
	  				result=null;
	  			}
		}
		logger.info(result);
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(result);
		
	
	}

}
