package com.eaglec.win.biz.service;

import java.util.List;
import java.util.Map;

import com.eaglec.win.domain.service.Service;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONResult;
import com.eaglec.win.view.JSONRows;
import com.eaglec.win.view.ServiceView;

/**
 * 服务Service<br/>
 * 封装对服务的相关操作
 * 
 * @author Xiadi
 * @since 2013-8-9
 * 
 */
public interface ServiceBiz {

	/**
	 * 添加服务
	 *@author Xiadi
	 *@since 2013-8-12 
	 *
	 *@param service
	 *@return Service
	 */
	public abstract Service add(Service service);

	/**
	 * 申请服务
	 *@author liuliping
	 *@since 2013-8-31
	 *
	 *@param sid 服务id
	 *@param uid 用户id
	 *@param name 申请人姓名
	 *@param telNum 联系电话
	 *@param email 邮箱
	 *@return 
	 */
	public abstract void buyService(Integer sid, Integer uid, String name, String telNum, String email);
	
	/**
	 * 保存服务(saveOrUpdate)
	 *@author lizhiwei
	 *@since 2013-8-15 
	 *
	 *@param service
	 *@return Service
	 */
//	public abstract Service save(Service service);
	
	/**
	 * 保存服务(saveOrUpdate)
	 *@author lizhiwei
	 *@since 2013-8-15 
	 *
	 *@param service
	 *@return Service
	 */
	public abstract Service update(Service service);
	
	/**
	 * 根据id得到服务
	 *@author Xiadi
	 *@since 2013-8-12 
	 *
	 *@param id 服务id
	 *@return Service
	 */
	public abstract Service findServiceById(Integer id);

	/**
	 * 根据名称得到单个服务
	 *@author Xiadi
	 *@since 2013-8-12 
	 *
	 *@param serviceName
	 *@return Service
	 */
	public abstract Service findServiceByName(String serviceName);
	
	/**
	 * 查询某个状态下的所有服务
	 *@author Xiadi
	 *@since 2013-8-12 
	 *
	 *@param serviceName
	 *@return Service
	 */
	public abstract List<Service> queryServiceByStatus(String serviceStatus);

	/**
	 * 删除服务对象
	 *@author Xiadi
	 *@since 2013-8-12 
	 *
	 *@param service 服务对象
	 */
	public abstract void delete(Service service);
	
	
	/**
	 * 
	 * @author huyj
	 * @sicen 2013-8-12
	 * @description 根据服务状态查询服务，支持多状态查询 
	 * @param serviceStatus 服务状态 使用,分割 例：01,02,03
	 */
	public abstract  List<Service> findServiceListByStatus(String queryStatus,int start,int limit);
	
	/**
	 * 
	 * @author liuliping
	 * @sicen 2013-8-12
	 * @description 根据服务状态查询服务，支持多状态查询 ,并按某一列降序
	 * @param serviceStatus 服务状态 使用,分割 例：01,02,03
	 * @param column 按次列来降序查询
	 */
	public abstract List<Service> findServiceListByStatus(String queryStatus, int start,
			int limit, String column);
	
	/**
	 * 
	 * @author lizw
	 * @sicen 2013-8-12
	 * @description 根据服务状态查询服务，支持多状态查询 
	 * @param serviceStatus 服务状态 使用,分割 例：01,02,03
	 */
	public abstract JSONData<Service> findServiceListByQuerytatusAndServiceName(
			String queryStatus, String serviceName,String enterprisename, int start, int limit);
	
	/**
	 * 前台根据服务状态，企业ID，服务名称查询服务
	 * @author pangyf
	 * @since 2013-9-6
	 * @param queryStatus
	 * @param eid
	 * @param serviceName
	 * @param page
	 * @param rows
	 * @return
	 */
	public abstract JSONRows<Service> findServiceList(String queryStatus,
			Integer eid, String serviceName, int page, int rows);
	/**
	 * 
	 * @author huyj
	 * @sicen 2013-8-12
	 * @description 根据服务名称查询服务，支持模糊查询
	 * @param serviceStatus 服务名称 
	 */
	public abstract List<Service> findServiceListByName(String serviceName,int start,int limit);
	
