package com.eaglec.win.biz.user;

import java.util.List;

import com.eaglec.win.domain.base.OrganRecommendation;
import com.eaglec.win.view.JSONData;

/**
 * 配置top的接口
 * 
 * @author huyj
 * 
 */
public interface OrganRecommendationBiz {

	/**
	 * 查找所有
	 * 
	 * @author huyj
	 * @sicen 2013-11-1
	 * @return
	 */
	public abstract List<OrganRecommendation> fingAll();

	/**
	 * 添加
	 * 
	 * @author huyj
	 * @sicen 2013-11-1
	 * @param organRecommendation
	 * @return
	 */
	public abstract OrganRecommendation add(
			OrganRecommendation organRecommendation);

	/**
	 * 修改
	 * 
	 * @author huyj
	 * @sicen 2013-11-1
	 * @param organRecommendation
	 * @return
	 */
	public abstract OrganRecommendation update(
			OrganRecommendation organRecommendation);

	/**
	 * 删除
	 * 
	 * @author huyj
	 * @sicen 2013-11-1
	 * @param id
	 */
	public abstract void delete(Integer id);

	/**
	 * 查找所有 json格式
	 * 
	 * @author huyj
	 * @sicen 2013-11-1
	 * @return
	 */
	public abstract JSONData<OrganRecommendation> fingJsonAll();

}
