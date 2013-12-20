package com.eaglec.win.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.biz.user.OrganRecommendationBiz;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.base.OrganRecommendation;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONEntity;
import com.eaglec.win.view.JSONResult;

@Controller
@RequestMapping(value = "/enter")
public class EnterpriseController extends BaseController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);
	@Autowired
	private EnterpriseBiz enterpriseBiz;

	@Autowired
	private OrganRecommendationBiz recommendationBiz;

	/**
	 * 按eid查询所有的企业用户
	 * 
	 * @author pangyf
	 * @since 2013-8-22
	 * 
	 */
	@RequestMapping(value = "/query")
	public void query(
			@RequestParam(value = "eid", required = false) String eid,
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "30", required = false) int limit,
			HttpServletRequest request, HttpServletResponse response) {
		this.outJson(response,
				enterpriseBiz.findEnterpriseListById(eid, start, limit));
	}

	/**
	 * 根据Eid查询企业信息
	 * 
	 * @author wzlee
	 * @since 2013-8-24
	 * 
	 */
	@RequestMapping(value = "/load")
	public void load(@RequestParam(value = "eid", required = false) int eid,
			HttpServletRequest request, HttpServletResponse response) {
		this.outJson(
				response,
				new JSONEntity<Enterprise>(true, enterpriseBiz
						.loadEnterpriseByEid(eid)));
	}

	/**
	 * 新服务关联企业
	 * 
	 * @author pangyf
	 * @since 2013-9-3
	 * 
	 */
	@RequestMapping(value = "/check")
	public void getEnterprise(
			@RequestParam(value = "icRegNumber", required = false) String icRegNumber,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("新服务关联企业,组织机构号：" + icRegNumber);
		List<Enterprise> list = enterpriseBiz
				.findEnterpriseByIcRegNumber(icRegNumber);
		if (list.isEmpty()) {
			this.outJson(response, new JSONResult(false, "组织机构号输入有误,请重新再试!"));
		} else {
			Enterprise enterprise = list.get(0);
			if (enterprise.getType() == Constant.ENTERPRISE_TYPE_ORG) {
				this.outJson(response,
						new JSONResult(true, enterprise.getName()));
			} else {
				this.outJson(response, new JSONResult(false,
						"该企业没有认证为服务机构，不能添加服务"));
			}
		}
	}

	/**
	 * 查寻所有的服务机构
	 * 
	 * @author pangyf
	 * @since 2013-9-10
	 * 
	 */
	@RequestMapping(value = "/loadenter")
	public void loadEnter(HttpServletRequest request,
			HttpServletResponse response) {
		this.outJson(response, enterpriseBiz.findEnterpriseByType(Constant.ENTERPRISE_TYPE_ORG));
	}
	
	/**
	 * 跳转到服务机构搜索结果页
	 * 
	 * @author liuliping
	 * @sicen 2013-09-30
	 * @param name 服务机构名称
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/search")
	public String toResult(
			@RequestParam(value = "name", defaultValue = "", required = true)String name, 
			String encodedName, Integer industryType, String businessPattern,
			@RequestParam(required = true, defaultValue = "20", value = "limit")int limit, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String start = request.getParameter("pager.offset");    //分页起始参数
		start = StringUtils.isEmpty(start) ? "0" : start;
		Integer start_int = null;
		try{
			start_int = Integer.parseInt(start);
		} catch (NumberFormatException e) {
			logger.info(e.getLocalizedMessage());
		}		
		
		/*
		 * 由于分页标签pager只能通过get请求传递分页的参数,所以需要对中文进行编码成unicode十六进制码,
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
		JSONData<Enterprise> jd = enterpriseBiz.findByName(name,
				limit, start_int.intValue());    //根据服务机构名称搜索
		enterpriseBiz.findEnterpriseListByCid(null, encodedName, industryType, businessPattern, start_int, limit);
		
		model.addAttribute("total", jd.getTotal());
		model.addAttribute("enterprises", jd.getData());
		model.addAttribute("name", name);
		model.addAttribute("encodedName", encodedName);
		return "enterprise/org_search_result";
	}

	/**
	 * 查找服务机构top10
	 * 
	 * @author huyj
	 * @sicen 2013-11-2
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/findAllOrgan")
	public void findAllOrgan(HttpServletRequest request,
			HttpServletResponse response) {
		this.outJson(response, recommendationBiz.fingAll());
	}

	/**
	 * 添加机构top10配置
	 * 
	 * @author huyj
	 * @sicen 2013-11-1
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addOrgan")
	public void addOrgan(OrganRecommendation organ, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			organ.setEnterprise(enterpriseBiz.loadEnterpriseByEid(organ
					.getEnterprise().getId()));
			recommendationBiz.add(organ);
			this.outJson(response, new JSONResult(true, "添加成功"));
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "添加失败"));
			e.printStackTrace();
		}
	}


	/**
	 * 删除机构top10配置
	 * 
	 * @author huyj
	 * @sicen 2013-11-1
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/deleteOrgan")
	public void deleteOrgan(OrganRecommendation organ,
			HttpServletRequest request,
			HttpServletResponse response) {
		try {
			recommendationBiz.delete(organ.getId());
			this.outJson(response, new JSONResult(true, "删除成功"));
		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, "删除失败"));
			e.printStackTrace();
		}
	}
	
	/**
	 * 查找可以添加为机构top10的机构
	 * 
	 * @author huyj
	 * @sicen 2013-11-2
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/findTopEnter")
	public void findTopEnter(HttpServletRequest request,
			HttpServletResponse response) {
		this.outJson(response, enterpriseBiz.findCanAddEnter());
	}

}
