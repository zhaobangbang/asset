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

import net.sf.json.JSONObject;

/**
 * Servlet implementation class DevFenceSetting
 */
@WebServlet("/DevFenceSetting.do")
public class DevFenceSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(DevFenceSetting.class);   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DevFenceSetting() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");

        String devName  = request.getParameter("deveui");
        
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		ResultSet rs = null;
		String resp = "";
		if(devName != null)
		{
			String	sql = "select eye_lon,eye_lat,eye_radius from dev_fence_tbl where deveui = '" + devName + "'";		
		   JSONObject jObject = new JSONObject();
			try{
				    rs = dbm.executeQuery(sql);
					if(null != rs)
					{
						rs.beforeFirst(); 
						if(rs.next())
		       			{
							jObject.element("deveui", devName);
			       			jObject.element("eye_lon", rs.getFloat("eye_lon"));
			       			jObject.element("eye_lat", rs.getFloat("eye_lat"));
			       			jObject.element("eye_radius", rs.getInt("eye_radius"));
							rs.close();
		       			}
		       		}
                    resp = jObject.toString();
				} catch(SQLException ex) {
					logger.error("executeQuery1:" + ex.getMessage());
				}
			response.getWriter().write(resp);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
