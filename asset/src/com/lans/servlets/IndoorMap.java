package com.lans.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lans.common.InDoorApi;

/**
 * Servlet implementation class IndoorMap
 */
@WebServlet("/IndoorMap.do")
public class IndoorMap extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndoorMap() {
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
		/*HttpSession reqSession = request.getSession(false);
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
		}*/
		String parameter =request.getParameter("cmd");
		if(parameter.equals("token"))
		{
			String token = InDoorApi.ReqToken();
			response.getWriter().write("['" + token + "']");
		}
	}

}
