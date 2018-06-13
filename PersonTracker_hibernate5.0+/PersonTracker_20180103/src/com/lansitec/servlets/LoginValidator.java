package com.lansitec.servlets;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lansitec.common.UserMgrHBAccess;
import com.lansitec.dao.LogRecordDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.beans.LogRecord;
import com.lansitec.dao.beans.ManagersLoginInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.enumlist.LogObj;
import com.lansitec.enumlist.LogType;
import com.lansitec.infrastructure.util.SecurityUtil;

@Controller
@RequestMapping("/LoginValidator")
public class LoginValidator {
	public static final String ROLE_ATTR_NAME = "loginRole";
	private Logger logger = LoggerFactory.getLogger(LoginValidator.class);
	 /*Whether the user exist and the password is correct*/   
	public boolean userLog(String username, String userkey)
	   {
	   	if(null == username || null == userkey)
	   		return false;
	   	
	   	String regNameEx = "[A-Z,a-z,0-9,_]*";
	    String regKeyEx = "[A-Z,a-z,0-9,-,_,#,@]*";
	    	
	    if(!Pattern.compile(regNameEx).matcher(username).matches())
	    	return false;
	    	
	    if(!Pattern.compile(regKeyEx).matcher(userkey).matches())
	    	return false;	
	    		
	   	SystemManagers SysManagers = null;
	   	ProjectManagers proManagers = null;
		try {
			SysManagers = SystemManagersDAO.getMangersInfoByUK(username,userkey);
			if(SysManagers != null){
		   		return true;
		   	}else{
		   		proManagers = ProjectmanagersDAO.getUsersManagersByUK(username, userkey);
		   		if(proManagers != null){
		   			return true;
		   		}
		   	}
		} catch (Exception e) {
			logger.error("try/catch error in userLog!");
			e.printStackTrace();
		}
	   	
	   	return false;
	   }
   
    @RequestMapping(value="doGet",method=RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response,ManagersLoginInfo managersLogin) throws ServletException, IOException {
    	logger.info("receive do get in LoginValidator.doGet");
		String sName=managersLogin.getUsername();
		String rtn = "['ok']";
		String result = null;
		try {
			SystemManagers managers = SystemManagersDAO.getMangersInfoByUsername(sName);
			if(managers != null){
				result = "系统管理员";
			}else{
				ProjectManagers proManager = ProjectmanagersDAO.getUsersManagersByUN(sName);
				if(proManager != null){
					result = "项目管理员";
				}else{
				   result = null;
				}
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(result == null)
    	{
    		rtn = "['fail']";
    	}
    	
    	response.getWriter().write(rtn);
	}

    @RequestMapping(value="doPost",method=RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response,ManagersLoginInfo managersLogin,@RequestParam String operate) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sName = "";
		String smail = managersLogin.getUsermail();
		String result = "['ok']";
		String usrAction = "";
		if(smail == null){
			sName =managersLogin.getUsername();
			String sKey=managersLogin.getPassword();
			String rKey=managersLogin.getRkey();
			if(null==sName)
				sName="";
			if(null==sKey)
				sKey="";
			if(null==rKey)//不记住秘密
				rKey="";
			usrAction = "登录:";
				/*
				if(sName.equals("admin") && sKey.equals("lansitec#2016"))//后台管理员,用于初始登录.
				{
					request.getSession().setAttribute("usrname", sName);
					//response.sendRedirect("UsersManagement.jsp?name="+sName);
					request.getSession(true).setAttribute("prio", "超级管理员");
					//request.getRequestDispatcher("globalshow.jsp").forward(request, response);
				}
				*/
			response.setContentType("text/html; charset=utf-8");
			String prio = UserMgrHBAccess.userSystemExist(sName);
			if(prio.equals(""))
			{
				result = "['nouser']";
			}
			else
			{
				request.getSession().setAttribute("usrname", sName);
				request.getSession(true).setAttribute("prio", prio);
				if(userLog(sName, sKey))
				{
					// for login page to remember password
					String encodePass;
					try {
						encodePass = SecurityUtil.encrypt(sKey, "pcasset");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					Cookie lansiuser = new Cookie("lansiuser", sName + "-" + encodePass);
					lansiuser.setPath("/pcasset");
					if ((rKey != null) && (rKey.equals("true"))) {
						lansiuser.setMaxAge(3600 * 24 * 7);
					} else {
						lansiuser.setMaxAge(0);
					}
					response.addCookie(lansiuser);
					request.getSession(true).setAttribute("usrname", sName);
					request.getSession(true).setAttribute("prio", prio);
					usrAction += "成功";
				}
				else
				{
					result = "['nokey']";
					usrAction += "密码不正确";
				}					 
			}
		
		   //request.getRequestDispatcher("globalshow.jsp").forward(request, response);
		   response.getWriter().write(result);
		}
		else{
			sName = managersLogin.getUsername();
			usrAction = "找回密码：";
			if(null == sName)
				sName = "";
			response.setContentType("text/html; charset=utf-8");
			String prio = UserMgrHBAccess.userSystemExist(sName);
			if(prio.equals("")){
				result="['fail']";
				usrAction += "系统/项目管理员不存在";
			}
			else{
				boolean userValid = UserMgrHBAccess.userSystemValid(sName, smail);
				if(userValid){
					request.getSession(true).setAttribute("usrname", sName);
					request.getSession(true).setAttribute("prio", prio);
					usrAction += "找回密码成功";
				}
				else{
					result = "['fail']";
					usrAction += "提供信息不完整";
				}
			}
			response.getWriter().write(result);
		}
    	
    	/*String ip = request.getHeader("x-forwarded-for");  
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
    	        ip = request.getHeader("Proxy-Client-IP");  
    	}  
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
    	}  
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
    	}*/ 
		//记录用户登录信息
        LogRecord logRecord = new LogRecord(sName, LogObj.用户, LogType.登录, sName, usrAction,LocalDateTime.now());
        LogRecordDAO.create(logRecord);
    	
	}
}
