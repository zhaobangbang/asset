package com.lans.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.DataBaseMgr;
import com.lans.common.UserMgrDBAccess;

/**
 * Servlet implementation class RegisterValidator
 */
@WebServlet("/RegisterValidator.do")
public class RegisterValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(RegisterValidator.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterValidator() {
        super();
        // TODO Auto-generated constructor stub
    }
    public String userReg(HttpServletRequest request, String username, String userkey, String tel, String uid, String city, String mail, String permitkey)
    {
    	if(null == tel)
    		tel = "";

    	
    	String regNameEx = "[A-Z,a-z,0-9]*";
    	String regKeyEx = "[A-Z,a-z,0-9,-,_,#,@]*";
    	
    	if(!username.matches(regNameEx))
    		return new String("用户名只能包含字母,数字!");
    	
    	if(!userkey.matches(regKeyEx))
    		return new String("密码只能包含字母,数字或\"-_#@\"");	
    	
    	if(!uid.matches(regNameEx))
    		return new String("身份证号码含有非法字符!");
    	
    	if(!mail.contains("@") || !mail.contains("."))
    		return new String("邮箱格式不正确!");	
   	
    	if(!permitkey.matches(regNameEx))
    		return new String("注册码含有非法字符!");
   	
    	ServletContext ctx = request.getServletContext();
    	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
    	String sql = "select * from key_tbl where permitkey=\"" + permitkey + "\"";
    	ResultSet rs = null;
    	try{
    		rs = db.executeQuery(sql);
    		if(!rs.next()) //key doesn't exists.
    		{
    			rs.close();
    			return new String("注册码不可识别,请联系管理员!");
    		}
    		else
    		{
    			if(!rs.getString("usermail").equals(mail))
    			{
    				rs.close();
    				return new String("邮箱与申请注册码的邮箱不匹配!");
    			}
    		}
    		rs.close();
    		}catch(SQLException ex){
    			logger.error("executeQuery:" + ex.getMessage());
     	  }
     	String result =  UserMgrDBAccess.userExist(request, username); 
    	if(!result.equals(""))
    	{
    		return new String("此用户名已存在!");
    	}
     
    	java.util.Date date = new java.util.Date();
    	SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd");
    	sql = "insert into users_tbl(username,userkey,usertel,uid,city,usermail,time,prio) values(\'" + username + "\',\'" +  userkey + "\',\'" + tel + "\',\'" + uid + "\',\'" + city + "\',\'" + mail + "\',\'"  + shortDF.format(date) + "\','普通用户');";
     
    	
    	if(db.executeUpdate(sql) != 1) //Fail to insert the record.
    	{
    		return new String("添加用户失败!");
    	}
     	  
    	return new String("OK");	  
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
		  request.setCharacterEncoding("UTF-8");
		  String sName=request.getParameter("user");
	      String sKey=request.getParameter("passwd");
	      String sTel=request.getParameter("tel");
	      String sUid=request.getParameter("uid");
	      String sCity=request.getParameter("city");
		  String sMail=request.getParameter("email");
		  String pKey = request.getParameter("pkey");
		  //LansUtil.LogDebug("Register");
		  
	       if(null==sName)
	          sName="";
	       if(null==sKey)
	         sKey="";
			if(null==sMail)
			  sMail="";
			if(null == sTel)
				sTel="";
			if(null == sUid)
				sUid="";
			if(null == pKey)
				pKey = "";
	        if(null == sCity)
	        	sCity="";
	        response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			if(sName.equals("admin") || sName.equals("Admin"))
			{
			   response.getWriter().write("['此用户名为保留字!请重试！']");
			}
			else
			{
			   String result = userReg(request, sName, sKey, sTel, sUid, sCity, sMail, pKey);
			   response.getWriter().write("['" + result + "']");
			}
	}

}