	/**
	 * 
	 * @author huyj
	 * @sicen 2013-8-12
	 * @description 根据服务id查询服务
	 * @param id 服务名称  支持多id查询 每个id使用,分隔
	 */
	public abstract List<Service> findServiceListById(String id,int start,int limit);
	
	/**
	 * 
	 * @author huyj
	 * @sicen 2013-8-12
	 * @description 查找所有服务
	 * @return 所有服务
	 */
	public List<Service> findAll();
	
	/**
	 * 
	 * @author lizhiwei
	 * @sicen 2013-8-15
	 * @description 添加服务
	 * @param service 服务对象
	 */
//	public void saveService(Service service);
	
	/**
	 * 
	 * @author huyj
	 * @sicen 2013-8-12
	 * @description 修改服务信息及状态
	 * @param service 服务对象
	 */
	public void updateService(Service service);
	
	/**
	 * 根据服务状态和服务名字分页查询
	 * @author xuwf
	 * @since 2013-8-15
	 * 
	 * @param serviceStatus 服务状态
	 * @param servicename 服务名称  支持模糊查询
	 * @param start 起始
	 * @param limit 限制
	 * @return JSONData<Service> service列表 JSON格式
	 */
	public abstract JSONData<Service> queryStatistics(String currentStatus,String servicename,String enterprisename,int start,int limit);
	
	/**
	 * 根据服务类别和服务名字查询
	 * @author xuwf
	 * @since 2013-8-19
	 * 
	 * @param sname	服务名称  支持模糊查询
	 * @param catId 所属类别id
	 * @param start 起始
	 * @param limit 限制
	 * @return JSONData<Service> service列表 JSON格式
	 */
	public abstract JSONData<Service> queryByCatId(String sname,Integer catId,int start,int limit);
		
	/**
	 * 审核服务
	 * @author liuliping
	 * @since  2013-8-12
	 * 
	 * @param  service	服务对象
	 * @param access	服务审核通过:0,未通过:1
	 * @return	JSONResult	审核结果返回信息
	 */
	public abstract JSONResult updateAudit(Service service, int access);
	
	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @param categoryId 类别ID
	 * @description：根据类别ID查询该类别下的所有服务(前10条)
	 */
	public abstract List<Service> queryServiceByCategoryId(int categoryId, int start, int limit);
	
	/**
	 * 根据服务名称查询
	 *@author liuliping
	 *@since 2013-8-12 
	 *
	 *@param serviceName 服务名称
	 *@param order 查询结果按此降序
	 *@param start 分页起始
	 *@param limit 每页结果数量
	 *@return JSONData<ServiceView>
	 */
	public abstract JSONData<ServiceView> queryByName(String serviceName, String order, int start, int limit);
	
	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @param categoryId 类别ID
	 * @description：根据类别ID查询该类别下的所有服务
	 */
	public abstract List<Service> queryServiceByCategoryId(int categoryId);
	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @description：获取热门服务(默认前六条)
	 */
	public abstract List<Service> getHotService(int start, int limit);
	
	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @description：获取最新服务(默认前三条)
	 */
	public abstract List<Service> getNewService(int start, int limit);

	/**
	 * @date: 2013-08-28
	 * @author：liuliping
	 * @description： 获取服务对象部分数据,默认按id降序
	 * @param start 起始
	 * @param limit 分页条数
	 * @param order hql语句排序,例如"serviceNum DESC,"
	 * @param cid 服务分类id
	 * @return JSONData
	 */
	public abstract JSONData<ServiceView> findServicesData(
			int start, int limit, String order, Integer cid);
	
	/**
	 * 
	 * @author huyj
	 * @sicen 2013-9-14
	 * @description 已上架服务，申请变更
	 * @param service
	 *            服务对象
	 * @return
	 */
	public abstract Service updateUservice(Service service);
	
	public abstract JSONData<Service> findServiceAudit( 
			String queryStatus, String serviceName,String enterprisename,int start, int limit);

	Map<String, Object> updateAudit2(Service service, int access, String context, String operationUser);

	public abstract List<Service> queryTopthree();
	
}