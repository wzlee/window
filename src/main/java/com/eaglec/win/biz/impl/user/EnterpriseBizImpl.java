package com.eaglec.win.biz.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eaglec.win.biz.publik.CategoryBiz;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.dao.user.EnterpriseDao;
import com.eaglec.win.domain.base.Category;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.view.JSONData;

@Service
public class EnterpriseBizImpl implements EnterpriseBiz {
	@Autowired
	private EnterpriseDao enterpriseDao;

	@Autowired
	private CategoryBiz categoryBiz;
	@Override
	public JSONData<Enterprise> findEnterpriseListById(String id,int start, int limit) {
		
		String hql = "from Enterprise where id="+ id;
		
		return enterpriseDao.outJSONData(hql,start, limit);
	}

	@Override
	public Enterprise loadEnterpriseByEid(int eid) {
		return enterpriseDao.get(eid);
	}
	
	
	
	@Override
	public Enterprise save(Enterprise e) {
		return enterpriseDao.saveOrUpdate(e);
	}

	@Override
	public List<Enterprise> findEnterpriseByIcRegNumber(String icRegNumber) {
		String hql = "from Enterprise where icRegNumber= '" + icRegNumber + "'";
		return enterpriseDao.findList(hql);
	}

	@Override
	public List<Enterprise> findEnterpriseByType(Integer type) {
		String hql = "from Enterprise where industryType = "+Common.windowId+" and type= '" + type + "' and status=" + Constant.ENTERPRISE_STATUS_EFFECTIVE;
		return enterpriseDao.findList(hql);
	}
	
	@Override
	public Enterprise update(Enterprise e) {
		return enterpriseDao.update(e);
	}

	
	@Override
	public List<Enterprise> findTopTen(Integer type,int start,int limit) {
		String hql = "from Enterprise e where 1=1";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(type)){
			hql += " and type = :type";
			params.put("type", type);
		}
		hql += " order by e.salesRevenue desc";
		return enterpriseDao.findList(hql, params,start,limit);
	}	

	@Override
	public List<Enterprise> findEnterpriseByName(String name) {
		String hql = "from Enterprise where status != "+Constant.ENTERPRISE_STATUS_DELETED+
			" and isApproved = true and name= '" + name + "'";
		return enterpriseDao.findList(hql);
	}

	@Override
	public JSONData<Enterprise> findByName(String enterpName, int limit,
			int start) {
		StringBuilder sb = new StringBuilder();
		sb.append("FROM Enterprise WHERE 1=1 and status = "); 
		sb.append(Constant.ENTERPRISE_STATUS_EFFECTIVE);//有效的服务机构
		if (!StringUtils.isEmpty(enterpName)) {
			sb.append(" AND name like '%").append(enterpName).append("%'");
		}
		sb.append("AND type = ").append(Constant.ENTERPRISE_TYPE_ORG).append(" ORDER BY id DESC");    //默认按ID降序
		JSONData<Enterprise> jd = enterpriseDao.outJSONData(sb.toString(), start, limit);
		return jd;
	}

	@Override
	public JSONData<Enterprise> findEnterpriseListByCid(Integer cid,
			String name, Integer industryType, String businessPattern,
			int start, int limit) {
		StringBuilder cids = null;    //二级分类的id
		if (cid != null) {
			//1.cid所对应的服务类别分为一级分类和二级分类.根据cid查询二级分类,并根据结果来执行hql语句
			List<Category> children = categoryBiz.findCategoryChildren(cid.intValue());    //查询子类别
			int size = children.size();
			cids = new StringBuilder();
			if (size > 0) {    //大于0表示cid对应一级分类
				for (int i = 0; i < size - 1; i++) {	
					cids.append(children.get(i).getId()).append(",");
				}
				cids.append(children.get(size - 1).getId());
			} else {
				cids.append(cid);
			}
		}
		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT DISTINCT s.enterprise FROM Service s WHERE 1 = 1");
		hql.append(" and s.enterprise.status = "+Constant.ENTERPRISE_STATUS_EFFECTIVE);//有效的服务机构
		if (cids != null) {
			hql.append(" AND s.category.id in(").append(cids).append(")");
		}
		if (!StringUtils.isEmpty(name)) {
			hql.append(" AND s.enterprise.name like '%").append(name).append("%'");
		}
		if (industryType != null) {
			hql.append(" AND s.enterprise.industryType = ").append(industryType);
		}
		if (!StringUtils.isEmpty(businessPattern)) {
			hql.append(" AND s.enterprise.businessPattern = '").append(businessPattern).append("'");
		}
		
		return enterpriseDao.outJSONData(hql.toString(), start, limit);
	}

	@Override
	public List<Enterprise> findCanAddEnter() {
		String hql = "from Enterprise where industryType = "+Common.windowId+" and status = 1 and type = 3";
		return enterpriseDao.findList(hql);
	}

	@Override
	public Enterprise findEnterpriseByEid(String eid) {
		String hql = "from Enterprise where eid = '"+eid+"'";
		List<Enterprise> list =  enterpriseDao.findList(hql);
		return list.size() == 0?null:list.get(0);
	}
	
}
