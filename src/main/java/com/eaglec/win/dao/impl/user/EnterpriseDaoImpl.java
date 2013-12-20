package com.eaglec.win.dao.impl.user;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eaglec.win.dao.impl.BaseDaoImpl;
import com.eaglec.win.dao.user.EnterpriseDao;
import com.eaglec.win.domain.base.Enterprise;
import com.eaglec.win.domain.service.Service;

@Repository
public class EnterpriseDaoImpl extends BaseDaoImpl<Enterprise> implements
		EnterpriseDao {

	@Override
	public int add(Enterprise o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.add(o);
	}
	
	@Override
	public Enterprise save(Enterprise o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.save(o);
	}
	
	@Override
	public Map<String, Object> save(Enterprise o, Object obj) {
		o.setModifyTime(System.currentTimeMillis());
		return super.save(o, obj);
	}
	
	@Override
	public Enterprise saveOrUpdate(Enterprise o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.saveOrUpdate(o);
	}
	
	@Override
	public Enterprise update(Enterprise o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.update(o);
	}
}
