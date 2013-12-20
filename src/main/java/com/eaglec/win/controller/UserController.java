package com.eaglec.win.controller;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eaglec.win.aop.NeedSession;
import com.eaglec.win.aop.SessionType;
import com.eaglec.win.biz.user.ApproveBiz;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.base.OrgRegisterApproval;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.sync.DataSync;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.MD5;
import com.eaglec.win.utils.RandomUtils;
import com.eaglec.win.utils.mail.MessageUtil;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONResult;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private EnterpriseBiz enterpriseBiz;
	@Autowired
	private StaffBiz staffBiz;
	@Autowired
	private ApproveBiz approveBiz;

	/**
	 * 根据混合条件分页查询企业用户
	 * 
	 * @author xuwf
	 * @since 2013-8-22
	 * 
	 * @param username
	 * @param entershort
	 * @param entername
	 * @param startTime
	 * @param endTime
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/queryMultiple")
	public void queryMultiple(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "entershort", required = false) String entershort,
			@RequestParam(value = "entername", required = false) String entername,
			@RequestParam(value = "enterpriseType", required = false) Integer enterpriseType,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("start") Integer start,
			@RequestParam("limit") Integer limit, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[ username:" + username + ",entershort:"
				+ entershort + ",entername:" + entername + ",startTime:"
				+ startTime + ",endTime:" + endTime + "]");
	
		JSONData<User> users = userBiz.queryMultiple(username, entershort, 
				entername, enterpriseType, startTime, endTime, start, limit);
		this.outJson(response, users);
	}

	/**
	 * 根据用户名或企业简称查询所有的企业用户(加个条件查询自己所属窗口的 xuwf 2013-11-29)
	 * 
	 * @author pangyf
	 * @since 2013-8-22
	 * 
	 * @param uName
	 *            用户名或者企业简称
	 * @param userStatus
	 *            用户帐号状态
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/query")
	public void query(
			@RequestParam(value = "uName", required = false) String uName,
			@RequestParam(value = "eName", required = false) String eName,
			@RequestParam(value = "enterpriseType", required = false) Integer enterpriseType,
			@RequestParam(value = "userStatus", required = false) String userStatus,
			@RequestParam(value = "start", required = false) int start,
			@RequestParam(value = "limit", required = false) int limit,
			HttpServletRequest request, HttpServletResponse response) {
 
		this.outJson(response, userBiz.findAll(uName, eName, enterpriseType, userStatus, start, limit));
	}

	/**
	 * 用户状态更改(禁用,删除,恢复)
	 * 
	 * @author pangyf
	 * @since 2013-8-14
	 * 
	 * @param user对象
	 */

	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/update")
	public void update(User user, HttpServletRequest request, HttpServletResponse response) {

		try {
			User _user = userBiz.findUserById(user.getId());			
			if(_user.getIsPersonal()){//个人
				_user.setEnterprise(null);		
			}
			_user.setUserStatus(user.getUserStatus());
			userBiz.update(_user);
			/***********************主账号状态发生改变,旗下子账号统一变更(禁用主账号——子账号全部禁用)***************/
			if(!_user.getIsPersonal()){					
				/***********************主账号状态发生改变,企业统一变更(禁用、删除主账号——企业禁用)***************/
				if(user.getUserStatus() == Constant.EFFECTIVE){
					_user.getEnterprise().setStatus(Constant.ENTERPRISE_STATUS_EFFECTIVE);
				}else if(user.getUserStatus() == Constant.DISABLED){
					_user.getEnterprise().setStatus(Constant.ENTERPRISE_STATUS_DISABLED);
				}else if(user.getUserStatus() == Constant.DELETED){
					_user.getEnterprise().setStatus(Constant.ENTERPRISE_STATUS_DELETED);
				}
				enterpriseBiz.update(_user.getEnterprise());
				//同步
				DataSync.createDataSync("enterprise", "eid", _user.getEnterprise().getEid(), _user.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				DataSync.createDataSync("user", "uid", _user.getUid(), _user.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");							
				/*******************************************************************************/
			}
			this.outJson(response, new JSONResult(true, "帐号[" + user.getUserName()	+ "]修改状态成功!"));
			logger.info("[ " + user.getUserName() + " ]修改状态成功!");
			
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "帐号修改状态失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("帐号修改状态失败!异常信息:" + e.getLocalizedMessage());
		}

	}

	/**
	 * 用户编辑
	 * 
	 * @author pangyf
	 * @since 2013-8-26
	 * 
	 * @param user
	 *            对象
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "isreset", required = false) boolean isreset,User user, HttpServletRequest request, HttpServletResponse response) {

		try {						
			User _user = userBiz.findUserById(user.getId());
			if(_user.getIsPersonal()){//个人用户
				if(isreset){
					_user.setPassword(MD5.toMD5(user.getPassword()));
				}
				_user.setMobile(user.getMobile());
				_user.setAddress(user.getAddress());
				_user.setRemark(user.getRemark());
			}else {
				Enterprise enterprise = enterpriseBiz.loadEnterpriseByEid(user.getEnterprise().getId());
				String[] properties = {"forShort","name","enterpriseProperty","businessPattern","industryType","legalPerson","openedTime","salesRevenue",
						"staffNumber","linkman","tel","email","photo","address","mainRemark"};			
				this.copyProperties(user.getEnterprise(), enterprise, properties);
				_user.setEnterprise(enterprise);
				enterpriseBiz.update(enterprise);
				
				//同步
				DataSync.createDataSync("enterprise", "eid", enterprise.getEid(), enterprise.getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				if (isreset) {				
					_user.setPassword(MD5.toMD5(user.getPassword()));					
				}
			}
			userBiz.update(_user);
			
			//同步
			DataSync.createDataSync("user", "uid", _user.getUid(), _user.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
			this.outJson(response, new JSONResult(true, "帐号[" + user.getUserName()	+ "]修改状态成功!"));
			logger.info("[ " + user.getUserName() + " ]修改状态成功!");
			
//			Enterprise enterprise = enterpriseBiz.loadEnterpriseByEid(user.getEnterprise().getId());
//			_user.setEnterprise(enterprise);
//			if(_user.getIsPersonal()){//个人用户
//				if(isreset){
//					_user.setPassword(MD5.toMD5(user.getPassword()));
//				}
//				_user.setMobile(user.getMobile());
//				_user.setAddress(user.getAddress());
//				_user.setRemark(user.getRemark());
//			}else {
//				enterpriseBiz.update(enterprise);						
//				if (isreset) {				
//					_user.setPassword(MD5.toMD5(user.getPassword()));						
//				}
//			}
//			userBiz.update(_user);
//			this.outJson(response, new JSONResult(true, "帐号[" + user.getUserName()	+ "]修改状态成功!"));
//			logger.info("[ " + user.getUserName() + " ]修改状态成功!");
			
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "修改失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("帐号修改失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 添加用户
	 * 
	 * @author pangyf
	 * @since 2013-8-26
	 * 
	 * @param user
	 *            对象
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/add")
	public void add(User user, HttpServletRequest request,HttpServletResponse response) {

		List<User> list = userBiz.findUserListByName(user.getUserName());
		try {
			if (list.isEmpty()) {
				Enterprise _enterprise = new Enterprise();
				if(user.getEnterprise().getPhoto().equals("")){
					user.getEnterprise().setPhoto(_enterprise.getPhoto());
				}
				_enterprise = user.getEnterprise();	
				_enterprise.setIndustryType(Integer.valueOf(ResourceBundle.getBundle("config").getString("window.id")));	
				
				_enterprise.setType(Constant.ENTERPRISE_TYPE_PUBLIC);
				_enterprise.setIndustryType(Common.windowId);
				Enterprise enterprise = enterpriseBiz.save(_enterprise);
				//同步
				DataSync.createDataSync("enterprise", "eid", _enterprise.getEid(), _enterprise.getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				user.setPassword(MD5.toMD5(user.getPassword()));
				user.setEnterprise(enterprise);
				userBiz.add(user);
				//同步
				DataSync.createDataSync("user", "uid", user.getUid(), user.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				logger.info("[ " + user.getUserName() + " ]添加成功!");
				this.outJson(response,new JSONResult(true, "帐号[" + user.getUserName() + "]添加成功!"));
			} else {
				this.outJson(response, new JSONResult(true, "该用户名已存在!"));
			}
		} catch (Exception e) {
			this.outJson(response,	new JSONResult(false, "帐号[" + user.getUserName() + "]添加失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("帐号添加失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * @author cs 发送邮件
	 */
	@RequestMapping(value = "/sendmail")
	public void sendmail(HttpServletRequest request, Model model,
			HttpServletResponse response, String email) {
		String s = RandomUtils.generateRandomNumber();
		if (MessageUtil.sendMail("邮箱激活", "请点击以下链接激活你的账号" + "www.baidu.com",
				email)) {
			request.getSession().setAttribute(email, s);
			this.outJson(response, new JSONResult(true, "成功发送邮箱验证码"));
		} else {
			this.outJson(response, new JSONResult(false, "不能发送邮箱验证码"));
		}
	}

	/**
	 * @author cs 核对验证码
	 */
	@RequestMapping(value = "/checkcode")
	public void checkcode(HttpServletRequest request, Model model,
			HttpServletResponse response, String checkcode, String email) {
		if (checkcode != null
				&& request.getSession().getAttribute(email).equals(checkcode)) {
			this.outJson(response, new JSONResult(true, "验证码正确"));
		} else {
			this.outJson(response, new JSONResult(false, "验证码错误"));
		}
	}

	/**
	 * @author cs
	 * @since 找回会员名
	 */
	@RequestMapping(value = "/find_user")
	public String find_user(HttpServletRequest request) {
		String direction = request.getParameter("direction");
		if (!StringUtils.isEmpty(direction)) {
			request.setAttribute("direction", direction);
		}
		return "find_user";
	}

	/**
	 * @author cs
	 * @since 处理找回会员名
	 */
	@RequestMapping(value = "/handelfind_user")
	public void handelfind_user(HttpServletRequest request, Model model,
			HttpServletResponse response, String email) {
		User u = userBiz.findUserByEmail(email).get(0);
		if (MessageUtil.sendMail("找回会员名", "你用会员名" + u.getUserName(), email)) {
			this.outJson(response, new JSONResult(true, "会员名已发送至邮箱"));
		} else {
			this.outJson(response, new JSONResult(false, "邮箱发送失败，请稍后再试"));
		}
	}

	/**
	 * @author cs
	 * @since 找回密码
	 */
	@RequestMapping(value = "/find_password")
	public String find_password(HttpServletRequest request) {
		String direction = request.getParameter("direction");
		if (!StringUtils.isEmpty(direction)) {
			request.setAttribute("direction", direction);
		}
		return "find_password";
	}

	/**
	 * @author cs
	 * @since 处理找回密码
	 */
	@RequestMapping(value = "/handelfind_password")
	public void handelfind_password(HttpServletRequest request, Model model,
			HttpServletResponse response, String username, String email) {
		User u = userBiz.findUserByName(username).get(0);
		if (u.getEmail().equals(email)) {
			String s = RandomUtils.generateRandomNumber();
			u.setPassword(MD5.toMD5(s));
			userBiz.update(u);
			
			//同步
			DataSync.createDataSync("user", "uid", u.getUid(), u.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
			if (MessageUtil.sendMail("重置密码", "你用户名为" + username + "的账号的新密码为"
					+ s + "，请你速度登录更换密码！", email)) {
				this.outJson(response, new JSONResult(true, "密码已发送至邮箱"));
			} else {
				this.outJson(response, new JSONResult(false, "邮箱发送失败，请稍后再试"));
			}
		} else {
			this.outJson(response, new JSONResult(false, "您填写的邮箱与用户名不匹配"));
		}
	}

	/**
	 * @author cs
	 * @since 用户登录
	 */
	@RequestMapping(value = "/user_login")
	public String user_login(HttpServletRequest request) {
		String direction = request.getParameter("direction");
		if (!StringUtils.isEmpty(direction)) {
			request.setAttribute("direction", direction);
		}
		return "user_login";
	}

	/**
	 * 入口：http://localhost/user/user_login 会员登录验证
	 * 
	 * @param page
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userlogin")
	public String userlogin(HttpServletRequest request,
			HttpServletResponse response, Model model, String username,
			String password, String checkcode, String direction) {
		List<User> list = userBiz.findUserByName(username);
		String msg = "验证码错误";
		if (checkcode.equals(request.getSession().getAttribute("authcode")
				.toString())) {
			if (!list.isEmpty()) {
				User u = list.get(0);
				if (StringUtils.equals(u.getPassword(), MD5.toMD5(password))) {
					request.getSession().setAttribute("user", u);
					if (!StringUtils.isEmpty(direction)) {
						return direction;
					} else {
						return "index";
					}
				} else {
					msg = "密码错误";
				}
			} else {
				msg = "用户名不存在";
			}
		}
		request.setAttribute("msg", msg);
		return "main/user_login";
	}

	/**
	 * 用户退出
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response, Model model, String direction) {
		if (request.getSession().getAttribute("user") != null) {
			request.getSession().removeAttribute("user");
			return "redirect:" + direction;
		} else {
			return "redirect:" + direction;
		}
	}

	/**
	 * @author cs 注册完毕
	 */
	@RequestMapping(value = "/registerover")
	public String registerover(
			@RequestParam("licenceDuplicatefile") CommonsMultipartFile licenceDuplicatefile,
			@RequestParam("businessLetterfile") CommonsMultipartFile businessLetterfile,
			HttpServletRequest request, Model model,
			HttpServletResponse response, Enterprise enterprise, Integer sex,
			String occupation, Integer id, String tel1, String tel2) {
		String uploadPath = "upload";
		String contextPath = request.getSession().getServletContext()
				.getRealPath(uploadPath);
		File filepath = new File(contextPath);
		if (!filepath.exists()) {
			filepath.mkdir();
			logger.info("[" + filepath.getAbsolutePath() + "]创建成功!");
		}

		if (!licenceDuplicatefile.isEmpty()) {
			String fileName = licenceDuplicatefile.getOriginalFilename();
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			File file = new File(contextPath + "/images", new Date().getTime()
					+ fileType); // 新建一个文件
			try {
				licenceDuplicatefile.getFileItem().write(file); // 将上传的文件写入新建的文件中
				enterprise.setLicenceDuplicate(file.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!businessLetterfile.isEmpty()) {
			String fileName = businessLetterfile.getOriginalFilename();
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			File file = new File(contextPath + "/images", new Date().getTime()
					+ fileType); // 新建一个文件
			try {
				businessLetterfile.getFileItem().write(file); // 将上传的文件写入新建的文件中
				enterprise.setBusinessLetter(file.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		enterprise.setTel(tel1 + tel2);
		User u = userBiz.findUserById(id);
		userBiz.update(u);
		return "redirect:/";
	}

	/**
	 * 检查用户名
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param username
	 */
	@RequestMapping(value = "/checkname")
	public void checkname(HttpServletRequest request, Model model,
			HttpServletResponse response, String username) {
		if (userBiz.findUserByName(username).isEmpty()) {
			this.outJson(response, new JSONResult(true, "恭喜你，会员名可以注册"));
		} else {
			this.outJson(response, new JSONResult(false, "该会员名已被注册，请更换会员名"));
		}
	}

	/**
	 * 检查用户名和邮箱是否匹配
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param username
	 */
	@RequestMapping(value = "/nameMateEmail")
	public void nameMateEmail(HttpServletRequest request, Model model,
			HttpServletResponse response, String username, String email) {
		List<User> list = userBiz.findUserByName(username);
		if (!list.isEmpty()) {
			User u = list.get(0);
			if (u.getEmail().equals(email)) {
				this.outJson(response, new JSONResult(true, "用户名和邮箱匹配"));
			} else {
				this.outJson(response, new JSONResult(false, "用户名和邮箱不匹配"));
			}
		} else {
			this.outJson(response, new JSONResult(false, "用户名不存在"));
		}
	}

	/**
	 * 检查邮箱是否可用
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param username
	 */
	@RequestMapping(value = "/checkEmail")
	public void checkEmail(HttpServletRequest request, Model model,
			HttpServletResponse response, String email) {
		if (!userBiz.findUserByEmail(email).isEmpty()) {
			this.outJson(response, new JSONResult(false, "邮箱已经被注册，请更换邮箱"));
		} else {
			this.outJson(response, new JSONResult(true, "邮箱没有被注册，可以使用该邮箱"));
		}
	}

	/**
	 * @author cs 注册第二步
	 */
	@RequestMapping(value = "/register_step_2")
	public void registerStep2(HttpServletRequest request, Model model,
			HttpServletResponse response, User user) {
		try {
			String s = RandomUtils.generateRandomNumber();
			String check = s + "|" + System.currentTimeMillis();
			HtmlEmail mailer = new HtmlEmail();
			// mailer.setDebug(true);
			mailer.setCharset("utf-8");
			mailer.setHostName("smtp.exmail.qq.com");
			mailer.setSSLOnConnect(true);
			mailer.setAuthenticator(new DefaultAuthenticator(
					"admin@pservice.cn", "bmsoft2013"));
			mailer.addTo(user.getEmail());
			mailer.setFrom("admin@pservice.cn", "中小企业公共服务平台");
			mailer.setSubject("中小企业公共服务平台账号激活邮件<系统自动发出,请勿回复>");

			URL logo_url = new URL(
					"http://api.pservice.cn/logo/logo_smemall.png");
			String logo_src = mailer.embed(logo_url, "smemall-logo");
			String htmlEmailTemplate = ("<html>"
					+ "<div style='border-bottom:1px solid #ccc;'>"
					+ "<a href='#'>"
					+ "<img src='cid:"
					+ logo_src
					+ "'>"
					+ "</a>"
					+ "</div>"
					+ "<div style='padding:10px 0;'>"
					+ "<p>"
					+ "深圳市中小企业公共服务平台:<a href='http://smemall.com'>smemall.com</a><br>"
					+ "</p>"
					+ "<p>注册成功!点击 <a href='http://localhost/active?checkcode="
					+ check
					+ "&username="
					+ user.getUserName()
					+ "'>立即激活</a> 完成账号激活!</p>"
					+ "</div>"
					+ "<div style='font-size:12px;border-top:1px solid #ccc;'>"
					+ "<p style='margin:0;padding:0;'>网址:<a href='http://www.smemall.com' target='_blank'>http://www.smemall.com</a></p>"
					+ "<p style='margin:0;padding:0;'>邮箱:admin@pservice.cn</p>"
					+ "</div>" + "</html>");
			mailer.setHtmlMsg(htmlEmailTemplate);
			mailer.setTextMsg("您的邮箱不支持HTML格式的邮件!");
			mailer.send();
			// MessageUtil.sendMail("邮箱激活","恭喜你成功注册中小企业公共服务平台账号,请点击以下链接激活你的账号  http://localhost/active?checkcode="+check+"&username="+user.getUserName()
			// ,user.getEmail());
			if (userBiz.findUserByName(user.getUserName()) == null
					|| userBiz.findUserByName(user.getUserName()).size() == 0) {
				user.setApproved(false);
				user.setCheckcode(check);
				user.setPassword(MD5.toMD5(user.getPassword()));
				Enterprise enterprise = new Enterprise();
				enterpriseBiz.save(enterprise);
				user.setEnterprise(enterprise);
				userBiz.add(user);
				//同步
				DataSync.createDataSync("enterprise", "eid", user.getEnterprise().getEid(), user.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				DataSync.createDataSync("user", "uid", user.getUid(), user.getEnterprise().getType() == Constant.ENTERPRISE_TYPE_ORG ? "true" : "false");
				request.getSession().setAttribute("user", user);
				this.outJson(response, new JSONResult(true, "恭喜你注册成功"));
			} else {
				this.outJson(response, new JSONResult(false, "用户名重复，请更换用户名"));
			}
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "保存用户失败，请稍后再试"));
		}
	}

	/**
	 * @author cs 注册第二步
	 */
	@RequestMapping(value = "/register_success")
	public String register_success(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		String direction = request.getParameter("direction");
		if (StringUtils.isEmpty(direction)) {
			request.setAttribute("direction", direction);
		}
		return "register_success";
	}
	
	/**
	 * 添加个人用户
	 *@author xuwf
	 *@since 2013-10-29
	 *
	 *@param user 对象
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/addPersonal")
	public void addPersonal(User user,HttpServletRequest request,HttpServletResponse response) {
		
			List<User> list = userBiz.findUserListByName(user.getUserName());
			try {
				if(list.isEmpty()){
					user.setPassword(MD5.toMD5(user.getPassword()));
					user.setPersonal(true);
					userBiz.add(user);					
					logger.info("[ "+user.getUserName()+" ]添加成功!");
					this.outJson(response,new JSONResult(true,"帐号[" + user.getUserName() + "]添加成功!"));	
				}else{
					this.outJson(response,new JSONResult(false,"该用户名已存在!"));
				}
			} catch (Exception e) {
				this.outJson(response,new JSONResult(false,"帐号[" + user.getUserName() + "]添加失败!异常信息:"+e.getLocalizedMessage()));
				logger.info("帐号添加失败!异常信息:"+e.getLocalizedMessage());
			}
	}

	/**
	 * 获取服务机构注册列表
	 * @author Xiadi
	 * @since 2013-11-5 
	 *
	 * @param username
	 * @param enterpriseName
	 * @param applyTimeBegin
	 * @param applyTimeEnd
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/orgRegisterlist")
	public void getOrgRegisterlist(String username, String enterpriseName, 
			String applyTimeBegin, String applyTimeEnd,
			Integer applyType, Integer start, Integer limit,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			this.outJson(response, approveBiz.findOrgRegisterlist(username,
					enterpriseName, applyTimeBegin, applyTimeEnd, start, limit));
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, 
					"查询失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("查询失败!异常信息:" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 审核服务机构注册
	 * @author Xiadi
	 * @since 2013-11-5 
	 *
	 * @param approvedDetail
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/approveOrgRegister")
	public void approve(Integer rid, Integer status, HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		try {
			Manager manager = (Manager)request.getSession().getAttribute("manager");
			OrgRegisterApproval orgRegisterApproval = approveBiz.getOrgRegister(rid);
			// 通过时，判断用户或企业是否存在
			if (status == Constant.SERVICE_AUDIT_PASS) {
				if (userBiz.findUserByName(orgRegisterApproval.getUserName()).size() > 0) {
					this.outJson(response,new JSONResult(false, "审核失败！会员名已经存在"));
					return;
				}
				if (userBiz.findUserByEmail(orgRegisterApproval.getEmail()).size() > 0) {
					this.outJson(response, new JSONResult(false, "审核失败！注册邮箱已经存在"));
					return;
				}
				if (enterpriseBiz.findEnterpriseByName(orgRegisterApproval.getOrgName()).size() > 0) {
					this.outJson(response, new JSONResult(false, "审核失败！公司实名已经存在"));
					return;
				}
//				if (enterpriseBiz.findByIcRegNumber(orgRegisterApproval.getIcRegNumber()) != null) {
//					this.outJson(response, new JSONResult(false, "审核失败！组织机构号已经存在"));
//					return;
//				}
			}
			User user = approveBiz.addOrgRegisterApprove(orgRegisterApproval, status, manager);
			// 同步至枢纽
			if (user != null){
				DataSync.createDataSync("enterprise", "eid", user.getEnterprise().getEid(), "true");
				DataSync.createDataSync("user", "uid", user.getUid(), "true");
			}
			DataSync.createDataSync("orgregisterapproval", "oraid", orgRegisterApproval.getOraid(), "false");
			this.outJson(response, new JSONResult(true, "服务机构注册审核成功"));
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, "服务器异常!异常信息:" + e.getLocalizedMessage()));
			logger.info("服务器异常!异常信息:" + e.getLocalizedMessage());
			throw new Exception("审核服务机构注册异常");
		}
	}
}
