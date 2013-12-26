package com.eaglec.win.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.eaglec.win.aop.NeedSession;
import com.eaglec.win.aop.SessionType;
import com.eaglec.win.biz.publik.CategoryBiz;
import com.eaglec.win.biz.publik.FileManagerBiz;
import com.eaglec.win.biz.user.ApproveBiz;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserApproveBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.base.ApprovedDetail;
import com.eaglec.win.domain.base.Category;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.base.Flat;
import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.base.UserApprovedDetail;
import com.eaglec.win.sync.DataSync;
import com.eaglec.win.utils.Base64;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.CookieHelper;
import com.eaglec.win.utils.InputStreamUtils;
import com.eaglec.win.utils.MD5;
import com.eaglec.win.view.JSONResult;

@Controller
@RequestMapping(value="/ucenter")
public class UcenterController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(UcenterController.class);
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private StaffBiz staffBiz;	
	@Autowired
	private EnterpriseBiz enterpriseBiz;
	@Autowired
	private FileManagerBiz fileManagerBiz;
	@Autowired
	private CategoryBiz categoryBiz;
	@Autowired
	private ApproveBiz approveBiz;
	@Autowired
	private UserApproveBiz userApproveBiz;
	
	@RequestMapping(value = "/api")
	@ResponseBody
	public String syncLogin(@RequestParam Long time,@RequestParam String action,@RequestParam String code,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes attr){
		if(System.currentTimeMillis() - time > 1000*60*10){
			logger.info("AUTHRACATION EXPIRIED");
			return "AUTHRACATION EXPIRIED";
		}
		String _code = new String(Base64.decode(code.toCharArray()));
		String[] params = _code.split("&");
		if(params[0].equals(MD5.toMD5(ResourceBundle.getBundle("config").getString("oauth.client_secret")))){
			if(action.equals("synlogin")) {
				response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
				int $cookietime = 31536000;
				Cookie cookie = new Cookie(ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"),params[1]);
				cookie.setMaxAge($cookietime);
				cookie.setPath("/");
				response.addCookie(cookie);
				logger.info("SYNLOGIN API_RETURN_SUCCEED");
				return "SYNLOGIN API_RETURN_SUCCEED";
			} else if(action.equals("synlogout")) {
				response.addHeader("P3P"," CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
				Cookie cookie = new Cookie(ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"),"");
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
				logger.info("SYNLOGOUT API_RETURN_SUCCEED");
				return "SYNLOGOUT API_RETURN_SUCCEED";
			} else {
				logger.info("API_RETURN_FORBIDDEN");
				return "API_RETURN_FORBIDDEN";
			}		
		}else{
			logger.info("API_RETURN_FORBIDDEN");
			return "API_RETURN_FORBIDDEN";
		}
	}
	
	/**
	 * @author wzlee
	 * 窗口平台单点登录根据token获取openid
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/sync")
	public String ucenterSync(@RequestParam Long time,@RequestParam String code,HttpServletRequest request,HttpServletResponse response,Model model) {
		if(System.currentTimeMillis() - time > 1000*60*10){
			model.addAttribute("message", "AUTHRACATION EXPIRIED[请求超时!]");
			return "error/500";
		}else{
			String _code = new String(Base64.decode(code.toCharArray()));
			String[] params = _code.split("&");
			if(params[0].equals(MD5.toMD5(ResourceBundle.getBundle("config").getString("oauth.client_secret")))){
				response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
				int $cookietime = 31536000;
				Cookie cookie = new Cookie(ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"),params[1]);
				cookie.setMaxAge($cookietime);
				cookie.setPath("/");
				response.addCookie(cookie);
				
				HttpClient _httpClient = new HttpClient();
				String uri = ResourceBundle.getBundle("config").getString("ucenter.clients");
				GetMethod getMethod = new GetMethod(uri);
				getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
				getMethod.getParams().setContentCharset("UTF-8");
				NameValuePair[] _params = {
					new NameValuePair("client_id", ResourceBundle.getBundle("config").getString("oauth.client_id")),
					new NameValuePair("client_secret", ResourceBundle.getBundle("config").getString("oauth.client_secret"))
		        };
				getMethod.setQueryString(_params);
				try {
					int _statusCode = _httpClient.executeMethod(getMethod);
					if (_statusCode != HttpStatus.SC_OK) {
						model.addAttribute("message",getMethod.getStatusLine());
						return "error/500";
					}else{
						InputStream responseBody = getMethod.getResponseBodyAsStream();
						String responseString = InputStreamUtils.InputStreamTOString(responseBody, "UTF-8");
						List<Flat> flats = JSON.parseArray(responseString,Flat.class);
						List<String> srcs = new ArrayList<String>();
						for(Flat flat:flats){
							String login_params = MD5.toMD5(flat.getClient_secret())+"&"+params[1];
							String login_code = new String(Base64.encode(login_params.getBytes("utf-8")));
							srcs.add(flat.getUcenter_api()+"?time="+time+"&action=synlogin&code="+URLEncoder.encode(login_code, "utf-8"));
						}
						model.addAttribute("message", "登陆成功");
						model.addAttribute("srcs", srcs);
						model.addAttribute("redirect_url", params[2]);
						return "oauth/sync";
					}
				} catch (HttpException e) {
					model.addAttribute("message", e.getLocalizedMessage());
					return "error/500";
				} catch (IOException e) {
					model.addAttribute("message", e.getLocalizedMessage());
					return "error/500";
				} catch (Exception e) {
					model.addAttribute("message", e.getLocalizedMessage());
					return "error/500";
				} finally {
					getMethod.releaseConnection();
				}
			}else{
				model.addAttribute("message", "CLIENT_SECRET_INVALID[客户端密钥无效]");
				return "error/500";
			}
		}
		
//		Map<String,String> paramMap = new HashMap<String, String>();
//		String _code;
//		try {
//			_code = UcenterUtils.uc_authcode(code, "DECODE", ResourceBundle.getBundle("config").getString("oauth.client_secret"), 0);
//		} catch (Exception e) {
//			model.addAttribute("message", "DECODE CODE　EXCEPTION[CODE解码异常!]");
//			return "error/500";
//		}
//		String[] params = _code.split("&");
//		for(int i=0;i<params.length;i++){
//			String[] items = params[i].split("=");
//			if(items.length==2){
//				paramMap.put(items[0], items[1]);
//			}else if(items.length ==1){
//				paramMap.put(items[0], "");
//			}
//		}
//		if(paramMap.isEmpty()) {
//			model.addAttribute("message", "INVALID REQUEST[请求无效!]");
//			return "error/500";
//		}
//		if(new Date().getTime() - Long.parseLong(paramMap.get("time")) > 3600) {
//			model.addAttribute("message", "AUTHRACATION EXPIRIED[请求超时!]");
//			return "error/500";
//		}
//		response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
//		int $cookietime = 31536000;
//		Cookie cookie = new Cookie(ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"),paramMap.get("type")+"|"+paramMap.get("uid")+"|"+paramMap.get("token"));
//		cookie.setMaxAge($cookietime);
//		cookie.setPath("/");
//		response.addCookie(cookie);
//		
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
//				model.addAttribute("message",getMethod.getStatusLine());
//				return "error/500";
//			}else{
//				InputStream responseBody = getMethod.getResponseBodyAsStream();
//				String responseString = InputStreamUtils.InputStreamTOString(responseBody, "UTF-8");
//				List<Flat> flats = JSON.parseArray(responseString,Flat.class);
//				List<String> srcs = new ArrayList<String>();
//				for(Flat flat:flats){
//					srcs.add(flat.getUcenter_api()+"?time="+time+"&code="+URLEncoder.encode(UcenterUtils.uc_authcode("action=synlogin&type="+paramMap.get("type")+"&uid="+paramMap.get("uid")+"&token="+paramMap.get("token")+"&time="+time, "ENCODE", flat.getClient_secret(),0),"utf-8"));
//				}
//				model.addAttribute("message", "同步退出成功");
//				model.addAttribute("srcs", srcs);
//				model.addAttribute("redirect_url", paramMap.get("redirect_url"));
//				return "oauth/sync";
//			}
//		} catch (HttpException e) {
//			model.addAttribute("message", e.getLocalizedMessage());
//			return "error/500";
//		} catch (IOException e) {
//			model.addAttribute("message", e.getLocalizedMessage());
//			return "error/500";
//		} catch (Exception e) {
//			model.addAttribute("message", e.getLocalizedMessage());
//			return "error/500";
//		} finally {
//			getMethod.releaseConnection();
//		}
	}
	
	/**
	 * @throws Exception 
	 * @date: 2013-10-23
	 * @author：wzlee
	 * @description：同步登陆处理逻辑：
	 * 	1.根据cookie中的code&status获取用户(user/staff)对应的openID
	 * 	2.判断窗口用户(user/staff)是否存在对应此openID的用户(user/staff),存在即模拟此用户登陆,不存在进行下一步
	 * 	3.请求枢纽平台接口,openID作为参数获取用户信息(包含企业信息)
	 * 	4.判断用户企业的IndustryType是否为窗口ID,即用户所属的企业是否隶属于此窗口平台,是则进入第5步,否则设置用户企业属性为空,保存此用户信息并模拟此用户登陆
	 * 	5.判断窗口企业是否存在对应用户企业信息的IcRegNumber,存在即找出此企业并将其设置为此次请求用户的企业属性,不存在则在企业中添加此企业,并将此添加的企业设置为此用户的企业属性,并添加用户,模拟其登陆
	 */
	@RequestMapping(value = "/oauth")
	public String ucenterAauth(@RequestParam(defaultValue="/")String redirect_url,HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes attr) throws Exception{
		String windowID = ResourceBundle.getBundle("config").getString("window.id");
		Cookie SM_LOGIN = CookieHelper.getCookieByName(request, ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"));
		if(SM_LOGIN != null){
			String[] _code = SM_LOGIN.getValue().split("\\|");
			String type = _code[0];
			String uid = _code[1];
			String token = _code[2];
			if(type.equals("user")){
				User user = userBiz.findUserByUid(uid);
				if(user == null){
					HttpClient _httpClient = new HttpClient();
					String _uri = ResourceBundle.getBundle("config").getString("oauth.user")+"?type="+type+"&uid="+uid+"&token="+token;
					GetMethod _getMethod = new GetMethod(_uri);
					_getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
					_getMethod.getParams().setContentCharset("UTF-8");
					try {
						int _statusCode = _httpClient.executeMethod(_getMethod);
						if (_statusCode != HttpStatus.SC_OK) {
							model.addAttribute("message",_getMethod.getStatusLine());
							return "error/500";
						}else{
							InputStream _responseBody = _getMethod.getResponseBodyAsStream();
							String _responseString = InputStreamUtils.InputStreamTOString(_responseBody, "UTF-8");
							User _user = JSON.parseObject(_responseString, User.class);
							if(_user.getEnterprise() != null){
								Enterprise enterprise = _user.getEnterprise();
								if(enterprise.getIndustryType() == (Integer.parseInt(windowID))){
									List<Enterprise> enterprises = enterpriseBiz.findEnterpriseByIcRegNumber(enterprise.getIcRegNumber());
									if(enterprises.size() > 0){
										_user.setEnterprise(enterprises.get(0));
									}else{
										enterpriseBiz.save(enterprise);
										_user.setEnterprise(enterprise);
									}
									
								}else{
									_user.setEnterprise(null);
								}
							}
							userBiz.add(_user);
							request.getSession().setAttribute("user",_user);
							request.getSession().setAttribute("usertype", Constant.LOGIN_USER);
						}
					} catch (HttpException e) {
						model.addAttribute("message", e.getLocalizedMessage());
						return "error/500";
					} catch (IOException e) {
						model.addAttribute("message", e.getLocalizedMessage());
						return "error/500";
					} finally {
						_getMethod.releaseConnection();
					}
				}else{
					request.getSession().setAttribute("user", user);
					request.getSession().setAttribute("usertype", Constant.LOGIN_USER);
				}
			}else if(type.equals("staff")){
				Staff staff = staffBiz.findByStid(uid);
				if(staff == null){
					HttpClient _httpClient = new HttpClient();
					String _uri = ResourceBundle.getBundle("config").getString("oauth.user")+"?type="+type+"&uid="+uid+"&token="+token;
					GetMethod _getMethod = new GetMethod(_uri);
					_getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
					_getMethod.getParams().setContentCharset("UTF-8");
					try {
						int _statusCode = _httpClient.executeMethod(_getMethod);
						if (_statusCode != HttpStatus.SC_OK) {
							model.addAttribute("message",_getMethod.getStatusLine());
							return "error/500";
						}else{
							InputStream _responseBody = _getMethod.getResponseBodyAsStream();
							String _responseString = InputStreamUtils.InputStreamTOString(_responseBody, "UTF-8");
							Staff _staff = JSON.parseObject(_responseString, Staff.class);
							User _user = userBiz.findUserByUid(_staff.getParent().getUid());
							if(_user == null){
								userBiz.add(_user);
							}else{
								userBiz.update(_user);
							}
							_staff.setParent(_user);
							Staff __staff = staffBiz.findByStid(_staff.getStid());
							if(__staff == null){
								staffBiz.save(__staff);
							}else{
								staffBiz.update(__staff);
							}
							request.getSession().setAttribute("user",__staff);
							request.getSession().setAttribute("usertype", Constant.LOGIN_STAFF);
						}
					} catch (HttpException e) {
						model.addAttribute("message", e.getLocalizedMessage());
						return "error/500";
					} catch (IOException e) {
						model.addAttribute("message", e.getLocalizedMessage());
						return "error/500";
					} finally {
						_getMethod.releaseConnection();
					}
				}else{
					request.getSession().setAttribute("user", staff);
					request.getSession().setAttribute("usertype", Constant.LOGIN_STAFF);
				}
			}
		}
		return "redirect:"+redirect_url;
	}
	
	/**
	 * 企业用户管理中心 -- 认证入口
	 * 
	 * @author Xiadi
	 * @since 2013-8-31
	 * 
	 * @param request
	 * @param response
	 * @return "ucenter/auth_list" 当用户已有认证申请记录时<br/>
	 *         "ucenter/auth_guide" 当用户首次认证申请时<br/>
	 * @eg : http://localhost/ucenter/auth
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value = "/auth")
	public String toAuth(HttpServletRequest request, HttpServletResponse response){
		User user = (User)request.getSession().getAttribute("user");
		List<ApprovedDetail> list = approveBiz.findApprListByUid(user.getId());
		request.setAttribute("apprlist", list);
		if(list.isEmpty()){
			return "ucenter/auth_guide";
		}
		return "ucenter/auth_list";
	}
	
	/**
	 * 转至申请表单页面
	 * 
	 * @author Xiadi
	 * @since 2013-8-31
	 * 
	 * @param url
	 * @param request
	 * @param response
	 *            eg : http://localhost/ucenter/auth_form
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value = "/auth_{url}")
	public String toAuthForm(@PathVariable String url, HttpServletRequest request, 
			HttpServletResponse response){
		Category root = categoryBiz.findRootByClazz("service");
		request.setAttribute("root", root);
		return "ucenter/auth_" + url;
	}
	
	/**
	 * 验证组织机构号是否存在
	 * @author Xiadi
	 * @since 2013-9-24 
	 *
	 * @param icRegNumber
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/validateIcRegNumber")
	public void validateIcRegNumber(String icRegNumber, HttpServletRequest request, 
			HttpServletResponse response){
//		try {
//			List<Enterprise> list = enterpriseBiz.findEnterpriseByIcRegNumber(icRegNumber);
//			if(list.size() > 0 && 
//					list.get(0).getStatus() != Constant.ENTERPRISE_STATUS_DELETED &&
//					!list.get(0).getId().toString().equals(request.getParameter("eid"))){
//				super.outJson(response, new JSONResult(false, "组织机构号已存在！"));
//			}else{
				super.outJson(response, new JSONResult(true, "组织机构号可用！"));
//			}
//		} catch (Exception e) {
//			logger.info("验证组织机构号！异常信息:"+e.getLocalizedMessage());
//			super.outJson(response, new JSONResult(false, "服务器异常！"));
//		}
	}
	
	/**
	 * 验证公司实名是否存在
	 * @author Xiadi
	 * @since 2013-9-24 
	 *
	 * @param name
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/validateEname")
	public void validateEname(String name, HttpServletRequest request, 
			HttpServletResponse response){
		try {
			List<Enterprise> list = enterpriseBiz.findEnterpriseByName(name);
			if(list.size() > 0 && !list.get(0).getId().toString().equals(request.getParameter("eid"))){
				super.outJson(response, new JSONResult(false, "公司实名已存在！"));
			}else{
				super.outJson(response, new JSONResult(true, "公司实名可用！"));
			}
		} catch (Exception e) {
			logger.info("验证公司实名！异常信息:"+e.getLocalizedMessage());
			super.outJson(response, new JSONResult(false, "服务器异常！"));
		}
	}
	
	/**
	 * 用户提交实名认证申请
	 * 
	 * @author Xiadi
	 * @since 2013-9-2
	 * 
	 * @param approvedDetail
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.OR)
	@RequestMapping(value = "/apply")
	public void applyRealName(ApprovedDetail approvedDetail, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(approvedDetail.toString());
		try {		
			if(!approveBiz.addApply(approvedDetail)){
				super.outJson(response, new JSONResult(false, "提交失败！该用户已有相关申请,请先审核！"));
				return;
			}
			List<ApprovedDetail> list = approveBiz.findApprListByUid(approvedDetail.getUser().getId());
			// 与文件类关联
			if(approvedDetail.getApplyType() == Constant.APPROVED_APPLYTYPE_REALNAME || 
					(approvedDetail.getApplyType() == Constant.APPROVED_APPLYTYPE_ORG && 
					!approvedDetail.getEnterprise().getIsApproved())) { // 申请实名认证 或 直接申请服务机构
				fileManagerBiz.addRelateClazz(approvedDetail.getLicenceDuplicate(), "ApprovedDetail");
				fileManagerBiz.addRelateClazz(approvedDetail.getBusinessLetter(), "ApprovedDetail");
			}
			request.setAttribute("apprlist", list);
			logger.info("[ "+approvedDetail.getUser().getUserName()+" ]提交了认证申请!");
			super.outJson(response, new JSONResult(true, "认证申请提交成功！"));
		} catch (Exception e) {
			logger.info("申请实名认证失败！异常信息:"+e.getLocalizedMessage());
			super.outJson(response, new JSONResult(false, "认证申请提交失败！"));
		}
	}
	
	/**
	 * 分页查询指定类别的用户申请
	 * 
	 * @author Xiadi
	 * @since 2013-8-24
	 * 
	 * @param username 申请会员名
	 * @param enterpriseName 申请实名
	 * @param applyTimeBegin 开始时间
	 * @param applyTimeEnd  结束时间
	 * @param applyDesc 申请类型
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/approvelist")
	public void queryApproveList(String username, String enterpriseName, 
			String applyTimeBegin, String applyTimeEnd,
			Integer applyType, Integer start, Integer limit,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			this.outJson(response, approveBiz.findAll(username, enterpriseName, applyTimeBegin, 
					applyTimeEnd, applyType, start, limit));
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, 
					"查询失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("查询失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 审核用户申请
	 * 
	 * @author Xiadi
	 * @since 2013-8-26
	 * 
	 * @param approvedDetail
	 *            未审核的申请
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/approve")
	public void approve(ApprovedDetail approvedDetail, HttpServletRequest request, 
			HttpServletResponse response){
		try {
			Manager manager = (Manager)request.getSession().getAttribute("manager");
			approvedDetail.setManager(manager);
			ApprovedDetail ret = approveBiz.addApprove(approvedDetail);
			if(ret.getApproveStatus() != null){
				if(ret.getApproveStatus() == 0) {
					this.outJson(response, new JSONResult(true,"审核成功!"));
					return;// 0 表示未通过 无需改变企业信息
				}
				if(ret.getApplyType() == Constant.APPROVED_APPLYTYPE_REALNAME|| 
						(ret.getApplyType() == Constant.APPROVED_APPLYTYPE_ORG && !ret.getEnterprise().getIsApproved())) { // 申请实名认证 或 直接申请服务机构
					ret.getEnterprise().setIcRegNumber(ret.getIcRegNumber());
					ret.getEnterprise().setLicenceDuplicate(ret.getLicenceDuplicate());
					ret.getEnterprise().setBusinessLetter(ret.getBusinessLetter());
					ret.getEnterprise().setName(ret.getName());
					ret.getEnterprise().setIsApproved(true);
					ret.getEnterprise().setType(Constant.ENTERPRISE_TYPE_REALNAME);
				}
				if(ret.getApplyType() == Constant.APPROVED_APPLYTYPE_ORG) { // 实名过后 申请服务机构
					ret.getEnterprise().setType(Constant.ENTERPRISE_TYPE_ORG);
				}
				
				enterpriseBiz.save(ret.getEnterprise());
				//同步
				DataSync.createDataSync("enterprise", "eid", ret.getEnterprise().getEid(), ret.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				DataSync.createDataSync("approveddetail", "adid", ret.getAdid(), "false");
				this.outJson(response,new JSONResult(true,"审核成功!"));
				
				
			}
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, 
					"审核失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("审核失败!异常信息:" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 查看指定企业申请服务机构的服务列表
	 * 
	 * @author Xiadi
	 * @since 2013-8-29
	 * 
	 * @param eid
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/applyServiceList")
	public void applyServiceList(Integer eid, HttpServletRequest request, 
			HttpServletResponse response){
		try {
			this.outJson(response, approveBiz.findRelationListByEid(eid));
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, 
					"查询失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("查询失败!异常信息:" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 查看指定企业申请服务机构的服务列表
	 * 
	 * @author pangyf
	 * @since 2013-9-6
	 *  
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value = "/service_list")
	public String toService(HttpServletRequest request, 
			HttpServletResponse response){
		// 测试用户
		return "ucenter/service_list"; 
	}
	
	/**
	 * 卖家订单
	 * @author xuwf
	 * @since 2013-9-6
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value="/seller_order")
	public String sellerOrder(HttpServletRequest request, 
			HttpServletResponse response){
		
		return "ucenter/seller_order";
	}
	
	/**
	 * 卖家申诉
	 * @author xuwf
	 * @since 2013-9-9
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value="/seller_appeal")
	public String sellerAppeal(HttpServletRequest request, 
			HttpServletResponse response){
		
		return "ucenter/seller_appeal";
	}
	
	/**
	 * 买家申诉
	 * @author xuwf
	 * @since 2013-9-9
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value="/buyer_appeal")
	public String buyerAppeal(HttpServletRequest request, 
			HttpServletResponse response){
		
		return "ucenter/buyer_appeal";
	}
	
	/**
	 * 买家订单
	 * @author xuwf
	 * @since 2013-9-7
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value="/buyer_order")
	public String buyerOrder(HttpServletRequest request, 
			HttpServletResponse response){
		
		return "ucenter/buyer_order";
	}
	
	/**
	 * 个人用户提交实名认证申请
	 * 
	 * @author xuwf
	 * @since 2013-10-28
	 * 
	 * @param userapprovedDetail
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.OR)
	@RequestMapping(value = "/applyPersonal")
	public void applyPersonalRealName(UserApprovedDetail approvedDetail, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(approvedDetail.toString());
		try {		
			if(!userApproveBiz.addApply(approvedDetail)){
				super.outJson(response, new JSONResult(false, "提交失败！该用户已有相关申请,请先审核！"));
				return;
			}
			List<UserApprovedDetail> list = userApproveBiz.findApprListByUid(approvedDetail.getUser().getId());
			
			// 与文件类关联
			if(approvedDetail.getApplyType() == Constant.APPROVED_APPLYTYPE_REALNAME) {// 申请实名认证
				fileManagerBiz.addRelateClazz(approvedDetail.getPersonalPhoto(), "UserApprovedDetail");
				fileManagerBiz.addRelateClazz(approvedDetail.getIdCardPhoto(), "UserApprovedDetail");
			}
			request.setAttribute("apprlist", list);
			logger.info("[ "+approvedDetail.getUser().getUserName()+" ]提交了认证申请!");
			super.outJson(response, new JSONResult(true, "认证申请提交成功！"));
		} catch (Exception e) {
			logger.info("申请实名认证失败！异常信息:"+e.getLocalizedMessage());
			super.outJson(response, new JSONResult(false, "认证申请提交失败！"));
		}
	}
	
	/**
	 * 分页查询指定类别的用户申请
	 * 
	 * @author xuwf
	 * @since 2013-10-28
	 * 
	 * @param username 		  	 申请会员名
	 * @param enterpriseName 	申请实名
	 * @param applyTimeBegin 	开始时间
	 * @param applyTimeEnd   	结束时间
	 * @param applyDesc 	   	申请类型
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/userapprovelist")
	public void queryUserApproveList(String username,String enterpriseName,String applyTimeBegin, 
		String applyTimeEnd,Integer applyType, Integer start, Integer limit,
		HttpServletRequest request, HttpServletResponse response) {
		try {
			this.outJson(response, userApproveBiz.findAll(username,enterpriseName,applyTimeBegin, 
					applyTimeEnd, applyType, start, limit));
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, 
					"查询失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("查询失败!异常信息:" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 审核个人用户申请
	 * 
	 * @author xuwf
	 * @since 2013-10-28
	 * 
	 * @param approvedDetail
	 *            未审核的申请
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/userApprove")
	public void userApprove(UserApprovedDetail userapprovedDetail, HttpServletRequest request, 
			HttpServletResponse response){
		try {
			Manager manager = (Manager)request.getSession().getAttribute("manager");
			userapprovedDetail.setManager(manager);
			UserApprovedDetail ret = userApproveBiz.addApprove(userapprovedDetail);
			if(ret.getApproveStatus() != null){
				this.outJson(response,new JSONResult(true,"审核成功!"));
				if(ret.getApproveStatus() == 0) return;// 0 表示未通过 无需改变企业信息
				if(ret.getApplyType() == 1) { // 申请实名认证 
					ret.getUser().setRealName(ret.getName());
					ret.getUser().setPersonalPhoto(ret.getPersonalPhoto());
					ret.getUser().setIdCardPhoto(ret.getIdCardPhoto());
					ret.getUser().setCertSign(Constant.APPROVED_APPLYTYPE_REALNAME);
				}
				userBiz.update(ret.getUser());
				//同步
				DataSync.createDataSync("user", "uid", ret.getUser().getUid(), ret.getUser().getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				//用户的基本信息改变，随之改变session中的保存用户
				request.getSession().setAttribute("loginUser", ret.getUser());
				request.getSession().setAttribute("user", ret.getUser());
			}
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, 
					"查询失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("查询失败!异常信息:" + e.getLocalizedMessage());
		}
	}
}
