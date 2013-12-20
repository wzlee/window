package com.eaglec.win.biz.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.user.OrganRecommendationBiz;
import com.eaglec.win.dao.user.OrganRecommendationDao;
import com.eaglec.win.domain.base.OrganRecommendation;
import com.eaglec.win.utils.Common;
import com.eaglec.win.view.JSONData;

@Service
public class OrganRecommendationBizImpl implements OrganRecommendationBiz {
	@Autowired
	OrganRecommendationDao recommendationDao;

	@Override
	public OrganRecommendation add(OrganRecommendation organRecommendation) {
		return recommendationDao.saveOrUpdate(organRecommendation);
	}

	@Override
	public OrganRecommendation update(OrganRecommendation organRecommendation) {
		return recommendationDao.update(organRecommendation);
	}

	@Override
	public void delete(Integer id) {
		recommendationDao.delete(id);
	}

	@Override
	public List<OrganRecommendation> fingAll() {
		String hql = "from OrganRecommendation where enterprise.industryType = "+Common.windowId
				+" and enterprise.status = 1 order by sort";
		return recommendationDao.findList(hql);
	}

	@Override
	public JSONData<OrganRecommendation> fingJsonAll() {
		String hql = "from OrganRecommendation where 1=1";
		return recommendationDao.outJSONData(hql, 0, 1);
	}
}
