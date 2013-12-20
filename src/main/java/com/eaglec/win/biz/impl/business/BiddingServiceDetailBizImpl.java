package com.eaglec.win.biz.impl.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.business.BiddingServiceDetailBiz;
import com.eaglec.win.dao.business.BiddingServiceDetailDao;
import com.eaglec.win.domain.business.BiddingServiceDetail;
import com.eaglec.win.view.JSONData;

@Service
public class BiddingServiceDetailBizImpl implements BiddingServiceDetailBiz {
	
	@Autowired
	private BiddingServiceDetailDao biddingServiceDetailDao;
	
	@Override
	public void add(BiddingServiceDetail biddingServiceDetail) {
		biddingServiceDetailDao.save(biddingServiceDetail);
	}

	@Override
	public JSONData<BiddingServiceDetail> getByBidId(Integer bidServiceId,
			int start, int limit) {
		String hql = "from BiddingServiceDetail d where d.biddingService.id = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", bidServiceId);
		return biddingServiceDetailDao.outJSONData(hql, params, start, limit);
	}
}
