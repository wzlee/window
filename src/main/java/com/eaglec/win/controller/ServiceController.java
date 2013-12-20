package com.eaglec.win.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.eaglec.win.aop.NeedSession;
import com.eaglec.win.aop.SessionType;
import com.eaglec.win.biz.cms.ChannelBiz;
import com.eaglec.win.biz.publik.CategoryBiz;
import com.eaglec.win.biz.service.ServiceBiz;
import com.eaglec.win.biz.service.ServiceDetailBiz;
import com.eaglec.win.biz.service.ServiceRecommendationBiz;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.base.Category;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.cms.Channel;
import com.eaglec.win.domain.service.Service;
import com.eaglec.win.domain.service.ServiceDetail;
import com.eaglec.win.domain.service.ServiceRecommendation;
import com.eaglec.win.sync.DataSync;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONResult;
import com.eaglec.win.view.ServiceView;

@Controller
@RequestMapping(value = "/service")
public class ServiceController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceController.class);

	@Autowired
	private ServiceBiz serviceBiz;
	@Autowired
	private CategoryBiz categoryBiz;
	@Autowired
	private EnterpriseBiz enterpriseBiz;
	@Autowired
	private ChannelBiz channelBiz;
	@Autowired
	private ServiceRecommendationBiz servicerecommendationlbiz;
	@Autowired
	private ServiceDetailBiz serviceDetailBiz;

	/**
	 * 为运营平台自己添加服务
	 * 
	 * @author lizhiwei
	 * @since 2013-8-20
	 * 
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/save")
	public void add(Service service, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			//得到运营支撑系统的组织机构号
			String icRegNumber = Common.icRegNumber;			
			service.setServiceNo(service.getCategory().getCode() + "-" + Long.toString(new Date().getTime()));
			List<Enterprise> list = enterpriseBiz.findEnterpriseByIcRegNumber(icRegNumber);
			if(!list.isEmpty()){
				Enterprise enterprise = list.get(0);
				service.setEnterprise(enterprise);
				//serviceBiz.add(service);
				this.outJson(response, new JSONResult(true, "服务添加成功!"));
				logger.info("[ " + service.getServiceName() + " ]添加成功!");
			}else {
				this.outJson(response, new JSONResult(false, "不存在此组织机构号"));
			}
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "服务保存失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("服务保存失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 为机构添加服务
	 * 
	 * @author lizhiwei
	 * @since 2013-8-20
	 * 
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/saveforuser")
	public void addForUser(Service service, HttpServletRequest request,	HttpServletResponse response) {
		try {
			service.setServiceNo(categoryBiz.findById("service",service.getCategory().getId()).getCode() + "-"+ Long.toString(new Date().getTime()));
			Enterprise enterpirse = enterpriseBiz.loadEnterpriseByEid(service.getEnterprise().getId());
			service.setEnterprise(enterpirse);
			Manager manager = (Manager)request.getSession().getAttribute("manager");
			//保存服务
			Service _service = serviceBiz.add(service);
			//同步服务
			DataSync.createDataSync("service", "sid", service.getSid(), "false");
			//创建流水
			ServiceDetail sd = new ServiceDetail(_service, Constant.SERVICE_AUDIT_NORMAL, _service.getCurrentStatus());			
			sd.setOperationUser(manager.getUsername());
			sd.setUserType(Constant.LOGIN_MANAGER);
			sd.setBelongPlat(Common.windowId);
			//保存流水
			serviceDetailBiz.create(sd);
			//同步服务流水
			DataSync.createDataSync("servicedetail", "sdid", sd.getSdid(), "false");			
			this.outJson(response, new JSONResult(true, "服务添加成功!"));
			logger.info("[ " + service.getServiceName() + " ]服务添加成功!");					
		} catch (Exception e) {
			this.outJson(response,new JSONResult(false, "服务保存失败!异常信息:"+ e.getLocalizedMessage()));
			logger.info("服务保存失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 查询服务，按状态查询
	 * 
	 * @author lizhiwei
	 * @since 2013-8-15
	 * 
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/query")
	public void query(
			@RequestParam("queryStatus") String queryStatus,
			@RequestParam(value = "serviceName", defaultValue = "", required = false) String serviceName,
			@RequestParam(value = "enterprisename", defaultValue = "", required = false) String enterprisename,
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "25", required = false) int limit,
			HttpServletRequest request, HttpServletResponse response) {
		this.outJson(response, serviceBiz
				.findServiceListByQuerytatusAndServiceName(queryStatus,
						serviceName, enterprisename,start, limit));
	}
	
	/**
	 * 查询已经是top3的服务
	 * 
	 * @author cs
	 * @since 2013-8-15
	 * 
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/findTopthree",method=RequestMethod.POST)
	public void findTopthree(HttpServletRequest request, HttpServletResponse response) {
//		List<ServiceRecommendation> newlist = new ArrayList<ServiceRecommendation>();
		List<ServiceRecommendation> list = servicerecommendationlbiz.fingAll();
		
		for (Iterator<ServiceRecommendation> sit = list.iterator(); sit.hasNext();) {
			ServiceRecommendation servicerecommendation = sit.next();
			Service service = servicerecommendation.getService();
			Integer status = service.getCurrentStatus();
			if (status != Constant.SERVICE_STATUS_ADDED && status != Constant.SERVICE_STATUS_DOWN_AUDIT) {
				sit.remove();
				servicerecommendationlbiz.delete(servicerecommendation.getId());
			}
		}
		
		this.outJson(response, list);
	}
	
	/**
	 * 查询满足条件已上架和下架审核中的服务到combox中去
	 * 
	 * @author cs
	 * @since 2013-8-15
	 * 
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/queryTopthree",method=RequestMethod.POST)
	public void queryTopthree(HttpServletRequest request, HttpServletResponse response) {
		this.outJson(response, serviceBiz.queryTopthree());
	}
	
	/**
	 * 删除top3服务
	 * 
	 * @author cs
	 * @since 2013-8-15
	 * 
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/deletetop",method=RequestMethod.POST)
	public void deletetop(HttpServletRequest request, HttpServletResponse response,Integer id) {
		try{
			servicerecommendationlbiz.delete(id);
			this.outJson(response, new JSONResult(true, "删除成功！"));
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "删除失败！"));
		}
	}
	
	/**
	 * 删除top3服务
	 * 
	 * @author cs
	 * @since 2013-8-15
	 * 
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/updatetop",method=RequestMethod.POST)
	public void updatetop(HttpServletRequest request, HttpServletResponse response,Integer id,Integer topthree,Integer serviceid) {
		try {
			ServiceRecommendation s = null;
			if(id==null){
				s = new ServiceRecommendation();
				s.setTopthree(topthree);
				Service service = serviceBiz.findServiceById(serviceid);
				Integer status = service.getCurrentStatus();
				if(status != Constant.SERVICE_STATUS_ADDED && status != Constant.SERVICE_STATUS_DOWN_AUDIT){
					this.outJson(response, new JSONResult(false, "保存失败，该服务状态无法被推荐！"));
					return;
				}
				s.setService(service);
				
				servicerecommendationlbiz.add(s);
				this.outJson(response, new JSONResult(true, "保存成功！"));
			}else{
				s = servicerecommendationlbiz.fingOne(id);
				s.setTopthree(topthree);
				Service service = serviceBiz.findServiceById(serviceid);
				Integer status = service.getCurrentStatus();
				if(status != Constant.SERVICE_STATUS_ADDED && status != Constant.SERVICE_STATUS_DOWN_AUDIT){
					this.outJson(response, new JSONResult(false, "保存失败，该服务状态无法被推荐！"));
					return;
				}
				s.setService(service);
				
				servicerecommendationlbiz.update(s);
				this.outJson(response, new JSONResult(true, "保存成功！"));
			}
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "保存失败，系统异常了！"));
			e.printStackTrace();
		}
	}
	
	/**
	 * 审核服务查询
	 * 
	 * @author liuliping
	 * @since 2013-09-14
	 * 
	 * @param queryStatus 查询状态
	 * @param serviceName 服务名称
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/queryaudit")
	public void queryaudit(
			@RequestParam("queryStatus") String queryStatus,
			@RequestParam(value = "serviceName", defaultValue = "", required = false) String serviceName,
			@RequestParam(value = "enterprisename", defaultValue = "", required = false) String enterprisename,
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "25", required = false) int limit,
			HttpServletRequest request, HttpServletResponse response) {
		JSONData<Service> jd = serviceBiz.findServiceAudit(queryStatus, serviceName,enterprisename,start, limit);
		outJson(response, jd);
	}

	/**
	 * 前台查询服务，按状态查询
	 * 
	 * @author pangyf
	 * @since 2013-9-6
	 * 
	 * @param queryStatus
	 * @param serviceName
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/queryservice")
	public void queryService(
			@RequestParam("queryStatus")String queryStatus,
			@RequestParam("eid")Integer eid,
			@RequestParam(value="serviceName",defaultValue="",required=false) String serviceName,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "rows", required = false) int rows,
			HttpServletRequest request, HttpServletResponse response) {
		this.outJson(
				response,
				serviceBiz.findServiceList(queryStatus, eid, serviceName, rows
						* (page - 1), rows));
	}

	/**
	 * 查询服务，按某列降序
	 * 
	 * @author liuliping
	 * @since 2013-08-24
	 * 
	 * @param
	 * @return
	 * @eg
	 */
	@RequestMapping(value = "/queryByColumn")
	public void queryByColumn(
			@RequestParam("queryStatus") String queryStatus,
			@RequestParam(value = "column", defaultValue = "id", required = false) String column,
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "12", required = false) int limit,
			HttpServletRequest request, HttpServletResponse response) {
		List<Service> list = serviceBiz.findServiceListByStatus(queryStatus,
				start, limit, column);
		outJson(response, list);
	}

	/**
	 * 跳转服务详情页面
	 * 
	 * @author liuliping
	 * @since 2013-08-30
	 * 
	 * @param id
	 *            服务对象的id
	 * @return
	 * @eg
	 */
	@RequestMapping(value = "/detail")
	public String toDetail(Integer id, String op, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if ((id != null) && (id.intValue() > 0)) {
			Service service = serviceBiz.findServiceById(id);
			// 查询该服务所属类别
			Category category = service.getCategory();
			// 如果此类别还有父类，得到父类
			if (category.getPid() != null && category.getLeaf()) {
				Category pcategory = categoryBiz.findById("service",
						category.getPid());
				model.addAttribute("pcategory", pcategory);
			}

			model.addAttribute("service", service);
			model.addAttribute("category", category);
		}
		return "module/service/detail";
	}

	/**
	 * 跳转到服务搜索结果页面 显示的样式分为两种:矩形和列表形
	 * 
	 * @author liuliping
	 * @since 2013-08-30
	 * 
	 * @param name
	 *            搜索服务的名字
	 * @param encodedName
	 *            编码过的搜索关键字(ASCII码16进制)
	 * @param type
	 *            服务结果显示的方式(0:矩形;1:列表形)
	 * @param order
	 *            查询结果按此参数降序 (服务次数:'serviceNum',评价:'',时间:'',价格:'')
	 * @return
	 * @eg
	 */
	@RequestMapping(value = "/result")
	public String toResult(
			@RequestParam(value = "name", defaultValue = "", required = true) String name,
			String encodedName,
			@RequestParam(value = "type", defaultValue = "0", required = true) int type,
			@RequestParam(value = "order", defaultValue = "serviceNum", required = false) String order,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		int limit = 0;
		String redirect = null; // 重定向的访问地址
		String start = request.getParameter("pager.offset");
		start = StringUtils.isEmpty(start) ? "0" : start;
		if (type ==Constant.RESULT_TYPE_BIG) {
			limit = Common.resultNumJ; // 分页查询单页结果数量(大图)
			redirect = "module/service/result_1";
		} else {
			limit = Common.resultNumL; // 分页查询单页结果数量(列表图)
			redirect = "module/service/result_2";
		}

		/*
		 * 由于分页标签pager只能通过get请求传递分页的参数,所以需要对中文进行编码成ASCII十六进制码,
		 * encodeName这个参数就是name的编码后的字符串
		 */
		if ((StringUtils.isEmpty(name)) && !(StringUtils.isEmpty(encodedName))) {
			try {
				name = URLDecoder.decode(encodedName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isEmpty(encodedName)) {
			try {
				encodedName = URLEncoder.encode(name, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		JSONData<ServiceView> jd = serviceBiz.queryByName(name, order,
				Integer.parseInt(start), limit); // 分页查询

		// 加载枢纽平台平道列表
		@SuppressWarnings("unchecked")
		List<Channel> cl = (List<Channel>) request.getSession().getAttribute(
				"channelList");
		if (cl == null) {
			cl = channelBiz.findChnnelByCtype(Constant.CATEGORY_ID);
			request.getSession().setAttribute("channelList", cl);
		}

		model.addAttribute("name", name);
		model.addAttribute("encodedName", encodedName);
		model.addAttribute("order", order);
		/*****把serviceview里面的评价先四舍五入*****/
		List<ServiceView> serviceviews = jd.getData();
		for (ServiceView serviceView : serviceviews) {
			serviceView.setEvaluateScore(Common.round(serviceView.getEvaluateScore()).intValue());
		}
		/**********************************/
		model.addAttribute("serviceViews", jd.getData());
		model.addAttribute("total", jd.getTotal());
		return redirect;
	}

	/**
	 * 服务列表页的分页
	 * 
	 * @author liuliping
	 * @param order
	 *            服务端按照次列名来数据查询排序
	 * @param cid
	 *            服务类别id
	 * @param serviceName
	 *            服务名称
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/paging")
	public String paging(
			@RequestParam(value = "order", defaultValue = "", required = false) String order,
			@RequestParam(value = "limit", defaultValue = "9", required = false) int limit,
			@RequestParam(value = "cid", defaultValue = "", required = false) Integer cid,
			String serviceName, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {
		String start = request.getParameter("pager.offset"); // 分页起始参数
		start = StringUtils.isEmpty(start) ? "0" : start;
		if (!StringUtils.isEmpty(order)) { // 查询结果根据order来降序
			String[] columns = order.split(",");
			for (int i = 0; i < columns.length; i++) {
				order = columns[i] + " DESC,";
			}
		}
		JSONData<ServiceView> jd = serviceBiz.findServicesData(
				Integer.parseInt(start), limit, order, cid);

		/* 加载枢纽平台平道列表 */
		@SuppressWarnings("unchecked")
		List<Channel> cl = (List<Channel>) request.getSession().getAttribute(
				"channelList");
		if (cl == null) {
			cl = channelBiz.findChnnelByCtype(Constant.CATEGORY_ID);
			request.getSession().setAttribute("channelList", cl);
		}

		/* 服务类别信息,面包屑显示 */
		Category category = categoryBiz.findById("service", cid);
		Category pcategory = categoryBiz.findById("service", category.getPid());

		model.addAttribute("cid", cid);
		model.addAttribute("pcategoryName", pcategory.getText());
		model.addAttribute("categoryName", category.getText());
		model.addAttribute("start", start);
		model.addAttribute("serviceViews", jd.getData());
		model.addAttribute("total", jd.getTotal());
		return "module/service/list";
	}

	/**
	 * 服务查询输出部分数据，结果集按某列降序
	 * 
	 * @author liuliping
	 * @since 2013-08-24
	 * 
	 * @param order
	 *            结果排序hql语句，默认是"",例如"serviceNum, id"
	 * @param start
	 *            起始数
	 * @param limit
	 *            分页大小
	 * @return
	 * @eg
	 */
	@RequestMapping(value = "/getServiceData")
	public void getServiceData(
			@RequestParam(value = "order", defaultValue = "", required = false) String order,
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "15", required = false) int limit,
			@RequestParam(value = "cid", defaultValue = "", required = false) Integer cid,
			HttpServletRequest request, HttpServletResponse response) {
		if (!StringUtils.isEmpty(order)) {
			String[] columns = order.split(",");
			for (int i = 0; i < columns.length; i++) {
				order = columns[i] + " DESC,";
			}
		}
		JSONData<ServiceView> jd = serviceBiz.findServicesData(start, limit,
				order, cid);
		outJson(response, jd.getData());
	}

	/**
	 * * 查询服务，按状态和服务名称（模糊查询）查询
	 * 
	 * @author xuwf
	 * @since 2013-8-15
	 * 
	 * @param status
	 *            服务状态
	 * @param currentPage
	 *            当前页
	 * @param start
	 *            起始值
	 * @param limit
	 *            每页条数
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/find")
	public void find(
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sname", required = false) String sname,
			@RequestParam(value = "enterprisename", required = false) String enterprisename,
			@RequestParam("page") Integer page,
			@RequestParam("start") Integer start,
			@RequestParam("limit") Integer limit, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[ status:" + status + ",sname," + sname+ ",enterprisename"+enterprisename
				+ "currentPage:" + page + ",start:" + start + ",limit:" + limit
				+ "]");
		if (null == status) {
			status = "";
		}
		JSONData<Service> services = serviceBiz.queryStatistics(status, sname,enterprisename,
				start, limit);
		this.outJson(response, services);
	}

	/**
	 * * 查询服务，按类别和服务名称（模糊查询）查询
	 * 
	 * @author xuwf
	 * @since 2013-8-19
	 * 
	 * @param sname
	 *            服务名称
	 * @param catId
	 *            类别编号
	 * @param currentPage
	 *            当前页
	 * @param start
	 *            起始值
	 * @param limit
	 *            每页条数
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/findByCate")
	public void findByCate(
			@RequestParam(value = "sname", required = false) String sname,
			@RequestParam(value = "catId", required = false) String cateId,
			@RequestParam("page") Integer page,
			@RequestParam("start") Integer start,
			@RequestParam("limit") Integer limit, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[catId:" + cateId + ",sname," + sname
				+ "currentPage:" + page + ",start:" + start + ",limit:" + limit
				+ "]");
		Integer catId = null;
		try {
			catId = Integer.parseInt(cateId);
		} catch (NumberFormatException e) {
			catId = 1;
		}
		JSONData<Service> services = serviceBiz.queryByCatId(sname, catId,
				start, limit);
		this.outJson(response, services);
	}

	/**
	 * 服务信息编辑(新服务、已删除)
	 * @author pangyf
	 * @since 2013-8-14
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/edit")
	public void update(Service service, HttpServletRequest request,	HttpServletResponse response) {
		logger.info("queryString:" + request.getQueryString());
		try {
			/*************服务信息修改**************/
			Service _service = serviceBiz.findServiceById(service.getId());
			String[] properties = {"serviceName","category","serviceMethod","linkMan","tel","email","costPrice","picture","serviceProcedure"};
			this.copyProperties(service, _service, properties);
			Manager manager = (Manager)request.getSession().getAttribute("manager");			
			serviceBiz.update(_service);
			DataSync.createDataSync("service", "sid", _service.getSid(), "false");
			ServiceDetail sd = new ServiceDetail(_service, Constant.SERVICE_AUDIT_NORMAL, _service.getCurrentStatus());			
			sd.setOperationUser(manager.getUsername());
			sd.setUserType(Constant.LOGIN_MANAGER);
			sd.setBelongPlat(Common.windowId);
			serviceDetailBiz.create(sd);
			DataSync.createDataSync("servicedetail", "sdid", sd.getSdid(), "false");
			this.outJson(response, new JSONResult(true, "服务本地修改成功,同步枢纽平台成功!"));
		    logger.info("[ " + service.getServiceName() + " ]本地修改成功,同步枢纽平台成功!");
		} catch (Exception e) {
			this.outJson(response,new JSONResult(false, service.getCurrentStatus() + "|服务编辑失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("服务编辑失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 服务状态更新(上、下架,删除)
	 * @author pangyf
	 * @since 2013-8-14
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/update")	
	public void apply(Service service, HttpServletRequest request,HttpServletResponse response) {
		logger.info("queryString:" + request.getQueryString());
		try {			
			/*************服务状态修改**************/
			Service _service = serviceBiz.findServiceById(service.getId());			
			String[] properties = {"currentStatus","lastStatus","locked"};
			this.copyProperties(service, _service, properties);
			Manager manager = (Manager)request.getSession().getAttribute("manager");			
			serviceBiz.update(_service);
			if(_service.getCurrentStatus() == Constant.SERVICE_STATUS_DELETEED){
				if(_service.getLastStatus() != null || _service.getLastStatus() == Constant.SERVICE_STATUS_DOWN){
					DataSync.createDataSync("service", "sid", _service.getSid(), "true");
				}else {
					DataSync.createDataSync("service", "sid", _service.getSid(), "false");
				}				
			}else {
				DataSync.createDataSync("service", "sid", _service.getSid(), "false");
			}			
			ServiceDetail sd = new ServiceDetail(_service, Constant.SERVICE_AUDIT_NORMAL, _service.getCurrentStatus());
			sd.setOperationUser(manager.getUsername());
			sd.setUserType(Constant.LOGIN_MANAGER);
			sd.setBelongPlat(Common.windowId);
			serviceDetailBiz.create(sd);
			DataSync.createDataSync("servicedetail", "sdid", sd.getSdid(), "false");			
			this.outJson(response, new JSONResult(true, "服务本地更新成功,同步枢纽平台成功!"));
			logger.info("[ " + service.getServiceName() + " ]本地更新成功,同步枢纽平台成功!");
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "服务更新失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("服务更新失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 前台服务更新(上下架,变更，删除)
	 * 
	 * @author pangyf
	 * @since 2013-9-7
	 * 
	 * @param id
	 * @param currentStatus
	 * @return
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value = "/statuschange")
	public void statuschange(@RequestParam("id") Integer id,@RequestParam("currentStatus") Integer currentStatus,	HttpServletRequest request, HttpServletResponse response) {
		logger.info("queryString:" + request.getQueryString());
		try {
			Service service = serviceBiz.findServiceById(id);
			service.setLastStatus(service.getCurrentStatus());
			service.setCurrentStatus(currentStatus);
			User user = (User)request.getSession().getAttribute("user");			
			serviceBiz.update(service);	
			DataSync.createDataSync("service", "sid", service.getSid(), "true");
			ServiceDetail sd = new ServiceDetail(service, Constant.SERVICE_AUDIT_NORMAL, service.getCurrentStatus());			
			sd.setOperationUser(user.getUserName());
			sd.setUserType(Constant.LOGIN_MANAGER);
			sd.setBelongPlat(Common.windowId);
			serviceDetailBiz.create(sd);
			DataSync.createDataSync("servicedetail", "sdid", sd.getSdid(), "false");			
			logger.info("[ " + service.getServiceName() + " ]更新成功!");
			this.outJson(response, new JSONResult(true, "服务更新成功!"));
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "服务更新失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("服务更新失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 用户在前台页面申请服务,即购买服务
	 * 
	 * @author liuliping
	 * @since 2013-8-31
	 * 
	 * @param sid
	 *            服务id(必须)
	 * @param uid
	 *            用户id(必须)
	 * @param name
	 *            申请人姓名(必须)
	 * @param telNum
	 *            联系电话(必须)
	 * @param email
	 *            邮箱(必须)
	 * @return
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value = "/buy")
	public void buyService(Integer sid, Integer uid, String name,
			String telNum, String email, HttpServletRequest request,
			HttpServletResponse response) {
		serviceBiz.buyService(sid, uid, name, telNum, email);
	}

	/**
	 * 删除服务
	 * 
	 * @author lizhiwei
	 * @since 2013-8-15
	 * 
	 * @param service
	 * @return
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/delete")
	public void delete(@RequestParam("services") String services,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("queryString:" + request.getQueryString());
		List<Service> list = JSON.parseArray(services, Service.class);
		try {
			for (Service service : list) {
				serviceBiz.delete(service);
				logger.info("[ " + service.getServiceName() + " ]删除成功!");
			}
			this.outJson(response, new JSONResult(true, "服务删除成功!"));
		} catch (Exception e) {
			this.outJson(
					response,
					new JSONResult(false, "服务删除失败!异常信息:"
							+ e.getLocalizedMessage()));
			logger.info("服务删除失败!异常信息:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 审核服务
	 * 
	 * @author liuliping
	 * @since 2013-08-15
	 * 
	 * @param id
	 *            服务主键
	 * @return access 服务审核:0通过,1未通过
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/audit")
	public void audit(Service service, Integer access, String context, 
			HttpServletRequest request,HttpServletResponse response) {
		//Service obj = serviceBiz.findServiceById(service.getId());
		Manager manager = (Manager)request.getSession().getAttribute("manager");
		Map<String, Object> resultMap = serviceBiz.updateAudit2(service, access, context, manager.getUsername());
		Boolean success = (Boolean)resultMap.get("success");
		if(success.booleanValue() == true) {	//业务层执行成功
			ServiceDetail nsd = (ServiceDetail)resultMap.get("serviceDetail");
			if(access == 0) {	//如果审核通过则，同步至所有窗口（此时服务状态为已上架，已下架）
				DataSync.createDataSync("service", "sid", service.getSid(), "true");				
				// 同步至枢纽 服务流水
				DataSync.createDataSync("servicedetail", "sdid", nsd.getSdid(), "false");				
			} else {
				DataSync.createDataSync("service", "sid", service.getSid(), "false");				
				// 同步至枢纽 服务流水
				DataSync.createDataSync("servicedetail", "sdid", nsd.getSdid(), "false");
			}
//			else if(service.getCurrentStatus() == Constant.SERVICE_STATUS_NEW) {		//新服务不同步
//				
//			}
		} 
		outJson(response, resultMap.get("jsonResult"));
	}
//	public void audit(Service service, Integer access,HttpServletRequest request, HttpServletResponse response) {
//		try {
//			JSONResult jr = serviceBiz.updateAudit(service, access);
//			outJson(response, jr);			
//		}catch(Exception e){
//			
//		}
//	}

	/**
	 * @author huyj
	 * @sicen 2013-8-14
	 * @description 更新服务内容
	 * @param service
	 *            服务对象
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/updateComments")
	public void updateComments(Service service, HttpServletRequest request,	HttpServletResponse response) {
		try {
			Manager manager = (Manager)request.getSession().getAttribute("manager");			
			// 申请变更时，添加一条流水信息，记录要更改的内容
			ServiceDetail serviceDetail = new ServiceDetail(service,
					Constant.SERVICE_AUDIT_NORMAL,
					Constant.SERVICE_STATUS_CHANGE_AUDIT);			
			serviceDetail.setOperationUser(manager.getUsername());
			serviceDetail.setUserType(Constant.LOGIN_MANAGER);
			serviceDetail.setBelongPlat(Common.windowId);		
			serviceDetail = serviceDetailBiz.create(serviceDetail);
			DataSync.createDataSync("servicedetail", "sdid", serviceDetail.getSdid(), "true");		
			Service _serivice = serviceBiz.findServiceById(service.getId());			
			// 设置服务流水标识
			_serivice.setDetailsId(serviceDetail.getId());
			_serivice.setLastStatus(Constant.SERVICE_STATUS_ADDED);
			_serivice.setCurrentStatus(Constant.SERVICE_STATUS_CHANGE_AUDIT);
			serviceBiz.update(_serivice);
			DataSync.createDataSync("service", "sid", _serivice.getSid(), "false");
			logger.info("[ " + service.getServiceName() + " ]申请变更成功!");
			this.outJson(response, new JSONResult(true, "服务申请变更成功!"));
		} catch (Exception e) {
			this.outJson( response,new JSONResult(false, "服务申请变更失败!异常信息:" + e.getLocalizedMessage()));
		}
	}
}
