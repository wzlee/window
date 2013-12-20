package com.eaglec.win.biz.cms;

import java.util.List;

import com.eaglec.win.domain.cms.Module;

public interface ModuleBiz {
	
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：获取该频道下的所有的模块
	 */
	public List<Module> findAll(String mchannel);
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：获取所有的模版
	 */
	public List<?> findAllTemplate();
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：添加模块
	 */
	public int add(Module m);
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：删除模块
	 */
	public void delete(Module m);
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：修改模块
	 */
	public void update(Module m);
}
