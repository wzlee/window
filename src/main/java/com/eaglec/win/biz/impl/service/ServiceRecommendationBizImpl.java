package com.eaglec.win.biz.impl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.service.ServiceRecommendationBiz;
import com.eaglec.win.dao.service.ServiceRecommendationDao;
import com.eaglec.win.domain.service.ServiceRecommendation;

@Service
public class ServiceRecommendationBizImpl implements ServiceRecommendationBiz {
	
	@Autowired
	private ServiceRecommendationDao servicerecommendationdao;

	@Override
	public List<ServiceRecommendation> fingAll() {
		List<ServiceRecommendation> list = servicerecommendationdao.findList("from ServiceRecommendation");
		return list == null ? new ArrayList<ServiceRecommendation>() : list;
	}

	@Override
	public ServiceRecommendation fingOne(Integer id) {
		return servicerecommendationdao.get(id);
	}

	@Override
	public ServiceRecommendation add(ServiceRecommendation serviceRecommendation) {
		return servicerecommendationdao.save(serviceRecommendation);
	}

	@Override
	public ServiceRecommendation update(ServiceRecommendation serviceRecommendation) {
		return servicerecommendationdao.update(serviceRecommendation);
	}

	@Override
	public void delete(Integer id) {
		servicerecommendationdao.delete(id);
	}

}
