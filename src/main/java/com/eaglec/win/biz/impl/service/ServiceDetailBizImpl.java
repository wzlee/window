package com.eaglec.win.biz.impl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eaglec.win.biz.service.ServiceDetailBiz;
import com.eaglec.win.dao.service.ServiceDetailDao;
import com.eaglec.win.domain.service.ServiceDetail;

@Service
public class ServiceDetailBizImpl implements ServiceDetailBiz {

	@Autowired
	private ServiceDetailDao serviceDetailDao;
	
	@Override
	public ServiceDetail create(ServiceDetail sd) {		
		return serviceDetailDao.save(sd);
	}

}
