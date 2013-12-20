package com.eaglec.win.biz.impl.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.business.OrderOperateLogBiz;
import com.eaglec.win.dao.business.OrderOperateLogDao;
import com.eaglec.win.domain.business.OrderOperateLog;

@Service
public class OrderOperateLogBizImpl implements OrderOperateLogBiz {
	@Autowired
	private OrderOperateLogDao orderOperateLogDao;
	
	@Override
	public void saveOrderOperLog(OrderOperateLog orderOperateLog) {
		orderOperateLogDao.save(orderOperateLog);
	}

}
