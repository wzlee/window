package com.eaglec.win.utils;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Common {
	public static String superAdmin;	//超级管理员名称
	public static String uploadPath;	//文件上传路径
	public static int serviceHotNum;	//热门服务加载多少条数据
	public static int serviceNewNum;	//最新服务加载多少条数据
	public static int newsIndexNum;		//行业新闻加载多少条数据
	public static int resultNumJ;		//服务搜索结果分页显示数量（矩形）
	public static int resultNumL;		//服务搜索结果分页显示数量（列表形）
	public static int newsPageNum;		//新闻频道中的新闻列表，每页显示多少条数据
	public static String channelCode;	//频道编码
	public static String icRegNumber;		//运营支撑系统的工商注册号
	public static String CENTER_WEBSITE;		//枢纽平台网址	
	public static Integer windowId;			//窗口对象的16个窗口号==企业表中的所属窗口	
	public static Boolean syncSwitch;
	public static String syncUrl;
	
	static{
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		uploadPath = bundle.getString("uploadPath");
		serviceHotNum = Integer.parseInt(bundle.getString("service.hotNum"));
		serviceNewNum = Integer.parseInt(bundle.getString("service.newNum"));
		newsIndexNum = Integer.parseInt(bundle.getString("news.indexNum"));
		resultNumJ = Integer.parseInt(bundle.getString("result.numJ"));
		resultNumL = Integer.parseInt(bundle.getString("result.numL"));
		newsPageNum = Integer.parseInt(bundle.getString("news.pageNum"));
		channelCode = bundle.getString("channel.code");
		icRegNumber = bundle.getString("icRegNumber");
		CENTER_WEBSITE = bundle.getString("center.website");
		windowId = Integer.parseInt(bundle.getString("window.id"));
		syncSwitch = Boolean.parseBoolean(bundle.getString("sync.switch"));
		syncUrl = bundle.getString("sync.url");
	}

	/**
	 * @date：2013-3-19
	 * @author：lwch
	 * @description：设置cookie内容
	 */
	public static void setCookies(HttpServletResponse response, String cookieName, String cookieValue, int limitTime, String path){
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(limitTime);	//cookie保存一个季度(90天)
		cookie.setPath(path);
		response.addCookie(cookie);
	}
	
	/**
	 * @date：2013-3-19
	 * @author：lwch
	 * @description：删除cookiesName[]中的cookies
	 */
	public static void removeCookies(HttpServletRequest request, HttpServletResponse response, String cookiesName[]){
		Cookie[] cookies = request.getCookies();  
        if (cookies != null) {
            for (Cookie cookie : cookies) {
            	for (int i = 0; i < cookiesName.length; i++) {
					if (cookiesName[i].equals(cookie.getName())) {  
						cookie.setValue("");
						cookie.setMaxAge(0); 
						cookie.setPath("/");
						response.addCookie(cookie); 
					}  
				}
            }  
        }
	}
	
	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @description：随机产生一个10位的随机数
	 */
	public static String getPictureFileName() {
		String s = "";
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			int n = random.nextInt(36);
			if (n >= 0 && n <= 9) {
				s += String.valueOf(n);
			}else{
				n = n-10;
				s += (char)(97+n);
			}
		}
		return s;
	}
	
	/**
	 * 保留double小数点2位
	 * @author xuwf
	 * @since 2013-10-24
	 * @param num
	 * @return
	 */
	public static double parseDouble(double num){
		DecimalFormat df = new DecimalFormat("0.00");
		double db = Double.parseDouble(df.format(num));
		return db;
//		return ((int)(num*100))/100;
	}
	
	/**
	 * double四舍五入
	 * @author xuwf
	 * @since 2013-10-24
	 * 
	 * @param num
	 * @return
	 */
	public static Long round(double num){
		return Math.round(num);
	}
	

	/**
	 * 为同步创建getMethod
	 * @author pangyf
	 * @since 2013-10-30
	 * @param service
	 * @param url
	 * @return
	 */	
	public static GetMethod creatGetMethod(Object object,String url){
		
		try {
			
			String str_object = JSON.toJSONString(object, SerializerFeature.NotWriteRootClassName,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty);
			String uri = ResourceBundle.getBundle("config").getString(url);
			GetMethod getMethod = new GetMethod(uri);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			getMethod.getParams().setContentCharset("UTF-8");
			getMethod.setQueryString(URIUtil.encodeQuery(str_object));
			return getMethod;			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 创建同步
	 * @author pangyf
	 * @since 2013-10-31
	 * @param object   要同步的对象
	 * @param url	同步的地址（从配置文件config。properties中读取）
	 * @return
	 */
	public static String[] creatSync(Object object,String url){		
		try {
			String[] reStrings = new String[2];			
			String str_object = JSON.toJSONString(object, SerializerFeature.NotWriteRootClassName,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty);
			String uri = ResourceBundle.getBundle("config").getString(url);
			GetMethod getMethod = new GetMethod(uri);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			getMethod.getParams().setContentCharset("UTF-8");
			getMethod.setQueryString(URIUtil.encodeQuery(str_object));
			
			HttpClient httpClient = new HttpClient();		
			
			int statusCode = httpClient.executeMethod(getMethod);
			
			reStrings[0] = Integer.toString(statusCode);			
			if (statusCode != HttpStatus.SC_OK) {
				reStrings[1] = getMethod.getStatusLine().toString();
			}else{
				InputStream responseBody = getMethod.getResponseBodyAsStream();
				String responseString = InputStreamUtils.InputStreamTOString(responseBody, "UTF-8");
				JSONObject jo = JSON.parseObject(responseString);
				reStrings[1] = Boolean.toString(jo.getBooleanValue("success"));
			}	
			
			return reStrings;
			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		
	}
	
//	public static String[] creatDataSync(String className, String wid, String uniqueKey, String uuid, String isSync, String url){		
//		try {
//			if(syncSwitch){
//				String[] reStrings = new String[2];
//				StringBuffer str_object = new StringBuffer("{");
//				str_object.append('"').append("className").append('"').append(":").append('"').append(className).append('"').append(",");
//				str_object.append('"').append("wid").append('"').append(":").append('"').append(wid).append('"').append(",");
//				str_object.append('"').append("uniqueKey").append('"').append(":").append('"').append(uniqueKey).append('"').append(",");
//				str_object.append('"').append("uuid").append('"').append(":").append('"').append(uuid).append('"').append(",");
//				str_object.append('"').append("isSync").append('"').append(":").append('"').append(isSync).append('"');
//				str_object.append("}");
//				String uri = ResourceBundle.getBundle("config").getString(url);
//				GetMethod getMethod = new GetMethod(uri);
//				getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//				getMethod.getParams().setContentCharset("UTF-8");
//				getMethod.setQueryString(URIUtil.encodeQuery(str_object.toString()));
//				
//				HttpClient httpClient = new HttpClient();		
//				
//				int statusCode = httpClient.executeMethod(getMethod);
//				
//				reStrings[0] = Integer.toString(statusCode);			
//				if (statusCode != HttpStatus.SC_OK) {
//					reStrings[1] = getMethod.getStatusLine().toString();
//				}else{
//					InputStream responseBody = getMethod.getResponseBodyAsStream();
//					String responseString = InputStreamUtils.InputStreamTOString(responseBody, "UTF-8");
//					JSONObject jo = JSON.parseObject(responseString);
////					reStrings[1] = Boolean.toString(jo.getBooleanValue("success"));
//				}				
//				return reStrings;
//			}else {
//				return null;
//			}
//		} catch (Exception e) {			
//			e.printStackTrace();
//			return null;
//		}
//		
//	}
	
	/**
	 * 两个对象根据properties复制属性
	 * @author pangyf
	 * @since 2013-10-30
	 * @param source  源对象
	 * @param target  目标对象
	 * @param properties   所要复制的属性数组
	 */
	public static void copyProperties(Object source, Object target, String[] properties){		
		PropertyDescriptor sourcePd,targetPd;
		if(properties != null){
			for (String propertie : properties) {				
				sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), propertie);
				targetPd = BeanUtils.getPropertyDescriptor(target.getClass(), propertie);
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						Method writeMethod = targetPd.getWriteMethod();
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}
						writeMethod.invoke(target, value);
					}
					catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
				
			}		
		}
	}
	
	/**
	 * @date: 2013-9-11
	 * @author：xuwf
	 * @description：随机产生一个5位的随机数
	 */
	public static String random() {
		String s = "";
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			int n = random.nextInt(9);	
			s += String.valueOf(n);	
		}
		return s;
	}

}



