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
import com.lans.infrastructure.util.SecurityUtil;

/**
 * Servlet implementation class RegisterCodeGen
 */
@WebServlet("/RegisterCodeGen.do")
public class RegisterCodeGen extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(RegisterCodeGen.class); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterCodeGen() {
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
		String mail = request.getParameter("email");
		String result = null;
		response.setContentType("text/html; charset=utf-8");
		
	  	if(null == mail)
	  	{
	  		result= new String("邮箱不能为空!");
	  		response.getWriter().write(result);
	  		return;
	  	}
	  	if(!mail.contains("@") || !mail.contains("."))
	  	{
	  		result = new String("邮箱格式不正确!");
	  		response.getWriter().write(result);
	  		return;
	  	}
	  	
	   	ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
	  	String sql = "select * from key_tbl where usermail = \'" + mail + "\'";
	  	ResultSet rs = null;
	  	try{
	  		rs = db.executeQuery(sql);
	  		if(rs.next())
	  		{
	  			rs.close();
	  			result= new String("此邮箱地址已存在!");
	  		}
	  		}catch(SQLException ex){
	  			logger.error("executeQuery:" + ex.getMessage());
	  		}
	  			
	  	String permitKey = null;
	  	while(true)
	  	{
	  		permitKey = SecurityUtil.getRandomString(30);
	  		sql = "select * from key_tbl where permitkey = \'" + permitKey + "\'";
	  		try{
	  			rs = db.executeQuery(sql);
	  			if(!rs.next())
	  			{
	  				rs.close();
	  				break;
	  			}
	  			}catch(SQLException ex){
	  				logger.error("executeQuery:" + ex.getMessage());
	  			}
	  	} 
	    
	    java.util.Date date = new java.util.Date();
		SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd");
	    sql = "insert into key_tbl(usermail,permitkey,time) values(\'" + mail + "\',\'" +  permitKey + "\',\'" + shortDF.format(date) + "\');";
	    

	   	if(db.executeUpdate(sql) != 1) //Fail to insert the record.
	   	{
	   		result = new String("生成注册邀请码失败!");
	   	}
	   	else {
	   		result = permitKey;
	   	}
	    	  /*
	    MailManager themail = new MailManager("smtp.163.com");//这里以新浪邮箱为例子  
	    String mailbody = "<a href='http://www.wphtu.ah.cn/' target='_blank'>芜湖职业技术学院</a>发送成功了哦";//邮件正文  
	    themail.setNeedAuth(true);  
	    themail.setSubject("JAVA发邮件的测试");//邮件主题  
	    themail.setBody(mailbody);//邮件正文  
	    themail.setTo(mail);//收件人地址  
	    themail.setFrom("webmaster@lansitec.com");//发件人地址  
	    //themail.addFileAffix("F:/download/email.rar");// 附件文件路径,例如：C:/222.jpg,*注；"/"的写法； 如果没有可以不写  
	    themail.setNamePass("webmaster@lansitec.com", "YIsi@2016");//发件人地址和密码 **改为相应邮箱密码  
	    themail.sendout();
	    */  	  
		
		response.getWriter().write(result);
	}

}
