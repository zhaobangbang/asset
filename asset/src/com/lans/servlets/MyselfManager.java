package com.lans.servlets;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.infrastructure.util.RSAUtils;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class MyselfManager
 */
@WebServlet("/MyselfManager.do")
public class MyselfManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(MyselfManager.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyselfManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usrName = request.getParameter("name");
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");

	   	logger.info("doGet in MySelfManager.do "+usrName);
		String sql = "select * from users_tbl where username=\""+usrName+"\"";
   		JSONObject jsonRsp = new JSONObject();
   		
		ResultSet rs = db.executeQuery(sql);
		try{
	       	rs.beforeFirst();
	       	if (rs.next()) {
	      		String value = rs.getString("username");
	      		jsonRsp.put("name", value);
	      		
	      		value = rs.getString("usertel");
	      		jsonRsp.put("phone", value);
	      		
	      		value = rs.getString("usermail");
	      		jsonRsp.put("email", value);
	      		
	      		value = rs.getString("uid");
	      		jsonRsp.put("uid", value);
	       	}
	       	else {
	       		jsonRsp.put("name", usrName);
	      		jsonRsp.put("phone", "not found");
	      		jsonRsp.put("email", "not found");
	      		jsonRsp.put("uid", "not found");
	       	}
	       	rs.close();
	    } catch(SQLException ex) {
	    	logger.error("MyselfManager doGet query error:" + ex.getMessage());
	    }

		logger.info("doGet respond in MySelfManager.do "+jsonRsp.toString());
		response.getWriter().write(jsonRsp.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String operate = request.getParameter("operate");

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
		
		if(null != operate && operate.equals("check"))//找回密码
		{
			String name = request.getParameter("user");
			String passwd = request.getParameter("passwd");
			int rowAffected = 0;
					
			if(name != null || passwd != null)
			{
				String sql = "update users_tbl set userkey=\"" + passwd + "\" where username=\""+name+"\"";

				logger.info(" doPost in MySelfManager.do "+sql);
	
				ServletContext ctx = request.getServletContext();
				DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
				rowAffected = db.executeUpdate(sql);
			}
			if (0 == rowAffected) {
				response.getWriter().write("['fail']");
			}
			else {
				response.getWriter().write("['ok']");
			}
		}
		else if(null != operate && operate.equals("update"))
		{
			String name = request.getParameter("user");
			String passwd = request.getParameter("passwd");
			String sql = "update users_tbl set userkey=\""+passwd + "\" where username=\""+name+"\"";
	
			logger.info(" doPost in MySelfManager.do "+sql);
			
			ServletContext ctx = request.getServletContext();
		   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
			int rowAffected = db.executeUpdate(sql);
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			if (0 == rowAffected) {
				response.getWriter().write("用户设置修改失败");
			}
			else {
				response.getWriter().write("用户设置修改成功");
			}
		}
		else{
			String passwd = request.getParameter("passwd");
			String phone = request.getParameter("phone");
			String email = request.getParameter("email");
			String uid = request.getParameter("uid");
			String name = request.getParameter("name");
			ServletContext ctx = request.getServletContext();
			@SuppressWarnings("unchecked")
			HashMap<String, Object> rsaKeys  = (HashMap<String, Object>) ctx.getAttribute("RsaKeys");
            RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeys.get("private");
			int rowAffected = 0;
			try {
				passwd = RSAUtils.decryptBase64(passwd, privateKey);
				phone = RSAUtils.decryptBase64(phone, privateKey);
				email = RSAUtils.decryptBase64(email, privateKey);
				uid = RSAUtils.decryptBase64(uid, privateKey);
				String sql = "update users_tbl set usertel=\""+phone+"\", usermail=\""+email+"\", userkey=\""+passwd
						+ "\", uid=\"" + uid + "\" where username=\""+name+"\"";

		        logger.info(" doPost in MySelfManager.do "+sql);
		
	   	        DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
		        rowAffected = db.executeUpdate(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			if (0 == rowAffected) {
				response.getWriter().write("用户设置修改失败");
			}
			else {
				response.getWriter().write("用户设置修改成功");
			}
		}
	}

}
