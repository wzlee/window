package com.eaglec.win.biz.impl.business;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.eaglec.win.biz.business.BiddingServiceBiz;
import com.eaglec.win.dao.business.BiddingServiceDao;
import com.eaglec.win.dao.business.ResponseBiddingDao;
import com.eaglec.win.dao.user.StaffDao;
import com.eaglec.win.dao.user.UserDao;
import com.eaglec.win.domain.business.BiddingService;
import com.eaglec.win.view.JSONData;

@Service
public class BiddingServiceBizImpl implements BiddingServiceBiz{
	
	@Autowired
	private BiddingServiceDao biddingServiceDao;
	@Autowired
	private ResponseBiddingDao responseBiddingDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private StaffDao staffDao;
	
	public BiddingService addOrModify(BiddingService biddingService) {
		return biddingServiceDao.saveOrUpdate(biddingService);
	}

	@Override
	public BiddingService findBiddingServiceById(Integer id) {
		return biddingServiceDao.get(id);
	}

	@Override
	public JSONData<BiddingService> queryBidding(String bidNo,String name, String status,
			String startTime, String endTime, int start, int limit) {
		String hql = "from BiddingService b where 1=1";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(bidNo)){
			hql += " and b.bidNo like :bidNo";
			params.put("bidNo", "%"+bidNo+"%");
		}
		if(!StringUtils.isEmpty(name)){
			hql += " and b.name like :name";
			params.put("name", "%"+name+"%");
		}
		if(!StringUtils.isEmpty(status)){
			hql += " and b.status in (" + status + ")";
		}
		if(!StringUtils.isEmpty(startTime)){
			startTime = startTime.substring(0, startTime.indexOf("T"));
			hql += " and b.createTime >= :startTime";
			params.put("startTime", startTime+ " 00:00:00");
		}
		if(!StringUtils.isEmpty(endTime)){
			endTime = endTime.substring(0, endTime.indexOf("T"));
			hql += " and b.createTime <= :endTime";
			params.put("endTime", endTime+ " 23:59:59");
		}
		hql += " order by b.createTime desc";
		return biddingServiceDao.outJSONData(hql, params, start, limit);
	}
}
