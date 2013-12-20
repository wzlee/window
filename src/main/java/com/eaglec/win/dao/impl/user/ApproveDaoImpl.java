package com.eaglec.win.dao.impl.user;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.eaglec.win.dao.impl.BaseDaoImpl;
import com.eaglec.win.dao.user.ApproveDao;
import com.eaglec.win.domain.base.ApprovedDetail;

@Service
public class ApproveDaoImpl extends BaseDaoImpl<ApprovedDetail> implements ApproveDao  {
	@Override
	public int add(ApprovedDetail o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.add(o);
	}
	
	@Override
	public ApprovedDetail save(ApprovedDetail o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.save(o);
	}
	
	@Override
	public Map<String, Object> save(ApprovedDetail o, Object obj) {
		o.setModifyTime(System.currentTimeMillis());
		return super.save(o, obj);
	}
	
	@Override
	public ApprovedDetail saveOrUpdate(ApprovedDetail o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.saveOrUpdate(o);
	}
	
	@Override
	public ApprovedDetail update(ApprovedDetail o) {
		o.setModifyTime(System.currentTimeMillis());
		return super.update(o);
	}
}
