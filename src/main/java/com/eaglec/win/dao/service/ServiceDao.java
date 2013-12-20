package com.eaglec.win.dao.service;

import com.eaglec.win.dao.BaseDao;
import com.eaglec.win.domain.service.Service;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.ServiceView;

/**
 * 服务Dao层
 * 提供对服务的数据操作
 * <br/>
 * 注：该层已继承了BaseDao，所有基本方法不须再写
 * @author Xiadi
 * @since 2013-8-12
 * 
 */
public interface ServiceDao extends BaseDao<Service>{
	public JSONData<ServiceView> findObjects(String hql, int start, int limit);
	
}