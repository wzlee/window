package com.eaglec.win.biz.impl.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.business.ResponseBiddingBiz;
import com.eaglec.win.dao.business.ResponseBiddingDao;
import com.eaglec.win.dao.user.StaffDao;
import com.eaglec.win.domain.business.ResponseBidding;
import com.eaglec.win.view.JSONData;

@Service
public class ResponseBiddingBizImpl implements ResponseBiddingBiz{
	
	@Autowired
	private ResponseBiddingDao responseBiddingDao;
	@Autowired
	private StaffDao staffDao;
	
	@Override
	public ResponseBidding getResponseBiddingById(int id) {
		return responseBiddingDao.get(id);		
	}
	
	/**
	 * 应标申请
	 * @author pangyf
	 * @since 2013-9-29
	 * @param rb
	 * @return ResponseBidding.id
	 */
	@Override
	public int apply(ResponseBidding rb) {
		return responseBiddingDao.add(rb);
	}

	@Override
	public ResponseBidding addOrModify(ResponseBidding rb) {		
		return responseBiddingDao.saveOrUpdate(rb);
	}

	@Override
	public JSONData<ResponseBidding> getByBidService(Integer bidServiceId,
			Integer status,int start,int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from ResponseBidding r where r.biddingService.id = :id and r.status = :status";
		params.put("id", bidServiceId);
		params.put("status", status);
		return responseBiddingDao.outJSONData(hql, params, start, limit);
	}

	@Override
	public long countResponseBiddingByBid(int bid) {
		String hql = "select count(*) from ResponseBidding where biddingService.id = " + bid;		
		return responseBiddingDao.count(hql);
	}
}
