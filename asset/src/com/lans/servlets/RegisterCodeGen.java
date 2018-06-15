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
	  		result= new String("���䲻��Ϊ��!");
	  		response.getWriter().write(result);
	  		return;
	  	}
	  	if(!mail.contains("@") || !mail.contains("."))
	  	{
	  		result = new String("�����ʽ����ȷ!");
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
	  			result= new String("�������ַ�Ѵ���!");
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
	   		result = new String("����ע��������ʧ��!");
	   	}
	   	else {
	   		result = permitKey;
	   	}
	    	  /*
	    MailManager themail = new MailManager("smtp.163.com");//��������������Ϊ����  
	    String mailbody = "<a href='http://www.wphtu.ah.cn/' target='_blank'>�ߺ�ְҵ����ѧԺ</a>���ͳɹ���Ŷ";//�ʼ�����  
	    themail.setNeedAuth(true);  
	    themail.setSubject("JAVA���ʼ��Ĳ���");//�ʼ�����  
	    themail.setBody(mailbody);//�ʼ�����  
	    themail.setTo(mail);//�ռ��˵�ַ  
	    themail.setFrom("webmaster@lansitec.com");//�����˵�ַ  
	    //themail.addFileAffix("F:/download/email.rar");// �����ļ�·��,���磺C:/222.jpg,*ע��"/"��д���� ���û�п��Բ�д  
	    themail.setNamePass("webmaster@lansitec.com", "YIsi@2016");//�����˵�ַ������ **��Ϊ��Ӧ��������  
	    themail.sendout();
	    */  	  
		
		response.getWriter().write(result);
	}

}
