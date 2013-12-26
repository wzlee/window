package com.eaglec.plat;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.base.Flat;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.service.Service;
import com.eaglec.win.utils.Base64;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.InputStreamUtils;
import com.eaglec.win.utils.MD5;

public class HttpClientTest {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientTest.class);
	
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private StaffBiz staffBiz;
	@Autowired
	private EnterpriseBiz enterpriseBiz;
	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void getUserInfo() throws Exception{
//		HttpClient _httpClient = new HttpClient();
//		String uri = ResourceBundle.getBundle("config").getString("ucenter.clients");
//		GetMethod getMethod = new GetMethod(uri);
//		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//		getMethod.getParams().setContentCharset("UTF-8");
//		NameValuePair[] _params = {
//			new NameValuePair("client_id", ResourceBundle.getBundle("config").getString("oauth.client_id")),
//			new NameValuePair("client_secret", ResourceBundle.getBundle("config").getString("oauth.client_secret"))
//        };
//		getMethod.setQueryString(_params);
//		try {
//			int _statusCode = _httpClient.executeMethod(getMethod);
//			if (_statusCode != HttpStatus.SC_OK) {
//				logger.info(getMethod.getStatusLine().toString());
//			}else{
//				InputStream responseBody = getMethod.getResponseBodyAsStream();
//				String responseString = InputStreamUtils.InputStreamTOString(responseBody, "UTF-8");
//				logger.info(responseString);
//			}
//		} catch (HttpException e) {
//			logger.info(e.getLocalizedMessage());
//		} catch (IOException e) {
//			logger.info(e.getLocalizedMessage());
//		} catch (Exception e) {
//			logger.info(e.getLocalizedMessage());
//		} finally {
//			getMethod.releaseConnection();
//		}
		HttpClient _httpClient = new HttpClient();
		String _uri = ResourceBundle.getBundle("config").getString("oauth.user")+"?type=user&uid=1ee6123b49bbe51c55f8d96de2df9c38&token=f0f513105337136c583e34bbab2b576a";
//		String _uri = "http://www.smemall.com.cn/oauth/user?type=user&uid=e625dec42191ae98a4c1e616d59f19f7&token=a7ca26deb234f4039f221c873360d996";
		GetMethod getMethod = new GetMethod(_uri);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		getMethod.getParams().setContentCharset("UTF-8");
		try {
			int _statusCode = _httpClient.executeMethod(getMethod);
			if (_statusCode != HttpStatus.SC_OK) {
				logger.info(getMethod.getStatusLine().toString());
			}else{
				InputStream _responseBody = getMethod.getResponseBodyAsStream();
				String _responseString = InputStreamUtils.InputStreamTOString(_responseBody, "UTF-8");
				logger.info(_responseString);
				User _user = JSON.parseObject(_responseString, User.class,Feature.IgnoreNotMatch);
				logger.info(_user.getIsApproved()?"是":"否");
			}
		} catch (HttpException e) {
			logger.info("获取用户信息异常:"+e.getLocalizedMessage());
		} catch (IOException e) {
			logger.info("获取用户信息异常:"+e.getLocalizedMessage());
		} finally {
			getMethod.releaseConnection();
		}
	}
	
	@Test
	public void serviceSync() throws Exception{
		try {
		} catch (Exception e) {
			logger.info("服务保存失败!异常信息:" + e.getLocalizedMessage());
		}
	}
	
}
