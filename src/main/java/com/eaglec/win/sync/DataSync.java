package com.eaglec.win.sync;

import java.sql.SQLException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;

import com.alibaba.fastjson.JSON;
import com.eaglec.win.domain.base.SyncException;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Dao;

/**
 * 数据同步类
 * @author PYF
 *
 */
public class DataSync {
	
	private static Dao dao;
	
	public static Dao getDao() {
		return dao;
	}

	public static void setDao(Dao dao) {
		DataSync.dao = dao;
	}

	/**
	 * 创建数据同步
	 * @author pangyf
	 * @since 2013-12-4
	 * @param className		表名（类名）
	 * @param uniqueKey		唯一键名（如企业的eid，用户的uid）
	 * @param uuid			唯一键值（UUID）
	 * @param isSync		是否同步至各个窗口
	 * @return	String[]	String[0]存放连接状态，String[1]存放返回信息
	 */
	public static String[] createDataSync(String className, String uniqueKey, String uuid, String isSync){
		
		try {
			//如果同步开关打开
			if(Common.syncSwitch){				
				String json = createJson(className, uniqueKey, uuid, isSync);			
				return createHttpClient(json, className);
			//如果同步开关关闭
			}else {
				return null;
			}
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 组装要请求的json串
	 * @author pangyf
	 * @since 2013-12-4
	 * @param className
	 * @param uniqueKey
	 * @param uuid
	 * @param isSync
	 * @return
	 */
	private static String createJson(String className, String uniqueKey, String uuid, String isSync){		
		StringBuffer str_object = new StringBuffer("{");
		str_object.append('"').append("className").append('"').append(":").append('"').append(className).append('"').append(",");
		str_object.append('"').append("wid").append('"').append(":").append('"').append(Common.windowId).append('"').append(",");
		str_object.append('"').append("uniqueKey").append('"').append(":").append('"').append(uniqueKey).append('"').append(",");
		str_object.append('"').append("uuid").append('"').append(":").append('"').append(uuid).append('"').append(",");
		str_object.append('"').append("isSync").append('"').append(":").append('"').append(isSync).append('"');
		str_object.append("}");	
		return str_object.toString();
	}
	
	/**
	 * 创建Http请求
	 * @author pangyf
	 * @since 2013-12-4
	 * @return
	 * @throws URIException 
	 */
	private static String[] createHttpClient(String json, String className){
		try {
			String[] reStrings = new String[2];
			GetMethod getMethod = new GetMethod(Common.syncUrl);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			getMethod.getParams().setContentCharset("UTF-8");			
			getMethod.setQueryString(URIUtil.encodeQuery(json));					
			HttpClient httpClient = new HttpClient();			
			int statusCode = httpClient.executeMethod(getMethod);
			//存放连接状态
			reStrings[0] = Integer.toString(statusCode);
	//		if (statusCode != HttpStatus.SC_OK) {
	//			reStrings[1] = getMethod.getStatusLine().toString();
	//		}else{
	//			InputStream responseBody = getMethod.getResponseBodyAsStream();
	//			String responseString = InputStreamUtils.InputStreamTOString(responseBody, "UTF-8");
	//			JSONObject jo = JSON.parseObject(responseString);
	//			reStrings[1] = Boolean.toString(jo.getBooleanValue("success"));
	//		}
			return reStrings;
		} catch (Exception e) {			
			// 记录异动表
			DataSync.addSyncException(new SyncException("同步数据请求", Common.windowId, className, 
					e.getLocalizedMessage(), JSON.toJSONString(json)));
			return null;
		}			
	}
	
	/**
	 * 异动表记录
	 * @author Xiadi
	 * @since 2013-12-2 
	 *
	 */
	public static void addSyncException(SyncException syncException){
		// 打印异常
		System.out.println(syncException);
		String sql = "INSERT INTO syncexception(action,wid,className,exceptionMsg,syncTime,jsonData) VALUES(?,?,?,?,?,?)";
		Object[] param = new Object[]{
				syncException.getAction(),
				syncException.getWid(),
				syncException.getClassName(),
				syncException.getExceptionMsg(),
				syncException.getSyncTime(),
				syncException.getJsonData()
		};
		try {
			dao.update(sql, param);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
