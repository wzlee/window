package com.eaglec.win.dao.impl.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eaglec.win.dao.impl.BaseDaoImpl;
import com.eaglec.win.dao.service.ServiceDao;
import com.eaglec.win.domain.service.Service;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.ServiceView;

@Repository
public class ServiceDaoImpl extends BaseDaoImpl<Service> implements ServiceDao {
	
	public JSONData<ServiceView> findObjects(String hql, int start, int limit) {
		int total = 0;
		JSONData<ServiceView> jd = new JSONData<ServiceView>();
		Query query = getCurrentSession().createQuery(hql);
		total = query.list().size();
		
		@SuppressWarnings("unchecked")
		List<ServiceView> list = query.setFirstResult(start)
				.setMaxResults(limit).list();
		jd.setData(list);
		jd.setTotal(total);
		return jd;
	}
	
	@Override
	public int add(Service o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.add(o);
	}
	
	@Override
	public Service save(Service o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.save(o);
	}
	
	@Override
	public Map<String, Object> save(Service o, Object obj) {
		o.setModifyTime(System.currentTimeMillis());
		return super.save(o, obj);
	}
	
	@Override
	public Service saveOrUpdate(Service o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.saveOrUpdate(o);
	}
	
	@Override
	public Service update(Service o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.update(o);
	}
}