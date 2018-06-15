package com.lans.servlets;

import java.io.IOException;
import java.util.List;

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
import com.lans.common.UserMgrDevAccess;

/**
 * Servlet implementation class UpdateUserDevWarningSetting
 */
@WebServlet("/UpdateUserDevWarningSetting.do")
public class UpdateUserDevWarningSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(UpdateUserDevWarningSetting.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUserDevWarningSetting() {
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
				logger.info(" doPost in UpdateUserDevWarningSetting.do ");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");

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
				
				String username = request.getParameter("usrname"); 
				String warn_inter = request.getParameter("warn_inter");
				String warn_stime = request.getParameter("warn_stime");
				String warn_etime = request.getParameter("warn_etime");
				//warn_sdate = request.getParameter("warn_sdate");
				String warn_tel1  = request.getParameter("warn_tel1");
				String warn_tel2  = request.getParameter("warn_tel2");
				int validday = Integer.parseInt(warn_inter);
				
				String result = null;
				ServletContext ctx = request.getServletContext();
			   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
			   	List<String> userDevlist = UserMgrDevAccess.getUserDev(request, username);
			   	if(userDevlist.size()!=0){
			    	for(String userdevEUI: userDevlist){
		   			  String sql = "update dev_list_tbl set warn_inter=\""+validday+"\", warn_stime=\""+warn_stime+"\", warn_etime=\""+ warn_etime
						 + "\", warn_tel1=\"" + warn_tel1 + "\", warn_tel2=\"" + warn_tel2
						 + "\" where deveui =\"" + userdevEUI + "\"";
		   			  int rowAffected = db.executeUpdate(sql);
		   			  if (1 == rowAffected) {
		   				result = "['ok']"; 
		   		
		   		    }
			     }
			    	response.getWriter().write(result);
			    }
		         else{
			   
					  response.getWriter().write("['fail']"); //username dosen't have deveui
		          }
	}
}
