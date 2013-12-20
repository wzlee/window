package com.eaglec.win.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eaglec.win.aop.NeedSession;
import com.eaglec.win.aop.SessionType;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.MD5;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONResult;

@Controller
@RequestMapping(value="/staff")
public class StaffController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(StaffController.class);
	@Autowired
	private StaffBiz staffBiz;
	@Autowired
	private UserBiz userBiz;	
	
	/**
	 * 添加子账号
	 * @author xuwf
	 * @since 2013-8-23
	 * 
	 * @param staff
	 * @param parentId	主账号id(查询在该主账号下面是否存在相同用户名的子账号)
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/save")
	public void add(
			@RequestParam(value="parentId",required=false)Integer parentId,
			@RequestParam(value="managerId",required=false)Integer managerId,
			Staff staff,HttpServletRequest request,HttpServletResponse response) {
		logger.info("queryParams[parentId:"+parentId+",managerId:"+managerId+"]");
		try {
			Long staffTotal = staffBiz.getTotal(parentId);
			Integer staffNum = Integer.valueOf(String.valueOf(staffTotal));	//子账号个数
			if(staffNum >=Constant.CUSER_NUM){
				this.outJson(response, new JSONResult(false, "每个企业子账号数量限制20个,如需添加,请联系管理员"));
				return;
			}
			if("".equals(staff.getUserName().trim())){//服务器端验证不能为空
				this.outJson(response, new JSONResult(false, "用户名不能为空"));
				return;
			}
			//得到子账号所属的主账号
			User parent = userBiz.findUserById(parentId);
			staff.setParent(parent);
			Staff staff1 = staffBiz.findByUserName(parent.getUserName()+staff.getUserName(),parentId);
			if(null != staff1){
				this.outJson(response, new JSONResult(false, "该用户名已经存在,请重新输入"));
				return;
			}
			//得到子账号的分配人(登录用户)
			Manager manager= (Manager) request.getSession().getAttribute("manager");
			staff.setManager(manager);
			//密码md5加密
			staff.setPassword(MD5.toMD5(staff.getPassword()));	
			//分配时间为当前时间
			String assignTime = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
			staff.setAssignTime(assignTime);
			
			//子账号用户名=主账号用户名+自己填写的个性化名称
			staff.setUserName(parent.getUserName()+staff.getUserName());	
			staffBiz.save(staff);
			this.outJson(response,new JSONResult(true,"子账号添加成功!"));
			logger.info("[ "+staff.getUserName()+" ]添加成功!");
		} catch (Exception e) {
			this.outJson(response,new JSONResult(false,"子账号保存失败!异常信息:"+e.getLocalizedMessage()));
			logger.info("子账号保存失败!异常信息:"+e.getMessage());
		}
	}
	
	/**
	 * 查询子账号列表
	 * @author xuwf
	 * @since 2013-8-23
	 * 
	 * @param parentId	主账号id	
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value="query")
	public void find(
			@RequestParam(value="parentId",required=false)Integer parentId,
			@RequestParam("start")Integer start,
			@RequestParam("limit")Integer limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[parentId:"+parentId+",start:"+start+",limit:"+limit+"]");
		JSONData<Staff> services = staffBiz.findStaff(parentId, start, limit);
		this.outJson(response,services);
	}
	
	/**
	 * 子账号用户状态更改(禁用,删除,恢复)
	 *@author xuwf
	 *@since 2013-8-23
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/update")
	public void apply(Staff staff,HttpServletRequest request,
			HttpServletResponse response) {
			try {
				staffBiz.update(staff);
				logger.info("[ "+staff.getUserName()+" ]更新成功!");
				this.outJson(response,new JSONResult(true,"子帐号更新成功!"));
			} catch (Exception e) {
				this.outJson(response,new JSONResult(false,"子帐号更新失败!异常信息:"+e.getLocalizedMessage()));
				logger.info("子帐号更新失败!异常信息:"+e.getLocalizedMessage());
			}
	}
	
	/**
	 * 子账号编辑
	 *@author xuwf
	 *@since 2013-8-28
	 *
	 *@param Staff 
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/edit")
	public void update(Staff staff,HttpServletRequest request,HttpServletResponse response) {
			logger.info("staff:"+staff.toString());
			try {
				staffBiz.update(staff);
				logger.info("[ "+staff.getUserName()+" ]修改成功!");
				this.outJson(response,new JSONResult(true,"子帐号[" + staff.getUserName() + "]修改成功"));
			} catch (Exception e) {
				this.outJson(response,new JSONResult(false,"子帐号[" + staff.getUserName() + "]修改失败!异常信息:"+e.getLocalizedMessage()));
				logger.info("帐号修改失败!异常信息:"+e.getLocalizedMessage());
			}
	}
	
	/**
	 * 运营平台加载子账号角色
	 * @author xuwf
	 * @since 2013-10-19
	 *
	 * @param request
	 * @param response
	 */
//	@NeedSession(SessionType.MANAGER)
//	@RequestMapping(value = "/loadStaffRole")
//	public void loadStaffRole(@RequestParam(value="enterpriseType",defaultValue="",required=false)Integer enterpriseType, HttpServletRequest request,HttpServletResponse response) {
//		logger.info(request.getParameterMap().toString());
//		try {
//			
//			if(!StringUtils.isEmpty(enterpriseType)){
//				List<StaffRole> staffRoleList = new ArrayList<StaffRole>();
//				if(enterpriseType >= Constant.ENTERPRISE_TYPE_ORG){//机构用户
//					staffRoleList = staffRoleBiz.queryRole(Constant.ROLE_SELLER_ENTERPRISE);
//				}else{//企业用户
//					staffRoleList = staffRoleBiz.queryRole(Constant.ROLE_BUYER_ENTERPRISE);
//				}		
//				
//				this.outJson(response,staffRoleList);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("类别加载失败:" + e.getLocalizedMessage());
//			super.outJson(response, new JSONResult(false, "类别加载失败!"));
//		}
//	}
}
