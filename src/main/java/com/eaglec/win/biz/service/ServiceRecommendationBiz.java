package com.eaglec.win.biz.service;

import java.util.List;

import com.eaglec.win.domain.service.ServiceRecommendation;


public interface ServiceRecommendationBiz {


	public abstract List<ServiceRecommendation> fingAll();
	
	public abstract ServiceRecommendation fingOne(Integer id);

	public abstract ServiceRecommendation add(ServiceRecommendation serviceRecommendation);

	public abstract ServiceRecommendation update(ServiceRecommendation serviceRecommendation);

	public abstract void delete(Integer id);
	
}
