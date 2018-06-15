package com.lans.servlets;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.common.UserMgrDBAccess;
import com.lans.infrastructure.util.Md5;
import com.lans.infrastructure.util.SecurityUtil;



/**
 * Servlet implementation class LoginValidator
 */
@WebServlet("/LoginValidator.do")
public class LoginValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ROLE_ATTR_NAME = "loginRole";
	private Logger logger = LoggerFactory.getLogger(LoginValidator.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginValidator() {
        super();
    }
/*Whether the user exist and the password is correct*/   
public boolean userLog(HttpServletRequest request, String username, String userkey)
   {
   	if(null == username || null == userkey)
   		return false;
   	
   	ServletContext ctx = request.getServletContext();
   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
   	
   	String regNameEx = "[A-Z,a-z,0-9,_]*";
    //String regKeyEx = "[A-Z,a-z,0-9,-,_,#,@]*";
    	
    if(!Pattern.compile(regNameEx).matcher(username).matches())
    	return false;
    	
    //if(!Pattern.compile(regKeyEx).matcher(userkey).matches())
    //	return false;	
    		
   	String sql = "select * from users_tbl where username = \'" + username + "\'";
   	ResultSet rs = null;
   	try{
   		rs = db.executeQuery(sql);
   		if(rs.next())
   		{
   	   		String key = rs.getString("userkey");
   	   		String cryptedKey = rs.getString("vcode");
   	   	    rs.close();
   	   		//У����Ϊ��˵���û���δ����У���룬��ʱ��¼ֱ�ӷ��ش���
   	   		if(null == cryptedKey || cryptedKey.equals("")){
   	   			return false;
   	   		}
   	   		String md5Str = Md5.getMD5(key+cryptedKey);
   	   		if(!userkey.equalsIgnoreCase(md5Str)){
   	   			return false;
   	   		}
   			sql = "update users_tbl set vcode='' where username='" + username + "'";
   			db.executeUpdate(sql);
   			return true;
   		}
   	}catch(SQLException ex){
   			logger.error("executeQuery:" + ex.getMessage());
   		}
   		return false;
   }
   
private String userKey(String username)
{
	if(null == username)
		return null;
	
	DataBaseMgr db = DataBaseMgr.getInstance();	
 		
	String sql = "select * from users_tbl where username = \'" + username + "\'";
	ResultSet rs = null;
	try{
		rs = db.executeQuery(sql);
		if(rs.next())
		{
	   		String key = rs.getString("userkey");
	   	    rs.close();
			
			return key;
		}
	}catch(SQLException ex){
			logger.error("executeQuery:" + ex.getMessage());
		}
		return null;
}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sName=request.getParameter("username");
		String rtn = "['ok']";
		
     	String result =  UserMgrDBAccess.userExist(request, sName); 
    	if(result.equals(""))
    	{
    		rtn = "['fail']";
    	}
    	
    	response.getWriter().write(rtn);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uId=request.getParameter("uid");
		String result = "['ok']";
		String usrAction = "";
		String sName = "";
	   	ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
	   	String sql = "";
	   	
		if(null == uId)//������������֤��Ϣ,�˴�����Ϊ��¼��֤������Ϊ�һ����롣
		{
			String vcode = request.getParameter("vcode");
			if(null != vcode){
				sName=request.getParameter("username");
				if(null == sName){
					return;
				}
				String randomKey = SecurityUtil.getRandomString(32);
				sql = "update users_tbl set vcode='" + randomKey +"' where username='" + sName + "'";
				db.executeUpdate(sql);
				result = "['" + randomKey + "']";
				response.getWriter().write(result);
				return;
			}
			
			sName=request.getParameter("username");
			String sKey=request.getParameter("password");
			String rKey=request.getParameter("rkey");
			if(null==sName)
				sName="";
			if(null==sKey)
				sKey="";
			if(null==rKey)//����ס����
				rKey="";
		  	
			usrAction = "��¼:";
			/*
			if(sName.equals("admin") && sKey.equals("lansitec#2016"))//��̨����Ա,���ڳ�ʼ��¼.
			{
				request.getSession().setAttribute("usrname", sName);
				//response.sendRedirect("UsersManagement.jsp?name="+sName);
				request.getSession(true).setAttribute("prio", "��������Ա");
				//request.getRequestDispatcher("globalshow.jsp").forward(request, response);
			}
			
			if (sName.equals("guest") && sKey.equals("guest"))
			{
				request.getSession().setAttribute("usrname", sName);
				String role = request.getParameter("role");
				if (role == null) {
					role = "��������Ա";
				}
				request.getSession(true).setAttribute("prio", role);
			}
			else if (sName.equals("lansi") && sKey.equals("lansitec"))
			{
				request.getSession().setAttribute("usrname", sName);
				request.getSession(true).setAttribute("prio", "��ͨ�û�");
			}
			*/
			response.setContentType("text/html; charset=utf-8");
			String prio = UserMgrDBAccess.userExist(request, sName);
			if(prio.equals(""))
			{
				result = "['nouser']";
				usrAction += "�û�������";
			}
			else
			{
				if(userLog(request, sName, sKey))
				{
					// for login page to remember password
					String encodePass;
					try {
						encodePass = SecurityUtil.encrypt(userKey(sName), "asset1e312a3frw12");
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					Cookie lansiuser = new Cookie("lansiuser", sName + "-" + encodePass);
					lansiuser.setPath("/asset");
					if ((rKey != null) && (rKey.equals("true"))) {
						lansiuser.setMaxAge(3600 * 24 * 7);
					} else {
						lansiuser.setMaxAge(0);
					}
					response.addCookie(lansiuser);
					request.getSession(true).setAttribute("usrname", sName);
					request.getSession(true).setAttribute("prio", prio);
					request.getSession().setMaxInactiveInterval(60*20);//����20���ӳ�ʱ
					//sName = URLEncoder.encode(sName, "UTF-8");
					//request.getRequestDispatcher("globalshow.jsp").forward(request, response);
					usrAction += "�ɹ�";

					@SuppressWarnings("unchecked")
					HashMap<String, Object> rsaKeys  = (HashMap<String, Object>) ctx.getAttribute("RsaKeys");
					RSAPublicKey pubKey = (RSAPublicKey) rsaKeys.get("public");
					String strPubKey = new String(Base64.encodeBase64(pubKey.getEncoded()), "UTF-8");
					result = "['" + strPubKey + "']";
				}
				else
				{
					result = "['nokey']";
					usrAction += "���벻��ȷ";
				}					 
			}
			//request.getRequestDispatcher("globalshow.jsp").forward(request, response);
			response.getWriter().write(result);
		}
		else
		{
			sName=request.getParameter("user");
			String sTel=request.getParameter("tel");
			String sEmail=request.getParameter("email");
			usrAction = "�һ����룺";
			
			if(null == sName)
				sName = "";
			if(null == sTel)
				sTel = "";
			if(null == sEmail)
				sEmail = "";
			
			response.setContentType("text/html; charset=utf-8");
			String prio = UserMgrDBAccess.userExist(request, sName);
			if(prio.equals(""))
			{
				result = "['fail']";
				usrAction += "�û�������";
			}
			else
			{
				boolean userValid = UserMgrDBAccess.userValid(request, sName, sTel, sEmail, uId);
				if(userValid)
				{
					request.getSession(true).setAttribute("usrname", sName);
					request.getSession(true).setAttribute("prio", prio);
					usrAction += "�һ�����ɹ�";
					//sName = URLEncoder.encode(sName, "UTF-8");
					//request.getRequestDispatcher("MyselfManager.jsp?name="+sName).forward(request, response);
				}
				else
				{
					result = "['fail']";
					usrAction += "�ṩ��Ϣ������";
					//response.getWriter().write("���ṩ����Ϣ����,�һ�����ʧ��!" + "<BR>" + "���ṩ������Ϊ: "+ sName);
				}
			}
			response.getWriter().write(result);
		}
		
		//��¼�û���¼��Ϣ
	   	
    	java.util.Date date = new java.util.Date();
    	SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	String ip = request.getHeader("x-forwarded-for");  
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
    	        ip = request.getHeader("Proxy-Client-IP");  
    	}  
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
    	}  
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
    	} 
    	sql = "insert into users_login_tbl(usrname,action,ip,time) values(\'" + sName + "\',\'" +  usrAction + "\',\'" + ip + "\',\'"  + shortDF.format(date) + "\');";
     
    	
    	db.executeUpdate(sql);
    	
    	sql = "update users_tbl set logintime='" + shortDF.format(date) + "' where username='" + sName + "';";
    	db.executeUpdate(sql);
	}
}
