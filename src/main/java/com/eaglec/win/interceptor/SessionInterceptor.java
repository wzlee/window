package com.eaglec.win.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.CookieHelper;
import com.eaglec.win.utils.InputStreamUtils;
import com.eaglec.win.HomeController;

@Repository
public class SessionInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private StaffBiz staffBiz;
	@Autowired
	private EnterpriseBiz enterpriseBiz;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Cookie SM_LOGIN = CookieHelper.getCookieByName(request, ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"));
		if(SM_LOGIN == null){
			if(request.getSession().getAttribute("user") != null && request.getSession().getAttribute("usertype") != null){
				request.getSession().removeAttribute("user");
				request.getSession().removeAttribute("usertype");
				logger.info("用户登陆信息清除成功!");
			}
		}else{
			String[] _code = SM_LOGIN.getValue().split("\\|");
			String type = _code[0];
			String uid = _code[1];
			String token = _code[2];
			if(type.equals("user")){
				User user = userBiz.findUserByUid(uid);
				if(user == null){
					HttpClient _httpClient = new HttpClient();
					String _uri = ResourceBundle.getBundle("config").getString("oauth.user")+"?type="+type+"&uid="+uid+"&token="+token;
					GetMethod getMethod = new GetMethod(_uri);
					getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
					getMethod.getParams().setContentCharset("UTF-8");
					try {
						int _statusCode = _httpClient.executeMethod(getMethod);
						if (_statusCode != HttpStatus.SC_OK) {
							logger.info("请求["+_uri+"]异常状态:"+getMethod.getStatusLine().toString());
						}else{
							InputStream _responseBody = getMethod.getResponseBodyAsStream();
							String _responseString = InputStreamUtils.InputStreamTOString(_responseBody, "UTF-8");
							User _user = JSON.parseObject(_responseString, User.class);
							if(_user.getIsPersonal()){
								_user.setEnterprise(null);
							}else{
								Enterprise enterprise = _user.getEnterprise();
								Enterprise _enterprise = enterpriseBiz.findEnterpriseByEid(enterprise.getEid());
								if(_enterprise == null){
									enterpriseBiz.save(enterprise);
									_user.setEnterprise(enterprise);
									request.getSession().setAttribute("loginEnterprise", enterprise);
								}else{
									_user.setEnterprise(_enterprise);
									request.getSession().setAttribute("loginEnterprise", _enterprise);
								}
							}
							userBiz.add(_user);
							request.getSession().setAttribute("user",_user);
							request.getSession().setAttribute("usertype", Constant.LOGIN_USER);
							request.getSession().setAttribute("loginUser", _user);
						}
					} catch (HttpException e) {
						logger.info("请求["+_uri+"]出现HTTP异常:"+e.getLocalizedMessage());
					} catch (IOException e) {
						logger.info("请求["+_uri+"]出现IO异常:"+e.getLocalizedMessage());
					} finally {
						getMethod.releaseConnection();
					}
				}else{
					request.getSession().setAttribute("user", user);
					request.getSession().setAttribute("usertype", Constant.LOGIN_USER);
					logger.info("["+user.getUserName()+"]同步登陆成功!");
				}
			}else if(type.equals("staff")){
				Staff staff = staffBiz.findByStid(uid);
				if(staff == null){
					HttpClient _httpClient = new HttpClient();
					String _uri = ResourceBundle.getBundle("config").getString("oauth.user")+"?type="+type+"&uid="+uid+"&token="+token;
					GetMethod getMethod = new GetMethod(_uri);
					getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
					getMethod.getParams().setContentCharset("UTF-8");
					try {
						int _statusCode = _httpClient.executeMethod(getMethod);
						if (_statusCode != HttpStatus.SC_OK) {
							logger.info("请求["+_uri+"]异常状态:"+getMethod.getStatusLine().toString());
						}else{
							InputStream _responseBody = getMethod.getResponseBodyAsStream();
							String _responseString = InputStreamUtils.InputStreamTOString(_responseBody, "UTF-8");
							Staff _staff = JSON.parseObject(_responseString, Staff.class);
							staffBiz.save(_staff);
							request.getSession().setAttribute("user",_staff);
							request.getSession().setAttribute("usertype", Constant.LOGIN_STAFF);
						}
					} catch (HttpException e) {
						logger.info("请求["+_uri+"]出现HTTP异常:"+e.getLocalizedMessage());
					} catch (IOException e) {
						logger.info("请求["+_uri+"]出现IO异常:"+e.getLocalizedMessage());
					} finally {
						getMethod.releaseConnection();
					}
				}else{
					request.getSession().setAttribute("user", staff);
					request.getSession().setAttribute("usertype", Constant.LOGIN_STAFF);
					logger.info("["+staff.getUserName()+"]同步登陆成功!");
				}
			}
		}
		return super.preHandle(request, response, handler);
	}
}
