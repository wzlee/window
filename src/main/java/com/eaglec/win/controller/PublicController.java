package com.eaglec.win.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eaglec.win.biz.auth.ManagerBiz;
import com.eaglec.win.biz.auth.MenuBiz;
import com.eaglec.win.biz.auth.RoleBiz;
import com.eaglec.win.biz.publik.FileManagerBiz;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.auth.Menu;
import com.eaglec.win.domain.base.FileManager;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.ImageUtil;
import com.eaglec.win.utils.RecursionTree;
import com.eaglec.win.utils.StrUtils;
import com.eaglec.win.view.JSONResult;

@Controller
@RequestMapping(value = "/public")
public class PublicController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PublicController.class);
	
	@Autowired
	private ManagerBiz managerBiz;
	@Resource
	private MenuBiz menuMangerBiz;
	@Autowired
	private RoleBiz roleManagerBiz;
	@Autowired
	private FileManagerBiz fileManagerBiz;
	
	/**
	 * 验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/authcode", method = RequestMethod.GET)
	public void validateCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		ImageUtil.randCaptcha(request, response);
	}
	
	/**
	 * ajax检查验证码
	 * @param checkcode
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/authcode", method = RequestMethod.POST)
	public void passportLogin(@RequestParam(required = true) String checkcode, 
			HttpServletRequest request, HttpServletResponse response) {
		if(checkcode.equals(request.getSession().getAttribute("authcode").toString())){
			this.outJson(response, new JSONResult(true, "验证码正确"));
		}else{
			this.outJson(response, new JSONResult(false, "验证码输入错误"));
		}
	}
	
	@RequestMapping("check")
	public void validatelogin(HttpServletResponse response,HttpServletRequest request){
		Manager manager = (Manager) request.getSession().getAttribute("manager");
		if(manager == null){
			this.outJson(response,new JSONResult(false,"会话未创建或已过期!"));
		}else{
			this.outJson(response,new JSONResult(true,"会话验证成功!"));
		}
	}


	@RequestMapping("login")
	public void managerlogin(HttpServletResponse response,
			HttpServletRequest request, String password, String username) {
		String msg = "用户名或密码不正确，请重新输入";
		if (!StringUtils.isEmpty(username)) {
			Manager manager = managerBiz.findUserByUsername(username);
			if (manager != null) {
				if (manager.getUserStatus() != 1) {
					msg = "该用户无权限登录!";
				} else if (null == manager.getRole()) {
					msg = "该用户未分配任何角色，请先为此用户分配角色!";
				} else {
					List<Menu> list = RecursionTree.getResult(menuMangerBiz
							.findMenus(manager.getRole().getMenuIds()));
					if (manager.getPassword().equals(password)) {
						request.getSession().setAttribute("manager", manager);
						request.getSession().setAttribute(
								manager.getUsername(),
								JSON.toJSONString(list.toArray()));
						this.outJson(response, new JSONResult(true,
								"登陆成功!页面即将跳转..."));
					}
				}
			}
		}
		this.outJson(response, new JSONResult(false, msg));
	}

	@RequestMapping("userlogout")
	public void userlogout(HttpServletResponse response,HttpServletRequest request,Manager manager){
		try {
			request.getSession().removeAttribute("manager");
			this.outJson(response, new JSONResult(true, "退出登录成功!"));
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "退出登录失败!"));
		}
	}

	/**
	 * 异步文件上传处理，返回文件名
	 * @author XiaDi
	 * @since 2013-7-23
	 * 
	 * @param file 文件
	 * @eg admin/channel.jsp 
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		try {
			String contextPath = request.getSession().getServletContext().getRealPath(Common.uploadPath);
			File filepath = new File(contextPath);
			FileManager fileManager = new FileManager();
			
			if (!filepath.exists()) {
				filepath.mkdir();
				logger.info("[" + filepath.getAbsolutePath() + "]创建成功!");
			}
			
			int index = file.getOriginalFilename().lastIndexOf(".");
			long timestamp = System.currentTimeMillis();
			String orgFileName = timestamp + file.getOriginalFilename().substring(index);
			File orgFile = new File(contextPath, orgFileName);
			file.transferTo(orgFile);
			/*保存文件对象信息*/
			String date = StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date());
			fileManager.setFname(Common.windowDomain + "/" + Common.uploadPath + "/" + orgFileName);
			fileManager.setDate(date);
			fileManagerBiz.saveFileManager(fileManager);			
			super.outJson(response, new JSONResult(true, Common.windowDomain + "/" + Common.uploadPath + "/" + orgFile.getName()));
		} catch (Exception e) {
			logger.info("Exception异常:" + e.getLocalizedMessage());
			super.outJson(response, new JSONResult(false, e.getLocalizedMessage()));
		}
	}
	
	/**
	 * kindeditor异步文件上传处理
	 * @author liuliping
	 * @since 2013-10-29
	 * 
	 * @param imgFile 文件
	 * @eg 支撑平台中的htmleditor
	 */
	@RequestMapping(value = "/uploadByKindedior", method = RequestMethod.POST)
	public void uploadByKindeditor(@RequestParam MultipartFile imgFile, HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getSession().getServletContext().getRealPath(Common.uploadPath);
		File filepath = new File(contextPath);
		FileManager fileManager = new FileManager();
		
		if (!filepath.exists()) {
			filepath.mkdir();
			logger.info("[" + filepath.getAbsolutePath() + "]创建成功!");
		}
		try {
			int index = imgFile.getOriginalFilename().lastIndexOf(".");
			long timestamp = System.currentTimeMillis();
			String orgFileName = timestamp + imgFile.getOriginalFilename().substring(index);
			File orgFile = new File(contextPath, orgFileName);
			imgFile.transferTo(orgFile);
			
			/*保存文件对象信息*/
			String date = StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date());
			fileManager.setFname(orgFileName);
			fileManager.setDate(date);
			fileManagerBiz.saveFileManager(fileManager);
			
			JSONObject obj = new JSONObject();
			obj.put("error", 0);
			obj.put("url",  request.getContextPath() + "/upload/" + orgFile.getName());
			super.outJson(response, obj);
		} catch (Exception e) {
			logger.info("Exception异常:" + e.getLocalizedMessage());
			JSONObject obj = new JSONObject();
			obj.put("error", 0);
			obj.put("message", "上传失败");
			super.outJson(response, obj);
		}
	}
	
	/**
	 * 异步删除文件，返回状态（true or false）
	 * @author XiaDi
	 * @since 2013-7-24
	 * 
	 * @param fileName 文件名
	 * @param response
	 */
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public void deleteFile(@RequestParam String fileName, HttpServletRequest request, HttpServletResponse response){
		String contextPath = request.getSession().getServletContext().getRealPath(Common.uploadPath);
		File orgFile = new File(contextPath, fileName);
		boolean b = false;
		if (orgFile.exists()) {
			b = orgFile.delete();
		}
		super.outJson(response, new JSONResult(true, b + ""));
	}
	
}
