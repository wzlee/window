package com.eaglec.win.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eaglec.win.biz.flat.FlatBiz;
import com.eaglec.win.domain.base.Flat;
import com.eaglec.win.view.JSONResult;

/**
 * 窗口平台信息维护
 * @author liuliping
 * @since 2013-10-12
 * */
@Controller
@RequestMapping(value = "/flat")
public class FlatController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(FlatController.class);
	
	@Autowired
	private FlatBiz flatBiz;
	
	
	/**
	 * 添加或修改窗口平台信息
	 * @author liuliping
	 * @since 2013-10-12
	 * @param flat 平台信息对象
	 * @return JSONData
	 * */
	@RequestMapping(value = "/addOrUpdate")
	public void addOrUpdate(Flat flat, HttpServletRequest request,HttpServletResponse response) {
		try {
			flatBiz.addOrUpdate(flat);
			outJson(response, new JSONResult(true, "操作成功"));
		} catch (Exception e) {
			outJson(response, new JSONResult(false, "操作失败"));
			e.printStackTrace();
			logger.info("添加或者修改平台'" + flat.getFlatName() +"'信息异常" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 根据平台名称查询平台信息
	 * @author liuliping
	 * @since 2013-10-12
	 * @param flatName 平台名称
	 * @return JSONData
	 * */
	@RequestMapping(value = "/query")
	public void queryByFlatName(String flatName, HttpServletRequest request,HttpServletResponse response) {
		outJson(response, flatBiz.queryByFlatName(flatName));
	}
}



