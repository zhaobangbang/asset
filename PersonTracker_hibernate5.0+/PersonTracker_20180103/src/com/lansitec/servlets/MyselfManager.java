package com.lansitec.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.beans.ManagersLoginInfo;
import com.lansitec.dao.beans.SystemManagers;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/MyselfManager")
public class MyselfManager {
    private Logger logger = LoggerFactory.getLogger(MyselfManager.class);

    @RequestMapping(value="doGet",method=RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response,ManagersLoginInfo managers) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usrName = managers.getUsername();
		SystemManagers manager = null;
		JSONObject jsonRsp = new JSONObject();
		try {
			manager = SystemManagersDAO.getMangersInfoByUsername(usrName);
			logger.info("doGet in MySelfManager "+usrName);
			if(manager != null){
				String value = manager.getUsername();
				jsonRsp.put("username", value);
				value = manager.getUsermail();
				jsonRsp.put("usermail", value);
			}
			else{
				jsonRsp.put("username", usrName);
	      		jsonRsp.put("usermail", "not found");
			}
			
		} catch (Exception e) {
			logger.error("MyselfManager doGet query error:" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("doGet respond in MySelfManager "+jsonRsp.toString());
		response.getWriter().write(jsonRsp.toString());
		
	}

    @RequestMapping(value="doPost",method=RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response,ManagersLoginInfo managers,@RequestParam String operate) throws ServletException, IOException {

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
		
		if(null != operate && operate.equals("check"))//’“ªÿ√‹¬Î
		{
			String name = managers.getUsername();
			String passwd = managers.getPassword();
			int rowAffected = 0;
					
			if(name != null || passwd != null)
			{
				try {
					SystemManagers SysManager = SystemManagersDAO.getMangersInfoByUsername(name);
					if(SysManager == null){
						logger.info("managers_tbl doen't exist the username {}",name);
						rowAffected = 0;
					}else{
						logger.info("update SystemManagers password {}",passwd);
						SysManager.setUserkey(passwd);
						SystemManagersDAO.update(SysManager);
						rowAffected = 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
			String name = managers.getUsername();
			String passwd = managers.getPassword();
			
			int rowAffected = 0;
			try {
				SystemManagers SysManager = SystemManagersDAO.getMangersInfoByUsername(name);
				if(SysManager == null ){
					logger.info("managers_tbl doen't exist the username {}",name);
					rowAffected = 0;
				}
				else{
					logger.info(" update SystemManagers password {} ",passwd);
					SysManager.setUserkey(passwd);
					SystemManagersDAO.update(SysManager);
					rowAffected = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			if (0 == rowAffected) {
				response.getWriter().write("['fail']");
			}
			else {
				response.getWriter().write("['ok']");
			}
		}
		else{
			String name = managers.getUsername();
			String passwd = managers.getPassword();
			String email = managers.getUsermail();
			int rowAffected = 0;
			
			
			SystemManagers manager;
			try {
				manager = SystemManagersDAO.getMangersInfoByUsername(name);
				if(manager == null){
		        	logger.info("managers_tbl doen't exist the username {}",name);
					rowAffected = 0;
		        }
		        else{
		        	logger.info("update SystemManagers username {} userkey {} useremail {}",name,passwd,email);
		        	manager.setUserkey(passwd);
		        	manager.setUsermail(email);
		        	SystemManagersDAO.update(manager);
		        	rowAffected = 1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			if (0 == rowAffected) {
				response.getWriter().write("['fail']");
			}
			else {
				response.getWriter().write("['ok']");
			}
		}
	}

}
