package com.eaglec.win.biz.impl.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import com.eaglec.win.biz.service.ServiceBiz;
import com.eaglec.win.dao.publik.CategoryDao;
import com.eaglec.win.dao.publik.FileManagerDao;
import com.eaglec.win.dao.service.ServiceDao;
import com.eaglec.win.dao.service.ServiceDetailDao;
import com.eaglec.win.domain.base.Category;
import com.eaglec.win.domain.base.FileManager;
import com.eaglec.win.domain.service.Service;
import com.eaglec.win.domain.service.ServiceDetail;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONResult;
import com.eaglec.win.view.JSONRows;
import com.eaglec.win.view.ServiceView;

@org.springframework.stereotype.Service
public class ServiceBizImpl implements ServiceBiz, ServletContextAware {

	@Autowired
	private ServiceDao serviceDao;	
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private ServiceDetailDao serviceDetailDao;
	@Autowired
	private FileManagerDao fileManagerDaoImpl;
	@Autowired
	private ServletContext servletContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eaglec.win.service.impl.ServiceService#add(com.eaglec.win.domain
	 * .Service)
	 */
	@Override
	public Service add(Service service) {
		
		/* 新增服务时,服务对象的图片要和资源文件管理FileManager建立关联,方便统一管理资源文件 */
		if (!StringUtils.isEmpty(service.getPicture())) {
			FileManager fileManager = fileManagerDaoImpl.findOne(service
					.getPicture());
			fileManager.setClazz("Service");
			fileManager.setStatus(true);
			fileManagerDaoImpl.update(fileManager);
		}
		
		return serviceDao.save(service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eaglec.win.service.impl.ServiceService#findServiceById(java.lang
	 * .Integer)
	 */
	@Override
	public Service findServiceById(Integer id) {
		return serviceDao.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eaglec.win.service.impl.ServiceService#findServiceByName(java.lang
	 * .String)
	 */
	@Override
	public Service findServiceByName(String serviceName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", serviceName);
		return serviceDao.get("from Service where serviceName=:name", map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eaglec.win.service.impl.ServiceService#delete(com.eaglec.win.domain
	 * .Service)
	 */
	@Override
	public void delete(Service service) {
		serviceDao.delete(service);
		ServiceDetail sd = new ServiceDetail(service.getCurrentStatus(),
				service.getId(), service.getServiceName(), null);
		serviceDetailDao.save(sd);
	}

	@Override
	public List<Service> queryServiceByStatus(String serviceStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", serviceStatus);
		return serviceDao.findList("from Service where currentStatus=:status",
				map);
	}

	// xuwf
	@Override
	public JSONData<Service> queryStatistics(String currentStatus,
			String servicename,String enterprisename, int start, int limit) {
		String hql = "from Service s where enterprise.industryType = "+Common.windowId;
		if (!"".equals(currentStatus) && null != currentStatus) {
			hql += " and s.currentStatus in (" + currentStatus + ")";
		}
		if (!"".equals(servicename) && null != servicename) {
			hql += " and s.serviceName like '%" + servicename + "%'";
		}
		if(!StringUtils.isEmpty(enterprisename)){
			hql += " and s.enterprise.name like '%" + enterprisename + "%'";
		}
		hql += " order by s.registerTime desc";
		return serviceDao.outJSONData(hql, start, limit);
	}

	// xuwf
	@Override
	public JSONData<Service> queryByCatId(String sname, Integer catId,
			int start, int limit) {
		String hql = "from Service s where s.category.clazz = 'service'";
		if (!"".equals(sname.trim()) && null != sname) {
			hql += " and s.serviceName like '%" + sname + "%'";
		}
		if (catId != 1 && null != catId) {// 等于1代表所有类型
			Category category = categoryDao.get(catId); // 根据id得到该类别对象
			if (category.getLeaf() == false && category.getPid() != null) {
				hql += " and (s.category.pid = " + catId
						+ " or s.category.id = " + catId + ")";
			} else {
				hql += " and s.category.id = " + catId;
			}
		}
		hql += " order by s.registerTime desc";
		return serviceDao.outJSONData(hql, start, limit);
	}

	@Override
	public List<Service> findServiceListByStatus(String queryStatus, int start,
			int limit) {
		// begin 2013-8-12 by huyj
		String hql = "from Service where currentStatus in (" + queryStatus
				+ ")";
		return serviceDao.findList(hql, start, limit);
		// end 2013-8-12 by huyj
	}

	@Override
	public List<Service> findServiceListByStatus(String queryStatus, int start,
			int limit, String column) {
		String hql = "from Service where currentStatus in (" + queryStatus
				+ ") order by " + column + " desc";
		return serviceDao.findList(hql, start, limit);
	}

	@Override
	public List<Service> findServiceListByName(String serviceName, int limit,
			int start) {
		// begin 2013-8-12 by huyj
		String hql = "from Service where serviceName like '%" + serviceName
				+ "%'";
		return serviceDao.findList(hql, start, limit);
		// end 2013-8-12 by huyj
	}

	@Override
	public List<Service> findAll() {
		// begin 2013-8-12 by huyj
		String hql = "from Service where 1=1 order by id";
		return serviceDao.findList(hql);
		// end 2013-8-12 by huyj
	}

	@Override
	public List<Service> findServiceListById(String id, int start, int limit) {
		// begin 2013-8-12 by huyj
		String hql = "from Service where id in (" + id + ")";
		return serviceDao.findList(hql, start, limit);
		// end 2013-8-12 by huyj
	}

	// @Override
	// public void saveService(Service service) {
	// serviceDao.save(service);
	// }

	@Override
	public void updateService(Service service) {
		// begin 2013-8-12 by huyj
		serviceDao.update(service);
		// end 2013-8-12 by huyj
	}

	// @Override
	// public Service save(Service service) {
	// return serviceDao.saveOrUpdate(service);
	// }
	
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
	@Override
	public JSONRows<Service> findServiceList(String queryStatus, Integer eid,
			String serviceName, int page, int rows) {
		String hql = "from Service where currentStatus in(" + queryStatus
				+ ") and enterprise.id=" + eid;		
		if (StringUtils.isNotEmpty(serviceName)) {
			hql += " and serviceName like '%" + serviceName + "%' ";
		}
		hql += "order by registerTime desc";
		return serviceDao.outJSONRows(hql, page, rows);
	}
	
	@Override
	public JSONResult updateAudit(Service service, int access) {
		JSONResult jr = new JSONResult(false, null);
		Integer cs = null; // 服务当前状态
		Integer ls = null; // 服务上一个状态
		ServiceDetail sd = null; // 服务流水
		try {
			// Service service = serviceDao.get(id);
			cs = service.getCurrentStatus();
			ls = service.getLastStatus();

			if (access == 0) { // 通过审核,更改为下一个服务状态
				switch (cs) {
				case Constant.SERVICE_STATUS_ADDED_AUDIT: // 上架审核中
					service.setCurrentStatus(Constant.SERVICE_STATUS_ADDED); // 已上架
					break;
				case Constant.SERVICE_STATUS_CHANGE_AUDIT: // 变更审核中
					service.setCurrentStatus(ls);
					break;
				case Constant.SERVICE_STATUS_DOWN_AUDIT: // 下架审核中
					service.setCurrentStatus(Constant.SERVICE_STATUS_DOWN); // 已下架
					break;
				default:
					break;
				}
			} else { // 未通过审核
				service.setCurrentStatus(ls); // 恢复上一个状态
			}
			service.setLastStatus(cs);
			service.setLocked(false); // 解锁编辑
			
			if ((cs == Constant.SERVICE_STATUS_CHANGE_AUDIT) && (access == 1)) {	//拒绝变更变更审核时候				
				sd = serviceDetailDao.get(service.getDetailsId());
				sd.setOperatStatus(Constant.SERVICE_AUDIT_FAIL);
				Service entity = serviceDao.get(service.getId());
				entity.setCurrentStatus(Constant.SERVICE_STATUS_ADDED);
				entity.setLastStatus(Constant.SERVICE_STATUS_CHANGE_AUDIT);
				
				serviceDao.update(entity);
				jr.setSuccess(true);
				jr.setMessage("更新服务状态成功");
			} else {
				serviceDao.update(service);
				sd = new ServiceDetail(service.getCurrentStatus(), service.getId(),	service.getServiceName(), ls);
				serviceDetailDao.save(sd);
				jr.setSuccess(true);
				jr.setMessage("更新服务状态成功");
			}		
			
			return jr;
		} catch (Exception e) {
			jr.setSuccess(false);
			jr.setMessage("更新服务状态失败!");
			e.printStackTrace();
			return jr;
		}
	}
	
	/**
	 * 服务审核
	 * 
	 * @author liuliping
	 * @since 2013-10-15
	 * */
	@Override
	public Map<String, Object>  updateAudit2(Service service, int access, String context, String operationUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();	//返回值
		JSONResult jr = new JSONResult(false, null);
		Integer cs = null; // 服务当前状态
		Integer ls = null; // 服务上一个状态
		ServiceDetail nsd = null;
		try {
			// Service service = serviceDao.get(id);
			cs = service.getCurrentStatus();
			ls = service.getLastStatus();

			if (access == 0) { // 通过审核,更改为下一个服务状态
				switch (cs) {
				case Constant.SERVICE_STATUS_ADDED_AUDIT: // 上架审核中
					service.setCurrentStatus(Constant.SERVICE_STATUS_ADDED); // 已上架
					break;
				case Constant.SERVICE_STATUS_CHANGE_AUDIT: // 变更审核中
					service.setCurrentStatus(ls);
					break;
				case Constant.SERVICE_STATUS_DOWN_AUDIT: // 下架审核中
					service.setCurrentStatus(Constant.SERVICE_STATUS_DOWN); // 已下架
					break;
				default:
					break;
				}
				service.setLastStatus(cs);
				nsd = new ServiceDetail(Constant.SERVICE_AUDIT_PASS,
						service.getId(), service.getServiceName(),
						service.getLastStatus());
			} else { // 未通过审核
				service.setCurrentStatus(ls); // 恢复上一个状态
				service.setLastStatus(cs);
				nsd = new ServiceDetail(Constant.SERVICE_AUDIT_FAIL,
						service.getId(), service.getServiceName(),
						service.getLastStatus());
			}
			service.setLocked(false); // 解锁编辑

			/*
			 * 当拒绝变更审核时候,将流水表数据状态更新; 反之,将流水表的数据更新到服务表中去
			 */
			if ((cs == Constant.SERVICE_STATUS_CHANGE_AUDIT) && (access == 1)) { // 拒绝变更变更审核时候
				Service entity = serviceDao.get(service.getId());
				entity.setCurrentStatus(Constant.SERVICE_STATUS_ADDED);
				entity.setLastStatus(Constant.SERVICE_STATUS_CHANGE_AUDIT);
				serviceDao.update(entity);
			} else {
				serviceDao.update(service);
			}
			nsd.setBelongPlat(Common.windowId);
			nsd.setOperationUser(operationUser);
			nsd.setUserType(Constant.LOGIN_MANAGER);
			nsd.setContext(context);
			serviceDetailDao.save(nsd);
			jr.setSuccess(true);
			jr.setMessage("更新服务状态成功");
			resultMap.put("jsonResult", jr);
			resultMap.put("serviceDetail", nsd);
			resultMap.put("success", true);
			return resultMap;
		} catch (Exception e) {
			jr.setMessage("更新服务状态失败!");
			resultMap.put("jsonResult", jr);
			resultMap.put("success", false);
			e.printStackTrace();
			return resultMap;
		}
	}

	@Override
	public Service update(Service service) {
		Service entity = serviceDao.get(service.getId());

		/*
		 * 上传的图片与已有的图片不相同时有以下几种情况 1.上传了图片，服务原本没有图片 2.上传了图片，服务原本有图片
		 * 3.未上传图片，服务原本有图片
		 */
		String picture = service.getPicture();
		String e_picture = entity.getPicture();
		if ((picture != null) && (!picture.equals(e_picture))) { // 服务的图片名和持久化对象的图片名不同时,进行更新
			if (!StringUtils.isEmpty(e_picture)) {
				FileManager fileManager = fileManagerDaoImpl.findOne(e_picture);
				if (fileManager != null) {
					File file = new File(
							servletContext.getRealPath(File.separator
									+ "upload" + File.separator + e_picture));
					file.delete();
					fileManagerDaoImpl.delete(fileManager);
				}
			}
			if (!StringUtils.isEmpty(picture)) { // 上传的图片不为空时，就建立与service的关联
				FileManager f = fileManagerDaoImpl.findOne(picture);
				f.setStatus(true);
				f.setClazz("Service");
				fileManagerDaoImpl.update(f);
			}
		}

		BeanUtils.copyProperties(service, entity); // 复制service的属性值到持久化对象entity
		return serviceDao.update(entity);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub

	}

	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @description：根据类别ID查询该类别下的所有服务(前10条)
	 */
	@Override
	public List<Service> queryServiceByCategoryId(int categoryId, int start, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("from Service where category.id = ").append(categoryId)
		  .append(" and currentStatus in (")
		  .append(Constant.SERVICE_STATUS_ADDED).append(",")
		  .append(Constant.SERVICE_STATUS_CHANGE_AUDIT).append(",")
		  .append(Constant.SERVICE_STATUS_DOWN_AUDIT).append(")")
		  .append(" and enterprise.status = ").append(Constant.ENTERPRISE_STATUS_EFFECTIVE);
	  //String hql = "from Service where category.id="+ categoryId +" and currentStatus=" + Constant.SERVICE_STATUS_ADDED;
		return serviceDao.findList(sb.toString(), start, limit);
	}
	
	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @description：根据类别ID查询该类别下的所有服务
	 */
	@Override
	public List<Service> queryServiceByCategoryId(int categoryId) {
		String hql = "from Service where category.id="+ categoryId +" and currentStatus=" + Constant.SERVICE_STATUS_ADDED;
		return serviceDao.findList(hql);
	}

	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @description：获取热门服务(默认前六条)
	 */
	@Override
	public List<Service> getHotService(int start, int limit) {
		String hql = "from Service where currentStatus = '" + Constant.SERVICE_STATUS_ADDED + "' order by serviceNum desc";
		return serviceDao.findList(hql, start, limit);
	}

	/**
	 * @date: 2013-8-26
	 * @author：lwch
	 * @description：获取最新服务(默认前三条)
	 */
	public List<Service> getNewService(int start, int limit) {
		String hql = "from Service where currentStatus = '" + Constant.SERVICE_STATUS_ADDED + "' order by registerTime desc";
		return serviceDao.findList(hql, start, limit);
	}

	/**
	 * @date: 2013-08-28
	 * @author：liuliping
	 * @description：首页 获取服务对象部分数据,默认按id降序
	 * @param start
	 *            起始
	 * @param limit
	 *            分页条数
	 * @param order
	 *            hql语句排序,例如"serviceNum DESC,"
	 * @param cid
	 *            服务分类id
	 * @return list 返回服务部分数据列表
	 */
	public JSONData<ServiceView> findServicesData(int start, int limit,
			String order, Integer cid) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT new com.eaglec.win.view.ServiceView(s.id,s.picture,s.serviceName,s.costPrice,s.serviceProcedure,s.enterprise.id,s.enterprise.name,s.enterprise.status, s.enterprise.isApproved,s.registerTime,s.serviceNum,s.tel,s.email,s.evaluateScore) FROM Service s WHERE s.currentStatus in (")
		  .append(Constant.SERVICE_STATUS_ADDED).append(",")
	      .append(Constant.SERVICE_STATUS_CHANGE_AUDIT).append(",")
		  .append(Constant.SERVICE_STATUS_DOWN_AUDIT).append(")")
		  .append(" and s.enterprise.status = ").append(Constant.ENTERPRISE_STATUS_EFFECTIVE)
		  .append(" and s.enterprise.industryType = ").append(Common.windowId);
		if ((cid != null) && (cid.intValue() > 0)) {
			sb.append(" and s.category.id=").append(cid);
		}
		sb.append(" ORDER BY ").append(order).append(" s.id DESC");
		JSONData<ServiceView> jd = serviceDao.findObjects(sb.toString(), start,
				limit);
		return jd;
	}

	@Override
	public void buyService(Integer sid, Integer uid, String name,
			String telNum, String email) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONData<Service> findServiceListByQuerytatusAndServiceName(
			String queryStatus, String serviceName, String enterprisename,int start, int limit) {
		String hql = "from Service where enterprise.industryType = "+Common.windowId+
				" and currentStatus in(" + queryStatus + ")";		
		if (queryStatus
				.contains(Integer.toString(Constant.SERVICE_STATUS_DOWN))
				&& queryStatus.contains(Integer
						.toString(Constant.SERVICE_STATUS_ADDED_AUDIT))) {
			hql += " and lastStatus not in ("+ Constant.SERVICE_STATUS_NEW + ")";
		}
		if (StringUtils.isNotEmpty(serviceName)) { 
			hql += " and serviceName like '%" + serviceName + "%'";
		}
		if(!StringUtils.isEmpty(enterprisename)){
			hql += " and enterprise.name like '%" + enterprisename + "%'";
		}
		hql += "order by registerTime DESC";
		return serviceDao.outJSONData(hql, start, limit);
	}
	
	/**
	 * 查找待审核的服务
	 * */
	public JSONData<Service> findServiceAudit(
			String queryStatus, String serviceName, String enterprisename,int start, int limit) {
		/*第一步，查询出待审核的服务*/
		StringBuilder hql4Service = new StringBuilder();
		hql4Service.append("FROM Service WHERE enterprise.industryType = ").append(Common.windowId)
				.append(" AND currentStatus in(").append(queryStatus).append(")");
		if (!StringUtils.isEmpty(serviceName)) {
			hql4Service.append(" AND serviceName like '%").append(serviceName).append("%'");
		}
		if(!StringUtils.isEmpty(enterprisename)){
			hql4Service.append(" AND enterprise.name like '%").append(enterprisename).append("%'");
		}
		hql4Service.append(" ORDER BY id DESC");
		JSONData<Service> jd = serviceDao.outJSONData(hql4Service.toString(), start, limit);
		
		/*第二步，查询变更审核的服务流水*/
		for (Service temp : jd.getData()) {
			if((temp.getDetailsId() != null) && 
					(temp.getCurrentStatus().intValue() == Constant.SERVICE_STATUS_CHANGE_AUDIT)) {
				ServiceDetail detail = serviceDetailDao.get(temp.getDetailsId());
				if(null !=detail)
				{
					temp.setLinkMan(detail.getLinkMan());
					temp.setTel(detail.getTel());
					temp.setEmail(detail.getEmail());
					temp.setServiceMethod(detail.getServiceMethod());
					temp.setServiceProcedure(detail.getServiceProcedure());
					temp.setChargeMethod(detail.getChargeMethod());
					temp.setCostPrice (detail.getCostPrice());
					temp.setPicture (detail.getPicture());
				}
			}
		}
		/*第三部，输出*/
		jd.setSuccess(true);
		return jd;
	}

	/**
	 * 根据服务名称分页查询
	 *@author liuliping
	 *@since 2013-8-12 
	 *
	 *@param serviceName 服务名称
	 *@param order 查询结果按此降序
	 *@param start 分页起始
	 *@param limit 每页结果数量
	 *@return List<Service>
	 */
	public JSONData<ServiceView> queryByName(String serviceName, String order, int start, int limit) {
		StringBuilder sb = new StringBuilder();
		sb.append(
			"SELECT new com.eaglec.win.view.ServiceView(s.id,s.picture,s.serviceName,s.costPrice,s.serviceProcedure,s.enterprise.id,s.enterprise.name,s.enterprise.status,s.enterprise.isApproved,s.registerTime,s.serviceNum,s.tel,s.email,s.evaluateScore) FROM Service s WHERE s.currentStatus in (")
			.append(Constant.SERVICE_STATUS_ADDED).append(",")
			.append(Constant.SERVICE_STATUS_CHANGE_AUDIT).append(",")
			.append(Constant.SERVICE_STATUS_DOWN_AUDIT).append(")")
			.append(" and s.enterprise.status = ").append(Constant.ENTERPRISE_STATUS_EFFECTIVE);
//		sb.append("SELECT new com.eaglec.win.view.ServiceView(s.id,s.picture,s.serviceName,s.costPrice,s.serviceProcedure,s.enterprise.id,s.enterprise.name, s.enterprise.isApproved,s.registerTime,s.serviceNum,s.tel,s.email,s.evaluateScore) FROM Service s WHERE s.currentStatus =").
//			append(Constant.SERVICE_STATUS_ADDED);
		if (!StringUtils.isEmpty(serviceName)) {
			sb.append(" AND s.serviceName like '%");
			sb.append(serviceName).append("%'");
		}
		sb.append(" ORDER BY s.").append(order).append(" DESC");
		JSONData<ServiceView> jd = serviceDao.findObjects(sb.toString(), start, limit);
		return jd;
	}

	/**
	 * 
	 */
	@Override
	public Service updateUservice(Service service) {
		Service _serivice = null;
		// 申请变更时，添加一条流水信息，记录要更改的内容
		ServiceDetail serviceDetail = new ServiceDetail(service, Constant.SERVICE_AUDIT_NORMAL);		
		serviceDetail.setBelongPlat(Common.windowId);		
		serviceDetail = serviceDetailDao.save(serviceDetail);
		// 通过serviceid获取服务
		service = serviceDao.get(service.getId());
		// 设置服务流水标识
		service.setDetailsId(serviceDetail.getId());
		service.setLastStatus(Constant.SERVICE_STATUS_ADDED);
		service.setCurrentStatus(Constant.SERVICE_STATUS_CHANGE_AUDIT);
		_serivice = serviceDao.update(service);
		return _serivice;
	}

	@Override
	public List<Service> queryTopthree() {
		String hql = "from Service where currentStatus in("+Constant.SERVICE_STATUS_ADDED+","+Constant.SERVICE_STATUS_DOWN_AUDIT+")";		
		return serviceDao.findList(hql);
	}
}