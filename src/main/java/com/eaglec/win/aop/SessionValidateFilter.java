package com.eaglec.win.aop;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eaglec.win.utils.Common;

public class SessionValidateFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		 SysContent.setRequest((HttpServletRequest) request);
		 SysContent.setResponse((HttpServletResponse) response);
		 
		 /* center_website是枢纽平台的网址,让jsp能从request中获取改变量*/
		 Object obj = request.getAttribute("center_website");
		 if (obj == null) {
			 request.setAttribute("center_website", Common.CENTER_WEBSITE);    //枢纽平台网址
		 }
		 chain.doFilter(request, response); 
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
