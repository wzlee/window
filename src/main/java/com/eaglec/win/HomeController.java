package com.eaglec.win;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.eaglec.win.biz.cms.ChannelBiz;
import com.eaglec.win.biz.cms.ModuleBiz;
import com.eaglec.win.biz.news.NewsBiz;
import com.eaglec.win.biz.publik.CategoryBiz;
import com.eaglec.win.biz.service.ServiceBiz;
import com.eaglec.win.biz.service.ServiceRecommendationBiz;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.biz.user.OrganRecommendationBiz;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.controller.BaseController;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.base.Category;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.base.Flat;
import com.eaglec.win.domain.base.OrganRecommendation;
import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.cms.Channel;
import com.eaglec.win.domain.cms.Module;
import com.eaglec.win.domain.cms.News;
import com.eaglec.win.domain.service.Service;
import com.eaglec.win.domain.service.ServiceRecommendation;
import com.eaglec.win.sync.DataSync;
import com.eaglec.win.utils.Base64;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.CookieHelper;
import com.eaglec.win.utils.InputStreamUtils;
import com.eaglec.win.utils.MD5;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.ServiceView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ModuleBiz moduleBiz;
	@Autowired
	private ChannelBiz channelBiz;
	@Autowired
	private CategoryBiz categoryBiz;
	@Autowired
	private NewsBiz newsBiz;
	@Autowired
	private ServiceBiz serviceBiz;
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private StaffBiz staffBiz;
	@Autowired
	private EnterpriseBiz enterpriseBiz;
	@Autowired
	OrganRecommendationBiz recommendationBiz;
	@Autowired
	private ServiceRecommendationBiz servicerecommendationlbiz;
	
	@RequestMapping(value = "/")
	public String index(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		model.addAttribute("page", "index");
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
		List<Service> servicelist = new ArrayList<Service>();
		List<ServiceRecommendation> list = servicerecommendationlbiz.fingAll();
		
		for (Iterator<ServiceRecommendation> sit = list.iterator(); sit.hasNext();) {
			ServiceRecommendation servicerecommendation = sit.next();
			Service service = servicerecommendation.getService();
			Integer status = service.getCurrentStatus();
			if (status != Constant.SERVICE_STATUS_ADDED && status != Constant.SERVICE_STATUS_DOWN_AUDIT) {
				sit.remove();
				servicerecommendationlbiz.delete(servicerecommendation.getId());
			}else{
				servicelist.add(service);
			}
		}
		model.addAttribute("topthree", JSON.toJSONString(servicelist.toArray()));
		return "index";
	}
	
	/**
	 * @date: 2013-9-12
	 * @author：lwch
	 * @description：子窗口首页频道加载
	 */
	@RequestMapping(value = "/banner")
	public String banner(HttpServletRequest request,HttpServletResponse response){
		try {
			List<Module> m = moduleBiz.findAll(Common.channelCode);
			request.getSession().setAttribute("bannerList", m);
			return "layout/banner";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getLocalizedMessage());
			return "error/500";
		}
		
	}
	/**
	 * 进入管理后台逻辑判断
	 * @param request
	 * @param response
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String home(HttpServletRequest request,HttpServletResponse response,Locale locale, Model model) {
		logger.info("Welcome SMEMall! The client locale is:"+locale+",ip is:"+request.getRemoteAddr()+",userAgent is:"+request.getHeader("user-agent"));
		Manager manager = (Manager) request.getSession().getAttribute("manager");
		if(manager==null){
			return "manage/login";
		}else{
			request.setAttribute("manager", JSON.toJSONString(manager));
			return "manage/home";
		}
	}
	
	/**
	 * 组件测试:eg：http://localhost/test/upload
	 * @param component
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{page}", method = RequestMethod.GET)
	public String linkJSP(@PathVariable String page,HttpServletRequest request,HttpServletResponse response,Model model) {
		model.addAttribute("page", page);
		return page;
	}
	
	/**
	 * 全部服务分类
	 * @author liuliping
	 * @since 2013-08-23

	 * @param request
	 * @param response
	 * @param model
	 * @return node/category 返回jewelry.jsp
	 */
	@RequestMapping(value = "/categoryComponent")
	public String categoryComponent(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<Category> cy = categoryBiz.findCategoryParent();
		model.addAttribute("categoryList", cy);
		return "layout/category";
	}
	
	/**
	 * 导航栏
	 * @author liuliping
	 * @since 2013-08-23

	 * @param request
	 * @param response
	 * @param model
	 * @return node/category 返回jewelry.jsp
	 */
	@RequestMapping(value = "/navigator")
	public String navigator(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Channel> cl = channelBiz.findChnnelByCtype(null);
		model.addAttribute("channelList", cl);
		return "layout/navigator";
	}
	
	/**
	 * 行业新闻,返回最新的五条新闻
	 * @author liuliping
	 * @since 2013-08-23

	 * @param request
	 * @param response
	 * @param model
	 * @return node/newsComponent 返回newsComponent.jsp
	 */
	@RequestMapping(value = "/newsComponent")
	public String newsComponent(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String hql = "FROM News ORDER BY id DESC";
		List<News> newsList = newsBiz.getNewsList(hql, 0, 5);
		model.addAttribute("newsList", newsList);
		return "layout/newsComponent";
	}
	
	
	/**
	 * 查询服务机构Top10企业
	 * @author xuwf
	 * @since 2013-9-13
	 * @param type	类型：服务机构
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/topTen")
	public String findTopTen(Integer type,
			HttpServletRequest request,HttpServletResponse response, Model model){
		List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
		for(OrganRecommendation organ: recommendationBiz.fingAll()){
			enterpriseList.add(organ.getEnterprise());
		}
		model.addAttribute("enterpriseList", enterpriseList);
		return "layout/organize";
	}
	
	/**
	 * 服务排行
	 * @author liuliping
	 * @since 2013-08-23

	 * @param request
	 * @param response
	 * @param model
	 * @return node/serviceRunklist 返回serviceRunklist.jsp
	 */
	@RequestMapping(value = "/serviceRunklist")
	public String serviceRunklist(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		JSONData<ServiceView> jd = serviceBiz.findServicesData(0, 8,
				"serviceNum DESC,", null);
		model.addAttribute("serviceList", jd.getData());
		return "layout/serviceRunklist";
	}
	
	/**
	 * 子窗口珠宝首页顶部搜索服务
	 * @author liuliping
	 * @since 2013-08-28
	 * @param name 服务名称	
	 * @param start 起始
	 * @param limit 分页条数
	 * @param request
	 * @param response
	 * @param model
	 * @return node/serviceList 跳转到serviceList.jsp
	 */
	@RequestMapping(value = "/searchService")
	public String searchService(String name, int start, 
			int limit,	HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<Service> serviceList = serviceBiz.
				findServiceListByName(name, limit, start);
		model.addAttribute("serviceList", serviceList);
		return "node/serviceList";
	}
	

	/**
	 * 邮箱激活
	 * @author cs
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/active")
	public String active(HttpServletRequest request, Model model,HttpServletResponse response) {
		String checkcode=request.getParameter("checkcode");
		String username =request.getParameter("username");
		if(checkcode!=null&&!"".equals(checkcode)&&username!=null&&!"".equals(username)){
			if(userBiz.findUserByName(username)!=null&&userBiz.findUserByName(username).size()!=0){
//				String check=checkcode.split("\\|")[0];
				long data = System.currentTimeMillis()-Long.parseLong(checkcode.split("\\|")[1]);
				//当时间小于24小时有效
				if(data<86400000){
					if(userBiz.findUserByName(username).get(0).getCheckcode().equals(checkcode)){
						if(userBiz.findUserByName(username).get(0).getIsApproved()){
							request.setAttribute("message", "你已经激活了，不需要再次激活");
							return "active_success";
						}else{
							userBiz.findUserByName(username).get(0).setApproved(true);
							userBiz.update(userBiz.findUserByName(username).get(0));
							
							User _user = userBiz.findUserByName(username).get(0);
							
							//同步
							DataSync.createDataSync("user", "uid", _user.getUid(), _user.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
							
							request.setAttribute("message", "恭喜你，邮箱成功激活");
							return "active_success";
						}
					}else{
						request.setAttribute("message", "激活码不正确!");
						return "active_success";
					}
				}else{
					request.setAttribute("message", "验证时间已过24小时，请重新申请激活!");
					return "active_success";
				}
			}else{
				request.setAttribute("message", "用户名不正确!");
				return "active_success";
			}
		}else{
			request.setAttribute("message", "链接地址不正确!");
			return "active_success";
		}
	}
	
	/**
	 * 同步登陆跳转
	 * @param redirect_url
	 * @param state
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/signup")
	public void userSignup(@RequestParam(required=false,defaultValue="")String redirect_url,@RequestParam(required=false)String state,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String uri = ResourceBundle.getBundle("config").getString("ucenter.signup");
		String client_id = ResourceBundle.getBundle("config").getString("oauth.client_id");
		String _redirect_uri = (redirect_url.equals("")?request.getHeader("referer"):redirect_url);
		response.sendRedirect(uri+"?client_id="+client_id+"&redirect_url="+_redirect_uri);
	}
	
	/**
	 * 同步登陆跳转
	 * @param redirect_url
	 * @param state
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/login")
	public void userLogin(@RequestParam(required=false,defaultValue="")String redirect_url,@RequestParam(required=false)String state,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String uri = ResourceBundle.getBundle("config").getString("ucenter.login");
		String client_id = ResourceBundle.getBundle("config").getString("oauth.client_id");
		String _redirect_uri = (redirect_url.equals("")?request.getHeader("referer"):redirect_url);
		response.sendRedirect(uri+"?client_id="+client_id+"&redirect_url="+_redirect_uri);
	}
	
	@RequestMapping(value = "/logout")
	public String userLogout(@RequestParam(required=false,defaultValue="")String redirect_url,@RequestParam(required=false)String state,HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
		String _redirect_uri = (redirect_url.equals("")?request.getHeader("referer"):redirect_url);
		Cookie SM_LOGIN = CookieHelper.getCookieByName(request, ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"));
		if(SM_LOGIN == null){
			return "redirect:"+_redirect_uri;
		}else{
			response.addHeader("P3P"," CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
			Cookie cookie = new Cookie(ResourceBundle.getBundle("config").getString("ucenter.cookie.pre"),"");
			cookie.setMaxAge(0);
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
						Long time = System.currentTimeMillis();
						String login_params = MD5.toMD5(flat.getClient_secret());
						String login_code = new String(Base64.encode(login_params.getBytes("utf-8")));
						srcs.add(flat.getUcenter_api()+"?time="+time+"&action=synlogout&code="+URLEncoder.encode(login_code, "utf-8"));
					}
					model.addAttribute("message", "同步退出成功");
					model.addAttribute("srcs", srcs);
					model.addAttribute("redirect_url",_redirect_uri);
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
		}
	}
	
	/**
	 * @author wzlee
	 * 用户中心首页
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ucenter")
	public void userCenter(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
		String uri = ResourceBundle.getBundle("config").getString("ucenter.index");
		response.sendRedirect(uri);
	}
	
	/**
	 * 验证session失败页面
	 * @author Xiadi
	 * @since 2013-9-9 
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/error/{url}")
	public String userVerify(@PathVariable String url, HttpServletRequest request, HttpServletResponse response) {
		return "error/" + url;
	}
	
	/**
	 * 联系我们
	 * 
	 * @author huyj
	 * @sicen 2013-9-12
	 * @param request
	 * @param response
	 * @return 联系我们的页面
	 */
	@RequestMapping(value = "/contact")
	public String contact(HttpServletRequest request,
			HttpServletResponse response) {
		return "contact";
	}

	/**
	 * 关于我们
	 * 
	 * @author huyj
	 * @sicen 2013-9-12
	 * @param request
	 * @param response
	 * @return 关于我们的页面
	 */
	@RequestMapping(value = "/about")
	public String about(HttpServletRequest request, HttpServletResponse response) {
		return "about";
	}
	
	public static void main(String args[]){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 1);
		Date expiry = calendar.getTime();
		System.out.println(expiry.getTime() - new Date().getTime());
	}
}
