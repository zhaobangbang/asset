package com.lansitec.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lansitec.servlets.CityManager;
import com.lansitec.servlets.DeviceListMgr;


public class Filters implements Filter{

	public static Object Object;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// 防止流读取一次后就没有了, 所以需要将流继续写出去
	    ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest)request,(HttpServletResponse)response);
	    //(Object instanceof DeviceListMgr) ||
	    if( (Object instanceof CityManager)){
	    	 ((CityManager) Object).getRequestAndResponse((HttpServletRequest)requestWrapper,(HttpServletResponse)response);
	    }
	    
	            
	    //.out.println(json);

		filterChain.doFilter(requestWrapper, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
