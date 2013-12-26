package com.eaglec.win;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.utils.InputStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={
        "file:src/main/webapp/WEB-INF/spring/*.xml", 
        "file:src/main/webapp/WEB-INF/spring/appServlet/*.xml"})
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
		HttpClient httpClient = new HttpClient();
		String uri = "http://localhost:8080/oauth/user?code=7J5D9in4yui6J8zfAwHN&status=1382588050797";
		GetMethod getMethod = new GetMethod(uri);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		getMethod.getParams().setContentCharset("UTF-8");
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.debug("error");
			}else{
				InputStream responseBody = getMethod.getResponseBodyAsStream();
				String responseString = InputStreamUtils.InputStreamTOString(responseBody, "UTF-8");
				logger.info("响应数据:"+responseString);
				JSONObject jo = JSON.parseObject(responseString);
//				User _user = userBiz.findUserById(jo.getInteger("id"));
//				if(_user == null){
					User user = JSON.parseObject(responseString, User.class);
					logger.info(user.toString());
//					enterpriseBiz.save(user.getEnterprise());
					userBiz.add(user);
//				}else{
//					logger.debug(_user.toString());
//				}
			}
		} catch (HttpException e) {
			logger.debug(e.getLocalizedMessage());
		} catch (IOException e) {
			logger.debug(e.getLocalizedMessage());
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
