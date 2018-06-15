package com.lans.listener;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class ReqFilter
 */
@WebFilter(dispatcherTypes = {
				DispatcherType.REQUEST, 
				DispatcherType.FORWARD, 
				DispatcherType.INCLUDE
		}
					, urlPatterns = { "*.jsp", "*.do","*.html" })
public class ReqFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ReqFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;  
		String Uri = req.getRequestURI();
		String targetType = Uri.substring(Uri.lastIndexOf(".") + 1, Uri.length());
		String targetUri = Uri.substring(Uri.lastIndexOf("/") + 1, Uri.length());
   
		if(!"do".equals(targetType))
		{  //bopin.jsp 第三方用户登录页面，不显示lansi logo
			if(!"index.jsp".equals(targetUri) && !"bopin.jsp".equals(targetUri) && !"gw.jsp".equals(targetUri))  
			{    
				String sName = (String)req.getSession().getAttribute("usrname");
				if (null == sName || sName.equals(""))
				{
					java.io.PrintWriter out = response.getWriter(); 
					StringBuffer Url = req.getRequestURL();
				    System.out.println("request filted:" + Url);
				    out.println("<html>");    
				    out.println("<script>");    
				    out.println("window.open ('" + Url.substring(0,Url.indexOf("asset")) + "asset/index.jsp','_top')");
				    out.println("</script>");    
				    out.println("</html>");  
				    return; 
				}
			} 		
		}
		else
		{
			if( !"LoginValidator.do".equals(targetUri) && !"NodeQueryWS.do".equals(targetUri))  
			{    
				String sName = (String)req.getSession().getAttribute("usrname");
				if (null == sName || sName.equals(""))
				{
					java.io.PrintWriter out = response.getWriter(); 
					StringBuffer Url = req.getRequestURL();
				    System.out.println("request filted:" + Url);		    		
				    out.println("expires");
				    return; 
				}
			} 			
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
