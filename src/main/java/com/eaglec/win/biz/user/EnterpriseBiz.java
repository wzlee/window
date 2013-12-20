package com.eaglec.win.biz.user;
import java.util.List;

import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.view.JSONData;


/**
 * 
 * 封装对企业的相关操作
 * 
 * @author pangyf
 * @since 2013-8-22
 * 
 */
public interface EnterpriseBiz {
	
	public abstract JSONData<Enterprise> findEnterpriseListById(String id,int start, int limit);
	
	public abstract Enterprise loadEnterpriseByEid(int eid);
	
	public Enterprise save(Enterprise e);
	
	public abstract List<Enterprise> findEnterpriseByIcRegNumber(String icRegNumber);
	
	/**
	 * 
	 * 根据Type查找企业
	 * 
	 * @author pangyf
	 * @since 2013-9-10
	 * 
	 */
	public abstract List<Enterprise> findEnterpriseByType(Integer type);
	
	public abstract List<Enterprise> findEnterpriseByName(String name);
	public abstract Enterprise findEnterpriseByEid(String eid);
	
	public Enterprise update(Enterprise e);
	
	public abstract JSONData<Enterprise> findByName(String enterpName, int limit, int start);
	
	public abstract JSONData<Enterprise> findEnterpriseListByCid(Integer cid, String name, Integer industryType, String businessPattern, int start, int limit);
	
	/**
	 * 查找行业机构Top10(子窗口首页显示)	按年营业额降序排序
	 * @author xuwf
	 * @since 2013-9-13
	 * 
	 * @param type	企业类型(服务机构)	
	 * @return
	 */
	public abstract List<Enterprise> findTopTen(Integer type,int start,int limit);

	/**
	 * 查找可以添加为top10的服务机构
	 * 
	 * @author huyj
	 * @sicen 2013-11-2
	 * @return
	 */
	public abstract List<Enterprise> findCanAddEnter();
}
