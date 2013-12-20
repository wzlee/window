package com.eaglec.win.controller;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.httpclient.methods.GetMethod;
import org.hibernate.mapping.Array;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.eaglec.win.utils.InputStreamUtils;
import com.eaglec.win.view.JSONResult;

public class BaseController {
	
	public void outHTML(HttpServletResponse response,String html) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(html);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public void outHTMLP(HttpServletResponse response,String callback,String html) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(callback+"("+html+")");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	public void outJson(HttpServletResponse response,Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteEnumUsingToString,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outJson2(HttpServletResponse response, Object object) {
		try {
			String json = JSON.toJSONString(object,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
//			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			response.setContentType("textml;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void outJson(HttpServletResponse response,List<?> list) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(JSON.toJSONString(list.toArray(),SerializerFeature.WriteDateUseDateFormat));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出JSONP(跨域响应)
	 * @param object
	 */
	public void outJsonP(HttpServletResponse response,String callback,Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(callback+"("+json+")");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 为同步创建getMethod
	 * @author pangyf
	 * @since 2013-10-30
	 * @param service
	 * @param url
	 * @return
	 */	
	public GetMethod creatGetMethod(Object object,String url){
		
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
	 * @param object
	 * @param url
	 * @return
	 */
	public String[] creatSync(Object object,String url){		
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
	
	/**
	 * 两个对象根据properties复制属性
	 * @author pangyf
	 * @since 2013-10-30
	 * @param source
	 * @param target
	 * @param properties
	 */
	public void copyProperties(Object source, Object target, String[] properties){		
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
	
	
}
